#!/bin/bash
DIRNAME_PATH=$(dirname "$0")
SCRIPT_PATH=$(cd "$DIRNAME_PATH"; pwd)
PRO_PATH=$(cd "$(dirname "$0")";cd ..; pwd)
START_SCRIPT="start.sh"
DAEMON_SCRIPT="daemon.sh"

DAEMON_NOW=`ps -ef | grep "$SCRIPT_PATH/$DAEMON_SCRIPT" | grep -v grep | grep -v $$ | wc -l`
if [ $DAEMON_NOW -gt 0 ];then
   DAEMON_PID=`ps -ef | grep "$SCRIPT_PATH/$DAEMON_SCRIPT" | grep -v grep | grep -v $$ | awk '{print $2}'`
   echo "$SCRIPT_PATH/$DAEMON_SCRIPT has running,PID:$DAEMON_PID"
   exit 1
fi

while [ 1 ]
do
	PRO_NOW=`ps -ef | grep java | grep "$PRO_PATH" | grep -v grep | wc -l`
	if [ $PRO_NOW -lt 1 ]; then
		$SCRIPT_PATH/$START_SCRIPT
	fi
    sleep 5
done
exit 0
