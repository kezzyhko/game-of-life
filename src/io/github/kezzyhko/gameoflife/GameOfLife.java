package io.github.kezzyhko.gameoflife;

import io.github.kezzyhko.gameoflife.GUI.CelledPanel;
import io.github.kezzyhko.gameoflife.GUI.JComponentWrapper;
import io.github.kezzyhko.gameoflife.GUI.UnfocusablePanel;
import io.github.kezzyhko.gameoflife.GUI.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeMap;



public final class GameOfLife {
    private static final String WINDOW_TITLE = "Game Of Life";
    private static final String
            THEMES_DIRECTORY = "./GameOfLife/themes/",
            PATTERNS_DIRECTORY = "./GameOfLife/patterns/",
            CUSTOMIZATION_FILE = "./GameOfLife/customization";
    private static final int MIN_DELAY = 10, MAX_DELAY = 1000, DEFAULT_DELAY = 100, DELAY_DELTA = 10;
    private static final int DEFAULT_WIDTH = -1, DEFAULT_HEIGHT = -1, CELL_SIZE = 16;
    private static final String DEFAULT_INITIAL_STATE_RLE = "";



    //arguments parser & create files & constructor
    public static void main(String[] args) {
        int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
        String initialStateRle = DEFAULT_INITIAL_STATE_RLE;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s":
                case "--size":
                    width = Integer.parseInt(args[i+1]);
                    height = Integer.parseInt(args[i+2]);
                    i+=2;
                    break;
                case "--rle":
                    initialStateRle = args[i+1];
                    i+=1;
                    break;
                default:
                    System.out.println("Wrong argument '" + args[i] + "'");
                    System.out.println("Use '-s' or '--size' to specify the size of the board in cells");
                    System.out.println("Use '--rle' to specify the initial state");
                    System.exit(1);
                    break;
            }
        }
        new GameOfLife(width, height, initialStateRle);
    }
    static {
        Properties light_theme = new Properties();
        Properties dark_theme = new Properties();
        Properties kezzyhko_theme = new Properties();

        light_theme.setProperty("deadCellColor", "FFFFFFFF");
        light_theme.setProperty("aliveCellColor", "FF000000");
        light_theme.setProperty("backgroundColor", "FFC0C0C0");
        light_theme.setProperty("patternColor", "550000FF");
        light_theme.setProperty("cellType", "SQUARE");

        dark_theme.setProperty("deadCellColor", "FF404040");
        dark_theme.setProperty("aliveCellColor", "FFFFFFFF");
        dark_theme.setProperty("backgroundColor", "FF808080");
        dark_theme.setProperty("patternColor", "555555FF");
        dark_theme.setProperty("cellType", "SQUARE");

        kezzyhko_theme.setProperty("deadCellColor", "FF533A7B");
        kezzyhko_theme.setProperty("aliveCellColor", "FF6969B3");
        kezzyhko_theme.setProperty("backgroundColor", "FF3A015C");
        kezzyhko_theme.setProperty("patternColor", "550000FF");
        kezzyhko_theme.setProperty("cellType", "CIRCLE");

        try {
            FileSystem.createNewFile(THEMES_DIRECTORY + "Default Light", light_theme);
            FileSystem.createNewFile(THEMES_DIRECTORY + "Default Dark", dark_theme);
            FileSystem.createNewFile(THEMES_DIRECTORY + "kezzyhko", kezzyhko_theme);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can not create files for themes");
        }

        if (!FileSystem.fileExists(CUSTOMIZATION_FILE)) {
            Properties customization = new Properties();

            customization.setProperty("theme", "Default Light");
            customization.setProperty("delay", String.valueOf(DEFAULT_DELAY));

            try {
                FileSystem.createNewFile(CUSTOMIZATION_FILE, customization);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Can not create file for settings");
            }
        }
    }
    private GameOfLife(int width, int height, String initialStateRle) {
        //create visual components
        window = new Window(width * CELL_SIZE, height * CELL_SIZE);
        window.setTitle(WINDOW_TITLE);
        int gamePanelWidth  =  window.getInnerWidth() / CELL_SIZE;
        int gamePanelHeight = window.getInnerHeight() / CELL_SIZE;

        gamePanel = new CelledPanel(gamePanelWidth, gamePanelHeight, CELL_SIZE);

        patternsPanel = new CelledPanel(gamePanelWidth, gamePanelHeight, CELL_SIZE, Essentials.TRANSPARENT_COLOR);
        patternsPanel.setTransparent(true);

        settingsPanel = new SettingsPanel();
        settingsPanel.setTransparent(true);
        settingsPanel.setAllSizes(gamePanel.getWidth(), gamePanel.getHeight());

        mainPanel = new JComponentWrapper(new JLayeredPane());
        mainPanel.addComponent(gamePanel, JLayeredPane.DEFAULT_LAYER);
        mainPanel.addComponent(patternsPanel, JLayeredPane.DEFAULT_LAYER+1);
        mainPanel.addComponent(settingsPanel, JLayeredPane.POPUP_LAYER);
        mainPanel.setAllSizes(gamePanel.getWidth(), gamePanel.getHeight());
        window.addComponent(mainPanel);



        //add keybindings & listeners
        mainPanel.setFocusable(true);
        mainPanel.setFocusTraversalKeysEnabled(false);
        mainPanel.addKeyBinding("ctrl pressed CONTROL", event -> settingsPanel.setVisible(true));
        mainPanel.addKeyBinding("released CONTROL", event -> settingsPanel.setVisible(false));

        mainPanel.addKeyBinding("SPACE", event -> setPaused(SET_PAUSED_GENERAL, !pausedGeneral));

        mainPanel.addKeyBinding("ESCAPE", event -> {
            if (settingsPanel.patternsComboBox.getSelectedIndex() > 0) {
                settingsPanel.patternsComboBox.setSelectedIndex(-1);
            } else {
                settingsPanel.toggleVisible();
            }
        });

        mainPanel.addKeyBinding("Q", event -> setPattern(pattern.rotatedCCW()));
        mainPanel.addKeyBinding("E", event -> setPattern(pattern.rotatedCW()));

        mainPanel.addKeyBinding("W", event -> settingsPanel.speedSlider.setValue(settingsPanel.speedSlider.getValue()-DELAY_DELTA));
        mainPanel.addKeyBinding("S", event -> settingsPanel.speedSlider.setValue(settingsPanel.speedSlider.getValue()+DELAY_DELTA));

        mainPanel.addKeyBinding("A", event -> pauseAndGoToState(currentStateIndex-1));
        mainPanel.addKeyBinding("D", event -> pauseAndGoToState(currentStateIndex+1));

        mainPanel.addMouseAdapter(new MouseAdapter() {
            private Coords getCellCoords(MouseEvent event) {
                CelledPanel.Cell cell = gamePanel.getCellAt(event.getPoint());
                if (cell == null) return null;
                return new Coords(gamePanel.getCellAt(event.getPoint()).coords);
            }
            private boolean toggleToAlive, needToToggle;

            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    setPaused(SET_PAUSED_BY_DRAWING, true);
                    if (pattern != null) {
                        Pattern translatedPattern = pattern.translated(getCellCoords(event));
                        getCurrentState().setAlive(translatedPattern, true);
                        translatedPattern.paint(gamePanel, aliveCellColor);
                        if (!event.isShiftDown()) {
                            settingsPanel.patternsComboBox.setSelectedIndex(-1);
                        }
                    } else {
                        toggleToAlive = !getCurrentState().isAlive(getCellCoords(event));
                        needToToggle = true;
                        mouseDragged(event);
                    }
                    branch();
                }
            }
            @Override
            public void mouseMoved(MouseEvent event) {
                setPatternCoords(getCellCoords(event));
            }
            @Override
            public void mouseDragged(MouseEvent event) {
                if (needToToggle) {
                    CelledPanel.Cell visualCell = gamePanel.getCellAt(event.getPoint());
                    if (visualCell != null) {
                        getCurrentState().setAlive(getCellCoords(event), toggleToAlive);
                        visualCell.setColor(getCellColor(toggleToAlive));
                    }
                }
            }
            @Override
            public void mouseReleased(MouseEvent event) {
                setPaused(SET_PAUSED_BY_DRAWING, false);
                if (event.getButton() == MouseEvent.BUTTON1) {
                    needToToggle = false;
                }
            }
            @Override
            public void mouseExited(MouseEvent event) {
                setPatternCoords(null);
            }
        });



        //set initial state & restore user customization
        history.add(new Pattern(initialStateRle));
        try {
            Properties customization = new Properties();
            customization.load(new FileInputStream(CUSTOMIZATION_FILE));
            settingsPanel.patternsComboBox.setSelectedIndex(-1);
            settingsPanel.themesComboBox.setSelectedItem(customization.getProperty("theme"));
            settingsPanel.speedSlider.setValue(Integer.parseInt(customization.getProperty("delay")));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can not open settings file");
        }
        window.show();
    }



    //visual components
    private Window window;
    private class SettingsPanel extends UnfocusablePanel {
        public final JSlider speedSlider;
        public final JComboBox<String> themesComboBox, patternsComboBox;
        public final JButton togglePauseButton, previousStepButton, nextStepButton;
        public SettingsPanel() {
            speedSlider = new JSlider(JSlider.HORIZONTAL, MIN_DELAY, MAX_DELAY, DEFAULT_DELAY);
            speedSlider.setInverted(true);
            speedSlider.setOpaque(false);
            speedSlider.addChangeListener(event -> {
                int value = speedSlider.getValue();
                timer.setDelay(value);
                timer.setInitialDelay(value);
                try {
                    FileSystem.changeProperty(CUSTOMIZATION_FILE, "delay", String.valueOf(value));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            togglePauseButton = new JButton("|>");
            togglePauseButton.addActionListener(event -> setPaused(SET_PAUSED_GENERAL, !pausedGeneral));

            previousStepButton = new JButton("<-");
            previousStepButton.addActionListener(event -> pauseAndGoToState(currentStateIndex-1));

            nextStepButton = new JButton("->");
            nextStepButton.addActionListener(event -> pauseAndGoToState(currentStateIndex+1));

            themesComboBox = new JComboBox<>(FileSystem.getFilesNames(THEMES_DIRECTORY));
            themesComboBox.addActionListener(event -> {
                String selected = themesComboBox.getItemAt(themesComboBox.getSelectedIndex());
                try {
                    Properties theme = new Properties();

                    theme.load(new FileInputStream(THEMES_DIRECTORY + selected));
                    aliveCellColor = new Color(Long.valueOf(theme.getProperty("aliveCellColor"), 16).intValue(), true);
                    deadCellColor = new Color(Long.valueOf(theme.getProperty("deadCellColor"), 16).intValue(), true);
                    backgroundColor = new Color(Long.valueOf(theme.getProperty("backgroundColor"), 16).intValue(), true);
                    patternColor = new Color(Long.valueOf(theme.getProperty("patternColor"), 16).intValue(), true);

                    CelledPanel.CellType cellType = CelledPanel.CellType.valueOf(theme.getProperty("cellType"));
                    gamePanel.setCellType(cellType);
                    patternsPanel.setCellType(cellType);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Can not open theme file");
                }

                try {
                    FileSystem.changeProperty(CUSTOMIZATION_FILE, "theme", selected);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Can not save selected theme to the settings file");
                }

                for (int y = 0; y < gamePanel.heightInCells; y++) {
                    for (int x = 0; x < gamePanel.widthInCells; x++) {
                        gamePanel.getCell(x, y).setColor(getCellColor(getCurrentState().isAlive(x, y)));
                    }
                }

                gamePanel.setBackground(backgroundColor);
                settingsPanel.setBackground(backgroundColor);
                window.setBackground(backgroundColor);

                gamePanel.repaint();
            });

            patternsComboBox = new JComboBox<>(FileSystem.getFilesNames(PATTERNS_DIRECTORY));
            patternsComboBox.addActionListener(event -> {
                String selected = patternsComboBox.getItemAt(patternsComboBox.getSelectedIndex());
                if (selected != null) {
                    try {
                        setPattern(new Pattern(FileSystem.getFileContents(PATTERNS_DIRECTORY + selected)));
                    } catch (FileNotFoundException e) {
                        setPattern(null);
                    }
                } else {
                    setPattern(null);
                }
            });

            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;

            // first line with three buttons
            c.gridy = 0;
            c.gridx = 0;
            addComponent(previousStepButton, c);
            c.gridx = 1;
            addComponent(togglePauseButton, c);
            c.gridx = 2;
            addComponent(nextStepButton, c);

            // other lines
            c.gridx = 0;
            c.gridwidth = 3;
            c.gridy = 1;
            addComponent(themesComboBox, c);
            c.gridy = 2;
            addComponent(patternsComboBox, c);
            c.gridy = 3;
            addComponent(speedSlider, c);

            this.toggleVisible();
        }
    }
    private SettingsPanel settingsPanel;
    private JComponentWrapper mainPanel;
    private CelledPanel gamePanel;
    private CelledPanel patternsPanel;

    private Color deadCellColor = Color.WHITE, aliveCellColor = Color.BLACK, backgroundColor = Color.LIGHT_GRAY, patternColor = new Color(0x550000FF, true);
    public Color getCellColor(boolean isAlive) {
        return isAlive ? aliveCellColor : deadCellColor;
    }



    //patterns
    private Pattern pattern;
    private Coords patternCoords = null;
    private void setPattern(Pattern newPattern) {
        paintPattern(Essentials.TRANSPARENT_COLOR);
        pattern = newPattern;
        paintPattern(patternColor);
    }
    private void setPatternCoords(Coords newCoords) {
        paintPattern(Essentials.TRANSPARENT_COLOR);
        patternCoords = newCoords;
        paintPattern(patternColor);
    }
    private void paintPattern(Color color) {
        if (pattern != null && patternCoords != null) {
            pattern.translated(patternCoords).paint(patternsPanel, color);
        }
    }



    //history manipulations
    private ArrayList<Pattern> history = new ArrayList<>();
    private int currentStateIndex = 0;
    public Pattern getCurrentState() {
        return history.get(currentStateIndex);
    }
    private void branch() {
        history.subList(currentStateIndex+1, history.size()).clear();
    }



    //pause manipulation
    private static final boolean SET_PAUSED_GENERAL = true, SET_PAUSED_BY_DRAWING = false;
    private boolean pausedGeneral = true, pausedByDrawing = false;
    public boolean isPaused() {
        return pausedGeneral || pausedByDrawing;
    }
    public void setPaused(boolean type, boolean value) {
        if (type) {
            if (pausedByDrawing && pausedGeneral && !value) {
                return;
            }
            pausedGeneral = value;
            settingsPanel.togglePauseButton.setText(isPaused() ? "|>" : "||");
        } else {
            pausedByDrawing = value;
        }

        if (isPaused()) {
            timer.stop();
        } else {
            timer.start();
            branch();
        }
    }



    //process of the game
    public void goToState(int index) {
        if (index < 0) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        while (index > history.size() - 1) {
            Pattern newState = getStateAfter(history.get(history.size() - 1));
            if (!newState.equals(getCurrentState())) {
                history.add(newState);
            }
        }

        getCurrentState().paint(gamePanel, deadCellColor);
        currentStateIndex = index;
        getCurrentState().paint(gamePanel, aliveCellColor);
    }
    public void pauseAndGoToState(int index) {
        if (pausedByDrawing) {
            return;
        }
        setPaused(SET_PAUSED_GENERAL, true);
        goToState(index);
    }

    private Pattern getStateAfter(Pattern state) {
        //count neighbours
        TreeMap<Coords, Integer> neighbours = new TreeMap<>();
        for (Coords coords : state.getAliveCells()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        Coords newCell = new Coords(
                                Math.floorMod(coords.x+i, gamePanel.widthInCells),
                                Math.floorMod(coords.y+j, gamePanel.heightInCells)
                        );
                        neighbours.put(newCell, Essentials.intFromInteger(neighbours.get(newCell))+1);
                    }
                }
            }
        }

        //make newState
        neighbours.keySet().removeIf(cell -> !(neighbours.get(cell) == 3 || (neighbours.get(cell) == 2 && state.isAlive(cell))));
        return new Pattern(neighbours.keySet());
    }
    private Timer timer = new Timer(DEFAULT_DELAY, event -> goToState(currentStateIndex+1));
}