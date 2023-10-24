# Spring Zeebe Client without Boot

When you have to integrate a lot of existing business logic developed with the
plain Spring Framework, maybe configured with the old application-context.xml
files, you don't want to use the spring-boot-starter-camunda and force
everything into a Spring Boot project.

Some projects, that are required to use an application server like Tomcat of
JBoss to get to production can't use the fat jar started from the command line.

This project tries the java configuration style of Spring to get the Zeebe
client up and running and register workers to pick up jobs.

The first way was to copy a lot of beans from the spring-boot-starter-camunda
library to start the client.

The second way was to add the classes of the spring-boot-starter-camunda and use
annotations to start the client.

Major drawbacks of the current version

- application.properties is not picked up

Old code snippets are left by intent with comments to allow more research for
the best solution in the future.
