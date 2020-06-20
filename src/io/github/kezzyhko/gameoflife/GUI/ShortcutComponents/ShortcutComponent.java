package io.github.kezzyhko.gameoflife.GUI.ShortcutComponents;

import io.github.kezzyhko.gameoflife.GUI.JComponentWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class ShortcutComponent extends JComponentWrapper {
    protected void addShortcut(KeyStroke keyStroke, Object... args) {
        getWrappedElement().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, args);
        getWrappedElement().getActionMap().put(args, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shortcutFunction(e, args);
            }
        });
    }
    protected void addShortcut(String shortcut, Object... args) {
        addShortcut(KeyStroke.getKeyStroke(shortcut), args);
    }
    protected abstract void shortcutFunction(ActionEvent e, Object... args);
}


//old code
/*
    protected static KeyStroke[] stringsToKeyStrokes(String[] strings) {
        KeyStroke[] keyStrokes = new KeyStroke[strings.length];
        for (int i = 0; i < strings.length; i++) {
            keyStrokes[i] = KeyStroke.getKeyStroke(strings[i]);
        }
        return keyStrokes;
    }





    public void bindInput(KeyStroke keyStroke, Object id) {
        getWrappedElement().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, id);
    }
    public void bindInput(String keyStroke, Object id) {
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

 */