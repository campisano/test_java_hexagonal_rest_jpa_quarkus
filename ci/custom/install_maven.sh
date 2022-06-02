#!/usr/bin/env bash

set -x -o errexit -o nounset -o pipefail

MVN_VER="3.6.2"
MVN_FILE="apache-maven-${MVN_VER}-bin.tar.gz"
URL="https://archive.apache.org/dist/maven/maven-3/${MVN_VER}/binaries/${MVN_FILE}"

mkdir -p .custom_cache/maven
wget -P .custom_cache/maven -c -nv --no-check-certificate "${URL}"
tar -C /usr/local -xzf ".custom_cache/maven/${MVN_FILE}"

ln -s "/usr/local/apache-maven-${MVN_VER}/bin/mvn" /usr/local/bin
