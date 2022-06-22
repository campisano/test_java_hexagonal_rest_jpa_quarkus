#!/usr/bin/env bash

set -x -o errexit -o nounset -o pipefail

# vars
export DOCKER_IMAGE=$(./ci/custom/get_docker_image_build.sh)

# get image
docker pull "${DOCKER_IMAGE}"

# build code isolatedly
# from https://askubuntu.com/a/1398147
docker pull tonistiigi/binfmt:latest
docker run --privileged --rm tonistiigi/binfmt --uninstall qemu-*
docker run --privileged --rm tonistiigi/binfmt --install all
docker buildx ls
docker buildx create --use
docker buildx build --platform=linux/arm64/v8 --file - --output type=local,dest=out .  << EOF
FROM ${DOCKER_IMAGE} as build
COPY ${CACHE_DIR}/. /srv/cache
COPY . /srv/repo
RUN bash -c "cd /srv/repo; ./ci/custom/internal_build.sh"

FROM scratch as artifact
COPY --from=build /srv/build/. /build
COPY --from=build /srv/cache/. /cache
EOF

mv -n out/cache/* ${CACHE_DIR}
mv -n out/build/* ${BUILD_DIR}
