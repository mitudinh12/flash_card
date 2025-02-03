module com.flash_card {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires de.saxsys.mvvmfx;
    requires com.google.gson;
    requires com.google.auth;
    requires com.google.api.client;
//    requires com.sun.net.httpserver;
    requires jdk.httpserver;
    requires java.desktop;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires com.google.api.client.json.gson;
    requires com.google.api.client.auth;
    requires org.slf4j;

    opens com.flash_card to javafx.fxml;
    opens com.flash_card.model.datasource;
    opens com.flash_card.model.entity;
    opens com.flash_card.model.dao;
    opens com.flash_card.view_model.user to javafx.fxml;
    opens com.flash_card.view_model.user_auth to javafx.fxml;
    opens com.flash_card.view.auth;
    opens com.flash_card.view.homepage;



    exports com.flash_card.view_model.user;
    exports com.flash_card;
    exports com.flash_card.view.auth;
    exports com.flash_card.view_model.user_auth;
}