package com.ibm.websphere.samples.daytrader.util;

import java.util.AbstractSequentialList;
import java.util.ListIterator;

public class KeyBlock extends AbstractSequentialList<Object> {

    public KeyBlock() {
        super();
        min = 0;
        max = 0;
        index = min;
    }

    public KeyBlock(int min, int max) {
        super();
        this.min = min;
        this.max = max;
        index = min;
    }

    private int min = 0;

    private int max = 0;

    private int index = 0;

    // TODO(annotation: Override)
    public int size() {
        return (max - min) + 1;
    }

    // TODO(annotation: Override)
    public ListIterator<Object> listIterator(int arg0) {
        return new KeyBlockIterator();
    }

    public class KeyBlockIterator implements ListIterator<Object> {

        // TODO(annotation: Override)
        public boolean hasNext() {
            return index <= max;
        }

        // TODO(annotation: Override)
        public synchronized Object next() {
            if (index > max) {
                throw new java.lang.RuntimeException("KeyBlock:next() -- Error KeyBlock depleted");
            }
            return new Integer(index++);
        }

        // TODO(annotation: Override)
        public boolean hasPrevious() {
            return index > min;
        }

        // TODO(annotation: Override)
        public Object previous() {
            return new Integer(--index);
        }

        // TODO(annotation: Override)
        public int nextIndex() {
            return index - min;
        }

        // TODO(annotation: Override)
        public int previousIndex() {
            throw new UnsupportedOperationException("KeyBlock: previousIndex() not supported");
        }

        // TODO(annotation: Override)
        public void add(Object o) {
            throw new UnsupportedOperationException("KeyBlock: add() not supported");
        }

        // TODO(annotation: Override)
        public void remove() {
            throw new UnsupportedOperationException("KeyBlock: remove() not supported");
        }

        // TODO(annotation: Override)
        public void set(Object arg0) {
        }
    }
}
