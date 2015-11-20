package edu.auburn.cardiomri.datastructure;

import java.awt.Color;

public class TensionPoint extends Point {
	boolean isSelected = false;
	
	static Color selectedTensionPointColor = Color.BLUE;
	static Color selectedContourColor = Color.BLUE;
	
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
	
	public Color getColor() {
		if (this.isSelected) {
			return TensionPoint.selectedTensionPointColor;
		}
		else {
			return TensionPoint.selectedContourColor;
		}
	}
	
	public boolean isSelected() {
		return this.isSelected;
	}
	
	public void isSelected(boolean b) {
		this.isSelected = b;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TensionPoint) {
			TensionPoint tp = (TensionPoint)obj;
			if (tp.getX() == this.getX() &&
				tp.getY() == this.getY()) {
				return true;
			}
		}
		return false;
	}
}
