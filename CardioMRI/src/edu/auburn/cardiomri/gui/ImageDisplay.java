package edu.auburn.cardiomri.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.SwingUtilities;

import com.pixelmed.display.SingleImagePanel;

import edu.auburn.cardiomri.datastructure.Contour;

public class ImageDisplay extends SingleImagePanel {

    /**
     * 
     */
//    private static final long serialVersionUID = -6920775905498293695L;
//    private Contour currentContour = null;
//    public MouseEvent e;
//    private GUIController guiController;
//
//    // Constructor
//    // Takes a image to be displayed
    public ImageDisplay(ConstructImage sImg) { // change from SourceImage to
                                               // ConstructImage
        super(sImg);
    }
//
//    // SingleImagePanel methods
//    @Override
//    public void mouseClicked(MouseEvent e) {
//        // System.out.println("Success");
//        // System.out.println("   " + e.getX()*2 + " " +e.getY()*2);
//        // System.out.print(this.getSelectedDrawingShapes());
//        if (SwingUtilities.isRightMouseButton(e)) {
//            guiController.getImageDisplayClick(e);
//        }
//
//        else if (currentContour != null) {
//            currentContour.addControlPoint(e.getX(), e.getY());
//            this.repaint();
//        } else {
//            // throw error, currentContour is null
//        }
//    }
//
//    // Allows for imageView to set the contour that the clicks get added to
//
//    public void setCurrentContour(Contour contour) {
//        currentContour = contour;
//    }
//
//    public void setContours(Vector<Contour> contours) {
//        this.setPreDefinedShapes(contours);
//    }
//
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D) g;
//        System.out.println("test");
//    }
//
//    public void setGuiController(GUIController guiController) {
//        this.guiController = guiController;
//    }
}
