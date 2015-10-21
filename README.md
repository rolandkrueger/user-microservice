# user-microservice
Micro-service for user account management - written with Spring Boot

This project provides a micro-service for user data management with a public RESTful interface. The RESTful API uses Spring HATEOAS for linking individual resources. By that, it is only necessary to know the service's base URL to work with it. Starting with the root document you can follow links to access the service's individual resources.    

The service can be used as a remote Spring Security user details service. It is completely stateless and can thus be used for scaling.
 
The service offers interfaces to register new users, confirm user registrations, and to sign in and sign off users.

# Public API

This project is a multi-module Maven project divided into two sub-modules. The `service` module contains the user service itself implemented as a Spring Boot application.

The second module `public-api` provides a Java API that can be used to access the RESTful service from any Java application. This module offers an easy to use fluent API that mirrors the structure of the linked resources.

Following are a few examples that demonstrate the service's public API.

## Register a new user

```
UserServiceAPI.init("http://www.example.com/user-service")
    .userRegistrations()
    .create("john.doe", "passw0rd", "john.doe@example.com");
```

## Confirm a user registration

```
String confirmationToken = "a3be0-23...";
UserServiceAPI.init("http://www.example.com/user-service")
   .userRegistrations()
   .findByToken(confirmationToken)
   .confirmRegistration();
```

## Find a user by user name

```
Optional<UserApiData> alice =
    UserServiceAPI.init("http://www.example.com/user-service")
    .users()
    .search()
    .findUserForLogin("alice").getData().stream().findFirst();
```

## Sign in a user

```
UserApiData alice = UserServiceAPI.init("http://www.example.com/user-service")
    .users()
    .search()
    .findUserForLogin("alice").getData().stream().findFirst().get();
alice.getResource().loggedIn();
```

## Update a user

```
UserApiData alice = UserServiceAPI.init("http://www.example.com/user-service")
    .users()
    .search()
    .findUserForLogin("alice").getData().stream().findFirst().get();
alice.setEmail("new.email@example.com");
alice.getAuthorities().add(new AuthorityApiData("ADMIN_ROLE", ""));

alice.getResource().getUpdateResource().update();
```

## Read hashed password for a user

```
String alicePasswordHash = service()
    .users()
    .search().findByUsername("alice")
    .getData().stream().findFirst().get()
    .getResource()
    .useProjection(UserProjections.FULL_DATA)
    .read()
    .getPassword();
```