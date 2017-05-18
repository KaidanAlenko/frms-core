#!/bin/bash

set -x

mvn flyway:migrate -Dflyway.configFile=etc/flyway/properties/frms-dev.properties
mvn flyway:migrate -Dflyway.configFile=etc/flyway/properties/frms-test.properties