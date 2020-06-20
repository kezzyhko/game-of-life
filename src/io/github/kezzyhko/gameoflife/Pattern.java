package io.github.kezzyhko.gameoflife;



import io.github.kezzyhko.gameoflife.GUI.CelledPanel;

import java.awt.*;
import java.util.*;

public class Pattern {
    public static final char RLE_ALIVE = 'o', RLE_DEAD = 'b', RLE_NEW_LINE = '$';
    public static final String RLE_CHARS_REGEX = "[" + RLE_ALIVE + RLE_DEAD + RLE_NEW_LINE + "]";

    TreeSet<Coords> cells = new TreeSet<>();

    public Pattern(Collection<Coords> c) {
        cells = new TreeSet<>(c);
    }
    public Pattern() {}
    public Pattern(String rle) {
        String[] terms = rle
                //remove spaces
                .replaceAll("\\s+", "")
                //insert "1" if number is not specified
                .replaceAll("(^|(?<="+RLE_CHARS_REGEX+"))(?="+RLE_CHARS_REGEX+")", "1")
                //split at letter-digit or digit-letter boundary
                .split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

        // Any unchecked character will be ignored

        int x = 0, y = 0;
        for (int i = 1; i < terms.length; i+=2) {
            int value = Integer.parseInt(terms[i-1]);
            switch (terms[i].toCharArray()[0]) {
                case RLE_DEAD:
                    x += value;
                    break;
                case RLE_NEW_LINE:
                    y += value;
                    x = 0;
                    break;
                case RLE_ALIVE:
                    for (int j = 0; j < value; j++) {
                        setAlive(x, y, true);
                        x++;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Illegal symbol in RLE: " + terms[i]);
            }
        }
    }

    public Set<Coords> getAliveCells() {
        return Collections.unmodifiableSet(cells);
    }

    public Dimension getSize() {
        Coords max = new Coords(-1, -1);
        for (Coords cell : cells) {
            if (cell.x > max.x) {
                max.x = cell.x;
            }
            if (cell.y > max.y) {
                max.y = cell.y;
            }
        }
        return new Dimension(max.x + 1, max.y + 1);
    }

    public boolean isAlive(int x, int y) {
        return isAlive(new Coords(x, y));
    }
    public boolean isAlive(Coords coords) {
        return cells.contains(coords);
    }

    public void setAlive(int x, int y, boolean value) {
        this.setAlive(new Coords(x, y), value);
    }
    public void setAlive(Coords coords, boolean value) {
        if (value) {
            cells.add(coords);
        } else {
            cells.remove(coords);
        }
    }
    public void setAlive(Pattern pattern, boolean value) {
        for (Coords coords : pattern.getAliveCells()) {
            setAlive(coords.x, coords.y, value);
        }
    }

    public Pattern rotatedCW() {
        return transformed(new Coords(0, 1), new Coords(-1, 0), new Coords(0, 0));
    }
    public Pattern rotatedCCW() {
        return transformed(new Coords(0, -1), new Coords(1, 0), new Coords(0, 0));
    }
    public Pattern translated(Coords newO) {
        return transformed(new Coords(1, 0), new Coords(0, 1), newO);
    }
    public Pattern transformed(Coords newX, Coords newY, Coords newO) {
        Pattern newPattern = new Pattern();
        for (Coords cell : cells) {
            newPattern.setAlive(
                    newX.x*cell.x + newY.x*cell.y + newO.x,
                    newX.y*cell.x + newY.y*cell.y + newO.y,
                    true
            );
        }
        return newPattern;
    }

    public void paint(CelledPanel panel, Color color) {
        for (Coords cell : cells) {
            CelledPanel.Cell visualCell = panel.getCell(
                    Math.floorMod(cell.x, panel.widthInCells),
                    Math.floorMod(cell.y, panel.heightInCells)
            );
            visualCell.setColor(color);
        }
        panel.repaint();
    }

    private String rleRepeat(char str, int times) {
        StringBuilder sb = new StringBuilder();
        if (times != 0) {
            if (times != 1) {
                sb.append(times);
            }
            sb.append(str);
        }
        return sb.toString();
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        final Iterator<Coords> iterator = cells.iterator();
        if (iterator.hasNext()) {
            int aliveInRowCount = 1;
            Coords previousCell, currentCell = iterator.next();
            result.append(rleRepeat(RLE_NEW_LINE, currentCell.y));
            result.append(rleRepeat(RLE_DEAD, currentCell.x));
            previousCell = currentCell;

            while (iterator.hasNext()) {
                currentCell = iterator.next();

                if (currentCell.y != previousCell.y || currentCell.x != (previousCell.x + 1)) {
                    result.append(rleRepeat(RLE_ALIVE, aliveInRowCount));
                    aliveInRowCount = 0;
                }

                result.append(rleRepeat(RLE_NEW_LINE, currentCell.y - previousCell.y));
                result.append(rleRepeat(RLE_DEAD, (currentCell.y == previousCell.y) ? (currentCell.x - previousCell.x - 1) : currentCell.x));

                aliveInRowCount++;
                previousCell = currentCell;
            }
            result.append(rleRepeat(RLE_ALIVE, aliveInRowCount));
        }
        return result.toString();
    }
}
