#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    mvn -f ../flogger-core/pom.xml deploy --settings cd/mvnsettings.xml
    mvn -f ../flogger-spring-boot/pom.xml deploy --settings cd/mvnsettings.xml
    mvn -f ../flogger-sample/pom.xml deploy --settings cd/mvnsettings.xml
fi
