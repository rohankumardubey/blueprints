package com.tinkerpop.blueprints.pgm.impls;

import com.tinkerpop.blueprints.pgm.Element;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a helper class for filtering an iterable of elements by their key/value.
 * Useful for graph implementations that do no support automatic key indices and need to filter on Graph.getVertices/Edges(key,value).
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PropertyFilteredIterable<T extends Element> implements Iterable<T> {

    private final String key;
    private final Object value;
    private final Iterable<T> iterable;

    public PropertyFilteredIterable(final String key, final Object value, final Iterable<T> iterable) {
        this.key = key;
        this.value = value;
        this.iterable = iterable;
    }

    public Iterator<T> iterator() {
        return new PropertyFilteredIterator<T>(iterable.iterator());
    }

    private class PropertyFilteredIterator<T extends Element> implements Iterator<T> {

        private Iterator<T> itty;
        private T nextElement = null;

        public PropertyFilteredIterator(final Iterator<T> itty) {
            this.itty = itty;
        }

        public void remove() {
            this.itty.remove();
        }

        public boolean hasNext() {
            if (null != nextElement)
                return true;
            else {
                try {
                    while (true) {
                        final T element = this.itty.next();
                        if (element.getProperty(key).equals(value)) {
                            this.nextElement = element;
                            return true;
                        }
                    }
                } catch (NoSuchElementException e) {
                    this.nextElement = null;
                    return false;
                }
            }
        }

        public T next() {
            if (null != this.nextElement) {
                final T temp = this.nextElement;
                this.nextElement = null;
                return temp;
            } else {
                while (true) {
                    final T element = this.itty.next();
                    if (element.getProperty(key).equals(value)) {
                        return element;
                    }
                }
            }
        }
    }

}