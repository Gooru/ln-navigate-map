package org.gooru.navigatemap.processor.coursepath.repositories.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gooru.navigatemap.processor.data.SignatureResource;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 6/4/17.
 */
public class SignatureResourceMapper implements ResultSetMapper<SignatureResource> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureResourceMapper.class);

    @Override
    public SignatureResource map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        SignatureResource result = new SignatureResource();

        result.setResourceId(r.getString("resource_id"));
        result.setResourceType(r.getString("resource_type"));
        result.setPublisher(r.getString("publisher"));
        result.setLanguage(r.getString("language"));
        result.setEfficacy(r.getDouble("efficacy"));
        result.setEngagement(r.getDouble("engagement"));
        result.setWeight(r.getDouble("weight"));
        result.setSuggestedCount(r.getLong("suggested_count"));

        return result;
    }

}
