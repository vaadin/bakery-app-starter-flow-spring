package com.vaadin.flow.demo.patientportal.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.patientportal.backend.data.entity.User;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;

/**
 * Created by viktor on 03/07/2017.
 */
@UIScope
@Tag("bakery-navigation")
@HtmlImport("/src/app/bakery-navigation.html")
public class BakeryNavigation extends PolymerTemplate<BakeryNavigation.Model> {
    public static class UserModel {
        private String name;
        private String image;
        private boolean alarms;

        public UserModel() {
            this.name = "Kate";
            this.image = "https://randomuser.me/api/portraits/women/10.jpg";
            this.alarms = true;
        }

        public UserModel(User user) {
            this.name = user.getName();
            this.image = "https://randomuser.me/api/portraits/women/10.jpg";
            this.alarms = true;
        }

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

    public void onLocationChange(LocationChangeEvent event) {
        getModel().setPage(event.getLocation().getPath());
    }
}
