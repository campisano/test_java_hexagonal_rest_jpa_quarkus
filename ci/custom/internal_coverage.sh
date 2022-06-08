#!/usr/bin/env bash

set -x -o errexit -o nounset -o pipefail

mkdir -p .custom_cache/var/cache/apt
cp -aT .custom_cache/var/cache/apt /var/cache/apt
rm -f /etc/apt/apt.conf.d/docker*

export DEBIAN_FRONTEND=noninteractive
apt-get -qq -y update
apt-get -qq -y install --no-install-recommends apt-utils &> /dev/null
apt-get -qq -y install --no-install-recommends libssl1.1 libcurl4 > /dev/null
apt-get -qq -y install --no-install-recommends wget unzip > /dev/null

mv -n /var/cache/apt/* .custom_cache/var/cache/apt/

SONARSCAN_VER="4.7.0.2747"
SONARSCAN_URL="https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONARSCAN_VER}-linux.zip"
mkdir -p .custom_cache/sonar/pkg
wget -P .custom_cache/sonar/pkg -c -nv --no-check-certificate "${SONARSCAN_URL}"
mkdir -p .custom_cache/sonar/scanner
unzip -d .custom_cache/sonar/scanner -q -n ".custom_cache/sonar/pkg/sonar-scanner-cli-${SONARSCAN_VER}-linux.zip"

mkdir -p .custom_cache/sonar/home
export SONAR_USER_HOME=.custom_cache/sonar/home
.custom_cache/sonar/scanner/sonar-scanner-${SONARSCAN_VER}-linux/bin/sonar-scanner \
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
