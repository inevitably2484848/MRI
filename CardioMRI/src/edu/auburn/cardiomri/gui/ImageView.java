package edu.auburn.cardiomri.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SingleImagePanel;
//import com.pixelmed.display.SourceImage;
import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.display.SourceImage;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.gui.ConstructImage;

public class ImageView implements java.util.Observer {

	private JLayeredPane layeredPanel;
	private JFrame jFrame;
	private JPanel panel;
	private JPanel glass;
	private MouseListener mouseListener;

	private ImageDisplay display = null;
	private Vector<Contour> contours;
	private Contour contourObject = new Contour(Contour.Type.DEFAULT), currentContour;


	// Observer methods
	@Override
	public void update(Observable obs, Object obj) {

		if (obj.getClass() == DICOMImage.class) { 

			this.panel.removeAll();

			this.display = null;

			DICOMImage dImage = ((DICOMImage) obj);
			this.contours = dImage.getContours();
			
			//AttributeList dList = ((AttributeList) obj);

			ConstructImage sImg = null;
			
			try {
				
				System.out.println("Image view reset Image");
				sImg = new ConstructImage(dImage);
				
				this.display = new ImageDisplay(sImg);

				this.panel.revalidate();

			} catch (DicomException e) {
				e.printStackTrace();
			}

			SingleImagePanel.deconstructAllSingleImagePanelsInContainer(this.panel);
			this.display.setCurrentContour(dImage.getContours().firstElement());
			this.display.setContours(contours);
			this.panel.removeAll();
			
			this.panel.add(display);
			this.panel.revalidate();
		}
		if(obj instanceof Vector<?>)
		{
			if(((Vector<Contour>) obj).firstElement().getClass() == contourObject.getClass())
			{
				contours = (Vector<Contour>) obj;
				if(this.display != null){
					this.display.setPreDefinedShapes(contours);
					this.display.setCurrentContour(contours.firstElement());
					this.display.repaint();
				}
			}
		}
		if(obj.getClass() == contourObject.getClass())
		{
			if(this.display != null){
				this.display.setCurrentContour((Contour) obj);
				this.display.repaint();
			}
			currentContour = (Contour) obj;
		}
	}

	// Setters
	/*
	 * Sets the class' mouseListener attribute.
	 *
	 * @param mL : MouseListener object that is used as the class' mouseListener
	 * attribute.
	 */
	public void setMouseListener(MouseListener mL) {
		this.mouseListener = mL;
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
	
	// Constructors
	public ImageView() {
		//System.out.println("ImageView()");
		
		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(1, 1));
		this.panel.setBackground(Color.BLACK);
		this.panel.setOpaque(true);
		this.panel.setVisible(true);
	}

}
