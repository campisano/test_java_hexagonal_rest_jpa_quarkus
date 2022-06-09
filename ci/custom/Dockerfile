ARG FROM_IMAGE
FROM $FROM_IMAGE

RUN rm -rf /var/lib/apt/lists/* /var/cache/apt/* /usr/share/man/*

RUN useradd -r -U app -d /srv/app -m
USER app:app

COPY ./app.jar /srv/app/app.jar

WORKDIR /srv/app
ENTRYPOINT java -jar app.jar
