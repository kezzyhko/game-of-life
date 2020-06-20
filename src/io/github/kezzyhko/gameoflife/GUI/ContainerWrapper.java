package io.github.kezzyhko.gameoflife.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class ContainerWrapper {
    private Container theContainer;
    protected Container getWrappedElement() {
        return theContainer;
    }

    protected ContainerWrapper() {}
    public ContainerWrapper(Container c) {
        theContainer = c;
    }

    public void addComponent(ContainerWrapper c) {
        addComponent(c, null);
    }
    public void addComponent(ContainerWrapper c, Object constraints) {
        addComponent(c.getWrappedElement(), constraints);
    }
    public void addComponent(Component c) {
        addComponent(c, null);
    }
    public void addComponent(Component c, Object constraints) {
        getWrappedElement().add(c, constraints);
    }

    public void addMouseAdapter(MouseAdapter l) {
        getWrappedElement().addMouseListener(l);
        getWrappedElement().addMouseMotionListener(l);
        getWrappedElement().addMouseWheelListener(l);
    }

    public boolean isVisible() {
        return getWrappedElement().isVisible();
    }
    public void setVisible(boolean visible) {
        getWrappedElement().setVisible(visible);
    }

    public void setBackground(Color color) {
        getWrappedElement().setBackground(color);
    }
    public void repaint() {
        getWrappedElement().repaint();
    }

    public void setFocusTraversalKeysEnabled(boolean focusTraversalKeysEnabled) {
        getWrappedElement().setFocusTraversalKeysEnabled(focusTraversalKeysEnabled);
    }
    public void setFocusable(boolean focusable) {
        getWrappedElement().setFocusable(focusable);
    }
    public void setFocusableRecursively(boolean focusable) {
        setFocusableRecursively(getWrappedElement(), focusable);
    }
    protected static void setFocusableRecursively(Component c, boolean focusable) {
        c.setFocusable(focusable);
        c.setFocusTraversalKeysEnabled(false);

        if (c instanceof Container) {
            for (Component component : ((Container)c).getComponents()) {
                setFocusableRecursively(component, focusable);
            }
        }
    }

    public void setBoxLayout(int axis) {
        getWrappedElement().setLayout(new BoxLayout(getWrappedElement(), axis));
    }
    public void setBorderLayout() {
        getWrappedElement().setLayout(new BorderLayout());
    }
    public void setGridBagLayout() {
        getWrappedElement().setLayout(new GridBagLayout());
    }
    public GroupLayout setGroupLayout() {
        GroupLayout layout = new GroupLayout(getWrappedElement());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        getWrappedElement().setLayout(layout);
        return layout;
    }
    public void setLayout(LayoutManager mgr) {
        getWrappedElement().setLayout(mgr);
    }

    public Dimension getPreferredSize() {
        return getWrappedElement().getPreferredSize();
    }
    public void setPreferredSize(Dimension d) {
        getWrappedElement().setPreferredSize(d);
    }
    public Dimension getSize() {
        return getWrappedElement().getSize();
    }
    public void setSize(Dimension d) {
        getWrappedElement().setSize(d);
    }
    public int getWidth() {
        return getWrappedElement().getWidth();
    }
    public int getHeight() {
        return getWrappedElement().getHeight();
    }
    public void setAllSizes(int width, int height) {
        setAllSizes(new Dimension(width, height));
    }
    public void setAllSizes(Dimension d) {
        getWrappedElement().setPreferredSize(d);
        getWrappedElement().setSize(d);
        getWrappedElement().setMinimumSize(d);
        getWrappedElement().setMaximumSize(d);
    }
}
