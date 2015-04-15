package edu.auburn.cardiomri.gui;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Vector;

import javafx.geometry.Point2D;

import javax.swing.SwingUtilities;

import com.pixelmed.display.SingleImagePanel;

import edu.auburn.cardiomri.datastructure.Contour;

public class ImageDisplay extends SingleImagePanel {

    /**
     * 
     */
    private static final long serialVersionUID = -6920775905498293695L;
    private Contour currentContour = null;
    public MouseEvent e;
    private GUIController guiController;
    protected AffineTransform imageToWindowCoordinateTransform;

    // Constructor
    // Takes a image to be displayed
    public ImageDisplay(ConstructImage sImg) { // change from SourceImage to
                                               // ConstructImage
        super(sImg);
    }

    // SingleImagePanel methods
    @Override
    public void mouseClicked(MouseEvent e) {
        // System.out.println("Success");
        // System.out.println("   " + e.getX()*2 + " " +e.getY()*2);
        // System.out.print(this.getSelectedDrawingShapes());
        if (SwingUtilities.isRightMouseButton(e)) {
            guiController.getImageDisplayClick(e);
        }

        else if (currentContour != null) {
            java.awt.geom.Point2D point = getImageCoordinateFromWindowCoordinate(e.getX(), e.getY());
            currentContour.addControlPoint(point.getX(), point.getY());
            this.repaint();
        } else {
            // throw error, currentContour is null
        }
    }

    // Allows for imageView to set the contour that the clicks get added to

    public void setCurrentContour(Contour contour) {
        currentContour = contour;
    }

    public void setContours(Vector<Contour> contours) {
        this.setPreDefinedShapes(contours);
    }

    public void paintComponent(Graphics g) {
        Vector<Shape> allControlPoints = new Vector<Shape>();
        for (Shape s : preDefinedShapes) {
//            DrawingUtilities.drawShadowedShape(s, g2d);
            for (Point2D controlPoint : ((Contour) s).getControlPoints()) {
                Ellipse2D ellipse = new Ellipse2D.Double(controlPoint.getX(), controlPoint.getY(), 1, 1);
                allControlPoints.add(ellipse);
            }
        }
        this.setPersistentDrawingShapes(allControlPoints);
        super.paintComponent(g);
        System.out.println(allControlPoints.size());
    }

    public void setGuiController(GUIController guiController) {
        this.guiController = guiController;
    }
}
