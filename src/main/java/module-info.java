module org.example.kurs {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j;

    opens org.example.kurs to javafx.fxml;
    exports org.example.kurs;
}