module com.flash_card {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    opens com.flash_card to javafx.fxml;
    opens com.flash_card.model.datasource;
    exports com.flash_card;
}