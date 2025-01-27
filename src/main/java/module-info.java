module com.flash_card {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.flash_card to javafx.fxml;
    exports com.flash_card;
}