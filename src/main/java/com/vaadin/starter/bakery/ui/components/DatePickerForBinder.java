package com.vaadin.starter.bakery.ui.components;


import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;

/**
 * date-picker-for-binder extends vaadin-date-picker in order to override the
 * `detached` method that causes problems by updating the invalid attribute when
 * date-picker is inside a dialog and it is closed.
 */
@Tag("date-picker-for-binder")
@HtmlImport("src/components/date-picker-for-binder.html")
public class DatePickerForBinder extends DatePicker {
}
