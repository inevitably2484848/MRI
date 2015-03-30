package edu.auburn.cardiomri.gui;

import java.awt.event.MouseEvent;
import java.util.Vector;

import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.display.SourceImage;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;

public class ImageDisplay extends SingleImagePanel {

	private Contour currentContour = null;
	private Vector<Contour> contours;
	
	//Constructor 
	//Takes a image to be displayed
	public ImageDisplay(ConstructImage sImg) { //change from SourceImage to ConstructImage
		super(sImg);
	}
	
	//SingleImagePanel methods 
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//System.out.println("Success");
		//System.out.println("   " + e.getX()*2 + " " +e.getY()*2);
		//System.out.print(this.getSelectedDrawingShapes());
		if (currentContour != null)
		{
			currentContour.addControlPoint(e.getX(), e.getY());
			this.repaint();	
		}
	}
	
	//Allows for imageView to set the contour that the clicks get added to
	
	public void setCurrentContour(Contour contour)
	{
		currentContour = contour;
	}
	
	public Vector<Contour> getContours() {
		return this.contours;
	}
	
	public void setContours(Vector<Contour> contours) {
		this.contours = contours;
		this.setPreDefinedShapes(this.contours);
		this.repaint();	
	}
}

