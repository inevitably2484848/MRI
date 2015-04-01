package edu.auburn.cardiomri.gui.views;

import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.gui.models.StudyStructureModel;
import edu.auburn.cardiomri.gui.nodes.StudyTreeNode;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class StudyStructureView implements java.util.Observer {

    private JButton newStudyButton;
    private JButton loadExistingButton;
    private JButton loadSingleButton;

    private StudyTreeNode rootNode;
    private JTree tree;

    private JPanel panel;

    private MouseListener mouseListener;

    private StudyStructureModel model;

    // Observer methods
    public void update(Observable obs, Object obj) {
        // System.out.println("StudyStructureView : updated");

        // New Indices
        if (obj.getClass() == int[].class) {
            int[] indices = (int[]) obj;

            int gIndex = indices[0];
            int sIndex = indices[1];
            int tIndex = indices[2];
            int iIndex = indices[3];

            // System.out.println("StudyStructureView : " + gIndex + " " +
            // sIndex + " " + tIndex + " " + iIndex);

            if (this.tree.getSelectionPath() != null) {
                this.tree.removeSelectionPath(this.tree.getSelectionPath());
            }

            TreePath tPath = new TreePath(this.rootNode);
            // Group
            tPath = tPath.pathByAddingChild(rootNode.getChildAt(gIndex));
            // Slice
            tPath = tPath.pathByAddingChild(rootNode.getChildAt(gIndex)
                    .getChildAt(sIndex));
            // Time
            tPath = tPath.pathByAddingChild(rootNode.getChildAt(gIndex)
                    .getChildAt(sIndex).getChildAt(tIndex));
            // Image
            tPath = tPath.pathByAddingChild(rootNode.getChildAt(gIndex)
                    .getChildAt(sIndex).getChildAt(tIndex).getChildAt(iIndex));

            // System.out.println("Length : " + tPath.getPathCount());

            this.tree.setSelectionPath(tPath);
        }

        // Study
        else if (obj.getClass() == Study.class) {

            this.panel.removeAll();
            this.panel.setLayout(new GridLayout(1, 1));

            this.rootNode = new StudyTreeNode(((Study) obj));

            this.tree = new JTree(this.rootNode);
            this.tree.setSize(this.panel.getSize());
            for (int i = 0; i < this.tree.getRowCount(); i++) {
                this.tree.expandRow(i);
            }
            // tree.setPreferredSize(new Dimension(150, 600));

            JScrollPane treeContainer = new JScrollPane(this.tree);

            treeContainer.setPreferredSize(new Dimension(150, 300));
            treeContainer.setSize(150, 300);

            this.tree.addMouseListener(this.mouseListener);

            this.panel.add(treeContainer);

            // this.panel.setVisible(true);

            this.panel.revalidate();
        }
    }

    /**
     * Sets the class' mouseListener attribute.
     * 
     * @param mL : MouseListener object that is used by the class as its
     *            mouseListener attribute.
     */
    public void setMouseListener(MouseListener mL) {
        this.mouseListener = mL;
    }

    /**
     * Returns the class' panel attribute.
     * 
     * @return The class' panel attribute.
     */
    public JPanel getPanel() {
        return this.panel;
    }

    /**
     * Returns the class' tree attribute.
     * 
     * @return The class' tree attribute.
     */
    public JTree getTree() {
        return this.tree;
    }

    // Button
    /**
     * Sets the initial buttons' (Create New Study, Load Existing Study, Load
     * Single Dicom Image) ActionListener attributes to the given
     * AttributeListener.
     * 
     * @param controller : ActionListener object that is set up to handle
     *            ActionEvents from the three initial buttons.
     */
    public void addController(ActionListener controller) {
        // System.out.println("StudyStructureView : adding controller");
        newStudyButton.addActionListener(controller);
        loadExistingButton.addActionListener(controller);
        loadSingleButton.addActionListener(controller);
    }

    // Constructor
    public StudyStructureView() {
        // System.out.println("StudyStructureView()");

        this.panel = new JPanel();

        this.newStudyButton = new JButton("Create New Study");
        this.loadExistingButton = new JButton("Load Existing Study");
        this.loadSingleButton = new JButton("Load Single DICOM");

        this.newStudyButton.setSize(100, 100);
        this.loadExistingButton.setSize(100, 100);
        this.loadSingleButton.setSize(100, 100);

        this.panel.add(newStudyButton);
        this.panel.add(loadExistingButton);
        this.panel.add(loadSingleButton);

        this.panel.setSize(300, 300);
    }

    public StudyStructureModel getModel() {
        return model;
    }

    public void setModel(StudyStructureModel model) {
        this.model = model;
    }
}
