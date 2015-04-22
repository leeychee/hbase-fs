cd %~dp0
set JMS_NOTIFIER_HOME=..
set CLASSPATH=..\conf
java -Xbootclasspath/a:%CLASSPATH% -jar ..\lib\hbase-fs-${project.version}.jar