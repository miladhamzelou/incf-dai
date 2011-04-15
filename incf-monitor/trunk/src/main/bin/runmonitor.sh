#!/bin/sh
# Run the monitor program

# Stripped down, the script will look like this. The variables are the ones
# set below except for JAVA_HOME which is set at the OS level.
#   cd $STARTDIR
#   $JAVA_HOME/bin/java $VMARGS -cp $classpath $MAINCLASS

#
# script constants
#
# java's location
JAVA_HOME=/usr

# jvm arguments
VMARGS="-Xms2048M -Xmx2048M"

# server home directory
SERVER_HOME=/usr/local/incf/incfmonitor

# identify main() method's class
MAINCLASS=org.incf.monitor.Monitor

# destination for sysout/syserr prior to formal logging
#STARTUPLOG=logs/sys.log

#
# execution sequence
#
# get in server directory
cd $SERVER_HOME

# set java class path; start with conf/                                                                            
jarfiles=`cd lib/; ls *.jar`
classpath=conf/
for jarfile in $jarfiles ; do
    classpath=$classpath:lib/$jarfile
done                             

# start monitor
#$JAVA_HOME/bin/java $VMARGS -cp $classpath $MAINCLASS > $STARTUPLOG 2>&1
$JAVA_HOME/bin/java $VMARGS -cp $classpath $MAINCLASS

