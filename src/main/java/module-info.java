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

    opens com.flash_card to javafx.fxml;
    opens com.flash_card.model.datasource;
    opens com.flash_card.model.entity;
    opens com.flash_card.model.dao;
    opens com.flash_card.view_model.user to javafx.fxml;
    opens com.flash_card.view_model.user_auth to javafx.fxml;
    opens com.flash_card.view.auth;
    opens com.flash_card.view.homepage;



    exports com.flash_card.view_model.user;
    opens com.flash_card.view to javafx.fxml;
    exports com.flash_card;
    exports com.flash_card.view.auth;
    exports com.flash_card.view_model.user_auth;
    exports com.flash_card.view;
    exports com.flash_card.view.createFlashcardPage;
    opens com.flash_card.view.createFlashcardPage to javafx.fxml;
    exports com.flash_card.view.flashcardPage;
    opens com.flash_card.view.flashcardPage to javafx.fxml;
    exports com.flash_card.view.classPage;
    opens com.flash_card.view.classPage to javafx.fxml;
    exports com.flash_card.view.teacherPage;
    opens com.flash_card.view.teacherPage to javafx.fxml;
}