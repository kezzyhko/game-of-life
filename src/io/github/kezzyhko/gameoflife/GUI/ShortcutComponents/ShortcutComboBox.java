package io.github.kezzyhko.gameoflife.GUI.ShortcutComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.Vector;

public class ShortcutComboBox<T> extends ShortcutComponent {
    public static final int NONE = -1, NEXT = -2, PREVIOUS = -3;

    private JComboBox<T> theComboBox;
    @Override
    public JComponent getWrappedElement() {
        return theComboBox;
    }

    public ShortcutComboBox(Set<T> items) {
        theComboBox = new JComboBox<>(new Vector<>(items));
        removeSelection();
    }
    public ShortcutComboBox(Set<T> items, ActionListener l) {
        this(items);
        addOnChoose(l);
    }

    public void addOnChoose(ActionListener... actionListeners) {
        for (ActionListener l: actionListeners) {
            theComboBox.addActionListener(l);
        }
    }

    public void addShortcut(String shortcut, int element) {
        super.addShortcut(shortcut, element);
    }
    @Override
    protected void shortcutFunction(ActionEvent e, Object... args) {
        int element = (int)args[0];
        int index;
        switch (element) {
            case NONE:
                removeSelection();
                return;
            case PREVIOUS:
                index = theComboBox.getSelectedIndex()-1;
                break;
            case NEXT:
                index = theComboBox.getSelectedIndex()+1;
                break;
            default:
                index = element;
                break;
        }
        theComboBox.setSelectedIndex(Math.floorMod(index, theComboBox.getItemCount()));
    }

    public T getSelectedItem() {
        return theComboBox.getItemAt(theComboBox.getSelectedIndex());
    }
    public void selectItem(T item) {
        theComboBox.setSelectedItem(item);
    }
    public void selectIndex(int index) {
        theComboBox.setSelectedIndex(index);
    }
    public void removeSelection() {
        selectIndex(-1);
    }
}