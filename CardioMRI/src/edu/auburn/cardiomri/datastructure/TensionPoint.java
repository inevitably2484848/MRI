package edu.auburn.cardiomri.datastructure;

import java.awt.Color;

public class TensionPoint extends Point {
	boolean isSelected = false;
	
	static Color selectedTensionPointColor = Color.RED;
	static Color selectedContourColor = Color.BLUE;
	
	public TensionPoint(double x, double y) {
		super(x, y);
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
