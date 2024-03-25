# Simple Sling Authenticator

This repository exposes a Sling Servlet under /servcies/support/auth which will return `Hello World` as response.

The service Support Auth Handler will set the authentication if the Authorization header is set with type Basic Auth.

# How to use

Install the bundle and set a debug logger on the `com.adobe.aem.support.authentication` package.

## Unsuccesful Auth 

curl -v -u "admin:admin2" -X POST http://localhost:4502/services/support/auth

## Successful Auth

curl -v -u "admin:admin" -X POST http://localhost:4502/services/support/auth

# How to build

`mvn clean install`