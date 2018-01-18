'use strict';

var gulp = require('gulp');
var eslint = require('gulp-eslint');
var htmlExtract = require('gulp-html-extract');
var stylelint = require('gulp-stylelint');

gulp.task('lint', ['lint:js', 'lint:html', 'lint:css']);

gulp.task('lint:js', function() {
  return gulp.src([
    '../src/main/webapp/frontend/*.js',
    '!../src/main/webapp/frontend/gridConnector.js',
    '../src/main/webapp/frontend/src/**/*.js',
    '../src/main/webapp/frontend/test/**/*.js'
  ])
    .pipe(eslint())
    .pipe(eslint.format())
    .pipe(eslint.failAfterError('fail'));
});

gulp.task('lint:html', function() {
  return gulp.src([
    '../src/main/webapp/frontend/*.html',
    '!../src/main/webapp/frontend/flow-component-renderer.html',
    '!../src/main/webapp/frontend/flow-grid-component-renderer.html',
    '../src/main/webapp/frontend/src/**/*.html',
    '../src/main/webapp/frontend/test/**/*.html'
  ])
    .pipe(htmlExtract({
      sel: 'script, code-example code',
      strip: true
    }))
    .pipe(eslint())
    .pipe(eslint.format())
    .pipe(eslint.failAfterError('fail'));
});

gulp.task('lint:css', function() {
  return gulp.src([
    '../src/main/webapp/frontend/*.html',
    '!../src/main/webapp/frontend/flow-component-renderer.html',
    '!../src/main/webapp/frontend/flow-grid-component-renderer.html',
    '../src/main/webapp/frontend/src/**/*.html',
    '../src/main/webapp/frontend/test/**/*.html'
  ])
    .pipe(htmlExtract({
      sel: 'style'
    }))
    .pipe(stylelint({
      reporters: [
        {formatter: 'string', console: true}
      ]
    }));
});
