package edu.auburn.cardiomri.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.auburn.cardiomri.datastructure.DICOMImage.ImageComparator;

/**
 * This class represents the images taken at a specific
 * time. It contains only a list of images.
 * 
 * @author Eric Turner
 *
 */
public class Time implements Serializable {

	private ArrayList<DICOMImage> images;
	private double TriggerTime;

	public ArrayList<DICOMImage> getImages() {
		return images;
	}

	public void setImages(ArrayList<DICOMImage> images) {
		this.images = images;
	}

	public double getTriggerTime() {
		return TriggerTime;
	}

	public void setTriggerTime(double triggerTime) {
		TriggerTime = triggerTime;
	}
	
	public void addImage(DICOMImage image) {
		images.add(image);
		Collections.sort(images, new ImageComparator());
	}
	
	// Constructor
	public Time() {
		this.images = new ArrayList<DICOMImage>();
	}
	
	/**
	 * Comparator for comparison and sorting of Time objects.
	 * Comparison based on the TriggerTime field
	 * @author Tony Bernhardt
	 *
	 */
	public static class TimeComparator implements Comparator<Time> {

		@Override
		public int compare(Time time1, Time time2) {
			if (time1.getTriggerTime() - time2.getTriggerTime() < 0) return -1;
			if (time1.getTriggerTime() - time2.getTriggerTime() == 0) return 0;
			//if (time1.getTriggerTime() - time2.getTriggerTime() > 0) 
			return 1;
		}
		
	}
}
