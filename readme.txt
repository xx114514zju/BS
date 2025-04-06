首先，需要有docker环境
打包命令（需要在mqtt-docker这一目录下运行）：
docker build -t iot-manager .
运行命令：
docker run -d -p 8088:80 iot-manager

