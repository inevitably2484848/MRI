package edu.auburn.cardiomri.datastructure;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeleteControlPointTest {

    private Contour contour;
    private List<Vector3d> controlPoints;
    
    @Before
    public void setUp() {
        contour = new Contour(Contour.Type.LV_EPI);
        contour.addControlPoint(1, 2);
        contour.addControlPoint(5, 8);
        
        controlPoints = contour.getControlPoints();
       
    }

    @After
    public void tearDown() {
        contour = null;
    }
	
    @Test
    public void testDeleteControlPoint() {
    	assertTrue(contour.deleteControlPoint(1, 2));
    }
    
    
    @Test
    public void testAddCPThatIsToCloseToAnotherCPWillNotAdd() {
    	int beforeSize = controlPoints.size();
    	
    	contour.addControlPoint(2,2);
    	
    	int afterSize = controlPoints.size();
    	
    	assertEquals(beforeSize, afterSize);
    }
    
    @Test
    public void testDeleteControlPointThatsNotThere(){
    	
    	boolean expected = false;
    	boolean actual = contour.deleteControlPoint(9, 8);
    	
    	assertEquals(expected, actual);
    }
    
    

}
