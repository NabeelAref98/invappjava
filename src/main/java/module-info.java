module com.wgu.software1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.wgu.software1.Controllers to javafx.fxml,javafx.graphics;
    opens com.wgu.software1 to javafx.fxml,javafx.graphics;
    opens com.wgu.software1.Data to javafx.base;

//    exports com.wgu.software1.;
}