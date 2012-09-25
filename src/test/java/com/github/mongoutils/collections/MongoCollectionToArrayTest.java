package com.github.mongoutils.collections;

import org.junit.Test;

public class MongoCollectionToArrayTest {

    MongoCollection<String> collection;

    @Test(expected = UnsupportedOperationException.class)
    public void toArray() {
        collection = new MongoCollection<String>(null, null);
        collection.toArray();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void toTypedArray() {
        collection = new MongoCollection<String>(null, null);
        collection.toArray(new String[] {});
    }

}
