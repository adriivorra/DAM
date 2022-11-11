module com.adri.encrypterfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.adri.encrypterfx to javafx.fxml;
    exports com.adri.encrypterfx;
}