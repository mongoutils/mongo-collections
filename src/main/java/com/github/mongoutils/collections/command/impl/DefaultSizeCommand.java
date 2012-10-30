package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.command.SizeCommand;
import com.mongodb.DBCollection;

public class DefaultSizeCommand implements SizeCommand {
    
    @Override
    public int size(DBCollection collection) {
        return (int) collection.count();
    }
    
}