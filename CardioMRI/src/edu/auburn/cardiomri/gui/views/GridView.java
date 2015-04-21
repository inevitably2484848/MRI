package edu.auburn.cardiomri.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.auburn.cardiomri.datastructure.Group;
import edu.auburn.cardiomri.datastructure.Slice;
import edu.auburn.cardiomri.gui.models.GridModel;

public class GridView extends View {
    private int s = 0;
    private int t = 0;
    private int i = 0;

    private Dimension size;
    private JButton[][] buttons;

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.substring(0, 6).equals("Button")) {
            // System.out.println("GUIController : resetting focus");
            StringTokenizer tokenizer = new StringTokenizer(
                    actionCommand.substring(7), ",");
            String timeStr = tokenizer.nextToken();
            String sliceStr = tokenizer.nextToken();

            int newTime = (Integer.parseInt(timeStr) - 1);
            int newSlice = (Integer.parseInt(sliceStr) - 1);

            this.t = newTime;
            this.s = newSlice;
            getGridModel().setCurrentImage(s, t, i);

            // this.mainComponent.requestFocusInWindow();
        }
    }

    // Observer methods
    public void update(Observable obs, Object obj) {
        // Updating the current DICOM image.
        if (obj.getClass() == int[].class) {
            // System.out.println("GridView : set new image");

            // first, set current back to regular
            this.buttons[this.t][this.s].setBorderPainted(true);
            this.buttons[this.t][this.s].setOpaque(false);
            this.buttons[this.t][this.s].setBackground(Color.GRAY);

            // then, set the new stuff
            int[] indices = (int[]) obj;
            this.s = indices[1];
            this.t = indices[2];
            this.i = indices[3];

            // System.out.println("G:" + this.g + " S:" + this.s + " T:" +
            // this.t + " I:" + this.i);

            this.buttons[this.t][this.s].setBackground(Color.GREEN);
            this.buttons[this.t][this.s].setOpaque(true);
            this.buttons[this.t][this.s].setBorderPainted(false);

            this.panel.revalidate();
        }
    }

    /**
     * Reinitializes the class' array of JButton objects and ensures that it
     * accurately reflects the current Group's Slice and Time structure.
     * 
     * @param group
     * 
     */
    public void setupGrid() {
        Group group = getGridModel().getGroup();

        // Figure out the size of the grid : time x slice
        int maxTime = 0;
        int numSlices = 0;
        for (Slice s : group.getSlices()) {
            if (maxTime < s.getTimes().size()) {
                maxTime = s.getTimes().size();
            }
            numSlices++;
        }

        // System.out.println("#Time:" + maxTime + " #Slice:" + numSlices);

        // Set up default size of grid
        if (maxTime < 12)
            maxTime = 12;
        if (numSlices < 7)
            numSlices = 7;

        // System.out.println("#Time:" + maxTime + " #Slice:" + numSlices);

        // System.out.println("Size of the table is " + maxTime + " x " +
        // numSlices);
        this.buttons = null;
        this.buttons = new JButton[maxTime][numSlices];

        // height by width
        GridLayout panelGrid = new GridLayout(numSlices + 1, maxTime + 1);
        panelGrid.setHgap(1);
        panelGrid.setVgap(1);

        this.panel = new JPanel(panelGrid);

        this.panel.setFocusable(false);

        // i - y axis (Slice)
        // j - x axis (Time)
        for (int i = 0; i < numSlices + 1; i++) {
            for (int j = 0; j < maxTime + 1; j++) {
                JButton button = new JButton();
                button.addActionListener(this);
                button.setActionCommand(new String("Button " + j + "," + i));
                button.setPreferredSize(new Dimension(20, 20));
                // first take care of the labeled row and column
                if (i == 0 && j == 0) {
                    button.setName("'");
                    button.setText("'");
                } else if (i == 0) {
                    button.setName(new String("" + j));
                    button.setText(new String("" + j));
                } else if (j == 0) {
                    button.setName(new String("" + i));
                    button.setText(new String("" + i));
                } else {
                    // then, take care of the other buttons
                    this.buttons[j - 1][i - 1] = button;
                    if (i == (this.s + 1) && j == (this.t + 1)) {
                        button.setBackground(Color.GREEN);
                        button.setOpaque(true);
                        button.setBorderPainted(false);
                    } else {
                        if (j > group.getSlices().get(this.s).getTimes().size()) {
                            button.setBorderPainted(false);
                        } else if (i > group.getSlices().size()) {
                            button.setBorderPainted(false);
                        }
                    }
                }
                panel.add(button);
            }
        }

        // Set the size of the scroll panel
        JScrollPane gridContainer = new JScrollPane();
        if (this.size != null) {
            gridContainer.setMinimumSize(new Dimension(this.size.width,
                    this.size.height - 15));
            gridContainer.setPreferredSize(new Dimension(this.size.width,
                    this.size.height - 15));
        } else {
            gridContainer.setMinimumSize(new Dimension(300, 185));
            gridContainer.setPreferredSize(new Dimension(300, 185));
        }

        // Add grid to main panel
        this.panel.add(gridContainer, BorderLayout.CENTER);

        // Set grid view scroll bars
        gridContainer
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        gridContainer
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        gridContainer.setFocusable(false);
    }

    /**
     * Sets the possible size of the panel that the class is in.
     * 
     * @param s : Dimension object that is used as the class' size attribute.
     */
    public void setSize(Dimension s) {
        this.size = s;
    }

    /**
     * Returns the class' s attribute.
     * 
     * @return The class' s attribute.
     */
    public int getSliceIndex() {
        return this.s;
    }

    /**
     * Returns the class' t attribute.
     * 
     * @return The class' t attribute.
     */
    public int getTimeIndex() {
        return this.t;
    }

    /**
     * Returns the class' study attribute.
     * 
     * @return The class' study attribute.
     */
    public int getImageIndex() {
        return this.i;
    }

    public GridView() {
        // System.out.println("GridView()");
        super();
        this.panel.setFocusable(false);

        this.s = 0;
        this.t = 0;
        this.i = 0;
    }

    public GridModel getGridModel() {
        return (GridModel) model;
    }

    public class LeftKeyAction extends AbstractAction {
        private static final long serialVersionUID = 6612132766001531904L;

        public void actionPerformed(ActionEvent e) {
            getGridModel().decrementTimeIndex();
        }
    }

    public class RightKeyAction extends AbstractAction {
        private static final long serialVersionUID = 6824940022077838332L;

        public void actionPerformed(ActionEvent e) {
            getGridModel().incrementTimeIndex();
        }
    }

    public class UpKeyAction extends AbstractAction {
        private static final long serialVersionUID = 4942341424740412096L;

        public void actionPerformed(ActionEvent e) {
            getGridModel().decrementSliceIndex();
        }
    }

    public class DownKeyAction extends AbstractAction {
        private static final long serialVersionUID = -7183889255252949565L;

        public void actionPerformed(ActionEvent e) {
            getGridModel().incrementSliceIndex();
        }
    }
}
