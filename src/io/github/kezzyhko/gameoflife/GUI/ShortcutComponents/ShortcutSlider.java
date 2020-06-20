package io.github.kezzyhko.gameoflife.GUI.ShortcutComponents;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;

public class ShortcutSlider extends ShortcutComponent {
    private JSlider theSlider;
    @Override
    protected JSlider getWrappedElement() {
        return theSlider;
    }

    public ShortcutSlider(int min, int max, int value, boolean isVertical, boolean isInverted) {
        theSlider = new JSlider(min, max, value);
        theSlider.setOrientation(isVertical ? JSlider.VERTICAL : JSlider.HORIZONTAL);
        theSlider.setInverted(isInverted);
        theSlider.setOpaque(false);
    }
    public ShortcutSlider(int min, int max, int value, boolean isVertical, boolean isInverted, ChangeListener l) {
        this(min, max, value, isVertical, isInverted);
        addOnChange(l);
    }

    public void addOnChange(ChangeListener... changeListeners) {
        for (ChangeListener l: changeListeners) {
            theSlider.addChangeListener(l);
        }
    }

    public void addShortcut(String shortcut, int changeAmount) {
        super.addShortcut(shortcut, changeAmount);
    }
    @Override
    protected void shortcutFunction(ActionEvent e, Object... args) {
        int changeAmount = (int)args[0];
        theSlider.setValue(theSlider.getValue()+changeAmount);
    }

    public int getValue() {
        return theSlider.getValue();
    }
    public void setValue(int n) {
        theSlider.setValue(n);
    }
}
