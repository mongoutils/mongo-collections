# mongo-collections

MongoDB-backed Java collection implementations.

## License

Apache 2.0 License (http://www.apache.org/licenses/LICENSE-2.0)

## Requirements / Dependencies

* Java 1.6+ (http://www.java.com/de/download/)
* Jackson 2.0.5+ (https://github.com/FasterXML/)
* MongoDB Java-Driver 2.8.0+ (https://github.com/mongodb/)

## How to get it

The maven dependecy:

```xml
<dependency>
    <groupId>com.github.mongodbutils</groupId>
    <artifactId>mongo-collections</artifactId>
    <version>1.3</version>
</dependency>
```

## Collections

### Map

There are three implementation of the ``java.util.Map`` (or ``java.util.concurrent.ConcurrentMap``) interface:

* CachingMap
* CachingConcurrentMap
* MongoConcurrentMap

The use of ``MongoConcurrentMap`` is to pass read and write requests through to MongoDB masked with
a very common interface (``Map``) - it makes MongoDB a kind of a Key-Value-Store.

To not send a request on every ``get()`` invocation one can decorate the ``MongoConcurrentMap``
with ``CachingMap`` or the ``CachingConcurrentMap`` which caches a subset of the entries in the backstore (MongoDB).

```java
// the mongo connection + db + collection
DBCollection collection = new Mongo("localhost", 27017).getDB("testDB").getCollection("testCollection");
// the serializers for mapping DBObjects to String and vice versa
DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>("key");
DBObjectSerializer<String> valueSerializer = new SimpleFieldDBObjectSerializer<String>("value");
// will produce documents like "{'key':...,'value':...,'_id':ObjectID(...)}"
Map<String, String> backstore = new MongoConcurrentMap<String, String>(collection, keySerializer, valueSerializer);
// max. 1000 in memory
Map<String, String> cache = new org.apache.commons.collections.LRUMap(1000);
Map<String, String> map = new CachingMap<String, String>(cache, backstore);
...
map.get(...);
...
map.put("key", "value");
...
```

### Collection

``MongoCollection`` implements the ``java.util.Collection`` interface and is primary used by the ``values`` method
of ``MongoConcurrentMap``, but it can be used as regular collection.

```java
// the mongo connection + db + collection
DBCollection collection = new Mongo("localhost", 27017).getDB("testDB").getCollection("testCollection");
DBObjectSerializer<String> serializer = ...;

Collection<String> mongoCollection = new MongoCollection<String>(collection, serializer);
...
mongoCollection.add(...);
...
mongoCollection.remove(...);
...
```

### Set

``MongoSet`` implements the ``java.util.Set`` interface and is primary used by the ``keySet`` and ``entrySet`` methods
of ``MongoConcurrentMap``, but it can be used as regular set.

```java
// the mongo connection + db + collection
DBCollection collection = new Mongo("localhost", 27017).getDB("testDB").getCollection("testCollection");
DBObjectSerializer<String> serializer = ...;

Set<String> mongoSet = new MongoSet<String>(collection, serializer);
...
mongoSet.add(...);
...
mongoSet.remove(...);
...
```

### Queue

``MongoQueue`` implements the ``java.util.Queue`` interface.

```java
// the mongo connection + db + collection
DBCollection collection = new Mongo("localhost", 27017).getDB("testDB").getCollection("testCollection");
DBObjectSerializer<String> serializer = ...;

Queue<String> mongoQueue = new MongoQueue<String>(collection, serializer);
...
mongoSet.offer("MyValue");
...
String value = mongoSet.poll();
...
```

## Test it using mongodb-vm

The project comes with a fully functional VM with an mongodb installation for testing purpose.
You need to have VirtualBox (https://www.virtualbox.org/) and Vagrant (http://vagrantup.com/) installed to run the VM.
All necessary ports are forwarded to the VM so you can connect to mongodb as it were installed on your system directly.

Check the project out, open a console in that directory and type:

```text
cd mongovm
vagrant up
```

Integration tests are done with https://github.com/joelittlejohn/embedmongo-maven-plugin.

## Motivation

There is a problem with both (distributed) caches and key-value-stores:

```text
They heavily depend on Memory and persistence is still an issue to most of them.
```

The most common solution is to limit the data amount or simply to buy new resources (bigger servers).

To get rid of this problem MongoDB is used as persistence store and accessed via the ``Map`` interface.
So under the bottom line we've got a persistent, distributed map without the need to run a separate
service (Redis, Infinispan or EHCache) besides the database (MongoDB).
