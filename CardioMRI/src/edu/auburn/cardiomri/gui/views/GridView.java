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

/**
 * The gird view seen in top left in the main workspace view, creates a lay out of slices and time frames
 * 
 * @note The x axis (left to right) is Time frames, they y axis (top to bottom) is Slices
 * 
 * @author Ben Gustafson
 */
public class GridView extends View {
	
	// s= slice, t= time, i= group_index
    private int s = 0;
    private int t = 0;
    private int i = 0;
    private int maxHeight = 0;
    private Dimension size;
    private static final Color END_SYSTOLE = Color.BLUE; //Lowest volume
    private static final Color END_DIASTOLE = Color.ORANGE;//Completely full
    private static final Color SELECTED_COLOR = Color.GREEN;
    private static final Color NORMAL_COLOR = Color.GRAY;
    private JButton[][] buttons;

    /**
     * Makes sure it is a button on the grid, then takes the x,y coordinates from the action command
     *
     */
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.substring(0, 6).equals("Button")) {
        	
            StringTokenizer tokenizer = new StringTokenizer(
                    actionCommand.substring(7), ",");
            String timeStr = tokenizer.nextToken();
            String sliceStr = tokenizer.nextToken();

            int newTime = (Integer.parseInt(timeStr) - 1);
            int newSlice = (Integer.parseInt(sliceStr) - 1);
            
            //if slice is 0, then button is on the very top row (Set ED or ES)
            if(newSlice == -1)
            {
            	rotateColumnType(newTime, ((JButton) e.getSource()));
                this.panel.revalidate();
            }
            else
            {
                getGridModel().setCurrentImage(newSlice, newTime, i);
            }
        }
    }

    /**
     * On receiving new slice, time, and group index information the current button is set to normal color and then new button is set to selected color
     * 
     * @note this is called from gridModel right after the setCurrentImage is called in grid model
     */
	public void update(Observable obs, Object obj) {
        if (obj.getClass() == int[].class) {

            // first, set current back to regular
            this.buttons[this.t][this.s].setBorderPainted(true);
            this.buttons[this.t][this.s].setOpaque(false);
            this.buttons[this.t][this.s].setBackground(NORMAL_COLOR);
            if(getGridModel().getGroup().getEd_index() == this.t )
            {
            	this.buttons[this.t][this.s].setBackground(END_DIASTOLE);
            	this.buttons[this.t][this.s].setOpaque(true);
	    		this.buttons[this.t][this.s].setBorderPainted(false);
            }
            if(getGridModel().getGroup().getEs_index() == this.t)
            {
            	this.buttons[this.t][this.s].setBackground(END_SYSTOLE);
            	this.buttons[this.t][this.s].setOpaque(true);
	    		this.buttons[this.t][this.s].setBorderPainted(false);
            }
            

            int[] indices = (int[]) obj;
            this.s = indices[0];
            this.t = indices[1];
            this.i = indices[2];
            
            // Then set new button
            this.buttons[this.t][this.s].setBackground(SELECTED_COLOR);
            this.buttons[this.t][this.s].setOpaque(true);
            this.buttons[this.t][this.s].setBorderPainted(false);

            this.panel.revalidate();
        }
    }

    /**
     * Sets up the class' array of JButton objects and ensures that it
     * accurately reflects the current Group's Slice and Time structure.
     * 
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
        // Default size of grid
        if (maxTime < 12)
            maxTime = 12;
        if (numSlices < 7)
            numSlices = 7;
        
        this.maxHeight = numSlices;
        this.buttons = null;
        this.buttons = new JButton[maxTime][numSlices];

        GridLayout panelGrid = new GridLayout(numSlices + 1, maxTime + 1);
        panelGrid.setHgap(1);
        panelGrid.setVgap(1);

        JPanel buttonPanel = new JPanel(panelGrid);
        buttonPanel.setFocusable(false);

        // i - y axis (Slice)
        // j - x axis (Time)
        for (int i = 0; i < numSlices + 1; i++) {
            for (int j = 0; j < maxTime + 1; j++) {
                JButton button = new JButton();
                button.addActionListener(this);
                button.setActionCommand(new String("Button " + j + "," + i));
                button.setPreferredSize(new Dimension(21, 21));
                button.setBackground(NORMAL_COLOR);
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
                        button.setBackground(SELECTED_COLOR);
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
                buttonPanel.add(button);
            }
        }
        
        //Set the ES and ED if they are there for the group
        if(group.getEd_index() > -1)
        {
        	changeButtonColumn(END_DIASTOLE,group.getEd_index(),false);
        }
        if(group.getEs_index() > -1)
        {
        	changeButtonColumn(END_SYSTOLE,group.getEs_index(),false);
        }

        // Set the size of the scroll panel
        JScrollPane gridContainer = new JScrollPane(buttonPanel);
        if (this.size != null) {
            gridContainer.setMinimumSize(new Dimension(this.size.width,
                    this.size.height - 15));
            gridContainer.setPreferredSize(new Dimension(this.size.width,
                    this.size.height - 15));
        } else {
            gridContainer.setMinimumSize(new Dimension(300, 185));
            gridContainer.setPreferredSize(new Dimension(300, 185));
        }

        // Set grid view scroll bars
        gridContainer
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        gridContainer
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        gridContainer.setFocusable(false);
        
        // Add grid to main panel
        this.panel.add(gridContainer, BorderLayout.CENTER);
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

    /**
     * Constructor, sets everything to 0 
     */
    public GridView() {
        super();
        this.panel.setFocusable(false);

        this.s = 0;
        this.t = 0;
        this.i = 0;
    }
    
    /**
     * Detects and changes the color and type of a column based on the current setting in gridModel
     * 
     * 
     * @note Also changes the title of the button sent to it
     */
    private void rotateColumnType(int x, JButton button) {
    	String name = new String("" + (x+1));
    	boolean	changed = false;
    	if(getGridModel().getGroup().getEd_index() == -1 && !(x == getGridModel().getGroup().getEs_index()))
    	{
    		changeButtonColumn(END_DIASTOLE,x,false);
    		getGridModel().getGroup().setEd_index(x);
    		name = "ED";
    		changed = true;
    	}
    	if(!changed && getGridModel().getGroup().getEs_index() == -1 &&  !(x == getGridModel().getGroup().getEd_index()))
    	{
    		changeButtonColumn(END_SYSTOLE,x,false);
    		getGridModel().getGroup().setEs_index(x);
    		name = "ES";
    		changed = true;
    	}
    	if(!changed && x == getGridModel().getGroup().getEd_index())
    	{	
    		//Reset ED index to null, and rotate to normal
			changeButtonColumn(NORMAL_COLOR,x,true);
			getGridModel().getGroup().setEd_index(-1);
			name = new String("" + (x+1));
    	}
    	if(!changed && x == getGridModel().getGroup().getEs_index())
    	{	
    		//Reset ES index to null, and rotate to normal
			changeButtonColumn(NORMAL_COLOR,x,true);
    		getGridModel().getGroup().setEs_index(-1);
    		name = new String("" + (x+1));
    	}
    	button.setText(name);
    	button.setName(name);
    	this.panel.revalidate();
	}
    
    /**
     * changes given column to the given color for the button, all but the selected one
     * 
     * @Note Currently does not work properly 
     * 
     * @param buttonColor	New color for the buttons
     * @param x				Column index of the button array
     * @param normal		This is an indication of if the button is going to be changed back to the normal color
     */
    private void changeButtonColumn(Color buttonColor, int x, boolean normal)
    {
    	for(int y = 0; y < this.maxHeight; y++)
    	{
    		if(!this.buttons[x][y].getBackground().equals(SELECTED_COLOR))
    		{
	    		this.buttons[x][y].setBackground(buttonColor);
	    		if(!normal)
	    		{
		    		this.buttons[x][y].setOpaque(true);
		    		this.buttons[x][y].setBorderPainted(false);
	    		}
	    		else
	    		{
	    			this.buttons[x][y].setOpaque(false);
		    		this.buttons[x][y].setBorderPainted(true);
	    		}
    		}
    	}
    }

    /**
     * Helper method for this model
     * 
     * @return the current model, but cased as a gridModel (it will always be one)
     */
    public GridModel getGridModel() {
        return (GridModel) model;
    }

    /**
     * Action key event for the left arrow button
     * 
     * @author Ben Gustafson
     *
     */
    public class LeftKeyAction extends AbstractAction {
        private static final long serialVersionUID = 6612132766001531904L;

        public void actionPerformed(ActionEvent e) {
        	System.out.println("got it");
            getGridModel().decrementTimeIndex();
        }
    }

    /**
     * Action key event for the Right arrow button
     * 
     * @author Ben Gustafson
     *
     */
    public class RightKeyAction extends AbstractAction {
        private static final long serialVersionUID = 6824940022077838332L;

        public void actionPerformed(ActionEvent e) {
            getGridModel().incrementTimeIndex();
        }
    }

    /**
     * Action key event for the Up arrow button
     * 
     * @author Ben Gustafson
     *
     */
    public class UpKeyAction extends AbstractAction {
        private static final long serialVersionUID = 4942341424740412096L;

        public void actionPerformed(ActionEvent e) {
            getGridModel().decrementSliceIndex();
        }
    }
    
    /**
     * Action key event for the Down arrow button
     * 
     * @author Ben Gustafson
     *
     */
    public class DownKeyAction extends AbstractAction {
        private static final long serialVersionUID = -7183889255252949565L;

        public void actionPerformed(ActionEvent e) {
            getGridModel().incrementSliceIndex();
        }
    }
}
