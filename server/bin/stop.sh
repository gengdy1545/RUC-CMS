JAVA_PID=$(ps -ef | grep ruoyi-admin.jar | grep -v grep | awk '{print $2}')
if [ -n "$JAVA_PID" ]; then
    echo "Killing Java process with PID: $JAVA_PID"
    kill $JAVA_PID
else
    echo "No Java process found running the JAR: $JAR_NAME"
fi
