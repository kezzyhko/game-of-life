package io.github.kezzyhko.gameoflife.GUI.ShortcutComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShortcutButton extends ShortcutComponent {
    private JButton theButton = new JButton();
    @Override
    public JButton getWrappedElement() {
        return theButton;
    }

    public ShortcutButton(String text) {
        theButton.setText(text);
    }
    public ShortcutButton(String text, ActionListener actionListener) {
        this(text);
        addOnClick(actionListener);
    }
    public ShortcutButton(String text, String shortcut) {
        this(text);
        addShortcut(shortcut);
    }
    public ShortcutButton(String text, ActionListener actionListener, String shortcut) {
        this(text, actionListener);
        addShortcut(shortcut);
    }

    public void addOnClick(ActionListener l) {
        theButton.addActionListener(l);
    }

    public void addShortcut(String shortcut) {
        super.addShortcut(shortcut);
    }
    @Override
    protected void shortcutFunction(ActionEvent e, Object... args) {
        click();
    }

    public void click() {
        theButton.doClick(0);
    }
    public void setVisible(boolean visible) {
        theButton.setVisible(visible);
    }
    public void setText(String text) {
        theButton.setText(text);
    }
}
