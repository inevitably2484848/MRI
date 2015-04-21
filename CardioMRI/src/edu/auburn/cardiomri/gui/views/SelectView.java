package edu.auburn.cardiomri.gui.views;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import edu.auburn.cardiomri.datastructure.Group;
import edu.auburn.cardiomri.datastructure.Group.AxisType;
import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.gui.models.SelectModel;

public class SelectView extends View {

    /**
     * 
     */
    private static final String CONTINUE = "Continue";
    private static final String SHORT_AXIS = "Short Axis";
    private static final String TWO_CHAMBER = "Two Chamber";
    private static final String FOUR_CHAMBER = "Four Chamber";

    public SelectView(SelectModel model) {
        super();
        this.model = model;
        String[] seriesDescriptions = model.getSeriesDescriptions();
        this.panel.setLayout(new GridBagLayout());

        JComboBox<String> axisTypeSA = new JComboBox<String>(seriesDescriptions);
        JComboBox<String> axisType2CH = new JComboBox<String>(
                seriesDescriptions);
        JComboBox<String> axisType4CH = new JComboBox<String>(
                seriesDescriptions);

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

    public void actionPerformed(ActionEvent event) {
        String actionCommand = event.getActionCommand();

        if (actionCommand.equals(CONTINUE)) {
            if (!getModel().validateStudy()) {
                // TODO: Show a dialog box with the error
            }
        } else {
            JComboBox<String> comboBox = (JComboBox<String>) event.getSource();

            if (actionCommand.equals(SHORT_AXIS)) {
                getModel().setShortAxis(comboBox.getSelectedIndex());
            } else if (actionCommand.equals(TWO_CHAMBER)) {
                getModel().setTwoChamber(comboBox.getSelectedIndex());
            } else if (actionCommand.equals(FOUR_CHAMBER)) {
                getModel().setFourChamber(comboBox.getSelectedIndex());
            }
        }

    }

    public SelectModel getModel() {
        return (SelectModel) this.model;
    }
}
