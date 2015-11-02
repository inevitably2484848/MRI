package edu.auburn.cardiomri.gui.actionperformed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.auburn.cardiomri.gui.views.WorkspaceView;

public class MenuBarFileActionPerformed implements ActionListener {

	private WorkspaceView wrkspcVw;
	public MenuBarFileActionPerformed(WorkspaceView wrkspcVw){
		this.wrkspcVw = wrkspcVw;
	}
	
	@Override
	public void actionPerformed(ActionEvent actionCommand) {
		
		String action = actionCommand.getActionCommand();

		
		if (action.equals("Save Study")) { 
            this.wrkspcVw.saveStudy();
        } 
		else if (action.equals("Save As Study")) {  
        	this.wrkspcVw.saveAsStudy();
        } 
		else if (action.equals("Open Existing")) {  
        	this.wrkspcVw.loadExistingStudy();
        }
		else if (action.equals("From Single DICOM")){
			
		}
		else if (action.equals("From File Structure")){
			
		}
		else if (action.equals("Rotate Image")) {   
			
            wrkspcVw.getWorkspaceModel().rotate();
        } 
	}

}
