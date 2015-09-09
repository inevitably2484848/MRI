/**
 * 
 */
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
	
	public Landmark(LandmarkType typeIn, double x, double y){
		this();
		landmarkType = typeIn;
		coordinates = new Vector3d(x,y,0);
	}
	public Landmark (LandmarkType typeIn){
		this();
		landmarkType = typeIn;
	}
	private Landmark(){
		
	}
	public void setLandmarkCoordinates(double x, double y){
		coordinates = new Vector3d(x,y,0);
		System.out.println("landmarks coordinates set: " + this.toString());
	}
	public Vector3d getCoordinates(){
		return coordinates;
	}
	public String toString(){
		String out = "[" + coordinates.getX() + "," + coordinates.getY() + "]";
		return out;
	}
	//type Enum
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
