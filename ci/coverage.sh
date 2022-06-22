#!/usr/bin/env bash

set -x -o errexit -o nounset -o pipefail

# vars
export DOCKER_IMAGE=$(./ci/custom/get_docker_image_build.sh)

# get image
docker pull "${DOCKER_IMAGE}"

# build code and run sonar isolatedly
docker run --rm \
       --env SONAR_ORGANIZATION \
       --env SONAR_TOKEN \
       --env SONAR_PROJECT \
       --mount type=bind,source="$(pwd)/${CACHE_DIR}",target=/srv/cache \
       --mount type=bind,source="$(pwd)/${BUILD_DIR}",target=/srv/build \
       --mount type=bind,source="$(pwd)",target=/srv/repo,readonly \
       "${DOCKER_IMAGE}" \
       /bin/bash -c \
       'cp -a /srv/repo /srv/exec; cd /srv/exec; ./ci/custom/internal_coverage.sh'
