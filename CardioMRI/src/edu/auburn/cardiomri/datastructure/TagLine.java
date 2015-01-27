package edu.auburn.cardiomri.datastructure;

import java.io.Serializable;

/**
 * This class represents a single tag line which is created
 * in a tagged MRI.
 * 
 * @author Eric Turner
 *
 */
public class TagLine implements Serializable {

	private int index;
	private int points;
	private double[] pointsPixel;
	private double[] pointsDist;
	private double[] pointsScanner;
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public double[] getPointsPixel() {
		return pointsPixel;
	}
	public void setPointsPixel(double[] pointsPixel) {
		this.pointsPixel = pointsPixel;
	}
	public double[] getPointsDist() {
		return pointsDist;
	}
	public void setPointsDist(double[] pointsDist) {
		this.pointsDist = pointsDist;
	}
	public double[] getPointsScanner() {
		return pointsScanner;
	}
	public void setPointsScanner(double[] pointsScanner) {
		this.pointsScanner = pointsScanner;
	}
}
