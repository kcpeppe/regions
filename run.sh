#!/bin/sh

export FLAGS="-Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintTenuringDistribution -XX:+PrintGCApplicationConcurrentTime -XX:+PrintReferenceGC -XX:+PrintGCDateStamps -Xmx4G -XX:+UseG1GC"
export PROFILE="-XX:+UnlockCommericalFeatures -XX:+FlightRecorder"
export MAIN=com.jclarity.region.G1GCHeapAnimation
export CLASS_PATH=out/production/regions

export ARGS=testdata.log

java $FLAGS -classpath $CLASS_PATH $MAIN $ARGS
