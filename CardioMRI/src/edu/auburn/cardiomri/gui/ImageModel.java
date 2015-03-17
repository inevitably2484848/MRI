package edu.auburn.cardiomri.gui;

import java.util.Vector;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;

public class ImageModel extends java.util.Observable {

	private Study study;
	private DICOMImage dImage;
	private int g, s, t, i;
	private Vector<Contour> contours;
	
	// Setters
	/*
	 * Sets the class' study attribute and notifies its Observers.
	 * 
	 *  @param s : The object that the class will use as its study attribute.
	 */
	public void setStudy(Study s) {
		//System.out.println("ImageModel : setStudy(Study s)");
		
		this.study = s;
	}
	
	/*
	 * Sets the class' currently selected Dicom indices and then updates its
	 * Observers.
	 * 
	 *  @param groupIndex : New groupIndex.
	 *  @param sliceIndex : New sliceIndex.
	 *  @param timeIndex : New timeIndex.
	 *  @param groupIndex : New imageIndex.
	 */
	public void setCurrentImage(int groupIndex, int sliceIndex, int timeIndex, int imageIndex) {
//System.out.println("ImageModel : setImage");
		
		this.g = groupIndex;
		this.s = sliceIndex;
		this.t = timeIndex;
		this.i = imageIndex;
		
		this.dImage = this.study.getGroups().get(g).getSlices().get(s).getTimes().get(t).getImages().get(i);
		this.setContourList(this.dImage.getContours());

		setChanged();
		notifyObservers(this.dImage);
	}
	
	public void setContourList(Vector<Contour> contour_list)
	{
		this.contours = contour_list;
		setChanged();
		notifyObservers(this.contours);
	}

	public Vector<Contour> getContourList()
	{
		return this.contours;
	}
	
	public void addContourToImage(Contour contour)
	{
		this.dImage.getContours().add(contour);
		
		setChanged();
		notifyObservers(contour);
	}
	
	// Getters
	/*
	 * Returns the currently selected DICOMImage object given the current
	 * indices.
	 * 
	 *  @return		The currently selected DICOMImage.
	 */
	public DICOMImage getImage() {
		return this.dImage;
	}
	
	// Constructors
	public ImageModel() {
		this.dImage = null;
	}
	
	
}
