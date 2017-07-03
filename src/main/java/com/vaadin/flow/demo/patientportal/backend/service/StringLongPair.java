
package com.vaadin.flow.demo.patientportal.backend.service;

/**
 * @author mstahv
 */

public class StringLongPair {
    private String group;
    private Long count;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public StringLongPair(String ageGroup, Long count) {
        this.group = ageGroup;
        this.count = count;
    }
    
}
