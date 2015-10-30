package edu.auburn.cardiomri.datastructure;

public class ControlPoint extends Point{
	//add fields for the tension points
	TensionPoint tPoint1;
	TensionPoint tPoint2;
	public ControlPoint(double x, double y) {
		super(x, y);
		tPoint1 = new TensionPoint(0.0, 0.0);
		tPoint2 = new TensionPoint(0.0, 0.0);
	}
	public TensionPoint getTension1() {
		return tPoint1;
	}
	public TensionPoint getTension2() {
		return tPoint2;
	}
}
