FROM ubuntu:22.04

# copy the project sources
COPY . /AWSBackupUtility
WORKDIR /AWSBackupUtility

ARG key_id
ENV AWS_ACCESS_KEY_ID=$key_id
ARG key
ENV AWS_SECRET_ACCESS_KEY=$key

# avoid issues with potentially non-ascii names of backup files
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

RUN apt-get update && apt-get install -y \
    software-properties-common \
    wget \
  && apt-get clean

# install java
RUN wget -O- https://apt.corretto.aws/corretto.key | apt-key add
RUN add-apt-repository -y 'deb https://apt.corretto.aws stable main'
RUN apt-get update && apt-get install -y \
    java-16-amazon-corretto-jdk \
  && apt-get clean

# build AWS Backup Utility
# as we can not ensure network reliability, we have to skip the tests while building the container
RUN ./gradlew build -x test

# colorouble promt
RUN echo "export PS1=\"\[\e]0;\u@\h: \w\a\]${debian_chroot:+($debian_chroot)}\[\033[1;33m\]\u@\h\[\033[00m\]:\[\033[01;34m\]\w\[\033[00m\]# \"" >> ~/.bashrc

ENTRYPOINT ["/bin/bash"]
