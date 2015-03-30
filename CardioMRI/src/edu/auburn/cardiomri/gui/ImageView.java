package edu.auburn.cardiomri.gui;

import java.awt.*;
import java.util.Observable;
import java.util.Vector;


import javax.swing.JPanel;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SingleImagePanel;


import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.gui.ConstructImage;

public class ImageView implements java.util.Observer {

	private JPanel panel;
	private ImageDisplay display = null;

	private Vector<Contour> contours;
	private Contour contourObject = new Contour(Contour.Type.DEFAULT), currentContour;

	// Observer methods
	@Override
	public void update(Observable obs, Object obj) {

		if (obj.getClass() == DICOMImage.class) { 

			this.display = null;
			
			SingleImagePanel.deconstructAllSingleImagePanelsInContainer(this.panel);

			this.panel.removeAll();
		
			DICOMImage dImage = ((DICOMImage) obj);
		
			ConstructImage sImg = null;

			try {

				sImg = new ConstructImage(dImage);

				this.display = new ImageDisplay(sImg);
				this.contours = dImage.getContours();
				this.display.setContours(this.contours);
				

			} catch (DicomException e) {
				e.printStackTrace();
			}
			
			
			this.display.revalidate();
			this.display.repaint();	
			this.panel.add(this.display);
			this.panel.revalidate();
			this.panel.repaint();
		}
		
		if(obj instanceof Vector<?>)
		{
			if(((Vector<Contour>) obj).firstElement().getClass() == contourObject.getClass())
			{
				this.contours = (Vector<Contour>) obj;
				if(this.display != null){
					this.display.setContours(contours);
					this.display.setPreDefinedShapes(contours);
					this.display.repaint();
					this.panel.repaint();
				}
			}
		}
		
		if(obj.getClass() == contourObject.getClass())
		{
			if(this.display != null){
				this.display.setCurrentContour((Contour) obj);
				this.display.repaint();
				this.panel.repaint();
			}
			currentContour = (Contour) obj;
		}
	}


	// Getters
	/*
	 * Returns the class panel attribute.
	 *
	 * @return The class' panel attribute.
	 */

	public JPanel getPanel() { 
		return this.panel; 
	}
	
	public void refresh()
	{
		this.display.revalidate();
		this.display.repaint();	
	}
	
	// Constructors
	public ImageView() {
		//System.out.println("ImageView()");
		
		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(1, 1));
		this.panel.setBackground(Color.BLACK);
		this.panel.setOpaque(true);
		this.panel.setVisible(true);
	}
	
	public ImageDisplay getImageDisplay() {
		return this.display;
	}

}
