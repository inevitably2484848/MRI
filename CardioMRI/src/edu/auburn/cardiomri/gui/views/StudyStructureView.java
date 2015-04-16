package edu.auburn.cardiomri.gui.views;

import java.util.Observable;

import javax.swing.JPanel;

import edu.auburn.cardiomri.gui.models.StudyStructureModel;

public class StudyStructureView extends View {
    private JPanel panel;
    private StudyStructureModel model;

    // Observer methods
    public void update(Observable obs, Object obj) {

    }

    /**
     * Returns the class' panel attribute.
     * 
     * @return The class' panel attribute.
     */
    public JPanel getPanel() {
        return this.panel;
    }

    // Constructor
    public StudyStructureView() {
        // System.out.println("StudyStructureView()");

        this.panel = new JPanel();
        this.panel.setSize(300, 300);
    }

    public StudyStructureModel getModel() {
        return model;
    }

    public void setModel(StudyStructureModel model) {
        this.model = model;
    }
}
