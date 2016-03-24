package edu.auburn.cardiomri.gui.views;

import java.awt.Shape;
import java.io.IOException;
import java.util.Vector;
import java.awt.Cursor;

import javax.swing.JSplitPane;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.GeometryOfSliceFromAttributeList;
import com.pixelmed.geometry.GeometryOfSlice;
import com.pixelmed.geometry.LocalizerPoster;
import com.pixelmed.geometry.LocalizerPosterFactory;

import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.gui.models.ImageModel;

/** This class creates the layout for main image panel, and the right most column of panels in the workspace view
 * 		Included are the 2 chamber and 4 chamber image views, and the image contour panel
 * 		Further functionality implemented on 2/25/2016 by Aaron Fregeau
 * @author Ben Gustafson
 * @author Aaron Fregeau 
 */
public class RightPanel extends View {

	protected static ImageView  mainImageView, twoChamberView, fourChamberView;
	protected static ModeView contourControl = null;
	protected static Cursor crossHair = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	protected static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	
	
	
	// changing the mode based on a single mode string
	public static void changeMode(String mode) {
		if (contourControl != null) {
			cursorChange(mode);
			contourControl.setMessage(mode);
		}
	}
	
	// changes the mode if a qualifier has been passed in
	public static void changeMode(String mode, String qualifier) {
		if (contourControl != null) {
			cursorChange(mode);
			contourControl.setMessage(mode, qualifier);
		
		}
	}
	
	// Changes the cursor to crosshair or back to default based on the mode
	private static void cursorChange(String mode) {
		if (mode.equals("LANDMARK MODE")) {
			mainImageView.setCursor(crossHair);
	    	twoChamberView.setCursor(crossHair);
	    	fourChamberView.setCursor(crossHair);
			}
		else {
			mainImageView.setCursor(defaultCursor);
			twoChamberView.setCursor(defaultCursor);
			fourChamberView.setCursor(defaultCursor);
		}
	}
	/**
	 * Takes all needed Views for the main image panel, and the right most column of panels in the workspace view
	 * 
	 * @param mainImage			Image View with the Short Axis group (Big, center image)
	 * @param twoChamber		Image View with two chamber group (Upper right image)
	 * @param fourChamber		Image View with the four chamber group (Middle Right image)
	 * @param contourControl    Image View for the contour control (Bottom right panel)
	 */
	public RightPanel(ImageView mainImage, ImageView twoChamber, ImageView fourChamber, ModeView contourControl)
	{
		super();
		this.mainImageView = mainImage;
		this.twoChamberView = twoChamber;
		this.fourChamberView = fourChamber;
		this.contourControl = contourControl;
		addSliceLines(this.mainImageView.getImageModel(), this.twoChamberView.getImageModel(), this.fourChamberView.getImageModel());
		SetupPanel();
	}
	
	/**
	 * Crates the split pane objects, sets divider locations, adds all of them to the internal panel
	 * 		Notice the first splitPane is added to the second one, then second one is added to the third one
	 * 
	 * @note Resize Weight is used when resizing the whole window, has to be the same as the divider locations
	 * @note It is is also crucial to only add the final split pane to the panel, adding individually will render them wrong. 
	 */
	private void SetupPanel()
	{
		
    	JSplitPane smallImagesPane = new JSplitPane(
	            JSplitPane.VERTICAL_SPLIT, true, this.twoChamberView.getPanel(), this.fourChamberView.getPanel());
  	
    	JSplitPane rightSideOfWindow = new JSplitPane(
	            JSplitPane.VERTICAL_SPLIT, true, smallImagesPane, this.contourControl.getPanel());
    	
    	JSplitPane imagePanes  = new JSplitPane(
    			JSplitPane.HORIZONTAL_SPLIT,true, this.mainImageView.getPanel(), rightSideOfWindow);

        smallImagesPane.setResizeWeight(0.47);
        rightSideOfWindow.setResizeWeight(0.6);
        imagePanes.setResizeWeight(0.75);

        smallImagesPane.setDividerLocation(0.47);
        rightSideOfWindow.setDividerLocation(0.6);
        imagePanes.setDividerLocation(0.75);

	    this.panel.add(imagePanes);
	}
	
	private void addSliceLines(ImageModel mainImageModel, ImageModel twoChamberModel, ImageModel fourChamberModel) {
		DICOMImage mainImage = mainImageModel.getImage();
		DICOMImage twoChamberImage = twoChamberModel.getImage();
		DICOMImage fourChamberImage = fourChamberModel.getImage();
		
			mainImageModel.setTwoChamberSliceLines(getSliceLines(twoChamberImage, mainImage));
			mainImageModel.setFourChamberSliceLines(getSliceLines(fourChamberImage, mainImage));

			twoChamberModel.setMainSliceLines(getSliceLines(mainImage, twoChamberImage));
			twoChamberModel.setFourChamberSliceLines(getSliceLines(fourChamberImage, twoChamberImage));

			fourChamberModel.setMainSliceLines(getSliceLines(mainImage, fourChamberImage));
			fourChamberModel.setTwoChamberSliceLines(getSliceLines(twoChamberImage, fourChamberImage));
    	
	}

	private Vector<Shape> getSliceLines(DICOMImage mainImage, DICOMImage secondImage) {
		Vector<Shape> intersectionShapes = findIntersection(mainImage, secondImage);
		return intersectionShapes;
	}

	private Vector<Shape> findIntersection(DICOMImage postImage, DICOMImage localizerImage) {
		AttributeList localizerAttributeList = new AttributeList();
		try {
			localizerAttributeList.read(localizerImage.getRawFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DicomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AttributeList postImageAttributeList = new AttributeList();
		try {
			postImageAttributeList.read(postImage.getRawFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DicomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GeometryOfSlice localizerGeometry = null;
		try {
			localizerGeometry = new GeometryOfSliceFromAttributeList(localizerAttributeList);
		} catch (DicomException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		GeometryOfSlice postImageGeometry = null;
		try {
			postImageGeometry = new GeometryOfSliceFromAttributeList(postImageAttributeList);
		} catch (DicomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalizerPoster localizerPoster = LocalizerPosterFactory.getLocalizerPoster(false,false);
		localizerPoster.setLocalizerGeometry(localizerGeometry);
		Vector<Shape> shapes = localizerPoster.getOutlineOnLocalizerForThisGeometry(postImageGeometry);
		return shapes;
	}
}
