#!/bin/bash

# 设置脚本在遇到任何非零返回值的命令时立即退出，以保证中间执行出错就停止后续执行
set -e

# 设置npm 环境
export PATH=/usr/local/lib/nodejs/node-v22.12.0-linux-x64/bin:$PATH

# 进入前端项目所在文件夹，如果文件夹不存在则报错并退出脚本
cd /home/user/rucdb_vclass/ruoyi-ui || {
    echo "无法进入 /home/user/rucdb_vclass/ruoyi-ui 文件夹，请检查路径是否正确"
    exit 1
}
echo "已成功进入 /home/user/rucdb_vclass/ruoyi-ui 文件夹，接下来执行 npm install --verbose 命令"

# 设置npm阿里源
npm config set registry http://registry.npmmirror.com

# 执行 npm install 命令，将输出重定向到 /dev/null 来忽略其详细输出
npm install > /dev/null
echo "npm install 命令执行成功，接着执行 npm run build:prod 命令"

# 执行 npm run build:prod 命令，将输出重定向到 /dev/null 来忽略其详细输出
npm run build:prod > /dev/null
echo "npm run build:prod 命令执行成功，现在开始移动前端项目生成的 dist 文件夹"

# 使用 sudo 权限删除 dist 文件夹
sudo rm -rf /home/web/front/dist

# 使用 sudo 权限移动新生成的 dist 文件夹到指定位置，捕获移动命令执行结果
sudo mv dist /home/web/front || {
    echo "移动 dist 文件夹到 /home/web/front 时出错，请检查权限及目标文件夹是否可写入"
    exit 1
}
echo "dist 文件夹已成功移动到 /home/web/front 下，接下来修改 dist 文件夹的所属用户和组为 web:web"

# 使用 sudo 权限修改 dist 文件夹所属用户和用户组为 web:web，
# 如果修改权限操作失败，输出提示信息并退出脚本
sudo chown -R web:web /home/web/front/dist || {
    echo "修改 dist 文件夹所属用户和组权限时出错，请检查权限及相关配置"
    exit 1
}

sudo /home/web/front/nginx/sbin/nginx -s reload || {
    echo "nginx 重启失败"
    exit 1
}
echo "nginx 重启成功"

echo "操作成功完成"


