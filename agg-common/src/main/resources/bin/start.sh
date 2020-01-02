#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
JAR_NAME=`ls *.jar`
LOGS_DIR=$DEPLOY_DIR/logs
STDOUT_FILE=$LOGS_DIR/stdout.log

PIDS=`ps -ef | grep java | grep "$JAR_NAME" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The $JAR_NAME already started and the PID is ${PIDS}."
    exit 1
fi

if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi

PROFILE_OPTS=""
JAVA_DEBUG_OPTS=""
JAVA_JMX_OPTS=""
JAVA_MEM_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$SERVER_DEBUG_PORT,server=y,suspend=n "
fi

if [ "$1" = "jmx" ]; then
    JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote.port=$SERVER_JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
fi

BITS=`java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS=" -server -Xmx2g -Xms1g -Xmn256m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m "
else
    JAVA_MEM_OPTS=" -server -Xmx1g -Xms1g -Xmn256m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m "
fi

echo -e "Starting the $JAR_NAME ...\c"

nohup java $PROFILE_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -jar $JAR_NAME \
    -Dhkfs.home="$DEPLOY_DIR" > $STDOUT_FILE 2>&1 &

COUNT=0
while [ $COUNT -lt 1 ]; do
    echo -e ".\c"
    sleep 1
    COUNT=`ps -ef | grep java | grep "$JAR_NAME" |awk '{print $2}' | wc -l`
    if [ $COUNT -gt 0 ]; then
        break
    fi
done
echo "OK!"
PIDS=`ps -ef | grep java | grep "$JAR_NAME" |awk '{print $2}'`
echo "$JAR_NAME Started and the PID is ${PIDS}."
echo "STDOUT: $STDOUT_FILE"