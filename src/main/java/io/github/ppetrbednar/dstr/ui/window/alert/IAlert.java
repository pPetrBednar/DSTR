package io.github.ppetrbednar.dstr.ui.window.alert;

/**
 *
 * @author Petr Bednář
 */
public interface IAlert {

    public void setCancelVisibility(boolean cancel);

    public void setText(String text);

    public Object getResult();
}
