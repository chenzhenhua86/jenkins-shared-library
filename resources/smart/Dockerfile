# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine
# Add Maintainer Info
MAINTAINER 434887245@qq.com
# 设置locale
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ENV TZ=Asia/Shanghai

RUN mkdir /app
COPY /<JOB_NAME>/target/<JOB_NAME>-assembly.tar.gz /app/<JOB_NAME>-assembly.tar.gz
RUN   cd  /app &&  tar -zxvf <JOB_NAME>-assembly.tar.gz && chmod 777 /app/<JOB_NAME>/bin/start.sh
WORKDIR /app/<JOB_NAME>
EXPOSE <PRO_PORT>

CMD ["/bin/sh","-c","sh /app/<JOB_NAME>/bin/start.sh <CURRENV> && tail -f /dev/null"]