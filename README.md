# RUC-CMS
CMS based on the RuoYi framework.

## Basic Installation

### Install MySQL

To install MySQL:

``` shell
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

> Note that for mysql 8+, you may have to set a native password for mysql root user before running mysql_secure_installation, for example:
> ``` shell
> sudo mysql
> mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'your_root_password';
> mysql> exit
> sudo mysql_secure_installation
> ```

Login MySQL and create a user and a metadata database:

``` shell
CREATE USER 'vtr_manager'@'%' IDENTIFIED BY 'password';
CREATE DATABASE ruc_db_vclass;
GRANT ALL PRIVILEGES ON ruc_db_vclass.* to 'vtr_manager'@'%';
FLUSH PRIVILEGES;
```

Ensure that MySQL server can be accessed remotely. Sometimes the default MySQL configuration binds the server to localhost thus declines remote connections.


Use `rucdb_vclass/sql/rucdb.sql` to create tables in `ruc_db_vclass`.

### Install JDK and Maven

Install a JDK using `apt`. JDK 8 is tested.

``` shell
sudo apt install openjdk-8-jdk openjdk-8-jre
```

Maven 3.6.3 is tested. You can download `apache-maven-xxx-bin.tar.gz` from [apache-maven](https://archive.apache.org/dist/maven/) and extract it to a suitable directory (e.g. /opt).

``` shell
tar -zxvf apache-maven-xxx-bin.tar.gz -C /path/to/installed
```

Add it to the user's environment variables.

``` shell
# open .bashrc
vim ~/.bashrc

# append maven path to PATH
export M2_HOME="/opt/apache-maven-xxx"
export PATH=$M2_HOME/bin:$PATH

# make the environment variable effective
source ~/.bashrc
```

Ensure Maven is using the required JDK:

``` shell
mvn -v
```

The printed Java version should be consistent to the Java version printed by `java --version`. Otherwise ensure `JAVA_HOME` is pointing to the `/path/to/the required/jdk`.

### Install redis

Install a Redis using `apt`. Redis 5.0.7 is tested.

``` shell
sudo apt update
sudo apt install redis-server
```

Once the installation is complete, the Redis service will start automatically. To check the status of the service, enter the following command:

``` shell
sudo systemctl status redis-server
```

If you are unable to connect to redis, modify the redis configuration file in /etc/redis/redis.conf.

``` shell
# Accept connection requests from any network address
bind 0.0.0.0

# Disable Protected Mode
protected-mode no

# Disable Daemon Mode
daemonize no
```

Restart redis.

``` shell
sudo systemctl restart redis-server
```

### Install Node.js
Install Node.js from [nodejs](https://nodejs.org/en/download/package-manager).

And modify the npm enviroment in `re-deploy.sh` and `re-fg.sh`.

### Install RUC-CMS
#### Creat Specialized Users

Create `web` user and grant root privileges.

``` shell
sudo adduser web
sudo usermod -aG sudo web
su - web
mkdir -p /home/web/temp
```

Modify the `profile` in `server/config/application.yml`.

If you set password of Redis, you need to modify the Redis password in `server/config/application.yml`.

Modify the `username` and `password` of MySQL in `server/config/application-druid.yml`.

Copy or move `front` and `server` directories to `web` user directory.

``` shell
sudo cp -r front /home/web/
sudo chown -R web:web /home/web/front
sudo cp -r server /home/web/
sudo chown -R web:web /home/web/server
```

#### Package bg program
In the rucdb_vclass folder, run `mvn clean && mvn package`. You will get `ruoyi-admin.jar` in the `rucdb_vclass/ruoyi-admin/target/` folder, you need to move this file to `/home/web/server`.

``` shell
cd rucdb_vclass
mvn clean && mvn package
sudo cp ruoyi-admin/target/ruoyi-admin.jar /home/web/server
sudo chown web:web /home/web/server/ruoyi-admin.jar
```

### Install nginx

Unzip `nginx-1.25.1.tar.gz` and install.
``` shell
tar -zxvf nginx-1.25.1.tar.gz -C /home/web/temp/
cd /home/web/temp/nginx-1.25.1
sudo apt install gcc libpcre3 libpcre3-dev zlib1g-dev openssl libssl-dev
./configure --prefix=/home/web/front/nginx
make
make install
```

#### Package fg program
After a successful build, a `dist` folder will be created in the root directory, which contains the built and packaged files, usually static files such as \*.js, \*.css, index.html and so on.

``` shell
cd rucdb_vclass/ruoyi-ui
npm config set registry http://registry.npmmirror.com
npm install
npm run build:prod
sudo cp -r dist /home/web/front
sudo chown -R web:web /home/web/front/dist
```

#### Start RUC-CMS
##### Start `ruoyi-admin.jar`

``` shell
cd /home/web/server/bin/
sh start.sh
sh log.sh # view log
```

##### Start `nginx`

Run `/home/web/front/nginx/sbin/nginx -t`
Check if it returns: nginx: config file /home/web/front/nginx/conf/nginx.conf Syntax is normal nginx: config file /home/web/front/nginx/conf/nginx.conf Test succeeded.

``` shell
/home/web/front/nginx/sbin/nginx
```

##### Reload

If you have modified the front-end code  `ruoyi-ui`, you can either run `re-fg.sh` as `root`, or repackage the front-end code as described above and restart nginx.

``` shell
/home/web/nginx/sbin/nginx -s reload
```

If you have modified the backend code, you can either run `re-bg.sh` as root, or repackage the backend code as described above. Either way you need to restart `ruoyi-admin.jar`.
``` shell
sh stop.sh
sh start.sh
sh log.sh # view log
```

You can also just run `re-deploy.sh` to redeploy the front-end and back-end code.
