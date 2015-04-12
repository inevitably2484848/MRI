package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JPanel;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SingleImagePanel;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.gui.ConstructImage;
import edu.auburn.cardiomri.gui.ImageDisplay;
import edu.auburn.cardiomri.gui.models.ImageModel;

public class ImageView implements java.util.Observer {
    private JPanel panel;
    private ImageDisplay display = null;
    private ImageModel model;

    // Observer methods
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void update(Observable obs, Object obj) {
        if (obj.getClass() == DICOMImage.class) {
            updateImage((DICOMImage) obj);
        } else if (obj.getClass() == Vector.class) {
            Vector vector = (Vector) obj;
            if ((vector.size() == 0)
                    || (vector.firstElement().getClass() == Contour.class)) {
                updateContours((Vector<Contour>) obj);
            }
        } else if (obj.getClass() == Contour.class) {
            updateCurrentContour((Contour) obj);
        }
    }

    private void updateImage(DICOMImage dImage) {
        display = null;
        SingleImagePanel.deconstructAllSingleImagePanelsInContainer(panel);
        panel.removeAll();

        try {
            ConstructImage sImg = new ConstructImage(dImage);
            display = new ImageDisplay(sImg);
        } catch (DicomException e) {
            e.printStackTrace();
        }

        display.revalidate();
        display.repaint();

        panel.add(display);
        panel.revalidate();
        panel.repaint();
    }

    private void updateCurrentContour(Contour contour) {
        if (display != null) {
            display.setCurrentContour(contour);
            display.repaint();
            panel.repaint();
        } else {
            // throw error?
        }
    }

    private void updateContours(Vector<Contour> contours) {
        if (this.display != null) {
            display.setContours(contours);
            display.repaint();
            panel.repaint();
        } else {
            // throw error?
        }
    }

    public void setModel(ImageModel model) {
        this.model = model;
    }

    public ImageModel getModel() {
        return this.model;
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public void refresh() {
        this.display.revalidate();
        this.display.repaint();
    }

    public ImageView() {
        // System.out.println("ImageView()");

        this.panel = new JPanel();
        this.panel.setSize(200, 200);
        this.panel.setLayout(new GridLayout(1, 1));
        this.panel.setBackground(Color.BLACK);
        this.panel.setOpaque(true);
        this.panel.setVisible(true);
    }

    public ImageDisplay getImageDisplay() {
        return this.display;
    }
}
