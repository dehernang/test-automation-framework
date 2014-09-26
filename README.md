Dependencies
===============

Required:

  1. Java JDK 7+
  2. Apache Ant 1.9+
  
Optional But Recommended:

  1. Local Oracle XE 11g
  2. Local Solr 4.9
  3. Local MongoDB

This test suites is ideal and has been tested with local Oracle, Solr and MongoDB. But it is also configurable to connect remotely by modifying config.properties
  
How To Use
===============

List of suites currently available:
testSuite1

Example 1: To run all test suites
```sh
ant run
```
or simply
```sh
ant
```

Example 2: To run example suite 1
```sh
ant testSuite1
```

See ./configs/build.xml for more details
