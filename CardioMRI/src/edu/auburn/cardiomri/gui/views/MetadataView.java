package edu.auburn.cardiomri.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.gui.models.MetadataModel;

public class MetadataView implements java.util.Observer {
    private JPanel panel;
    private JTable attrTable;
    private MetadataModel model;

    // Observer methods
    public void update(Observable obs, Object obj) {
        if (obj.getClass() == Study.class) {
            // System.out.println("MetaDataView : updated with Study");

            this.panel.removeAll();

            this.panel.revalidate();
        }
        if (obj.getClass() == DICOMImage.class) {
            // System.out.println("MetaDataView : updated with DICOMImage");

            this.panel.removeAll();

            String[] columnNames = { "Tag Names", "Value" };

            String[][] data = ((DICOMImage) obj).getAttributeInfo();

            attrTable = new JTable(data, columnNames);
            attrTable.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            attrTable.setGridColor(Color.GRAY);

            Dimension size = new Dimension(550, 300);
            attrTable.setPreferredScrollableViewportSize(size);

            JScrollPane tableContainer = new JScrollPane(attrTable);

            this.panel.add(tableContainer, BorderLayout.CENTER);

            this.panel.revalidate();
        }
    }

    /**
     * Returns the class' panel attribute.
     * 
     * @return The class' panel attribute.
     */
    public JPanel getPanel() {

        return this.panel;
    }

    public MetadataView() {
        // System.out.println("MetaDataView()");

        this.panel = new JPanel();
        this.panel.setName("Meta Data");
        this.panel.add("North", new Label("No DICOM has been imported"));

    }

    public MetadataModel getModel() {
        return model;
    }

    public void setModel(MetadataModel model) {
        this.model = model;
    }
}
