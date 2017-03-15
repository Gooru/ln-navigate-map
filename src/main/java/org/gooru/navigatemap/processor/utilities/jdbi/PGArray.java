package org.gooru.navigatemap.processor.utilities.jdbi;

import java.util.Collection;

/**
 * @author ashish on 15/3/17.
 */
public class PGArray<T> {
    private final Object[] elements;
    private final Class<T> type;

    public PGArray(Class<T> type, Collection<T> elements) {
        this.elements = toArray(elements);
        this.type = type;
    }

    private Object[] toArray(Collection<T> elements) {
        return elements.toArray();
    }

    public static <T> PGArray<T> arrayOf(Class<T> type, Collection<T> elements) {
        return new PGArray<T>(type, elements);
    }

    public Object[] getElements() {
        return elements;
    }

    public Class<T> getType() {
        return type;
    }
}

