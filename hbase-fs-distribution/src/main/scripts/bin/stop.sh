#!/bin/bash

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

JN_HOME="$bin"/..

PID_FILE=$JN_HOME/.pid

echo "stopping JMS Notifier...."
kill -9 `cat $PID_FILE`
echo "JMS Notifier stoped."
