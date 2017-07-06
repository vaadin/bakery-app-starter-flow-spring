# Patient Portal UI

This is the bakery app starter for Flow.
The patient-demo-portal project has been used as a starting point for this project.

## Build and Run

build: `mvn clean install -Dvaadin.productionMode`

run: `mvn jetty:run -Dvaadin.productionMode`



## Build and Run (in development)

In the development mode the app build / start time is shorter, but the app would not work in IE11.

build: `mvn clean install`

run:  `mvn jetty:run`


## Development vs. Production mode

The default mode when the application is built or started is 'development'. The 'production' mode is turned on by setting the `vaadin.productionMode` system property when building or starting the app.

In the 'production' mode all frontend resources of the application are passed through the `polymer build` command, which minifies them and outputs two versions: for ES5- and ES6-supporting browsers. That adds extra time to the build process, but reduces the total download size for clients and allows running the app in browsers that do not support ES6 (e.g. in Internet Explorer 11).