module ru.shapov.lab2mlt {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;


    opens ru.shapov.lab2mlt to javafx.fxml;
    exports ru.shapov.lab2mlt;
}