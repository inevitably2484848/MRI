package edu.auburn.cardiomri.datastructure;

import java.awt.Color;

public class ControlPoint extends Point{
	boolean isSelected = false;
	
	static Color pointSelectedColor = Color.RED;
	static Color contourSelectedColor = Color.ORANGE;
	
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
	
	
	public void setTension1(TensionPoint tpoint) {
		tPoint1 = tpoint;
	}
	
	public void setTension2(TensionPoint tpoint) {
		tPoint2 = tpoint;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ControlPoint) {
			ControlPoint cp = (ControlPoint)obj;
			if (cp.getTension1() == this.getTension1() && 
				cp.getTension2() == this.getTension2() &&
				cp.getX() == this.getX() &&
				cp.getY() == this.getY()) {
				return true;
			}
		}
		return false;
	}
	
	public Color getColor() {
		if (this.isSelected) {
			return ControlPoint.pointSelectedColor;
		}
		else {
			return ControlPoint.contourSelectedColor;
		}
	}

	public boolean isSelected() {
		return this.isSelected;
	}
	
	public void isSelected(boolean b) {
		this.isSelected = b;
	}
}
