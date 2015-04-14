package edu.auburn.cardiomri.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javafx.geometry.Point2D;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreePath;

import edu.auburn.cardiomri.dataimporter.DICOM3Importer;
import edu.auburn.cardiomri.dataimporter.DICOMFileTreeWalker;
import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Slice;
import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.datastructure.Study.NotInStudyException;
import edu.auburn.cardiomri.datastructure.Time;
import edu.auburn.cardiomri.gui.models.GridModel;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.MetadataModel;
import edu.auburn.cardiomri.gui.models.StudyStructureModel;
import edu.auburn.cardiomri.gui.nodes.ImageTreeNode;
import edu.auburn.cardiomri.gui.views.GridView;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.MetadataView;
import edu.auburn.cardiomri.gui.views.StudyStructureView;
import edu.auburn.cardiomri.lib.SerializationManager;

public class GUIController implements java.awt.event.ActionListener,
MouseListener {

	private JComponent mainComponent;
	private JFrame appFrame;

	private int gIndex = 0, sIndex = 0, tIndex = 0, iIndex = 0;

	private StudyStructureModel studyStructModel;
	private GridModel gridModel;
	private MetadataModel metadataModel;
	private ImageModel mainImageModel;
	private ImageModel imageModelSmallLeft;
	private ImageModel imageModelSmallRight;

	private StudyStructureView studyStructView;
	private GridView gridView;
	private MetadataView metadataView;
	private ImageView mainImageView;
	private ImageView imageViewSmallLeft;
	private ImageView imageViewSmallRight;
	

	private String filename;

	private JFileChooser fileChooser = new JFileChooser();

	public void getImageDisplayClick(MouseEvent e) {
		Point2D p = new Point2D(e.getX(), e.getY());
		this.mainImageView.selectContour(p);
	}

	// ActionListener methods
	public void actionPerformed(java.awt.event.ActionEvent e) {

		String actionCommand = e.getActionCommand();

		// System.out.println("GUIController : actionPerformed - " +
		// actionCommand);

		if (actionCommand.equals("Create New Study")) {
			this.createNewStudy(e);
		} else if (actionCommand.equals("Load Existing Study")) {
			this.loadExistingStudy(e);
		} else if (actionCommand.equals("Load Single DICOM")) {
			try {
				this.loadSingleDicom(e);
			} catch (NotInStudyException e1) {
				e1.printStackTrace();
			}
		} else if (actionCommand.equals("Save Study")) {
			this.saveStudy();
		} else if (actionCommand.equals("Save As Study")) {
			this.saveAsStudy();
		} else if (actionCommand.equals("Import DICOM")) {
			// Check to see if there is a Study to add things to
			if (this.studyStructModel.getStudy() != null) {
				this.importDicom(e);
			} else {
				// System.out.println("GUIController : failed attempt to import DICOM");
			}
		} else if (actionCommand.equals("Save Contours")) {
			this.saveContour();
		} else if (actionCommand.equals("Load Contours")) {
			try {
				this.setUpLoad();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (actionCommand.equals("Default Type")) {
			this.mainImageModel.addContourToImage(new Contour(Type.DEFAULT));
		} else if (actionCommand.equals("Closed Type")) {
			this.mainImageModel.addContourToImage(new Contour(
					Type.DEFAULT_CLOSED));
		} else if (actionCommand.equals("Open Type")) {
			this.mainImageModel
			.addContourToImage(new Contour(Type.DEFAULT_OPEN));
		} else if (actionCommand.equals("Delete All Contours")) {
			this.deleteAllContoursForImage();
		}
		else if (actionCommand.equals("Delete Selected Contour")) {
			this.deleteSelectedContour();
		} 
		else if (actionCommand.equals("Hide Selected Contour")) {
			this.hideSelectedContour();
		}
		else if (actionCommand.equals("Hide Contours")) {
			this.hideContours();
		}
		else if (actionCommand.equals("Delete All Contours")) {
			this.deleteAllContoursForImage();
		}
		else if (actionCommand.equals("Show Contours")) {
			this.showContours();
		}
		else if (actionCommand.substring(0, 6).equals("Button")) {
			// System.out.println("GUIController : resetting focus");
			StringTokenizer tokenizer = new StringTokenizer(
					actionCommand.substring(7), ",");
			String timeStr = tokenizer.nextToken();
			String sliceStr = tokenizer.nextToken();

			int newTime = (Integer.parseInt(timeStr) - 1);
			int newSlice = (Integer.parseInt(sliceStr) - 1);

			// System.out.println(newTime + " " + newSlice);

			// Check if it is valid
			if (newSlice != -1 && newTime != -1) {
				if (newSlice < this.studyStructModel.getStudy().getGroups()
						.get(this.gIndex).getSlices().size()) {
					Slice slice = this.studyStructModel.getStudy().getGroups()
							.get(this.gIndex).getSlices().get(newSlice);
					if (newTime < slice.getTimes().size()) {
						this.tIndex = newTime;
						this.sIndex = newSlice;
						this.updateNewDicom();
					}
				}
			}

			this.mainComponent.requestFocusInWindow();
		}
	}

	private void hideSelectedContour() {
		DICOMImage dImage = this.studyStructModel.getImage();
		Contour selected = this.mainImageModel.getSelectedContour();
		dImage.getContours().remove(selected);
		this.mainImageModel.getHiddenContours().add(selected);
		
	}

	private void hideContours() {
		DICOMImage dImage = this.studyStructModel.getImage();
		this.mainImageModel.getHiddenContours().addAll(dImage.getContours());
		dImage.getContours().clear();	
	}

	private void showContours() {
		if (this.mainImageModel.getHiddenContours().size() != 0) {
			DICOMImage dImage = this.studyStructModel.getImage();
			dImage.getContours().addAll((Vector<Contour>) this.mainImageModel.getHiddenContours());
			this.mainImageModel.getHiddenContours().clear();
		}
	}
	private void deleteSelectedContour() {
		DICOMImage dImage = this.studyStructModel.getImage();
		dImage.getContours().remove(this.mainImageModel.getSelectedContour());		
	}

	// MouseListener methods
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && !e.isConsumed()) {
			TreePath treePath = this.studyStructView.getTree()
					.getPathForLocation(e.getX(), e.getY());
			if (treePath != null) {
				// System.out.println("GUIController : Detected StudyStructure selection");

				Object[] pathObjs = treePath.getPath();
				if (pathObjs[pathObjs.length - 1].getClass() == ImageTreeNode.class) {
					int[] indices = ((ImageTreeNode) pathObjs[pathObjs.length - 1])
							.getIndices();
					this.gIndex = indices[0];
					this.sIndex = indices[1];
					this.tIndex = indices[2];
					this.iIndex = indices[3];

					this.updateNewDicom();
					this.mainComponent.requestFocusInWindow();
				}
			} else {
				// System.out.println("not a path");
				this.mainComponent.requestFocusInWindow();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Unused
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// System.out.println("MouseReleased");
		this.mainComponent.requestFocusInWindow();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Unused
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Unused
	}

	/**
	 * Opens a JFileChooser that allows the user to select a Directory, which
	 * will then be iterated through to generate a new Study object.
	 *
	 * @param e : ActionEvent object that is was used to originally used to call
	 *            the GUIController's actionPerformed method. (currently unused)
	 */
	private void createNewStudy(ActionEvent e) {
		// System.out.println("GUIController : Create New Study");

		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = fileChooser.showOpenDialog(this.mainComponent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// System.out.println("FileChooser : Load File Structure");

			String directory = fileChooser.getSelectedFile().getAbsolutePath();
			Path path = Paths.get(directory);

			DICOMFileTreeWalker fileTreeWalker = new DICOMFileTreeWalker();

			Study s = fileTreeWalker.addFileTreeToStudy(path, new Study());

			// update study structure Model
			this.updateNewStudy(s);
			this.updateNewDicom();
		} else {
			// System.out.println("FileChooser : Canceled choosing directory");
		}
	}

	/**
	 * Opens a JFileChooser that allows the user to select a Study model file
	 * (.smc).
	 *
	 * @param e : ActionEvent object that is was used to originally used to call
	 *            the GUIController's actionPerformed method. (currently unused)
	 */
	private void loadExistingStudy(ActionEvent e) {
		// System.out.println("GUIController : Load Existing Study");

		FileFilter studyFilter = new FileNameExtensionFilter(
				"Study file (.smc)", "smc");
		fileChooser.setFileFilter(studyFilter);

		int response = fileChooser.showOpenDialog(this.mainComponent);
		if (response == JFileChooser.APPROVE_OPTION) {
			this.filename = fileChooser.getSelectedFile().getAbsolutePath();

			Study newStudy = null;
			try {
				newStudy = SerializationManager.load(filename);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			if (newStudy != null) {
				this.updateNewStudy(newStudy);
				this.updateNewDicom();
			}
		}
	}

	/**
	 * Opens a JFileChooser that allows the user to select a single Dicom file
	 * and generate a Study object with the Dicom as the only image in it.
	 *
	 * @param e : ActionEvent object that is was used to originally used to call
	 *            the GUIController's actionPerformed method. (currently unused)
	 */
	private void loadSingleDicom(ActionEvent e) throws NotInStudyException {
		// System.out.println("GUIController : Load Single DICOM");

		FileFilter dicomType = new FileNameExtensionFilter("DICOM file (.dcm)",
				"dcm");
		fileChooser.addChoosableFileFilter(dicomType);

		int returnVal = fileChooser.showOpenDialog(this.mainComponent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			// System.out.println("File selected");

			// filechooser.getSelectedFile() returns a file object
			String filename = fileChooser.getSelectedFile().getPath();

			// System.out.println("StudyStructureController : Chose file - " +
			// filename);

			DICOMImage dImage = DICOM3Importer.makeDICOMImageFromFile(filename);

			Study s = new Study();
			s.addImage(dImage);

			// update study structure Model
			this.updateNewStudy(s);
			this.updateNewDicom();
		} else {
			// System.out.println("GUIController : Cancel choosing file");
		}
	}

	/**
	 * Opens a JFileChooser that allows the user to select a single Dicom file
	 * and adds it to the existing Study object.
	 *
	 * @param e : ActionEvent object that is was used to originally used to call
	 *            the GUIController's actionPerformed method. (currently unused)
	 */
	private void importDicom(ActionEvent e) {
		// System.out.println("GUIController : Import DICOM");

		FileFilter dicomFilter = new FileNameExtensionFilter(
				"DICOM file (.dcm)", "dcm");
		fileChooser.setFileFilter(dicomFilter);

		int returnVal = fileChooser.showOpenDialog(this.mainComponent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			// System.out.println("File selected");

			// filechooser.getSelectedFile() returns a file object
			String filename = fileChooser.getSelectedFile().getPath();

			// System.out.println("StudyStructureController : Chose file - " +
			// filename);

			DICOMImage dImage = DICOM3Importer.makeDICOMImageFromFile(filename);

			Study existingStudy = this.getStudyStructModel().getStudy();
			try {
				existingStudy.addImage(dImage);
			} catch (NotInStudyException e1) {
				e1.printStackTrace();
			}

			// update study structure Model
			this.updateNewStudy(existingStudy);
			this.updateNewDicom();
		} else {
			// System.out.println("GUIController : Cancel choosing file");
		}
	}

	/**
	 * deletes all of the Contours for the displayed image
	 */
	private void deleteAllContoursForImage() {
		DICOMImage dImage = this.studyStructModel.getImage();
		dImage.getContours().clear();
	}

	/**
	 * Saves current image's contour lines into a .txt file containing a header
	 * and the X and Y coordinates of all the points along the contour
	 * 
	 * @param contour : Contour object to be saved
	 */

	public void saveContour() {
		String path = System.getProperty("user.dir") + File.separator
				+ "contourPoints.txt";
		writeContoursToFile(this.studyStructModel.getStudy().getSOPInstanceUIDToDICOMImage(), path);

	}

	public static void writeContoursToFile(Map<String, DICOMImage> SOPInstanceUIDToDICOMImage, String path) {
		// TODO Categorize points based on location (i.e. LA, RA, Endo, Epi,
		// etc...
		// TODO merge with save() in SerializableManager
		Vector<Contour> contours;
		Writer writer = null;

		File f = new File(path);

		try {
			writer = new PrintWriter(new BufferedWriter(
					new FileWriter(f, false)));
			for (DICOMImage image : SOPInstanceUIDToDICOMImage.values()) {
				contours = image.getContours();
				if (contours.size() <= 1) {
					continue;
				} else {
					writer.write("\n" + image.getSopInstanceUID() + "\n");
					for (Contour c : contours) {
						if (c.getControlPoints().size() > 0) {
							int numPoints = c.getControlPoints().size()
									+ c.getGeneratedPoints().size();
							String header = c.getIntFromType() + "\n"
									+ numPoints + "\n";
							writer.write(header);
							for (javafx.geometry.Point2D point : c
									.getControlPoints()) {
								writer.write(BigDecimal.valueOf(point.getX())
										.setScale(4, BigDecimal.ROUND_UP)
										+ "\t"
										+ BigDecimal.valueOf(point.getY())
										.setScale(4,
												BigDecimal.ROUND_UP)
												+ "\n");
							}
							for (javafx.geometry.Point2D point : c
									.getGeneratedPoints()) {
								writer.write(BigDecimal.valueOf(point.getX())
										.setScale(4, BigDecimal.ROUND_UP)
										+ "\t"
										+ BigDecimal.valueOf(point.getY())
										.setScale(4,
												BigDecimal.ROUND_UP)
												+ "\n");
							}
						}
					}
					writer.write((-1) + "\n");
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads in a text file containing lists of coordinates for Contour objects
	 * for one or more images and assigns the Contours to their corresponding
	 * images.
	 * 
	 * @throws IOException
	 */

	public void setUpLoad() throws IOException {
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fileChooser.showOpenDialog(this.mainComponent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = new File(fileChooser.getSelectedFile().getPath());
			loadContour(file, this.studyStructModel.getStudy().getSOPInstanceUIDToDICOMImage());
		}
	}
	public static void loadContour(File file, Map<String, DICOMImage> SOPInstanceUIDToDICOMImage ) {
		// TODO #7, 8. log error if type not found...
		// TODO figure out how to separate control/generated points
		Vector<Contour> contours;
		List<Point2D> controlPoints;
		List<Point2D> generatedPoints;

		String sopInstanceUID;
		String[] line = new String[2];
		@SuppressWarnings("unused")
		String lineCheck;

		int contourType;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.readLine() != null) {
				contours = new Vector<Contour>();
				sopInstanceUID = reader.readLine();
				// System.out.println(sopInstanceUID);
				contourType = Integer.parseInt(reader.readLine());
				while ((lineCheck = reader.readLine()) != "-1") {
					// System.out.println("type: " + contourType + "\nnum "
					// + numPoints);
					controlPoints = new Vector<Point2D>();
					generatedPoints = new Vector<Point2D>();
					while ((line = reader.readLine().split("\t")).length >= 2) {
						float x = Float.parseFloat(line[0]);
						float y = Float.parseFloat(line[1]);
						if (x % Math.floor(x) == 0) { // adds first control
							// point twice.
							// remove?
							controlPoints.add(new Point2D(x, y));
						} else {
							generatedPoints.add(new Point2D(x, y));
						}
					}
					Contour contour = new Contour(
							Contour.getTypeFromInt(contourType));
					contour.setControlPoints(controlPoints);
					// contour.setGeneratedPoints(generatedPoints);
					contours.add(contour);
					if (line[0].equals("-1")) {
						break;
					} else {
						contourType = Integer.parseInt(line[0]);
					}
				}
				System.out
				.println("Reached end of overlay....loading next set of contours.");
				DICOMImage image = SOPInstanceUIDToDICOMImage.get(sopInstanceUID);
				image.getContours().addAll(contours);
			}
			reader.close();
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}


	/**
	 * Decrements the current time index and updates the models.
	 */
	private void decrementTimeIndex() {
		// Check to see if it is possible
		if (this.studyStructModel.getStudy() == null) {
			return;
		}
		if ((this.tIndex - 1) >= 0) {
			// then decrement
			// System.out.println("GUIController : decrement time index");
			this.tIndex--;
			this.updateNewDicom();
		} else {
			this.tIndex = this.studyStructModel.getStudy().getGroups()
					.get(gIndex).getSlices().get(sIndex).getTimes().size() - 1;
		}
	}

	/**
	 * Increments the current time index and updates the models.
	 */
	private void incrementTimeIndex() {
		// Check to see if it is possible
		if (this.studyStructModel.getStudy() == null) {
			return;
		}
		ArrayList<Time> curTimes = this.studyStructModel.getStudy().getGroups()
				.get(gIndex).getSlices().get(sIndex).getTimes();
		if ((this.tIndex + 1) < curTimes.size()) {
			// then increment
			// System.out.println("GUIController : increment time index");
			this.tIndex++;
			this.updateNewDicom();
		} else {
			// Loop
			this.tIndex = 0;
		}
	}

	/**
	 * Decrements the current slice index and updates the models.
	 */
	private void decrementSliceIndex() {
		// Check to see if it is possible
		if (this.studyStructModel.getStudy() == null) {
			return;
		}
		if (this.sIndex > 0) {
			// then increment
			// System.out.println("GUIController : decrement slice index");
			this.sIndex--;
			this.updateNewDicom();
		} else {
			// System.out.println("GUIController : failed attempt to decrement slice");
		}
	}

	/**
	 * Increments the current slice index and updates the models.
	 */
	private void incrementSliceIndex() {
		// Check to see if it is possible
		if (this.studyStructModel.getStudy() == null) {
			return;
		}
		ArrayList<Slice> curSlices = this.studyStructModel.getStudy()
				.getGroups().get(gIndex).getSlices();
		if ((this.sIndex + 1) < curSlices.size()) {
			// then increment
			// System.out.println("GUIController : increment slice index " +
			// curSlices.size());
			this.sIndex++;
			this.updateNewDicom();
		} else {
			// System.out.println("GUIController : failed attempt to increment slice");
		}
	}

	/**
	 * Saves the currently opened Study object as a .smc file at the previously
	 * used file path. If no file path was previously used then it will call on
	 * saveAsStudy().
	 */
	private void saveStudy() {
		if (this.studyStructModel.getStudy() != null) {
			// System.out.println("Saving Study");
			if (this.filename == null) {
				this.saveAsStudy();
			} else {
				try {
					SerializationManager.save(this.studyStructModel.getStudy(),
							this.filename);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Opens a JFileChooser that allows the user to select where they would like
	 * to save the currently displayed Study object and what name they would
	 * like to give it.
	 */
	private void saveAsStudy() {
		if (this.studyStructModel.getStudy() != null) {
			// System.out.println("Saving Study As...");

			JFileChooser saveFC = fileChooser;

			FileFilter studyFileFilter = new FileNameExtensionFilter(
					"Study file (.smc)", "smc");
			saveFC.setFileFilter(studyFileFilter);

			int response = saveFC.showSaveDialog(this.mainComponent);

			if (response == JFileChooser.APPROVE_OPTION) {
				// System.out.println("Choose to save");
				String newFilename = saveFC.getSelectedFile().getAbsolutePath();
				// Incorrect file extension
				if (!newFilename.endsWith(".smc")) {
					newFilename = newFilename.concat(".smc");
				}
				// System.out.println("filename : " + newFilename);
				try {
					SerializationManager.save(this.studyStructModel.getStudy(),
							newFilename);
					this.filename = newFilename;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (response == JFileChooser.CANCEL_OPTION) {
				// System.out.println("Choose to Cancel");
			}
		}
	}

	/**
	 * Method that will close the GUIController's applicationFrame variable.
	 */
	private void closeWindow() {
		// System.out.println("Close Window");
		this.appFrame.dispatchEvent(new WindowEvent(this.appFrame,
				WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * Updates the GUIController's models with the a new Study object.
	 *
	 * @param s : Study object that will used as the currently displayed Study.
	 */
	private void updateNewStudy(Study s) {
		// System.out.println("GUIController : updating new Study");

		this.studyStructModel.setStudy(s);
		this.gridModel.setStudy(s);
		this.metadataModel.setStudy(s);
		this.mainImageModel.setStudy(s);

		// TODO: Try and find 2CH 4CH images
		this.imageModelSmallLeft.setStudy(s);
		// this.imageModelSmallLeft.setImage(g, s, t, i);
		this.imageModelSmallRight.setStudy(s);
		// this.imageModelSmallLeft.setImage(g, s, t, i);
	}

	/**
	 * Update the GUIController's models with the group, slice, time, and image
	 * indices.
	 */
	private void updateNewDicom() {
		// System.out.println("GUIController : updating new DICOM");
		// TODO:
		this.studyStructModel.setCurrentImage(this.gIndex, this.sIndex,
				this.tIndex, this.iIndex);
		this.gridModel.setCurrentImage(this.gIndex, this.sIndex, this.tIndex,
				this.iIndex);
		this.metadataModel.setCurrentImage(this.gIndex, this.sIndex,
				this.tIndex, this.iIndex);
		this.mainImageModel.setCurrentImage(this.gIndex, this.sIndex,
				this.tIndex, this.iIndex);
		// this.mainImageModel
		// .addContourToImage(new Contour(Contour.Type.DEFAULT));
		this.imageModelSmallLeft.setCurrentImage(this.gIndex, this.sIndex,
				this.tIndex, this.iIndex);
		// this.imageModelSmallLeft.addContourToImage(new Contour(
		// Contour.Type.DEFAULT));
		this.imageModelSmallRight.setCurrentImage(this.gIndex, this.sIndex,
				this.tIndex, this.iIndex);
		// this.imageModelSmallRight.addContourToImage(new Contour(
		// Contour.Type.DEFAULT));
	}

	/**
	 * Sets the class' studyStructModel and studyStructView attributes.
	 *
	 * @param ssm : StudyStructureModel object that will be set to the class'
	 *            studyStructModel attribute.
	 * @param ssv : StudyStructureView object that will be set to the class'
	 *            studyStructView attribute.
	 */
	public void setStudyStruct(StudyStructureModel ssm, StudyStructureView ssv) {
		this.setStudyStructModel(ssm);
		this.setStudyStructView(ssv);
	}

	/**
	 * Sets the class' studyStructModel attribute.
	 *
	 * @param ssm : StudyStructureModel object that will be set to the class'
	 *            studyStructModel attribute.
	 */
	public void setStudyStructModel(StudyStructureModel ssm) {
		this.studyStructModel = ssm;
	}

	/**
	 * Sets the class' studyStructView attribute, and sets itself as a
	 * MouseListener to the object.
	 *
	 * @param ssv : StudyStructureView object that will be set to the class'
	 *            studyStructView attribute.
	 */
	public void setStudyStructView(StudyStructureView ssv) {
		this.studyStructView = ssv;
		this.studyStructView.setMouseListener(this);
	}

	/**
	 * Sets the class' gridModel and gridView attributes.
	 *
	 * @param gm : GridModel object that will be set to the class' gridModel
	 *            attribute.
	 * @param gv : GridView object that will be set the class' gridView
	 *            attribute.
	 */
	public void setGrid(GridModel gm, GridView gv) {
		this.setGridModel(gm);
		this.setGridView(gv);
	}

	/**
	 * Sets the class' gridModel attribute.
	 *
	 * @param gm : GridModel object that will be set to the class' gridModel
	 *            attribute.
	 */
	public void setGridModel(GridModel gm) {
		this.gridModel = gm;
	}

	/**
	 * Sets the class' gridView attribute and sets itself as an ActionListener.
	 *
	 * @param gv : GridView object that will be set to the class' gridView
	 *            attribute.
	 */
	public void setGridView(GridView gv) {
		this.gridView = gv;
		this.gridView.setModel(this.gridModel);
		this.gridView.setActionListener(this);
	}

	/**
	 * Sets the class' metaDataModel and metaDataView attributes.
	 *
	 * @param mdm : MetaDataModel object that will be set to the class'
	 *            metaDataModel attribute.
	 * @param mdv : MetaDataView object that will be set to the class'
	 *            metaDataView attribute.
	 */
	public void setMetadata(MetadataModel mdm, MetadataView mdv) {
		this.setMetadataModel(mdm);
		this.setMetadataView(mdv);
	}

	/*
	 * Sets the class' metaDataModel attribute.
	 * 
	 * @param mdm : MetaDataModel object that will be set to the class'
	 * metaDataModel attribute.
	 */
	public void setMetadataModel(MetadataModel mdm) {
		this.metadataModel = mdm;
	}

	/**
	 * Sets the class' metaDataView attribute.
	 *
	 * @param mdv : MetaDataView object that will be set to the class'
	 *            metaDataView attribute.
	 */
	public void setMetadataView(MetadataView mdv) {
		this.metadataView = mdv;
		this.metadataView.setModel(this.metadataModel);
	}

	/**
	 * Sets the class' imageModel and imageView attributes.
	 * 
	 * @param im : ImageModel object that will be set to the class' imageModel
	 *            attribute.
	 * @param iv : ImageView object that will be set to the class' imageView
	 *            attribute.
	 */
	public void setImageViewer(ImageModel im, ImageView iv) {
		this.setImageModel(im);
		this.setImageView(iv);
	}

	public void setImageViewerSmallLeft(ImageModel im, ImageView iv) {
		this.setImageModelSmallLeft(im);
		this.setImageViewSmallLeft(iv);
	}

	public void setImageViewerSmallRight(ImageModel im, ImageView iv) {
		this.setImageModelSmallRight(im);
		this.setImageViewSmallRight(iv);
	}

	/**
	 * Sets the class' imageModel attribute.
	 * 
	 * @param im : ImageModel object that will be set to the class' imageModel
	 *            attribute.
	 */
	public void setImageModel(ImageModel im) {
		this.mainImageModel = im;
	}

	public void setImageModelSmallLeft(ImageModel im) {
		this.imageModelSmallLeft = im;
	}

	public void setImageModelSmallRight(ImageModel im) {
		this.imageModelSmallRight = im;
	}

	/**
	 * Sets the class' imageView attribute and sets itself as a MouseListener.
	 * 
	 * @param iv : ImageView object that will be set to the class' imageView
	 *            attribute.
	 */
	public void setImageView(ImageView iv) {
		this.mainImageView = iv;
		this.mainImageView.setModel(mainImageModel);
	}

	public void setImageViewSmallLeft(ImageView iv) {
		this.imageViewSmallLeft = iv;
		this.imageViewSmallLeft.setModel(imageModelSmallLeft);
	}

	public void setImageViewSmallRight(ImageView iv) {
		this.imageViewSmallRight = iv;
		this.imageViewSmallRight.setModel(imageModelSmallRight);
	}

	/**
	 * Sets the class' mainComponent attribute. The class' KeyBindings will be
	 * attached to the mainComponent.
	 * 
	 * @param c : JComponent object will be used as the class' mainComponent
	 *            attribute.
	 */
	public void setMainComponent(JComponent c) {
		this.mainComponent = c;

		this.addKeyBindings();
	}

	/**
	 * Sets the class' appFrame attribute.
	 *
	 * @param f : JFrame object that will be used as the class' appFrame
	 *            attribute.
	 */
	public void setAppFrame(JFrame f) {
		this.appFrame = f;
	}

	/**
	 * Adds common KeyBindings (Ctrl+S, Ctrl+Shift+S, Ctrl+O, etc.) to the
	 * class' mainComponent attribute.
	 */
	private void addKeyBindings() {
		// Need to map KeyBindings
		this.mainComponent.getInputMap().put(KeyStroke.getKeyStroke("LEFT"),
				"left");
		this.mainComponent.getActionMap().put("left", new LeftKeyAction(this));

		this.mainComponent.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"),
				"right");
		this.mainComponent.getActionMap()
		.put("right", new RightKeyAction(this));

		this.mainComponent.getInputMap().put(KeyStroke.getKeyStroke("DOWN"),
				"down");
		this.mainComponent.getActionMap().put("down", new DownKeyAction(this));

		this.mainComponent.getInputMap()
		.put(KeyStroke.getKeyStroke("UP"), "up");
		this.mainComponent.getActionMap().put("up", new UpKeyAction(this));

		KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask());
		this.mainComponent.getInputMap().put(ctrlS, "save");
		this.mainComponent.getActionMap().put("save", new CtrlSAction(this));

		KeyStroke ctrlShiftS = KeyStroke.getKeyStroke(KeyEvent.VK_S, 21);
		this.mainComponent.getInputMap().put(ctrlShiftS, "save as");
		this.mainComponent.getActionMap().put("save as",
				new CtrlShiftSAction(this));

		KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask());
		this.mainComponent.getInputMap().put(ctrlO, "open existing");
		this.mainComponent.getActionMap().put("open existing",
				new CtrlOAction(this));

		KeyStroke ctrlW = KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask());
		this.mainComponent.getInputMap().put(ctrlW, "close");
		this.mainComponent.getActionMap().put("close", new CtrlWAction(this));
	}

	/**
	 * Returns the class' studyStructModel attribute.
	 *
	 * @return The GUIController's studyStructModel attribute.
	 */
	public StudyStructureModel getStudyStructModel() {
		return this.studyStructModel;
	}

	/**
	 * Returns the class' studyStructView attribute.
	 *
	 * @return The GUIController's studyStructView attribute.
	 */
	public StudyStructureView getStudyStructView() {
		return this.studyStructView;
	}

	/**
	 * Returns the class' gridModel attribute.
	 *
	 * @return The GUIController's gridModel attribute.
	 */
	public GridModel getGridModel() {
		return this.gridModel;
	}

	/**
	 * Returns the class' gridView attribute.
	 *
	 * @return The GUIController's gridView attribute.
	 */
	public GridView getGridView() {
		return this.gridView;
	}

	/**
	 * Returns the class' metaDataModel attribute.
	 *
	 * @return The GUIController's metaDataModel attribute.
	 */
	public MetadataModel getMetadataModel() {
		return this.metadataModel;
	}

	/**
	 * Returns the class' metaDataViewattribute.
	 *
	 * @return The GUIController's metaDataView attribute.
	 */
	public MetadataView getMetadataView() {
		return this.metadataView;
	}

	/**
	 * Returns the class' imageModel attribute.
	 *
	 * @return The GUIController's imageModel attribute.
	 */
	public ImageModel getImageModel() {
		return this.mainImageModel;
	}

	/*
	 * Returns the class' imageView attribute.
	 * 
	 * @return The GUIController's imageView attribute.
	 */
	public ImageView getImageView() {
		return this.mainImageView;
	}

	public ImageView getImageViewSmallLeft() {
		return imageViewSmallLeft;
	}

	public ImageView getImageViewSmallRight() {
		return imageViewSmallRight;
	}

	/**
	 * Returns the class' mainComponent attribute.
	 *
	 * @return The GUIController's mainComponent attribute.
	 */
	public JComponent getMainComponent() {
		return this.mainComponent;
	}

	// Default constructor
	public GUIController() {
		fileChooser
		.setCurrentDirectory(new File(System.getProperty("user.dir")));
		// System.out.println("GUIController : GUIController()");
	}

	// Action classes
	private class LeftKeyAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private GUIController controller;

		// AbstractAction methods
		public void actionPerformed(ActionEvent e) {
			this.controller.decrementTimeIndex();
		}

		// Constructor
		public LeftKeyAction(GUIController c) {
			this.controller = c;
		}
	}

	private class RightKeyAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private GUIController controller;

		// AbstractAction methods
		public void actionPerformed(ActionEvent e) {
			this.controller.incrementTimeIndex();
		}

		// Constructor
		public RightKeyAction(GUIController c) {
			this.controller = c;
		}

	}

	private class UpKeyAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private GUIController controller;

		// AbstractAction methods
		public void actionPerformed(ActionEvent e) {
			this.controller.decrementSliceIndex();
		}

		// Constructor
		public UpKeyAction(GUIController c) {
			this.controller = c;
		}
	}

	private class DownKeyAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private GUIController controller;

		// AbstractAction methods
		public void actionPerformed(ActionEvent e) {
			this.controller.incrementSliceIndex();
		}

		// Constructor
		public DownKeyAction(GUIController c) {
			this.controller = c;
		}
	}

	private class CtrlSAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private GUIController controller;

		// AbstractAction methods
		public void actionPerformed(ActionEvent e) {
			this.controller.saveStudy();
		}

		// Constructor
		public CtrlSAction(GUIController c) {
			this.controller = c;
		}
	}

	private class CtrlShiftSAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private GUIController controller;

		// AbstractAction methods
		public void actionPerformed(ActionEvent e) {
			this.controller.saveAsStudy();
		}

		// Constructor
		public CtrlShiftSAction(GUIController c) {
			this.controller = c;
		}
	}

	private class CtrlOAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private GUIController controller;

		// AbstractAction methods
		public void actionPerformed(ActionEvent e) {
			this.controller.loadExistingStudy(e);
			;
		}

		// Constructor
		public CtrlOAction(GUIController c) {
			this.controller = c;
		}
	}

	private class CtrlWAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private GUIController controller;

		// AbstractAction methods
		public void actionPerformed(ActionEvent e) {
			this.controller.closeWindow();
		}

		// Constructor
		public CtrlWAction(GUIController c) {
			this.controller = c;
		}
	}
}