module com.flash_card {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    opens com.flash_card to javafx.fxml;
    opens com.flash_card.model.datasource;
    opens com.flash_card.view to javafx.fxml;
    opens com.flash_card.model.entity to org.hibernate.orm.core;

    exports com.flash_card;
    exports com.flash_card.view;
}