package edu.auburn.cardiomri.datastructure;

public class TensionPoint extends Point {
	
	private ControlPoint cPoint;
	
	public TensionPoint(double x, double y, ControlPoint cPoint) {
		super(x, y);
		this.cPoint = cPoint;
	}
	
	public TensionPoint(double x, double y) {
		super(x, y);
	}
	
	public ControlPoint getControlPoint() {
		return this.cPoint;
	}
	
	public void setControlPoint(ControlPoint cPoint) {
		this.cPoint = cPoint;
	}
	
}
