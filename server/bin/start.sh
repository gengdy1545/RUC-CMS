nohup java -jar -Duser.timezone=Asia/Shanghai -Dspring.config.location=file:/home/web/server/config/application.yml -Dspring.config.additional-location=file:/home/web/server/config/application-druid.yml /home/web/server/ruoyi-admin.jar >/home/web/server/logs/nohup.log 2>&1 &
#nohup java -jar /home/web/server/ruoyi-admin.jar >/home/web/server/logs/nohup.log 2>&1 &
