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
apt-get -qq -y install --no-install-recommends libssl1.1 libcurl4 > /dev/null
apt-get -qq -y install --no-install-recommends wget tar gzip unzip > /dev/null

mv -n /var/cache/apt/* ${CACHE_DIR}/var/cache/apt/

./ci/custom/install_maven.sh
mvn -Dmaven.repo.local=${CACHE_DIR}/maven/.m2/repository -B -ntp clean verify

SONARSCAN_VER="4.7.0.2747"
SONARSCAN_URL="https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONARSCAN_VER}-linux.zip"
mkdir -p ${CACHE_DIR}/sonar/pkg
wget -P ${CACHE_DIR}/sonar/pkg -c -nv --no-check-certificate "${SONARSCAN_URL}"
mkdir -p ${CACHE_DIR}/sonar/scanner
unzip -d ${CACHE_DIR}/sonar/scanner -q -n "${CACHE_DIR}/sonar/pkg/sonar-scanner-cli-${SONARSCAN_VER}-linux.zip"

mkdir -p ${CACHE_DIR}/sonar/home
export SONAR_USER_HOME=${CACHE_DIR}/sonar/home
${CACHE_DIR}/sonar/scanner/sonar-scanner-${SONARSCAN_VER}-linux/bin/sonar-scanner \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=${SONAR_ORGANIZATION} \
    -Dsonar.projectKey=${SONAR_PROJECT} \
    -Dsonar.scm.provider=git \
    -Dsonar.working.directory=.scannerwork \
    -Dsonar.sources=src/main \
    -Dsonar.tests=src/test \
    -Dsonar.coverage.exclusions=src/test/** \
    -Dsonar.sourceEncoding=UTF-8 \
    -Dsonar.java.binaries=target/classes \
    -Dsonar.java.test.binaries=target/test-classes \
    -Dsonar.java.jdkHome=${JAVA_HOME} \
    -Dsonar.java.source=11

rm -rf .scannerwork
