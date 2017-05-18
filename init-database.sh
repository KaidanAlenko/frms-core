#!/bin/bash

set -x

export PGPASSWORD=postgres
psql -h 0.0.0.0 -U postgres -d postgres -a -f etc/flyway/init/create_databases.sql