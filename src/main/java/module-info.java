module com.daniel.reproductor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens com.daniel.reproductor to javafx.fxml;
    exports com.daniel.reproductor;
    exports com.daniel.reproductor.controller;
    opens com.daniel.reproductor.controller to javafx.fxml;
}