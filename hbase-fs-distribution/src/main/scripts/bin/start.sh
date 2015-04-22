#!/bin/sh

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

HFS_HOME="$bin"/..

PID_FILE=$HFS_HOME/.pid

LOG_DIR=$HFS_HOME/logs/

LOG_FILE=$LOG_DIR/main_`date +%F`.log

HFS_CP=$HFS_HOME/etc

export HFS_HOME=$HFS_HOME

for f in $HFS_HOME/lib/*.jar; do
    HFS_CP=$HFS_CP:$f;
done

echo "starting HBase filesystem RESTful service..."

mkdir -p $LOG_DIR

java -server -cp $HFS_CP org.lychee.fs.hbase.rest.Main >> $LOG_FILE 2>&1 &

echo $! > $PID_FILE
echo HBase filesystem RESTful service started in process `cat $PID_FILE`
