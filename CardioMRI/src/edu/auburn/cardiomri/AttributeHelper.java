package edu.auburn.cardiomri;

import com.pixelmed.dicom.Attribute;

public class AttributeHelper {

	public static boolean isByte(Attribute attr) {
		
		byte vr[] = attr.getVR();
		
		return vr[0] == 'O' && vr[1] == 'B';
	}
	
	public static boolean isShort(Attribute attr) {
		
		byte vr[] = attr.getVR();
		
		return ( ( (vr[0] == 'S' || vr[0] == 'U' || vr[0] == 'X' ) && vr[1] == 'S' ) 
				|| ( vr[0] == 'O' && vr[1] == 'W' ) );
	}
	
	public static boolean isFloat(Attribute attr) {
		
		byte vr[] = attr.getVR();
		
		return ( ( vr[0] == 'F' && ( vr[1] == 'D' || vr[1] == 'L' ) ) 
				|| ( vr[0] == 'U' && vr[1] == 'F' ) );
	}
	
	public static boolean isInteger(Attribute attr) {
		
		byte vr[] = attr.getVR();
		
		return ( vr[0] == 'I' && vr[1] == 'S' );
	}
	
	public static boolean isSequence(Attribute attr) {
		
		byte vr[] = attr.getVR();
		
		return ( vr[0] == 'S' && vr[1] == 'Q' );
		
	}
	
	public static boolean isString(Attribute attr) {
		
		return !isByte(attr) && !isShort(attr) && !isFloat(attr) && !isInteger(attr);
	}
	
}
