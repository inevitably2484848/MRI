/* Copyright (c) 2001-2012, David A. Clunie DBA Pixelmed Publishing. All rights reserved. */

package com.pixelmed.test;

import junit.framework.*;

public class TestSUVTransform_AllTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("All JUnit Tests");
		suite.addTest(TestSUVTransformValues.suite());
		return suite;
	}
	
}
