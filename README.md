# mongoutils-mongo-collections

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
    <version>0.1</version>
</dependency>
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
