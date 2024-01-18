FROM node:18

WORKDIR /xiaochen-web/
COPY . .
# 设置npm淘宝源
RUN npm config set registry https://registry.npm.taobao.org/
RUN npm install
RUN npm run build:pro

FROM nginx:alpine
LABEL MAINTAINER="zvz09"

COPY .docker-compose/nginx/conf.d/my.conf /etc/nginx/conf.d/my.conf
COPY --from=0 /xiaochen-web/dist /usr/share/nginx/html
RUN cat /etc/nginx/nginx.conf
RUN cat /etc/nginx/conf.d/my.conf
RUN ls -al /usr/share/nginx/html
