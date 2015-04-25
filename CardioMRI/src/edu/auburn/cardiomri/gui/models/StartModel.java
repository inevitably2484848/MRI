package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.datastructure.Study;

public class StartModel extends Model {
	protected Study study;
	
	/**
	 * Constructor for the start model
	 */
	public StartModel()
	{
		super();
	}
	
	/**
	 * Setter for study and notifies observers
	 * 
	 * @param study a Study of images
	 */
	public void setStudy(Study study)
	{
		this.study = study;
		setChanged();
		notifyObservers(study);
	}
}
