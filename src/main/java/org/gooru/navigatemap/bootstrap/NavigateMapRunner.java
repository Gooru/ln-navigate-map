package org.gooru.navigatemap.bootstrap;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.gooru.navigatemap.app.components.Finalizer;
import org.gooru.navigatemap.app.components.Finalizers;
import org.gooru.navigatemap.app.components.Initializer;
import org.gooru.navigatemap.app.components.Initializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 24/2/17.
 */
public class NavigateMapRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(NavigateMapRunner.class);
  private static JsonObject conf;
  private Vertx vertx;

  public static void main(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("No configuration file passed");
    }

    NavigateMapRunner runner = new NavigateMapRunner();
    runner.initializeConfig(args[0]);
    runner.setupForShutdown();

    runner.run();
  }

  private void run() {
    setupSystemProperties();
    Future<Void> startFuture = Future.future();
    initialize(startFuture);
    startFuture.setHandler(event -> {
      if (event.succeeded()) {
        LOGGER.info("Application startup complete");
      } else {
        LOGGER.error("Error in initialization. Exiting");
        Runtime.getRuntime().halt(1);
      }
    });
  }

  private void setupForShutdown() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> finalizeApplication()));
  }

  private void setupSystemProperties() {
    JsonObject systemProperties = conf.getJsonObject("systemProperties");
    for (Map.Entry<String, Object> property : systemProperties) {
      String propValue = systemProperties.getString(property.getKey());
      System.setProperty(property.getKey(), propValue);
    }
    String logbackFile = System.getProperty("logback.configurationFile");
    if (logbackFile != null && !logbackFile.isEmpty()) {
      setupLoggerMachinery(logbackFile);
    }
  }

  private void setupLoggerMachinery(String logbackFile) {
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    try {
      JoranConfigurator configurator = new JoranConfigurator();
      configurator.setContext(context);
      context.reset();
      configurator.doConfigure(logbackFile);
    } catch (JoranException je) {
      // StatusPrinter will handle this
    }

    StatusPrinter.printInCaseOfErrorsOrWarnings(context);

  }

  private void initialize(Future<Void> startFuture) {
    JsonObject vertxOptionsJson = conf.getJsonObject("vertxOptions");
    VertxOptions options = new VertxOptions(vertxOptionsJson);
    LOGGER.info("Initializing vertx options");

    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        this.vertx = res.result();
        Future<Void> future = Future.future();
        this.initializeApplication(future);
        future.setHandler(asyncResult -> {
          if (asyncResult.succeeded()) {
            this.deployVerticles(startFuture);
          } else {
            LOGGER.warn("Failed to initialize application", asyncResult.cause());
            startFuture.fail(asyncResult.cause());
          }
        });
      } else {
        LOGGER.warn("Failed: " + res.cause());
        startFuture.fail(res.cause());
      }
    });
  }

  private void deployVerticles(Future<Void> startFuture) {
    JsonObject verticles = conf.getJsonObject("verticles");
    List<Future<String>> futures = new ArrayList<>(verticles.size());

    for (Map.Entry<String, Object> verticle : verticles) {
      Future<String> future = Future.future();
      futures.add(future);

      vertx.deployVerticle(verticle.getKey(),
          new DeploymentOptions(verticles.getJsonObject(verticle.getKey())),
          future.completer());
    }

    CompositeFuture.all(eraseTypeList(futures)).setHandler(result -> {
      if (result.succeeded()) {
        LOGGER.info("All verticles deployed successfully");
        startFuture.complete();
      } else {
        LOGGER.warn("Verticles deployment failed", result.cause());
        startFuture.fail(result.cause());
      }
    });

  }

  private void initializeApplication(Future<Void> startFuture) {
    vertx.executeBlocking(future -> {
      Initializers initializers = new Initializers();
      for (Initializer initializer : initializers) {
        initializer.initializeComponent(vertx, NavigateMapRunner.getGlobalConfiguration());
      }
      future.complete();
    }, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Application initialization done");
        startFuture.complete();
      } else {
        LOGGER.warn("Application initialization failed", ar.cause());
        startFuture.fail(ar.cause());
      }
    });
  }

  private void finalizeApplication() {
    LOGGER.info("Finalization of application called");
    Finalizers finalizers = new Finalizers();
    for (Finalizer finalizer : finalizers) {
      finalizer.finalizeComponent();
    }
  }


  private List<Future> eraseTypeList(List<Future<String>> list) {
    return new ArrayList<>(list);
  }

  private void initializeConfig(String configFile) {
    if (configFile != null) {
      try (Scanner scanner = new Scanner(new File(configFile)).useDelimiter("\\A")) {
        String sconf = scanner.next();
        try {
          conf = new JsonObject(sconf);
        } catch (DecodeException e) {
          LOGGER.error("Configuration file " + sconf + " does not contain a valid JSON object");
          throw e;
        }
      } catch (FileNotFoundException e) {
        try {
          conf = new JsonObject(configFile);
        } catch (DecodeException de) {
          LOGGER.error("Argument does not point to a file and is not valid JSON: " + configFile);
          throw de;
        }
      }
    } else {
      LOGGER.error("Null file path");
      throw new IllegalArgumentException("Null configuration file");
    }
  }

  public static JsonObject getGlobalConfiguration() {
    return conf.copy();
  }
}
