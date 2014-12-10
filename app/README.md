# app

This is the repository backend that provides the actual repository services as well as the client files.


## Overview

The application is tested, packaged and (optionally) run via maven.

```
mvn clean verify exec:java
```

It serves the client from `target/classes/web`. To ensure that the resources are up to date, execute `grunt auto-build` in the `../client` directory.

To package the application as a single `jar` file, ready for distribution execute

```
mvn clean package
```


## License

MIT