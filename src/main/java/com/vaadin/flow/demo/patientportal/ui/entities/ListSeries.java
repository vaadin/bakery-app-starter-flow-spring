package com.vaadin.flow.demo.patientportal.ui.entities;

import java.util.List;

public class ListSeries {
    private List<String> series;
    private String title;

    public ListSeries() {}

    public List<String> getSeries() {
        return series;
    }

    public void setSeries(List<String> series) {
        this.series = series;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
