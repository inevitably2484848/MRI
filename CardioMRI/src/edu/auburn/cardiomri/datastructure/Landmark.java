
package edu.auburn.cardiomri.datastructure;




/**
 * This class is a datastructure for storing a landmark point and implements 
 * shape so SingleImagePane can draw the landmarks
 * @author F. Davis Quarles
 *
 */
public class Landmark {
	private Vector3d coordinates;
	private LandmarkType landmarkType;
	/**
	 * Constructor with type and coordinates
	 * @param typeIn Landmark type Enum
	 * @param x double of X coordinates
	 * @param y double of Y coordinates
	 */
	public Landmark(LandmarkType typeIn, double x, double y){
		this();
		landmarkType = typeIn;
		coordinates = new Vector3d(x,y,0);
	}
	/** Constructor 
	 * 
	 * @param typeIn Landmark type
	 */
	public Landmark (LandmarkType typeIn){
		this();
		landmarkType = typeIn;
	}
	/**
	 * Default constructor
	 */
	private Landmark(){
		
	}
	/**
	 * Set a Landmarks coordinates
	 * @param x double X coordinates
	 * @param y double Y coordinates
	 */
	public void setLandmarkCoordinates(double x, double y){
		coordinates = new Vector3d(x,y,0);
		System.out.println("landmarks coordinates set: " + this.toString());
	}
	/**
	 * get a landmarks coordinates
	 * @return Vector3d Landmarks coordinates
	 */
	public Vector3d getCoordinates(){
		return coordinates;
	}
	/**
	 * formats a landmark as a string
	 * @return String
	 */
	public String toString(){
		String out = "[" + coordinates.getX() + "," + coordinates.getY() + "]";
		return out;
	}
	//type Enum
	/**
	 * Enum of Landmark types
	 * @author davis
	 *
	 */
	public enum LandmarkType{
	ARV,
	IRV,
	MS,
	APEX,
	LVSEPTALBASE,
	LVLATERALBASE,
	LVANTERIORBASE,
	LVINFERIORBASE,
	RVAPEX,
	RVBASE,
	RVSEPTALBASE,
	RVANTERIORBASE,
	RVINFERIORBASE,
	}
	
	

}
