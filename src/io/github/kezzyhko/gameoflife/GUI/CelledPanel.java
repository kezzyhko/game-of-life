package io.github.kezzyhko.gameoflife.GUI;

import io.github.kezzyhko.gameoflife.Coords;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class CelledPanel extends JComponentWrapper {
    private final JPanel thePanel = new JPanel();
    @Override
    protected JPanel getWrappedElement() {
        return thePanel;
    }

    public final int widthInCells, heightInCells;
    private final int cellSize;

    private CellType cellType = CellType.SQUARE;
    public enum CellType {
        CIRCLE, SQUARE
    }
    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public CelledPanel(int widthInCells, int heightInCells, int cellSize, Color defaultColor) {
        this.widthInCells = widthInCells;
        this.heightInCells = heightInCells;
        this.cellSize = cellSize;

        setAllSizes(widthInCells * cellSize, heightInCells * cellSize);
        thePanel.setLayout(new GridLayout(heightInCells, widthInCells));

        for (int y = 0; y < heightInCells; y++) {
            for (int x = 0; x < widthInCells; x++) {
                addComponent(new Cell(defaultColor, x, y));
            }

        }
    }
    public CelledPanel(int widthInCells, int heightInCells, int cellSize) {
        this(widthInCells, heightInCells, cellSize, new Color(0x00FFFFFF, true));
    }
    public CelledPanel(int widthInCells, int heightInCells, int cellSize, Color defaultColor, CellType cellType) {
        this(widthInCells, heightInCells, cellSize, defaultColor);
        this.cellType = cellType;
    }
    public CelledPanel(int widthInCells, int heightInCells, int cellSize, CellType cellType) {
        this(widthInCells, heightInCells, cellSize);
        this.cellType = cellType;
    }

    public final class Cell extends ContainerWrapper {
        private JComponent theComponent;
        @Override
        public JComponent getWrappedElement() {
            return theComponent;
        }

        public final Coords coords;

        public Color getColor() {
            return theComponent.getBackground();
        }
        public void setColor(Color color) {
            theComponent.setBackground(color);
        }

        private Cell(Coords coords) {
            this.coords = coords;
        }
        private Cell(Component theComponent, Coords coords) {
            this(coords);
            this.theComponent = (JComponent) theComponent;
        }
        private Cell(Component theComponent, int x, int y) {
            this(theComponent, new Coords(x, y));
        }
        private Cell(Color color, Coords coords) {
            this(coords);
            this.theComponent = new JComponent() {
                @Override
                protected void paintComponent(Graphics g) {
                    if (getColor().getAlpha() == 0) {
                        return;
                    }

                    if (g instanceof Graphics2D) {
                        Graphics2D g2d = (Graphics2D)g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        //g2d.setStroke(new BasicStroke(2));
                    }

                    g.setColor(getColor());
                    switch (cellType) {
                        case CIRCLE:
                            g.fillOval(0, 0, cellSize, cellSize);
                            break;
                        case SQUARE:
                            g.fillRect(1, 1, cellSize - 2, cellSize - 2);
                            break;
                    }
                }
            };
            setColor(color);
            setAllSizes(cellSize, cellSize);
        }
        private Cell(Color color, int x, int y) {
            this(color, new Coords(x, y));
        }
    }
    public Cell getCell(int x, int y) {
        return getCell(new Coords(x, y));
    }
    public Cell getCell(Coords coords) {
        if (coords.x < 0 || coords.x >= widthInCells || coords.y < 0 || coords.y >= heightInCells) {
            return null;
        }
        return new Cell(thePanel.getComponent(coords.y*widthInCells+coords.x), coords);
    }
    public Cell getCellAt(Point p) {
        Component component = thePanel.getComponentAt(p);
        if (component == null || component.equals(thePanel)) {
            return null;
        }
        int index = Arrays.asList(thePanel.getComponents()).indexOf(component);
        return new Cell(component, index%widthInCells, index/widthInCells);
    }
}
