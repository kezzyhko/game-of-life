package io.github.kezzyhko.gameoflife.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JComponentWrapper extends ContainerWrapper {
    private JComponent theJComponent;
    protected JComponent getWrappedElement() {
        return theJComponent;
    }
    public void setTransparent(boolean isTransparent) {
        getWrappedElement().setOpaque(!isTransparent);
    }

    public void setOpaque(boolean value) {
        getWrappedElement().setOpaque(value);
    }

    protected JComponentWrapper() {}
    public JComponentWrapper(JComponent c) {
        theJComponent = c;
    }

    public void bindInput(KeyStroke keyStroke, Object id) {
        getWrappedElement().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, id);
    }
    public void bindInput(String keyStroke, Object id) {
        bindInput(KeyStroke.getKeyStroke(keyStroke), id);
    }
    public void bindInput(char keyStroke, Object id) {
        bindInput(KeyStroke.getKeyStroke(keyStroke), id);
    }
    public void bindAction(Object id, ActionListener actionListener) {
        getWrappedElement().getActionMap().put(id, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        });
    }
    public void addKeyBinding(KeyStroke keyStroke, ActionListener actionListener, Object id) {
        bindInput(keyStroke, id);
        bindAction(id, actionListener);
    }
    public void addKeyBinding(String keyStroke, ActionListener actionListener, Object id) {
        addKeyBinding(KeyStroke.getKeyStroke(keyStroke), actionListener, id);
    }
    public void addKeyBinding(char keyStroke, ActionListener actionListener, Object id) {
        addKeyBinding(KeyStroke.getKeyStroke(keyStroke), actionListener, id);
    }
    public void addKeyBinding(KeyStroke keyStroke, ActionListener actionListener) {
        addKeyBinding(keyStroke, actionListener, keyStroke);
    }
    public void addKeyBinding(String keyStroke, ActionListener actionListener) {
        addKeyBinding(KeyStroke.getKeyStroke(keyStroke), actionListener);
    }
    public void addKeyBinding(char keyStroke, ActionListener actionListener) {
        addKeyBinding(KeyStroke.getKeyStroke(keyStroke), actionListener);
    }
}
