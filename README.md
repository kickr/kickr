# kickr

A kicker (áka table soccer) result tracker application.

![Screenshot](https://raw.githubusercontent.com/kickr/kickr/master/resources/screenshot.png)


## Development

### Setup

Create a file `app/kickr.development.yml` based on the provided sample configuration.

You will need MySQL / MariaDB to resemble the official development setup. Set it up and add it to the configuration.

Install the required client dependencies via

```
(cd client && bower install && npm install)
```

### Run

Run the backend:

```
(cd app && mvn clean compile exec:java)
```

Add the client:

```
(cd client && grunt auto-build)
```

Open [http://localhost:8280](http://localhost:8280) to inspect the website.