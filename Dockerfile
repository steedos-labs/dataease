FROM registry.cn-qingdao.aliyuncs.com/dataease/alpine-openjdk17-jre

RUN mkdir -p /opt/apps/config /opt/dataease2.0/drivers/ /opt/dataease2.0/cache/ /opt/dataease2.0/data/map /opt/dataease2.0/data/static-resource/ /opt/dataease2.0/data/appearance/

ADD drivers/* /opt/dataease2.0/drivers/
ADD mapFiles/ /opt/dataease2.0/data/map/
ADD staticResource/ /opt/dataease2.0/data/static-resource/

WORKDIR /opt/apps

ADD core/core-backend/target/CoreApplication.jar /opt/apps/app.jar

ADD docker/config/application.yml /opt/apps/config/

ENV JAVA_APP_JAR=/opt/apps/app.jar
ENV JAVA_OPTIONS="-Dfile.encoding=utf-8 -Dloader.path=/opt/apps -Dspring.config.additional-location=/opt/apps/config/"

HEALTHCHECK --interval=15s --timeout=5s --retries=20 --start-period=30s CMD nc -zv 127.0.0.1 8100

CMD ["/deployments/run-java.sh"]
