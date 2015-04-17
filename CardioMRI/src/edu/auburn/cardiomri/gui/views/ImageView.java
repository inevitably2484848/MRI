package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javafx.geometry.Point2D;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import toxi.geom.Spline2D;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SingleImagePanel;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.gui.ConstructImage;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.Model;
import edu.auburn.cardiomri.util.ContourCalc;

public class ImageView extends SingleImagePanel implements ViewInterface, Observer {
	private JPanel panel;
	private ImageModel model;
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
		try {
			ConstructImage sImg = new ConstructImage(dImage);
			//TODO:update image for single image panel
            dirtySource(sImg);
            this.repaint();

		} catch (DicomException e) {
			e.printStackTrace();
		}
		
		//TODO: re-look at this because panel is now not only the main image but everything else
		//panel.add(display);
		panel.revalidate();
		panel.repaint();
	}

	private void updateCurrentContour(Contour contour) {
		currentContour = contour;
		panel.repaint();
	}

	private void updateContours(Vector<Contour> contours) {
		 this.setPreDefinedShapes(contours);
		panel.repaint();
	}
	public void setModel(Model model)
	{
		this.model = (ImageModel) model;
	}
	
	public void setModel(ImageModel imageModel)
	{
		this.model = imageModel;
		//TODO: set models of other views
	}

	public ImageModel getModel() {
		return this.model;
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void refresh() {
		this.revalidate();
		this.repaint();
	}

	public ImageView(ConstructImage sImg) {
		super(sImg);
		setUpMainPanel();
		//TODO: create other three views views here 
	}

	public void selectContour(Point2D p) {
		DICOMImage dImage = this.model.getImage();
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
		this.model.setSelectedContour(minDelta.getKey());
	}

	
	private void setUpMainPanel()
	{   
		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(1, 1));
		this.panel.setBackground(Color.BLACK);
		this.panel.setOpaque(true);
		this.panel.setVisible(true);
		
    	this.imageContourPanel = new JPanel();
        this.imageContourPanel.setSize(200, 200);
        this.imageContourPanel.setLayout(new GridLayout(1, 1));
        this.imageContourPanel.setBackground(Color.BLUE);
        this.imageContourPanel.setOpaque(true);
        this.imageContourPanel.setVisible(true);

    	//Split Screen into three main areas
    	JSplitPane smallImagesPane = new JSplitPane(
	            JSplitPane.VERTICAL_SPLIT, true, this.imageViewTwoChamber.getPanel(), this.imageViewFourChamber.getPanel());
  	
    	JSplitPane rightSideOfWindow = new JSplitPane(
	            JSplitPane.VERTICAL_SPLIT, true, smallImagesPane, this.imageContourPanel);
    	
    	JSplitPane imagePanes  = new JSplitPane(
    			JSplitPane.HORIZONTAL_SPLIT,true, this, rightSideOfWindow);
	   

	    smallImagesPane.setDividerLocation(FRAME_HEIGHT/4);
	    rightSideOfWindow.setDividerLocation(FRAME_HEIGHT/2);
	    imagePanes.setDividerLocation(11*FRAME_WIDTH/20);
	    
	    this.panel.add(imagePanes);
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
}
