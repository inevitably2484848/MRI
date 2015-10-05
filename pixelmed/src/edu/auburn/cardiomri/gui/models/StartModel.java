package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.datastructure.Study;

/**
 * 
 * Model for StartView. Starts the first frame which a study 
 * will be selected from 
 *
 */
public class StartModel extends Model {
	protected Study study;
	static boolean isLoadStudy;
	
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
	
	/**
	 * For loading a study, set a boolean to catch and change state in workspace model
	 * 
	 * @return
	 */
	
	public static void setLoadStudy() {
		isLoadStudy = true;
	}
	
	/**
	 * Returns if is loading a study
	 * 
	 * @return isLoadStudy boolean
	 */
	
	public static boolean getLoadStudy() {
		return isLoadStudy;
	}
	
	/**
	 * After study is loaded, sets loadStudy catch to false
	 * 
	 */
	
	public static void setLoadFalse() {
		isLoadStudy = false;
	}
}
