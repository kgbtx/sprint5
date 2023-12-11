module com.example.cs449project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cs449project to javafx.fxml;
    exports com.example.cs449project;
}