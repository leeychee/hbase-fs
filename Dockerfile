FROM		java:7-jre

MAINTAINER	Chunhui Liu <leeychee@gmail.com>

ENV			HFS_HOME=/var/opt/hfs

RUN			mkdir $HFS_HOME

COPY		hbase-fs-distribution/target/*.tar.gz /tmp/

RUN			tar -xzvf /tmp/*.tar.gz -C $HFS_HOME

RUN			rm -rf /tmp

WORKDIR		$HFS_HOME

CMD			["/usr/bin/java", "-Xbootclasspath/a:./conf", "-jar", "./lib/hbase-fs-rest-0.2.0.jar"]
