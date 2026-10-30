#!/bin/bash
export JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1 GRADLE_USER_HOME=/workspace/gradle-home
L=/workspace/gates/logs/vm6/solr18600-dual-release-round-1
cd /workspace/tmp/s18600/proto
flock /workspace/tools/.gradle-vm6.lock ./gradlew --no-daemon :solr:api:jar :solr:solrj:jar :solr:solrj-jetty:jar :solr:solrj-zookeeper:jar > $L/build-proto-3.log 2>&1
echo rc=$? > $L/build-proto-3.rc
