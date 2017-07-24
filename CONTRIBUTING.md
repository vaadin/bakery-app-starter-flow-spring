# Contributing to the Bakery App Starter

First off, thanks for taking the time to contribute!

The following is a set of guidelines to make cooperation between different individuals smooth and efficient. These are mostly guidelines, not rules. Use your best judgment, and feel free to propose changes to this document in a pull request.


#### Table of Contents

[Ways to contribute](#ways-to-contribute)
  * [Reporting Bugs](#reporting-bugs)
  * [Suggesting Enhancements](#suggesting-enhancements)
  * [Making Pull Requests](#making-pull-requests)

[Styleguides](#styleguides)
  * [Java Styleguide](#java-styleguide)
  * [JavaScript and CSS Styleguide](#javascript-and-css-styleguide)
  

## Ways to contribute

### Reporting Bugs

Please report bugs by opening issues with the `bug` label in [project's GitHub repo](https://github.com/vaadin/bakery-app-starter-flow-spring/issues/new?labels=bug).

When reporting a bug, please make sure to provide enough details and context so that the development team can reproduce the issue and understand what is the expected behavior. 


### Suggesting Enhancements

Please suggest enhancements by opening issues with the `enhancement` label in [project's GitHub repo](https://github.com/vaadin/bakery-app-starter-flow-spring/issues/new?labels=enhancement).

When reporting a bug, please make sure to provide enough background for your use case and a clear description of what is expected.


### Making Pull Requests

When making pull requests please make sure to follow the [styleguides](#styleguides).


## Styleguides

### Java Styleguide

_Tabs for indentation._

This repo follows the Eclipse Oxygen defaults for Java code.


### JavaScript and CSS Styleguide

_Two spaces for indentation._

This repo uses [ESLint](http://eslint.org/) for linting JavaScript code and [stylelint](https://stylelint.io/) for linting CSS. You can check if your code is following our standards by running `npm run lint` inside the `src/main/frontend` folder. It will automatically lint all `.js` and `.css` files, as well as `<style>` and `<script>` tags inside `.html` files.
 
The ESLint and stylelint configs are the same as in the [Vaadin Core Elements](https://github.com/vaadin/vaadin-element-skeleton) repo.
