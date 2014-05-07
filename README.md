fabric8-osgi-flyway-example
===========================

Fabric8 + Felix OSGi managed persistence (Aries + Hibernate) + REST service demonstration code + Flyway database migration integration.

# Pre-requisites

* JDK 7
* Maven 3.1.0 or newer

# Build and install

```
mvn clean install
```

# Provisioning

## Installation and initial configuration

* Download [latest build](https://repository.jboss.org/nexus/content/repositories/ea/io/fabric8/fabric8-karaf/) for ```fabric-karaf``` and extract it.
*(tested on fabric8-karaf-1.0.0.redhat-348)*
* Extract it
* ```cd``` to the newly extracted folder
* Define default administrative user (login: **admin**, password:**admin**) by uncommenting the last line of ```etc/user.properties```
* Start Fabric
```no-highlight
bin/fusefabric
```

If everything goes well, you should get a Fabric shell that looks like this:

```
Please wait while Fabric8 is loading...
100% [========================================================================]

______    _          _      _____
|  ___|  | |        (_)    |  _  |
| |_ __ _| |__  _ __ _  ___ \ V /
|  _/ _` | '_ \| '__| |/ __|/ _ \
| || (_| | |_) | |  | | (__| |_| |
\_| \__,_|_.__/|_|  |_|\___\_____/
  Fabric8 Container (1.0.0.redhat-340)
  http://fabric8.io/

Type 'help' to get started
and 'help [cmd]' for help on a specific command.
Hit '<ctrl-d>' or 'osgi:shutdown' to shutdown this container.

Open a browser to http://localhost:8181 to access the management console

Create a new Fabric via 'fabric:create'
or join an existing Fabric via 'fabric:join [someUrls]'

Fabric8:karaf@root>
```

## Start Fabric Ensemble
```
fabric:create --clean --wait-for-provisioning
```

## Define our own profile
```
profile-create --parents example-quickstarts-rest persistence-flyway-example
profile-edit --repositories mvn:com.github.pires.example/feature-persistence/0.1-SNAPSHOT/xml/features persistence-flyway-example
profile-edit --features persistence-aries-hibernate persistence-flyway-example
profile-edit --bundles mvn:com.github.pires.example/datasource-postgresdb/0.1-SNAPSHOT persistence-flyway-example
profile-edit --bundles mvn:com.github.pires.example/dal/0.1-SNAPSHOT persistence-flyway-example
profile-edit --bundles mvn:com.github.pires.example/dal-impl/0.1-SNAPSHOT persistence-flyway-example
profile-edit --bundles mvn:com.github.pires.example/rest/0.1-SNAPSHOT persistence-flyway-example
profile-edit --bundles mvn:com.github.pires.example/fabric8-osgi-flyway/0.1-SNAPSHOT persistence-flyway-example
profile-edit --bundles mvn:org.flywaydb/flyway-core/3.0  persistence-flyway-example
```

## Create and run new container with newly created profile

```
container-create-child --profile persistence-flyway-example root test
```

# Testing

In Hawt.io UI, go to ```API``` tab (in the parent container), check the host and port where ```UserService``` is available and point it down. Test the REST endpoint as you wish!

## REST API (JSON)

Create new user
```
PUT /user

Example JSON:

{
    "name":"fferreira",
    "properties":{"value":"{\"string1\":{\"mandatory\":false,\"value\":\"teste\",\"type\":\"string\"},\"num1\":{\"mandatory\":false,\"value\":123,\"type\":\"number\"}}"}
}

```

Count users
```
GET /user/count
```

List users
```
GET /user
```

# Troubleshooting

If ```rest``` bundle is waiting on ```UserService```, it's because ```dal-impl``` bundle is waiting on **hibernate-osgi** to announce its availability. Restart **hibernate-osgi** bundle, *et voilá*!

### Differences from original repository
- Working with postgreSQL
- Working with a JSON object as PostgreSQL column
