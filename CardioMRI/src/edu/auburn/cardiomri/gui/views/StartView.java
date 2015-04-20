package edu.auburn.cardiomri.gui.views;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.auburn.cardiomri.dataimporter.DICOM3Importer;
import edu.auburn.cardiomri.dataimporter.DICOMFileTreeWalker;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.datastructure.Study.NotInStudyException;
import edu.auburn.cardiomri.gui.models.StartModel;
import edu.auburn.cardiomri.util.StudyUtilities;

public class StartView extends View {

	protected JFileChooser fileChooser;
	
	public StartView()
	{
		super();
	        
        this.panel.setLayout(new GridBagLayout());

        //Add the three buttons
        JButton newStudy = new JButton("New Study");
        newStudy.setActionCommand("Create New Study");
        newStudy.addActionListener(this);
        this.panel.add(newStudy);
        
        JButton existingStudy = new JButton("Existing Study");
        existingStudy.setActionCommand("Load Existing Study");
        existingStudy.addActionListener(this);
        this.panel.add(existingStudy);
        
        
        JButton singleImage = new JButton("Single Image");
        singleImage.setActionCommand("Load Single DICOM");
        singleImage.addActionListener(this);
        this.panel.add(singleImage);
        
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {

	    String actionCommand = e.getActionCommand();
	    
	    if (actionCommand.equals("Create New Study")) {
	        this.createNewStudy();
	    } else if (actionCommand.equals("Load Existing Study")) {
	    	this.loadStudy();
	    } else if (actionCommand.equals("Load Single DICOM")) {
	        try {
	            this.loadSingleDicom();
	        } catch (NotInStudyException e1) {
	            e1.printStackTrace();
	        }
	    }
	}
    
    /**
     * Opens a JFileChooser that allows the user to select a Study model file
     * (.smc).
     */
    public void loadStudy() {
        FileFilter studyFilter = new FileNameExtensionFilter(
                "Study file (.smc)", "smc");
        fileChooser.setFileFilter(studyFilter);

        int response = fileChooser.showOpenDialog(this.panel);
        if (response == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getAbsolutePath();

            Study study = StudyUtilities.loadStudy(fileName);
            this.getStartModel().setStudy(study);
        }
    }

    /**
     * Opens a JFileChooser that allows the user to select a Directory, which
     * will then be iterated through to generate a new Study object.
     *
     */
    public void createNewStudy() {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showOpenDialog(this.panel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            String directory = fileChooser.getSelectedFile().getAbsolutePath();
            Path path = Paths.get(directory);

            DICOMFileTreeWalker fileTreeWalker = new DICOMFileTreeWalker();

            Study study = fileTreeWalker.addFileTreeToStudy(path, new Study());

            this.getStartModel().setStudy(study);
        } else {
            // System.out.println("FileChooser : Canceled choosing directory");
        }
    }

    /**
     * Opens a JFileChooser that allows the user to select a single Dicom file
     * and generate a Study object with the Dicom as the only image in it.
     * 
     * @throws NotInStudyException 
     */
    public void loadSingleDicom() throws NotInStudyException {

        FileFilter dicomType = new FileNameExtensionFilter("DICOM file (.dcm)","dcm");
        fileChooser.addChoosableFileFilter(dicomType);

        int returnVal = fileChooser.showOpenDialog(this.panel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getPath();

            DICOMImage dImage = DICOM3Importer.makeDICOMImageFromFile(filename);

            Study study = new Study();
            study.addImage(dImage);

            this.getStartModel().setStudy(study);
        } else {
            // System.out.println("GUIController : Cancel choosing file");
        }
    }
    
    public StartModel getStartModel()
    {
    	return (StartModel) this.model;
    }
}
