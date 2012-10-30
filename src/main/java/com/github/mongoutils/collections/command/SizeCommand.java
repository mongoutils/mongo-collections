package com.github.mongoutils.collections.command;

import com.mongodb.DBCollection;

public interface SizeCommand extends CollectionCommand {
    
    int size(DBCollection collection);
    
}
