package org.gooru.navigatemap.bootstrap.verticles;

import org.gooru.navigatemap.constants.Constants;
import org.gooru.navigatemap.responses.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * @author ashish on 25/2/17.
 */
public class UserContextVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserContextVerticle.class);
    private RedisClient redisClient;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eb = vertx.eventBus();

        initializeVerticle(startFuture);

        eb.<JsonObject>localConsumer(Constants.EventBus.MBEP_USER_CONTEXT, message -> {
            String op = message.headers().get(Constants.Message.MSG_OP);
            String key = message.headers().get(Constants.Message.MSG_HDR_KEY_CONTEXT);
            if (op.equalsIgnoreCase(Constants.Message.MSG_OP_CONTEXT_GET)) {
                Future<JsonObject> contextFetch = fetchContextFromRedis(key);
                contextFetch.setHandler(asyncResultHandler -> {
                    if (asyncResultHandler.succeeded()) {
                        ResponseUtil.processSuccess(message, asyncResultHandler.result());
                    } else {
                        ResponseUtil.processFailure(message);
                    }
                });

            } else if (op.equalsIgnoreCase(Constants.Message.MSG_OP_CONTEXT_SET)) {
                storeContextInRedis(key, message.body());
            } else {
                LOGGER.warn("Invalid op: {}", op);
            }
        }).completionHandler(result -> {
            if (result.succeeded()) {
                LOGGER.info("Context end point ready to listen");
            } else {
                LOGGER.error("Error registering the context handler. Halting the machinery");
                Runtime.getRuntime().halt(1);
            }
        });
    }

    private Future<JsonObject> fetchContextFromRedis(String contextKey) {
        Future<JsonObject> future = Future.future();

        redisClient.get(contextKey, redisAsyncResult -> {
            if (redisAsyncResult.succeeded()) {
                final String redisResult = redisAsyncResult.result();
                LOGGER.debug("Redis responded with '{}'", redisResult);
                if (redisResult != null) {
                    try {
                        JsonObject jsonResult = new JsonObject(redisResult);
                        future.complete(jsonResult);
                    } catch (DecodeException de) {
                        LOGGER.error("Exception while decoding json for context '{}'", contextKey, de);
                        future.fail(de);
                    }
                } else {
                    LOGGER.info("Context not found or invalid context");
                    future.complete(new JsonObject());
                }
            } else {
                LOGGER.error("Redis operation failed", redisAsyncResult.cause());
                future.fail(redisAsyncResult.cause());
            }
        });
        return future;
    }

    private void storeContextInRedis(String contextKey, JsonObject contextValue) {
        redisClient.set(contextKey, contextValue.toString(), updateHandler -> {
            if (updateHandler.succeeded()) {
                LOGGER.debug("Context for context key '{}' is updated", contextKey);
            } else {
                LOGGER.warn("Not able to update context for context key '{}'", contextKey, updateHandler.cause());
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        finalizeVerticle(stopFuture);
    }

    private void initializeVerticle(Future<Void> startFuture) {
        try {
            JsonObject configuration = config().getJsonObject("redisConfig");
            RedisOptions options = new RedisOptions(configuration);
            redisClient = RedisClient.create(vertx, options);
            redisClient.get("NonExistingKey", initHandler -> {
                if (initHandler.succeeded()) {
                    LOGGER.info("Initial connection check with Redis done");
                    startFuture.complete();
                } else {
                    startFuture.fail(initHandler.cause());
                }
            });
        } catch (Throwable throwable) {
            LOGGER.error("Not able to continue initialization.", throwable);
            startFuture.fail(throwable);
        }
    }

    private void finalizeVerticle(Future<Void> stopFuture) {
        if (redisClient != null) {
            redisClient.close(redisCloseAsyncHandler -> {
                if (redisCloseAsyncHandler.succeeded()) {
                    LOGGER.info("Redis client has been closed successfully");
                } else {
                    LOGGER.error("Error in closing redis client", redisCloseAsyncHandler.cause());
                }
                stopFuture.complete();
            });
        }
    }

}
