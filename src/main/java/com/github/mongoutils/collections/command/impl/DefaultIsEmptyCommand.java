package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.command.IsEmptyCommand;
import com.mongodb.DBCollection;

public class DefaultIsEmptyCommand implements IsEmptyCommand {
    
    @Override
    public boolean isEmpty(DBCollection collection) {
        return collection.count() <= 0;
    }
    
}
