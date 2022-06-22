#!/usr/bin/env bash

set -x -o errexit -o nounset -o pipefail

CACHE_DIR=/srv/cache
BUILD_DIR=/srv/build

mkdir -p ${CACHE_DIR}/var/cache/apt
cp -aT ${CACHE_DIR}/var/cache/apt /var/cache/apt
rm -f /etc/apt/apt.conf.d/docker*

export DEBIAN_FRONTEND=noninteractive
apt-get -qq -y update
apt-get -qq -y install --no-install-recommends apt-utils > /dev/null
apt-get -qq -y install --no-install-recommends wget tar gzip > /dev/null

mv -n /var/cache/apt/* ${CACHE_DIR}/var/cache/apt/

./ci/custom/install_maven.sh
mvn -Dmaven.repo.local=${CACHE_DIR}/maven/.m2/repository -B -ntp clean test
