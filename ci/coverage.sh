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
       --volume /var/run/docker.sock:/var/run/docker.sock \
       --mount type=bind,source="$(pwd)",target=/srv/repository \
       "${DOCKER_IMAGE}" \
       /bin/bash -c \
       'cd /srv/repository; ./ci/custom/internal_coverage.sh'
