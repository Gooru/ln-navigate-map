package org.gooru.navigatemap.infra.utilities.jdbi;

import java.sql.SQLException;
import java.util.UUID;

import org.postgresql.util.PGobject;

/**
 * Utitlity methods to help create PGobjects
 *
 * @author ashish on 10/5/18.
 */
public final class PGObjectUtils {

    private PGObjectUtils() {
        throw new AssertionError();
    }

    /*
     * Create a PGobject encapsulating UUID and setting value of null if UUID is null
     */
    public static PGobject getNullSafeUUID(UUID uuid) {
        try {
            PGobject result = new PGobject();
            result.setType("uuid");
            result.setValue(uuid != null ? uuid.toString() : null);
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Not able to get null safe UUID PGObject.", e);
        }
    }
}
