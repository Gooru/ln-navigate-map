package org.gooru.navigatemap.app.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ashish on 24/2/17.
 */
public class Finalizers implements Iterable<Finalizer> {

    private final Iterator<Finalizer> internalIterator;

    public Finalizers() {
        List<Finalizer> finalizers = new ArrayList<>();
        finalizers.add(DataSourceRegistry.getInstance());
        internalIterator = finalizers.iterator();
    }

    @Override
    public Iterator<Finalizer> iterator() {
        return new Iterator<Finalizer>() {

            @Override
            public boolean hasNext() {
                return internalIterator.hasNext();
            }

            @Override
            public Finalizer next() {
                return internalIterator.next();
            }

        };
    }

}
