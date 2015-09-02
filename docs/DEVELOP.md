# Development

This should give you a quick guide how to develop the application.


## Start required services

> Works on Arch Linux or other systemd enabled Linux distributions with Docker installed

This configures and starts up a MySQL database + Elastic Search via Docker.

It assumes you have the MySQL password exposed as `MYSQL_PASS` as an environment variable.


```
systemctl start docker

mkdir -p $(pwd)/data/mysql/var
docker run -d -p 3306:3306 -v "$(pwd)/data/mysql/var":/var/lib/mysql -e MYSQL_PASS=$MYSQL_PASS tutum/mysql

mkdir -p $(pwd)/data/elasticsearch/var
docker run -p 9300:9300 -p 9200:9200 -d -v "$(pwd)/data/elasticsearch/var":/usr/share/elasticsearch/data elasticsearch
```

Data is being persisted in the `data` directory.


## Run

Install [amvn](https://github.com/nikku/awesome-maven) for most fun during development. Use it like this:

```
amvn compile exec:java --watch --reload
```


## Database Administration

#### Import Data

```
(cd ../data && cat kickr-dump.sql) | mysql -h 127.0.0.1 -P 3306 -u admin -p -D kickr
```

### Inspect

```
mysql -h 127.0.0.1 -P 3306 -u admin -p -D kickr
```


## Database Migration

#### Inspect

mvn clean package && java -jar target/backend-0.0.1.jar db info config/dev.yml


#### Update (forward)

mvn clean package && java -jar target/backend-0.0.1.jar db migrate config/dev.yml


# OpenShift

```
export PATH=$HOME/app-root/data/jdk-8/bin/:$PATH

(cd $HOME/app-root/repo/app && java -jar target/backend-0.0.1.jar db migrate config/openshift.yml)
```

## import dump

```
(cd app-root/data/ && cat test-dump.sql) | mysql -h $OPENSHIFT_MYSQL_DB_HOST -P $OPENSHIFT_MYSQL_DB_PORT -u $OPENSHIFT_MYSQL_DB_USERNAME -p -D $OPENSHIFT_APP_NAME
```