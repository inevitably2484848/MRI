package edu.auburn.cardiomri.util;

public class Mode {
	protected static int mode = 0;   //0 - select ; 1 - contour ; 2 - landmark // kw
	protected static final int SELECT_MODE = 0;
	protected static final int CONTOUR_MODE = 1;
	protected static final int LANDMARK_MODE = 2;
	
	
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
