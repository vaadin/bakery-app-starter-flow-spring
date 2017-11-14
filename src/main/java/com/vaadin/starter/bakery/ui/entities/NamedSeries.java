package com.vaadin.starter.bakery.ui.entities;

import java.util.List;

public class NamedSeries {
    private List<Double> series;
    private String title;

    public NamedSeries() {}

    public List<Double> getSeries() {
        return series;
    }

    public void setSeries(List<Double> series) {
        this.series = series;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
