Short intro for vagrant and virtualbox installation
========================

## Vagrant 

* Download software [here](http://downloads.vagrantup.com/) (choose *.deb package if you are running __Ubuntu__ or __Debian__ and choose the latest version, as of writing [v.1.2.7](http://downloads.vagrantup.com/tags/v1.2.7) is available)

* Install the package via

```Shell
    sudo dpkg -i vagrant_1.2.7_x86_64.deb
```

## Virtualbox

* Download software [here](https://www.virtualbox.org/wiki/Linux_Downloads)

* Install the package via

```Shell
    sudo dpkg -i virtualbox-4.2_4.2.16-86992~Ubuntu~raring_amd64.deb 
```

## Time to clone my project

* Clone it

```Shell
    git clone git@github.com:randomnaja/mongo-collections.git
```


* Then go to mongovm directory

```Shell
    cd mongovm
```

* Cook time !

```Shell
    vagrant up
```

* And wait for a while, it will download the __box__ file, set it up and configure the MongoDB

* Test the MongoDB connection via 

```
    127.0.0.1:27017
```

* For the Rest Interface go to

```
    http://localhost:28017
```

