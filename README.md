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

# Running the Project in Production Mode

`mvn spring-boot:run -Dvaadin.productionMode`

The default mode when the application is built or started is 'development'. The 'production' mode is turned on by setting the `vaadin.productionMode` system property when building or starting the app.

In the 'production' mode all frontend resources of the application are passed through the `polymer build` command, which minifies them and outputs two versions: for ES5- and ES6-supporting browsers. That adds extra time to the build process, but reduces the total download size for clients and allows running the app in browsers that do not support ES6 (e.g. in Internet Explorer 11).

Note that if you switch between running in production mode and development mode, you need to do
```
mvn clean
```
before running in the other mode.
# License
A paid Pro or Prime subscription is required for creating a new software project from this starter. After its creation, results can be used, developed and distributed freely, but licenses for the used commercial components are required during development. The starter or its parts cannot be redistributed as a code example or template.

For full terms, see LICENSE
