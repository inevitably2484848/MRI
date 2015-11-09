
package edu.auburn.cardiomri.datastructure;




/**
 * This class is a datastructure for storing a landmark point and implements 
 * shape so SingleImagePane can draw the landmarks
 * @author F. Davis Quarles
 *
 */



public class Landmark extends Point {
	//type Enum
	/**
	 * Enum of Landmark types, add landmarks here and they will show up in the menu
	 * @author davis
	 *
	 */
	public enum LandmarkType{ 
		ARV("ARV","Anterior RV Insertion"), 
		IRV("IRV","Inferior RV"),
		MS("MS","Mid-Septum"),
		LVAPEX("LVAPEX","LV Apex"),
		LVSEPTALBASE("LVSEPTABLBASE","LV Septal Base"),
		LVLATERALBASE("LVLATERALBASE","LV Lateral Base"),
		LVANTERIORBASE("LVANTERIORBASE", "LV Anterior Base"),
		LVINFERIORBASE("LVINFERIORBASE","LV Inferior Base"),
		RVAPEX("RVAPEX","RV Apex"),
		RVLATERALBASE("RVLATERALBASE","RV Lateral Base"),
		RVSEPTALBASE("RVSEPTABLBASE","RV Septal Base"),
		RVANTERIORBASE("RVANTERIORBASE", "RV Anterior Base"),
		RVINFERIORBASE("RVINFERIORBASE","RV Inferior Base");
		
		private String lngName;
		private String abbv;
		private LandmarkType(String abbv, String lngName){
			this.abbv = abbv;
			this.lngName = lngName;
		}
		
		@Override 
		public String toString(){
			return lngName;
		}
		
		public String abbv(){
			return abbv;
		}
	}
	private LandmarkType landmarkType;
	/**
	 * Constructor with type and coordinates
	 * @param typeIn Landmark type Enum
	 * @param x double of X coordinates
	 * @param y double of Y coordinates
	 */
	public Landmark(LandmarkType typeIn, double x, double y){
		super(x, y);
		landmarkType = typeIn;
		
	}
	
	/**
	 * Set a Landmarks coordinates
	 * @param x double X coordinates
	 * @param y double Y coordinates
	 */
	public void setLandmarkCoordinates(double x, double y){
		super.setX(x);
		super.setY(y);
		//System.out.println("landmarks coordinates set: " + this.toString());
	}
	/**
	 * get a landmarks coordinates
	 * @return Vector3d Landmarks coordinates
	 */
	public double[] getCoordinates(){
		double output[] = {super.getX() , super.getY()};
		return output;
	}
	
	/**
	 * formats a landmark as a string
	 * @return String
	 */
	public String toString(){
		String out = "[" + super.getX() + "," + super.getY() + "]";
		return out;
	}
	public void moveLandmark(double x, double y){
		super.setX(x);
		super.setY(y);
	}

}

	
	
	
	
	

