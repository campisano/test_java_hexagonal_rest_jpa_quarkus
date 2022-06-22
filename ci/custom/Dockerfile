ARG FROM_IMAGE
FROM --platform=linux/amd64 $FROM_IMAGE AS linux-amd64

RUN rm -rf /var/lib/apt/lists/* /var/cache/apt/* /usr/share/man/*

RUN useradd -r -U app -d /srv/app -m
USER app:app

COPY ./.ci_build_amd64/app.jar /srv/app/app.jar



ARG FROM_IMAGE
FROM --platform=linux/arm64/v8 $FROM_IMAGE AS linux-arm64

RUN rm -rf /var/lib/apt/lists/* /var/cache/apt/* /usr/share/man/*

RUN useradd -r -U app -d /srv/app -m
USER app:app

COPY ./.ci_build_arm64v8/app.jar /srv/app/app.jar



ARG TARGETOS
ARG TARGETARCH
FROM ${TARGETOS}-${TARGETARCH} as final

WORKDIR /srv/app
CMD ["java", "-jar", "app.jar"]
