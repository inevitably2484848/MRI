package edu.auburn.cardiomri.util;

import edu.auburn.cardiomri.datastructure.Landmark.LandmarkType;

public class Mode {
	protected static int mode = 0;   //0 - select ; 1 - contour ; 2 - landmark // kw
	protected static final int SELECT_MODE = 0;
	protected static final int CONTOUR_MODE = 1;
	protected static final int LANDMARK_MODE = 2;
	
	protected static LandmarkType nextLandmarkType = null;
	
	
	 /** -----------------------------------------------------------------------
     *  Static Method so Other Classes can get the Mode Value
     *  @return mode - int
     *  @author KulW
     */
    public static int getMode(){
    	return mode;
    }
    
    public static void setMode(int modeIN){
    	mode = modeIN;
    }
    
    public static LandmarkType getNextLandmarkType() {
    	return nextLandmarkType;
    }
    
    public static void setNextLandmarkType(LandmarkType type) {
    	nextLandmarkType = type;
    }
    
    public static int contourMode(){
    	return 1;
    }
    public static int landmarkMode(){
    	return 2;
    }
    public static int selectMode(){
    	return 0;
    }

	
	
}
