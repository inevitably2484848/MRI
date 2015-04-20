package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.RunMVC;
import edu.auburn.cardiomri.datastructure.Study;

public class StartModel extends Model {
	protected RunMVC mvc;
	protected Study study;
	
	public StartModel()
	{
		super();
	}
	
	public void setMVC(RunMVC mvc)
	{
		this.mvc = mvc;
	}
	
	public void setStudy(Study study)
	{
		mvc.setStudy(study);
	}

	
	
}
