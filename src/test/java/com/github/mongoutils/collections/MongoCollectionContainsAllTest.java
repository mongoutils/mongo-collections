package com.github.mongoutils.collections;

import org.junit.Test;

public class MongoCollectionContainsAllTest {

    MongoCollection<String> collection;

    @Test(expected = UnsupportedOperationException.class)
    public void containsAll() {
        collection = new MongoCollection<String>(null, null);
        collection.containsAll(null);
    }

}
