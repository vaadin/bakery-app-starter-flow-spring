package com.vaadin.flow.demo.patientportal.ui;

import com.vaadin.annotations.ClientDelegate;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.patientportal.app.security.SecuredViewAccessControl;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;
import com.vaadin.ui.AttachEvent;
import com.vaadin.ui.History;
import com.vaadin.ui.UI;

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
        void setUsersMenuHidden(boolean usersMenuHidden);
        void setAdminMenuHidden(boolean usersMenuHidden);
        void setProductsMenuHidden(boolean productsMenuHidden);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UserModel user = new UserModel();
        user.setName("Kate");
        user.setImage("https://randomuser.me/api/portraits/women/10.jpg");
        user.setAlarms(true);
        getModel().setUser(user);
        getModel().setUsersMenuHidden(!SecuredViewAccessControl.isAccessGranted(UsersView.class));
        getModel().setAdminMenuHidden(!SecuredViewAccessControl.isAccessGranted(AdminView.class));
        getModel().setProductsMenuHidden(!SecuredViewAccessControl.isAccessGranted(AdminView.class));
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
