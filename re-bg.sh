#!/bin/bash

# 设置脚本在遇到任何非零返回值的命令时立即退出，以保证中间执行出错就停止后续执行
set -e

# 输出开始执行的提示信息
echo "开始执行脚本，首先进入项目主文件夹"

# 进入指定文件夹，如果文件夹不存在则报错并退出脚本
cd /home/user/rucdb_vclass || {
    echo "无法进入 /home/user/rucdb_vclass 文件夹，请检查路径是否正确"
    exit 1
}
echo "已成功进入 /home/user/rucdb_vclass 文件夹，接下来执行 mvn clean 命令"

# 执行 mvn clean 命令，将输出重定向到 /dev/null 来忽略其详细输出
mvn clean > /dev/null
echo "mvn clean 命令执行成功，接着执行 mvn package 命令"

# 执行 mvn package 命令，将输出重定向到 /dev/null 来忽略其详细输出
mvn package > /dev/null
echo "mvn package 命令执行成功，现在检查 ruoyi-admin.jar 文件是否存在"

# 检查 ruoyi-admin.jar 文件是否存在，不存在则报错并退出脚本
if [ ! -f /home/user/rucdb_vclass/ruoyi-admin/target/ruoyi-admin.jar ]; then
    echo "/home/user/rucdb_vclass/ruoyi-admin/target/ruoyi-admin.jar 文件不存在，请检查构建是否成功"
    exit 1
fi
echo "ruoyi-admin.jar 文件存在，现在开始移动该文件到指定位置"

# 使用 sudo 权限移动 ruoyi-admin.jar 文件到 /home/web/server 下，捕获移动命令执行结果，失败则报错并退出脚本
mv /home/user/rucdb_vclass/ruoyi-admin/target/ruoyi-admin.jar /home/web/server || {
    echo "移动 ruoyi-admin.jar 文件到 /home/web/server 时出错，请检查权限及目标文件夹是否可写入"
    exit 1
}
echo "ruoyi-admin.jar 文件已成功移动到 /home/web/server 下，接下来修改该文件的所属用户和组为 web:web"

# 使用 sudo 权限修改 ruoyi-admin.jar 文件所属用户和用户组为 web:web，并修改文件权限为 644，
# 如果修改权限操作失败，输出提示信息并退出脚本
chown web:web /home/web/server/ruoyi-admin.jar || {
    echo "修改 ruoyi-admin.jar 文件所属用户和组权限时出错，请检查权限及相关配置"
    exit 1
}
chmod 644 /home/web/server/ruoyi-admin.jar || {
    echo "修改 ruoyi-admin.jar 文件权限时出错，请检查权限及相关配置"
    exit 1
}

echo "操作成功完成"

