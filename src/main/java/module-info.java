module io.github.ppetrbednar.palladium {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires json.simple;
    requires java.sql;
    requires org.apache.commons.text;
    requires org.apache.logging.log4j;
    requires fontawesomefx;
    requires java.desktop;
    requires com.jfoenix;

    opens io.github.ppetrbednar.dstr to javafx.fxml;
    opens io.github.ppetrbednar.dstr.ui to javafx.fxml;

    opens io.github.ppetrbednar.dstr.ui.module.panel to javafx.fxml;
    opens io.github.ppetrbednar.dstr.ui.module.root to javafx.fxml;

    opens io.github.ppetrbednar.dstr.ui.style to javafx.fxml;

    opens io.github.ppetrbednar.dstr.ui.window.alert to javafx.fxml;

    exports io.github.ppetrbednar.dstr;
}