# Bakery app starter for Flow

This is the Bakery full stack starter for Flow, a fully functional and tested full stack application built with Vaadin Flow and Spring.

## Build and Run

build: `mvn clean install -Dvaadin.productionMode`

run: `mvn jetty:run -Dvaadin.productionMode`

## Running integration tests

Integration tests are implemented using TestBench. The tests take tens of minutes to run and are therefore included in a separate profile. To run the tests, execute

`mvn verify -Pit`

and make sure you have a valid TestBench license installed.

## Build and Run (in development)

In the development mode the app build / start time is shorter, but the app would not work in IE11.

build: `mvn clean install`

run:  `mvn jetty:run`


## Development vs. Production mode

The default mode when the application is built or started is 'development'. The 'production' mode is turned on by setting the `vaadin.productionMode` system property when building or starting the app.

In the 'production' mode all frontend resources of the application are passed through the `polymer build` command, which minifies them and outputs two versions: for ES5- and ES6-supporting browsers. That adds extra time to the build process, but reduces the total download size for clients and allows running the app in browsers that do not support ES6 (e.g. in Internet Explorer 11).


## Contributing to the Bakery App Starter

Please check the [guidelines for contributing](CONTRIBUTING.md) for details on how make pull requests or report issues.
