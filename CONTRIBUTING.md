# How to contribute

We welcome you to contribute! Learn how to setup a development environment and start hacking.


## Setup Development Environment

You need Java + maven (back-end technology) to build and run the application locally.


#### Setup Database

You will need to setup a MySQL / MariaDB database that persists the kickr data.

```
mysql> CREATE DATABASE kickr;
mysql> GRANT ALL ON kickr.* TO 'kickr_user'@'localhost' IDENTIFIED BY 'kickr_password';
mysql> EXIT;
```

Create a configuration file `config/dev.yml` based on the provided [sample configuration](https://github.com/kickr/kickr/blob/master/config/dev.sample.yml). Update it with your chosen database name, user and password.



## Migrate Database

The application uses database migrations. You can use the `db migrate` task provided by the kickr application itself to upgrade your database schema to the latest version.

```
(cd app && mvn package && \
   java -jar target/backend-0.0.1.jar db migrate config/dev.yml)
```


## Run Application

If you have set up a development environment use the command line to run the application.

```
(cd app && mvn clean compile exec:java)
```


Open [http://localhost:8280](http://localhost:8280) to inspect the website.
