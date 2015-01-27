package edu.auburn.cardiomri.gui;

import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;

public class StudyStructureModel extends java.util.Observable {
	
	private Study study;	
	private int g, s, t, i;
	private DICOMImage curImage;
	
	// Setters
	/*
	 * Sets the class' study attribute and notifies its Observers.
	 * 
	 *  @param s : The object that the class will use as its study attribute.
	 */
	public void setStudy(Study s) {
//System.out.println("StudyStructureModel : setStudy");
		this.study = s;
		this.curImage = this.study.getGroups().get(0).getSlices().get(0).getTimes().get(0).getImages().get(0);
		
		setChanged();
		notifyObservers(this.study);
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
		this.g = groupIndex;
		this.s = sliceIndex;
		this.t = timeIndex;
		this.i = imageIndex;
		
		this.curImage = this.study.getGroups().get(g).getSlices().get(s).getTimes().get(t).getImages().get(i);
		
		int indices[] = {this.g, this.s, this.t, this.i};
		
		setChanged();
		notifyObservers(indices);
	}
	
	
	// Getters
	/*
	 * Returns the class' study attribute.
	 * 
	 *  @return		The class' study attribute.
	 */
	public Study getStudy() { return this.study; }
	
	/*
	 * Returns the currently selected DICOMImage object given the current
	 * indices.
	 * 
	 *  @return		The currently selected DICOMImage.
	 */
	public DICOMImage getImage() { return this.curImage; }
	
	// Constructors
	public StudyStructureModel() {
//System.out.println("StudyStructureModel()");
		this.study = null;
	}

}
