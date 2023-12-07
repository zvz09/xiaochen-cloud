FROM openjdk:17-jdk-alpine
# author
MAINTAINER zvz09

# 挂载目录
VOLUME /home/xiaochen
# 创建目录
RUN mkdir -p /home/xiaochen
# 指定路径
WORKDIR /home/xiaochen
# 复制jar文件到路径
COPY ./build/libs/xiaochen-file-0.0.1-SNAPSHOT.jar /home/xiaochen/xiaochen-file.jar
# 启动网关服务
ENTRYPOINT ["java","-jar","xiaochen-file.jar"]