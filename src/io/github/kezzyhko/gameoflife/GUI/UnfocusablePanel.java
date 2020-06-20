package io.github.kezzyhko.gameoflife.GUI;

import javax.swing.*;
import java.awt.*;

public class UnfocusablePanel extends JComponentWrapper {
    private JPanel thePanel = new JPanel();
    @Override
    protected JPanel getWrappedElement() {
        return thePanel;
    }

    @Override
    public void addComponent(Component c, Object constraints) {
        super.addComponent(c, constraints);
        ContainerWrapper.setFocusableRecursively(c, false);
    }

    public void toggleVisible() {
        thePanel.setVisible(!thePanel.isVisible());
    }

    public UnfocusablePanel() {
        thePanel.setFocusable(false);
    }
}