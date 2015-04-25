package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javafx.geometry.Point2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import toxi.geom.Spline2D;

import com.pixelmed.display.SingleImagePanel;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.gui.ConstructImage;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.Model;
import edu.auburn.cardiomri.util.ContourCalc;

public class ImageView extends SingleImagePanel implements ActionListener,
        ViewInterface, Observer {
    protected Model model;
    protected JPanel imageContourPanel;
    private static final long serialVersionUID = -6920775905498293695L;

    public void update(Observable obs, Object obj) {
        if (obj.getClass() == DICOMImage.class) {
            DICOMImage dImage = (DICOMImage) obj;
            dirtySource(new ConstructImage(dImage));
            updateCurrentContour(getImageModel().getSelectedContour());
            updateVisibleContours(getImageModel().getVisibleContours());

            refresh();
        }
    }

    private void updateCurrentContour(Contour contour) {
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

    public void selectContour(Point2D p) {
        DICOMImage dImage = getImageModel().getImage();
        Spline2D spline1, spline2;
        float length1, length2, deltaLength;
        List<Point2D> controlPoints;
        HashMap<Contour, Float> deltaList = new HashMap<Contour, Float>();
        for (Contour c : dImage.getContours()) {
            if (c.getControlPoints().size() == 0)
                continue;
            else {
                spline1 = ContourCalc.getSplineFromControlPoints(
                        c.getControlPoints(), c.isClosedCurve());
                spline1.computeVertices(50);
                length1 = spline1.getEstimatedArcLength();
                controlPoints = c.getControlPoints();
                controlPoints.add(p);
                spline2 = ContourCalc.getSplineFromControlPoints(controlPoints,
                        c.isClosedCurve());
                spline2.computeVertices(50);
                length2 = spline2.getEstimatedArcLength();
                deltaLength = length2 - length1;

                deltaList.put(c, deltaLength);
            }
        }

        Map.Entry<Contour, Float> minDelta = null;
        for (Map.Entry<Contour, Float> entry : deltaList.entrySet()) {
            if (minDelta == null
                    || entry.getValue().compareTo(minDelta.getValue()) < 0) {
                minDelta = entry;
            }
        }
        getImageModel().setSelectedContour(minDelta.getKey());
    }

    // SingleImagePanel methods
    @Override
    public void mouseClicked(MouseEvent e) {
        java.awt.geom.Point2D mouseClick = getImageCoordinateFromWindowCoordinate(
                e.getX(), e.getY());
        if (SwingUtilities.isRightMouseButton(e)) {
            Point2D p = new Point2D(e.getX(), e.getY());
            selectContour(p);
        } else {
            if (!getImageModel().addControlPoint(mouseClick.getX(),
                    mouseClick.getY())) {
                System.err.println("currentContour is null");
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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
            getImageModel().selectContour(imageContourPanel);

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
