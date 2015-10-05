package edu.auburn.cardiomri.util;
/**
 * Mode is used on the mouseclick listen. 
 * The action performed from a mouse click is based 
 * on the mode that you are in
 * @author Kullen
 * @version 10/5/2015
 */

public class Mode {
	protected static int mode = 0;   //0 - select ; 1 - contour ; 2 - landmark // kw
	protected static final int SELECT_MODE = 0;
	protected static final int CONTOUR_MODE = 1;
	protected static final int LANDMARK_MODE = 2;
	
    public static int getMode(){
    	return mode;
    }
    
   // to set the mode in another class Mode.setMode(Mode.contourMode()) 
    public static void setMode(int modeIN){
    	mode = modeIN;
    }
    
    public static int contourMode(){
    	return CONTOUR_MODE;
    }
    public static int landmarkMode(){
    	return LANDMARK_MODE;
    }
    public static int selectMode(){
    	return SELECT_MODE;
    }

	
	
}
