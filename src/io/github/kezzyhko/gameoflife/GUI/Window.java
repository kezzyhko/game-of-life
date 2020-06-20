package io.github.kezzyhko.gameoflife.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends ContainerWrapper {
    private final JFrame theFrame = new JFrame();
    @Override
    protected Container getWrappedElement() {
        return theFrame.getContentPane();
    }

    public final boolean isFullscreen;
    private final int innerWidth, innerHeight;
    public int getInnerWidth() {
        return innerWidth;
    }
    public int getInnerHeight() {
        return innerHeight;
    }

    {
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setLocationByPlatform(true);
        theFrame.setResizable(false);
    }
    public Window(int innerWidth, int innerHeight) {
        isFullscreen = innerWidth < 0 || innerHeight < 0;
        if (isFullscreen) {
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(theFrame);
//            theFrame.setUndecorated(true);
//            theFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            this.innerWidth = d.width;
            this.innerHeight = d.height;
        } else {
            theFrame.getContentPane().setPreferredSize(new Dimension(innerWidth, innerHeight));
            theFrame.pack();

            this.innerWidth = innerWidth;
            this.innerHeight = innerHeight;
        }
    }
    public Window() {
        this(-1, -1);
    }

    public void setTitle(String title) {
        theFrame.setTitle(title);
    }
    public void dispose() {
        theFrame.dispose();
    }
    public void rebuild() {
        theFrame.validate();
    }
    public void show() {
        theFrame.setVisible(true);
    }
    public ComponentOrientation getComponentOrientation() {
        return theFrame.getContentPane().getComponentOrientation();
    }
    public void setComponentOrientation(ComponentOrientation o) {
        theFrame.getContentPane().setComponentOrientation(o);
    }

    public void bindInput(KeyStroke keyStroke, Object id) {
        theFrame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, id);
    }
    public void bindInput(String keyStroke, Object id) {
        bindInput(KeyStroke.getKeyStroke(keyStroke), id);
    }
    public void bindAction(Object id, ActionListener actionListener) {
        theFrame.getRootPane().getActionMap().put(id, new AbstractAction() {
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
}