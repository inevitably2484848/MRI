package edu.auburn.cardiomri.datastructure;

public class Point {
	
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	
	public double distance(Point p) {
		double deltaX = this.getX() - p.getX();
        double deltaY = this.getY() - p.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}
	
	public Point midpoint(Point p) {
		double midX = (this.getX() + p.getX()) / 2.0;
        double midY = (this.getY() + p.getY()) / 2.0;

        Point midpoint = new Point(midX, midY);
        return midpoint;
	}
	
}
