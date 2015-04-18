# How to contribute

We welcome you to contribute! See below how to setup a development environment and start hacking right away.


## Setup a Development Environment

You need Java + maven (back-end technology) and NodeJS + npm (front-end technology) to build and run the application locally. See our [README](https://github.com/kickr/kickr#technology-stack) to get a broader view on our technology stack.


### Initialize MySQL / MariaDB

You will need MySQL / MariaDB to resemble the official development setup. Set it up:

```
mysql> CREATE DATABASE kickr;
mysql> GRANT ALL ON kickr.* TO 'kickr_user'@'localhost' IDENTIFIED BY 'kickr_password';
mysql> EXIT;
```


### Create a Configuration

Create a file `app/kickr.development.yml` based on the provided [sample configuration](https://github.com/kickr/kickr/blob/master/app/kickr.development.sample.yml). Update it with your with your chosen database name, user and password.


### Install Client Dependencies

Install the stuff that is needed to build the application client via

```
(cd client && bower install && npm install && npm install -g grunt-cli)
```


## Migrate Database

The application uses database migrations. You can use the `db migrate` task provided by the kickr application itself to upgrade your database schema to the latest version.

```
(cd app && mvn package && \
   java -jar target/backend-0.0.1.jar db migrate kickr.development.yml)
```


## Run the Application

If you have set up a development environment use the command line to run the application.

#### Backend

```
(cd app && mvn clean compile exec:java)
```

#### Client

```
(cd client && grunt auto-build)
```

Open [http://localhost:8280](http://localhost:8280) to inspect the website.
