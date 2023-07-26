module geneticisst.checkerss {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;

    opens geneticisst.checkerss to javafx.fxml;
    exports geneticisst.checkerss;
}