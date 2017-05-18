#!/bin/bash

set -x

mvn flyway:clean   -Dflyway.configFile=etc/flyway/properties/frms-dev.properties
mvn flyway:clean   -Dflyway.configFile=etc/flyway/properties/frms-test.properties

mvn flyway:migrate -Dflyway.configFile=etc/flyway/properties/frms-dev.properties
mvn flyway:migrate -Dflyway.configFile=etc/flyway/properties/frms-test.properties