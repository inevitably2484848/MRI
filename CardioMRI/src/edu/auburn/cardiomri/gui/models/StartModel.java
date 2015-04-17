package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.datastructure.Study;

public class StartModel extends Model {
	protected Study study;
	
	public StartModel()
	{
		super();
	}
	
	public void setStudy(Study study)
	{
		this.study = study;
		setChanged();
		notifyObservers(study);
	}
}
