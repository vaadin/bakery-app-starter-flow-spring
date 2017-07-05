package com.vaadin.flow.demo.patientportal.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;
import com.vaadin.ui.AttachEvent;

/**
 * Created by viktor on 03/07/2017.
 */
@UIScope
@Tag("bakery-navigation")
@HtmlImport("frontend://src/app/bakery-navigation.html")
public class BakeryNavigation extends PolymerTemplate<BakeryNavigation.Model> {
    public static class UserModel {
        private String name;
        private String image;
        private boolean alarms;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public boolean isAlarms() {
            return alarms;
        }

        public void setAlarms(boolean alarms) {
            this.alarms = alarms;
        }
    }

    public interface Model extends TemplateModel {
        void setPage(String page);
        void setUser(UserModel user);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UserModel user = new UserModel();
        user.setName("Kate");
        user.setImage("https://randomuser.me/api/portraits/women/10.jpg");
        user.setAlarms(true);
        getModel().setUser(user);
    }

    public void onLocationChange(LocationChangeEvent event) {
        getModel().setPage(event.getLocation().getPath());
    }
}
