
package edu.auburn.cardiomri.datastructure;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.util.ContourCalc;

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
	public enum Type{ 
		ARV("ARV","Anterior RV Insertion"), 
		IRV("IRV","Inferior RV"),
		MS("MS","Mid-Septum"),
		LVAPEX("LVAPEX","LV Apex"),
		LVSEPTALBASE("LVSEPTALBASE","LV Septal Base"),
		LVLATERALBASE("LVLATERALBASE","LV Lateral Base"),
		LVANTERIORBASE("LVANTERIORBASE", "LV Anterior Base"),
		LVINFERIORBASE("LVINFERIORBASE","LV Inferior Base"),
		RVAPEX("RVAPEX","RV Apex"),
		RVLATERALBASE("RVLATERALBASE","RV Lateral Base"),
		RVSEPTALBASE("RVSEPTALBASE","RV Septal Base"),
		RVANTERIORBASE("RVANTERIORBASE", "RV Anterior Base"),
		RVINFERIORBASE("RVINFERIORBASE","RV Inferior Base");
		
		private String lngName;
		private String abbv;
		private Type(String abbv, String lngName){
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
	
	

	boolean isSelected = false;
	boolean isVisible = true;
	
	static Color selectedColor = Color.RED;
	static Color unselectedColor = Color.WHITE;
	
	private Vector3d coordinates;

	private Type landmarkType;
	/**
	 * Constructor with type and coordinates
	 * @param typeIn Landmark type Enum
	 * @param x double of X coordinates
	 * @param y double of Y coordinates
	 */
	public Landmark(Type typeIn, double x, double y){
			super(x, y);
			landmarkType = typeIn;
	}
	public Type getType(){
		return this.landmarkType;
	}
	
	public void setType(Type typeIn) {
        this.landmarkType = typeIn;
    }


	
	public boolean isSelected() {
		return this.isSelected;
	}
	
	public void isSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public boolean isVisible() {
		return this.isVisible;
	}
	
	public void isVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public Color getColor() {
		if (this.isSelected) {
			return Landmark.selectedColor;
		}
		else {
			return Landmark.unselectedColor;
		}
	}
	

	/**
	 * Set a Landmarks coordinates
	 * @param x double X coordinates
	 * @param y double Y coordinates
	 */
	
	@Override
	public boolean equals(Object b){
		if(!(b instanceof Landmark)) {
			return false;
		}
		else if (this.getType() == ((Landmark) b).getType()){
			return true;
		}
		else return false;
	}
	
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
		String out = this.landmarkType.abbv + ": [" + super.getX() + "," + super.getY() + "]";
		return out;
	}
	public void moveLandmark(double x, double y){
		super.setX(x);
		super.setY(y);
	}
	
	
	
	public Integer getIntFromType() {
        return Landmark.TYPE_TO_INTEGER.get(getType());
    }
	
	public static Type getTypeFromInt(int type) {
        return Landmark.INTEGER_TO_TYPE.get(type);
    }
	
	public static Boolean isLandmarkFromInt(int type) { 
    	return Landmark.IS_LANDMARK.get(type);
    }

	
	public static final Map<Type, Integer> TYPE_TO_INTEGER;
    public static final Map<Integer, Type> INTEGER_TO_TYPE;
    public static final Map<Integer, Boolean> IS_LANDMARK;
    
	static {
		Map<Integer, Type> tempIntegerToType = new HashMap<Integer, Type>();

        tempIntegerToType.put(-2, Type.ARV);
        tempIntegerToType.put(-3, Type.IRV);
        tempIntegerToType.put(-4, Type.MS);
        tempIntegerToType.put(-5, Type.LVAPEX);
        tempIntegerToType.put(-6, Type.LVSEPTALBASE);
        tempIntegerToType.put(-7, Type.LVLATERALBASE);
        tempIntegerToType.put(-8, Type.LVANTERIORBASE);
        tempIntegerToType.put(-9, Type.LVINFERIORBASE);
        tempIntegerToType.put(-10, Type.RVAPEX);
        tempIntegerToType.put(-11, Type.RVLATERALBASE);
        tempIntegerToType.put(-12, Type.RVSEPTALBASE);
        tempIntegerToType.put(-13, Type.RVANTERIORBASE);
        tempIntegerToType.put(-14, Type.RVINFERIORBASE);
        
        INTEGER_TO_TYPE = Collections.unmodifiableMap(tempIntegerToType);
        
        
        Map<Type, Integer> tempTypeToInteger = new HashMap<Type, Integer>();
        tempTypeToInteger.put(Type.ARV, -2);
        tempTypeToInteger.put(Type.IRV, -3);
        tempTypeToInteger.put(Type.MS, -4);
        tempTypeToInteger.put(Type.LVAPEX, -5);
        tempTypeToInteger.put(Type.LVSEPTALBASE, -6);
        tempTypeToInteger.put(Type.LVLATERALBASE, -7);
        tempTypeToInteger.put(Type.LVANTERIORBASE, -8);
        tempTypeToInteger.put(Type.LVINFERIORBASE, -9);
        tempTypeToInteger.put(Type.RVAPEX, -10);
        tempTypeToInteger.put(Type.RVLATERALBASE, -11);
        tempTypeToInteger.put(Type.RVSEPTALBASE, -12);
        tempTypeToInteger.put(Type.RVANTERIORBASE, -13);
        tempTypeToInteger.put(Type.RVINFERIORBASE, -14);
        TYPE_TO_INTEGER = Collections.unmodifiableMap(tempTypeToInteger);
        
        Map<Integer, Boolean> tempIsLandmark = new HashMap<Integer, Boolean>();
        // Control Points
        tempIsLandmark.put(-2, Boolean.TRUE);
        tempIsLandmark.put(-3, Boolean.TRUE);
        tempIsLandmark.put(-4, Boolean.TRUE);
        tempIsLandmark.put(-5, Boolean.TRUE);
        tempIsLandmark.put(-6, Boolean.TRUE);
        tempIsLandmark.put(-7, Boolean.TRUE);
        tempIsLandmark.put(-8, Boolean.TRUE);
        tempIsLandmark.put(-9, Boolean.TRUE);
        tempIsLandmark.put(-10, Boolean.TRUE);
        tempIsLandmark.put(-11, Boolean.TRUE);
        tempIsLandmark.put(-12, Boolean.TRUE);
        tempIsLandmark.put(-13, Boolean.TRUE);
        tempIsLandmark.put(-14, Boolean.TRUE);

        IS_LANDMARK = Collections.unmodifiableMap(tempIsLandmark);
	}
}

	
	
	
	
	

