package com.vaadin.flow.demo.patientportal.ui;

import com.vaadin.annotations.ClientDelegate;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;
import com.vaadin.ui.AttachEvent;
import com.vaadin.ui.History;
import com.vaadin.ui.UI;

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

    @ClientDelegate
    private void logout(){
        UI ui = getUI().get();
        History history = ui.getPage().getHistory();
        ui.getSession().getSession().invalidate();
        //Reload the page after invalidating the session will redirect
        // to login page
        history.go(0);
    }
}
