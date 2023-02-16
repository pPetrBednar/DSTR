package io.github.ppetrbednar.dstr.ui.module.root;


import io.github.ppetrbednar.dstr.logic.DSTRController;
import io.github.ppetrbednar.dstr.ui.Root;
import io.github.ppetrbednar.dstr.ui.handler.ICompositor;
import io.github.ppetrbednar.dstr.ui.handler.Module;
import io.github.ppetrbednar.dstr.ui.handler.ViewType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Petr Bednář
 */
public class Main extends Module<Main, Root> {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    private final Compositor compositor = new Compositor();
    private boolean init;
    @FXML
    private AnchorPane container;

    public void test(ActionEvent actionEvent) {
        DSTRController.test();
    }

    private class Compositor implements ICompositor {

        @Override
        public void compose(ViewType type) {
            decompose();
        }

        @Override
        public void decompose() {
            container.getChildren().clear();
        }
    }

    public void compose(ViewType type) {
        compositor.compose(type);
    }
}
