# Running the Project in Development Mode

`mvn spring-boot:run`

Wait for the application to start

Open http://localhost:8080/ to view the application.

Default credentials are admin@vaadin.com/admin for admin access and
barista@vaadin.com/barista for normal user access.

Note that when running in development mode, the application will not work in IE11.

# Running integration tests

Integration tests are implemented using TestBench. The tests take tens of minutes to run and are therefore included in a separate profile. To run the tests, execute

`mvn verify -Pit`

and make sure you have a valid TestBench license installed.

# Running the Project in Production Mode

`mvn spring-boot:run -Dvaadin.productionMode`

The default mode when the application is built or started is 'development'. The 'production' mode is turned on by setting the `vaadin.productionMode` system property when building or starting the app.

In the 'production' mode all frontend resources of the application are passed through the `polymer build` command, which minifies them and outputs two versions: for ES5- and ES6-supporting browsers. That adds extra time to the build process, but reduces the total download size for clients and allows running the app in browsers that do not support ES6 (e.g. in Internet Explorer 11).
