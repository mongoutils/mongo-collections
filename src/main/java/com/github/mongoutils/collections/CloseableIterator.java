package com.github.mongoutils.collections;

import java.util.Iterator;

public interface CloseableIterator<T> extends Iterator<T> {

    void close();

}
