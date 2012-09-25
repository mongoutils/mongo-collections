package com.github.mongoutils.collections;

import org.junit.Test;

public class MongoCollectionRemoveAllTest {

    MongoCollection<String> collection;

    @Test(expected = UnsupportedOperationException.class)
    public void removeAll() {
        collection = new MongoCollection<String>(null, null);
        collection.removeAll(null);
    }

}
