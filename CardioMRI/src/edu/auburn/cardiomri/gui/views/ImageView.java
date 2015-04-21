package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javafx.geometry.Point2D;

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

public class ImageView extends SingleImagePanel implements ActionListener, ViewInterface, Observer {
	protected Model model;
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
	protected JPanel imageContourPanel;
	private static final long serialVersionUID = -6920775905498293695L;
	private Contour currentContour = null;
	
	//TODO: make them a different view
	private ImageView imageViewFourChamber;
	private ImageView imageViewTwoChamber;

	
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
		SingleImagePanel.deconstructAllSingleImagePanelsInContainer(this);
		
		ConstructImage sImg = new ConstructImage(dImage);
        dirtySource(sImg);
        updateContours(getImageModel().getContours());
        
		this.revalidate();
		this.repaint();
	}

	private void updateCurrentContour(Contour contour) {
		currentContour = contour;
		this.repaint();
	}

	private void updateContours(Vector<Contour> contours) {
		 this.setPreDefinedShapes(contours);
		this.repaint();
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
				spline1 = ContourCalc.getSplineFromControlPoints(c.getControlPoints(), c.isClosedCurve());
				spline1.computeVertices(50);
				length1 = spline1.getEstimatedArcLength();
				controlPoints = c.getControlPoints();
				controlPoints.add(p);
				spline2 = ContourCalc.getSplineFromControlPoints(controlPoints, c.isClosedCurve());
				spline2.computeVertices(50);
				length2 = spline2.getEstimatedArcLength();
				deltaLength = length2 - length1;

				deltaList.put(c, deltaLength);
			}
		}

		Map.Entry<Contour, Float> minDelta = null;
		for(Map.Entry<Contour, Float> entry : deltaList.entrySet()) {
			if (minDelta == null || entry.getValue().compareTo(minDelta.getValue()) < 0)
			{
				minDelta = entry;
			}
		}
		getImageModel().setSelectedContour(minDelta.getKey());
	}
	
	// SingleImagePanel methods
    @Override
    public void mouseClicked(MouseEvent e) {
        // System.out.println("Success");
        // System.out.println("   " + e.getX()*2 + " " +e.getY()*2);
        // System.out.print(this.getSelectedDrawingShapes());
        if (SwingUtilities.isRightMouseButton(e)) {
        	//The code in this method needs to be moved here, I couldn't find it.
            Point2D p = new Point2D(e.getX(), e.getY());
            selectContour(p);
        }

        else if (currentContour != null) {
            currentContour.addControlPoint(e.getX(), e.getY());
            this.repaint();
        } else {
            // throw error, currentContour is null
        }
    }

    // Allows for imageView to set the contour that the clicks get added to

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        System.out.println("test");
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
        } else if (actionCommand.equals("Delete Selected Contour")) {
            getImageModel().deleteSelectedContour();
        } else if (actionCommand.equals("Hide Selected Contour")) {
            getImageModel().hideSelectedContour();
        } else if (actionCommand.equals("Hide Contours")) {
            getImageModel().hideContours();
        } else if (actionCommand.equals("Show Contours")) {
            getImageModel().showContours();
        } else if (actionCommand.equals("Delete All Contours")) {
            getImageModel().deleteAllContours();
        }
    }
}
