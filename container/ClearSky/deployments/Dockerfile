FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD clearsky-boot-0.0.1.jar clearskyboot.jar
RUN sh -c 'touch /clearskyboot.jar'
ENV JAVA_OPTS="-Dspring.profiles.active=prod"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /clearskyboot.jar" ]
