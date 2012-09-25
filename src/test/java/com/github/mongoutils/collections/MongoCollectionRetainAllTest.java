package com.github.mongoutils.collections;

import org.junit.Test;

public class MongoCollectionRetainAllTest {

    MongoCollection<String> collection;

    @Test(expected = UnsupportedOperationException.class)
    public void retainAll() {
        collection = new MongoCollection<String>(null, null);
        collection.retainAll(null);
    }

}
