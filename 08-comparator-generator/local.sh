#!/bin/bash

./gradlew processor:jar
./gradlew uberJar
java -jar build/libs/08-comparator-generator-1.0.jar
