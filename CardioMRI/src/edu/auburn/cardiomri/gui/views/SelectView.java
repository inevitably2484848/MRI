package edu.auburn.cardiomri.gui.views;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import edu.auburn.cardiomri.datastructure.Group;
import edu.auburn.cardiomri.datastructure.Group.AxisType;
import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.gui.models.SelectModel;

/**
 * This view is where an image will be chosen for each image panel in
 * WorkspaceView
 * 
 * @author Justin
 *
 */
public class SelectView extends View {
    private static final String CONTINUE = "Continue";
    private static final String SHORT_AXIS = "Short Axis";
    private static final String TWO_CHAMBER = "Two Chamber";
    private static final String FOUR_CHAMBER = "Four Chamber";

    /**
     * Creates three combo boxes and a button each with an 
     * action listener in order for a user to select a axis
     * for each image panel
     * 
     * @param model the Select model of the view
     */
    public SelectView(SelectModel model) {
        super();
        this.model = model;
        String[] seriesDescriptions = model.getSeriesDescriptions();
        this.panel.setLayout(new GridBagLayout());

        JComboBox<String> axisTypeSA = new JComboBox<String>(seriesDescriptions);
        axisTypeSA.setRenderer(new ComboBoxRenderer("ShortAxis"));
        axisTypeSA.setSelectedIndex(-1);
        JComboBox<String> axisType2CH = new JComboBox<String>(
                seriesDescriptions);
        axisType2CH.setRenderer(new ComboBoxRenderer("Two Chamber"));
        axisType2CH.setSelectedIndex(-1);
        JComboBox<String> axisType4CH = new JComboBox<String>(
                seriesDescriptions);
        axisType4CH.setRenderer(new ComboBoxRenderer("Four Chamber"));
        axisType4CH.setSelectedIndex(-1);

        axisTypeSA.setActionCommand(SHORT_AXIS);
        axisType2CH.setActionCommand(TWO_CHAMBER);
        axisType4CH.setActionCommand(FOUR_CHAMBER);

        axisTypeSA.addActionListener(this);
        axisType2CH.addActionListener(this);
        axisType4CH.addActionListener(this);

        JButton continueButton = new JButton(CONTINUE);
        continueButton.setActionCommand(CONTINUE);
        continueButton.addActionListener(this);

        this.panel.add(axisTypeSA);
        this.panel.add(axisType2CH);
        this.panel.add(axisType4CH);
        this.panel.add(continueButton);
    }
    
    /**
     * Performs a command on each combo box when a axis is selected
     */
    public void actionPerformed(ActionEvent event) {
        String actionCommand = event.getActionCommand();

        if (actionCommand.equals(CONTINUE)) {
        	getModel().setShortAxis(7);   //*  TESTING REMOVE 
        	getModel().setTwoChamber(5);  //*  TESTING REMOVE 
        	getModel().setFourChamber(6); //*  TESTING REMOVE 
            if (!getModel().validateStudy()) {
                // TODO: Show a dialog box with the error
            }
        } else {
            JComboBox<String> comboBox = (JComboBox<String>) event.getSource();

            if (actionCommand.equals(SHORT_AXIS)) {
            	
                //getModel().setShortAxis(comboBox.getSelectedIndex());
            } else if (actionCommand.equals(TWO_CHAMBER)) {
            	
                //getModel().setTwoChamber(comboBox.getSelectedIndex());
            } else if (actionCommand.equals(FOUR_CHAMBER)) {
            	
                //getModel().setFourChamber(comboBox.getSelectedIndex());
            }
        }

    }
    
    /***/
    public SelectModel getModel() {
        return (SelectModel) this.model;
    }
    
    /**
     * This is just for showing the drop down titles 
     * 
     * http://stackoverflow.com/questions/23882640/how-to-set-the-title-of-a-jcombobox-when-nothing-is-selected
     * 
     *@author bengustafson
     */
    public class ComboBoxRenderer extends JLabel implements ListCellRenderer
    {
        private String title;

        public ComboBoxRenderer(String title)
        {
            this.title = title;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean hasFocus)
        {
            if (index == -1 && value == null) setText(title);
            else setText(value.toString());
            return this;
        }
    }
}
