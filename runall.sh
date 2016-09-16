#!/bin/bash
#    This is how your programs will be tested

# set up CLASSPATH
export CLASSPATH="$CLASSPATH:src/test/java/lib/*:src/main/java/:src/test/java/"

# run all tests
javac src/main/java/*.java src/test/java/*.java
java TestRunner
