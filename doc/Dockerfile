FROM centos:centos8
RUN mkdir -p /usr/share/jekins && \
    yum install -y java-1.8.0-openjdk podman && \
    sed -i 's/#mount_program/mount_program/g' /etc/containers/storage.conf && \
    sed -i 's/mountopt/#mountopt/g' /etc/containers/storage.conf && \
USER root
WORKDIR /home/jenkins
ENTRYPOINT ["jenkins-slave"]
