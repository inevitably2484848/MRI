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
	private static final int SELECT_MODE 	= 0;
	private static final int CONTOUR_MODE 	= 1;
	private static final int LANDMARK_MODE 	= 2;
	
    public static int getMode(){
    	return mode;
    }
    
    public static String modeToast(){
    	switch (getMode()){
	    	case SELECT_MODE: {
	    		return "SELECT MODE";
	    	}
	    	case CONTOUR_MODE : {
	    		return "CONTOUR MODE";
	    	}
	    	case LANDMARK_MODE : {
	    		return "LANDMARK_MODE";
	    	}
	    	default : {
	    		return null;
	    	}
    	}
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
