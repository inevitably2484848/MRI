package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javafx.geometry.Point2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.pixelmed.display.SingleImagePanel;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.gui.ConstructImage;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.Model;

public class ImageView extends SingleImagePanel implements ActionListener,
        ViewInterface, Observer {
    protected Model model;
    protected JPanel imageContourPanel;
    private static final long serialVersionUID = -6920775905498293695L;

    /**
     * Redraws the DICOMImage, updates the selected contour's control points,
     * and updates the set of visible contours.
     */
    public void update(Observable obs, Object obj) {
        DICOMImage dImage = getImageModel().getImage();
        dirtySource(new ConstructImage(dImage));
        updateSelectedContour(getImageModel().getSelectedContour());
        updateVisibleContours(getImageModel().getVisibleContours());

        refresh();
    }

    /**
     * Updates the set of control points that should be drawn onto the screen.
     * Control points are drawn as 2x2 ellipses.
     * 
     * @param contour The currently selected contour
     */
    private void updateSelectedContour(Contour contour) {
        Vector<Shape> allControlPoints = new Vector<Shape>();
        if (contour != null) {
            for (Point2D controlPoint : contour.getControlPoints()) {
                Ellipse2D ellipse = new Ellipse2D.Double(controlPoint.getX(),
                        controlPoint.getY(), 2, 2);
                allControlPoints.add(ellipse);
            }
        }

        this.setPreDefinedShapes(allControlPoints);
    }

    /**
     * Updates the list of contours to be drawn onto the screen
     * 
     * @param contours List of Contour objects
     */
    private void updateVisibleContours(Vector<Contour> contours) {
        this.setSelectedDrawingShapes(contours);
    }

    /**
     * This is copy/pasted from the View class.
     */
    public void setModel(Model model) {
        this.model = model;
        this.model.addObserver(this);
    }

    public ImageModel getImageModel() {
        return (ImageModel) this.model;
    }

    public Model getModel() {
        return this.model;
    }

    /**
     * Returns a JPanel with this ImageView as its only element
     */
    public JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setSize(200, 200);
        panel.setLayout(new GridLayout(1, 1));
        panel.setBackground(Color.BLACK);
        panel.add(this);
        return panel;
    }

    public void refresh() {
        this.revalidate();
        this.repaint();
    }

    public ImageView(ConstructImage sImg) {
        super(sImg);
    }

    /**
     * Handle mouse click events. Left clicks add a control point. Right clicks
     * select the nearest visible contour.
     */
    public void mouseClicked(MouseEvent e) {
        java.awt.geom.Point2D mouseClick = getImageCoordinateFromWindowCoordinate(
                e.getX(), e.getY());

        if (SwingUtilities.isRightMouseButton(e)) {
            getImageModel().selectContour(mouseClick.getX(), mouseClick.getY());
        } else {
            if (!getImageModel().addControlPoint(mouseClick.getX(),
                    mouseClick.getY())) {
                System.err.println("currentContour is null");
            }
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals("Default Type")) {
            getImageModel().addContourToImage(new Contour(Type.DEFAULT));
        } else if (actionCommand.equals("Closed Type")) {
            getImageModel().addContourToImage(new Contour(Type.DEFAULT_CLOSED));
        } else if (actionCommand.equals("Open Type")) {
            getImageModel().addContourToImage(new Contour(Type.DEFAULT_OPEN));
        } else if (actionCommand.equals("Delete Contour")) {
            if (getImageModel().getSelectedContour() == null) {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "Please select a contour to delete.");
            } else {
                getImageModel().deleteSelectedContour();
            }
        } else if (actionCommand.equals("Hide Contour")) {
            if (getImageModel().getSelectedContour() == null) {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "Please select a contour to hide.");
            } else {
                getImageModel().hideSelectedContour();
            }

        } else if (actionCommand.equals("Select Contour")) {
            Vector<Contour> visibleContours = getImageModel()
                    .getVisibleContours();
            if (visibleContours.size() > 0) {
                Contour[] contours = new Contour[visibleContours.size()];
                visibleContours.toArray(contours);

                Contour c = (Contour) JOptionPane.showInputDialog(
                        imageContourPanel, "Select Contour: ", "Contours",
                        JOptionPane.PLAIN_MESSAGE, null, contours, "ham");

                getImageModel().setSelectedContour(c);
            } else {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "There are no visible contours to select.");
            }
        } else if (actionCommand.equals("Hide Contours")) {
            if (getImageModel().getContours() == null
                    || getImageModel().getContours().size() == 0) {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "There are no contours to hide.");
            } else {
                getImageModel().hideAllContours();
            }
        } else if (actionCommand.equals("Show Contours")) {
            if (getImageModel().getHiddenContours() == null
                    || getImageModel().getHiddenContours().size() == 0) {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "There are no contours to show.");
            } else {
                getImageModel().showAllContours();
            }
        } else if (actionCommand.equals("Delete All Contours")) {
            if (getImageModel().getContours() == null
                    || getImageModel().getContours().size() == 0) {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "There are no contours to delete.");
            } else {
                getImageModel().deleteAllContours();
            }
        }
    }

}
