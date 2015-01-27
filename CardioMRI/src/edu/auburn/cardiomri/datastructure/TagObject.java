package edu.auburn.cardiomri.datastructure;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * This class represents the total tag object created
 * when using tagged MRI. The object consists of all the
 * tag lines from the image.
 * 
 * @author Eric Turner
 *
 */
public class TagObject implements Serializable {

	private ArrayList<TagLine> tagLines;
	// Parameters
	private int imageNumber;
	private int series;
	private double tagAngle;
	// PointsFileHeader
	
	public ArrayList<TagLine> getTagLines() {
		return tagLines;
	}
	public void setTagLines(ArrayList<TagLine> tagLines) {
		this.tagLines = tagLines;
	}
	public int getImageNumber() {
		return imageNumber;
	}
	public void setImageNumber(int imageNumber) {
		this.imageNumber = imageNumber;
	}
	public int getSeries() {
		return series;
	}
	public void setSeries(int series) {
		this.series = series;
	}
	public double getTagAngle() {
		return tagAngle;
	}
	public void setTagAngle(double tagAngle) {
		this.tagAngle = tagAngle;
	}
	
	
}
