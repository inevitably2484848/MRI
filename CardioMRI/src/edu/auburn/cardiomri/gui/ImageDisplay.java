package edu.auburn.cardiomri.gui;

import java.awt.event.MouseEvent;
import java.util.Vector;

import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.display.SourceImage;

import edu.auburn.cardiomri.datastructure.Contour;

public class ImageDisplay extends SingleImagePanel {

	private Contour currentContour;
	private Vector<Contour> contours = new Vector<Contour>();
	
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
		currentContour.addControlPoint(e.getX(), e.getY());
		
		if(currentContour.getControlPoints().size() == 3)
		{
			System.out.println("Added contour");
			contours.add(currentContour);
			this.setPreDefinedShapes(contours);
			this.revalidate();
		}
		this.repaint();	
	}
	
	//Allows for imageView to set the contour that the clicks get added to
	
	public void setCurrentContour(Contour contour)
	{
		currentContour = contour;
	}
	
}

