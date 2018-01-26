# Running the Project in Development Mode

`mvn spring-boot:run`

Wait for the application to start

Open http://localhost:8080/ to view the application.

Default credentials are admin@vaadin.com/admin for admin access and
barista@vaadin.com/barista for normal user access.

Note that when running in development mode, the application will not work in IE11.

# Running Integration Tests and Linter

Integration tests are implemented using TestBench. The tests take tens of minutes to run and are therefore included in a separate profile. To run the tests, execute

`mvn verify -Pit`

and make sure you have a valid TestBench license installed.

Run linter to check frontend code by adding `-DrunLint` to build/run command.

# Automatic Restart and Live Reload

To activate spring-boot-devtools is needed to:
1. Add spring-boot-devtools dependency
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
    <scope>runtime</scope>
</dependency>
```
2. Fork the process used to run the application by changing spring-boot-maven-plugin configuration
```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>${spring-boot.version}</version>
    <configuration>
        <fork>true</fork>
    </configuration>
</plugin>
```
3. Create and place into the `src/main/resources/META-INF` the following spring-devtools.properties
```
restart.include.flow=/flow.*\.jar
restart.include.vaadin=/vaadin.*\.jar
```
4. Optionally you might want to avoid the data generator to be run on each single reload, therefore, make H2 database store entities in file-system instead of in memory by adding the following lines to the `src/main/resources/application.properties`
```
spring.datasource.url=jdbc:h2:file:~/bakery-test-data
spring.jpa.hibernate.ddl-auto=update
```
To trigger the restart it is needed to update classpath.
In Eclipse it can be done automatically after save modified file.
In IntelliJ IDEA can be done manually `Build -> Build Project`

Live reload is supported and browser extensions can be found at http://livereload.com/extensions/.

# Running the Project in Production Mode

`mvn spring-boot:run -Dvaadin.productionMode`

The default mode when the application is built or started is 'development'. The 'production' mode is turned on by setting the `vaadin.productionMode` system property when building or starting the app.

In the 'production' mode all frontend resources of the application are passed through the `polymer build` command, which minifies them and outputs two versions: for ES5- and ES6-supporting browsers. That adds extra time to the build process, but reduces the total download size for clients and allows running the app in browsers that do not support ES6 (e.g. in Internet Explorer 11).

Note that if you switch between running in production mode and development mode, you need to do
```
mvn clean
```
before running in the other mode.

# Running Scalability Tests

Scalability tests can be run as follows

1. Configure the number of concurrent users and a suitable ramp up time in the end of the `src/test/scala/*.scala` files, e.g.:
	```setUp(scn.inject(rampUsers(300) over (300 seconds))).protocols(httpProtocol)```

2. If you are not running on localhost, configure the baseUrl in the beginning of the `src/test/scala/*.scala` files, e.g.:
	```val baseUrl = "http://my.server.com"```

3. Make sure the server is running at the given URL

4. Start a test from the command line, e.g.:
	 ```mvn -Pscalability gatling:execute -Dgatling.simulationClass=BaristaFlow```

5. Test results are stored into target folder, e.g.:
	```target/gatling/BaristaFlow-1487784042461/index.html```

# License
A paid Pro or Prime subscription is required for creating a new software project from this starter. After its creation, results can be used, developed and distributed freely, but licenses for the used commercial components are required during development. The starter or its parts cannot be redistributed as a code example or template.

For full terms, see LICENSE
