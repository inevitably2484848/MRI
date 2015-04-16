package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Vector;

import javafx.geometry.Point2D;

import javax.swing.JPanel;

import toxi.geom.Spline2D;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SingleImagePanel;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.gui.ConstructImage;
import edu.auburn.cardiomri.gui.GUIController;
import edu.auburn.cardiomri.gui.ImageDisplay;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.util.ContourCalc;

public class ImageView extends View {
	private JPanel panel;
	private ImageDisplay display = null;
	private ImageModel model;
	private GUIController guiController;

	// Observer methods
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void update(Observable obs, Object obj) {
		if (obj.getClass() == DICOMImage.class) {
			updateImage((DICOMImage) obj);
		} else if (obj.getClass() == Vector.class) {
			Vector vector = (Vector) obj;
			if ((vector.size() == 0)
					|| (vector.firstElement().getClass() == Contour.class)) {
				updateContours((Vector<Contour>) obj);
			}
		} else if (obj.getClass() == Contour.class) {
			updateCurrentContour((Contour) obj);
		}
	}

	private void updateImage(DICOMImage dImage) {
		display = null;
		SingleImagePanel.deconstructAllSingleImagePanelsInContainer(panel);
		panel.removeAll();

		try {
			ConstructImage sImg = new ConstructImage(dImage);
			display = new ImageDisplay(sImg);
			display.setGuiController(guiController);

		} catch (DicomException e) {
			e.printStackTrace();
		}

		display.revalidate();
		display.repaint();

		panel.add(display);
		panel.revalidate();
		panel.repaint();
	}

	private void updateCurrentContour(Contour contour) {
		if (display != null) {
			display.setCurrentContour(contour);
			display.repaint();
			panel.repaint();
		} else {
			// throw error?
		}
	}

	private void updateContours(Vector<Contour> contours) {
		if (this.display != null) {
			display.setContours(contours);
			display.repaint();
			panel.repaint();
		} else {
			// throw error?
		}
	}

	public void setModel(ImageModel model) {
		this.model = model;
	}

	public ImageModel getModel() {
		return this.model;
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void refresh() {
		this.display.revalidate();
		this.display.repaint();
	}

	public ImageView() {
		// System.out.println("ImageView()");

		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(1, 1));
		this.panel.setBackground(Color.BLACK);
		this.panel.setOpaque(true);
		this.panel.setVisible(true);
	}

	public ImageDisplay getImageDisplay() {
		return this.display;
	}

	public void selectContour(Point2D p) {
		DICOMImage dImage = this.model.getImage();
		Spline2D spline1, spline2;
		float length1, length2, deltaLength;
		List<Point2D> controlPoints;
		HashMap<Contour, Float> deltaList = new HashMap<Contour, Float>();
		for (Contour c : dImage.getContours()) {
			if (c.getControlPoints().size() == 0) 
				continue;
			else {
				spline1 = ContourCalc.getSplineFromControlPoints(c.getControlPoints(), c.isClosedCurve());
				spline1.computeVertices(50);
				length1 = spline1.getEstimatedArcLength();
				controlPoints = c.getControlPoints();
				controlPoints.add(p);
				spline2 = ContourCalc.getSplineFromControlPoints(controlPoints, c.isClosedCurve());
				spline2.computeVertices(50);
				length2 = spline2.getEstimatedArcLength();
				deltaLength = length2 - length1;

				deltaList.put(c, deltaLength);
			}
		}

		Map.Entry<Contour, Float> minDelta = null;
		for(Map.Entry<Contour, Float> entry : deltaList.entrySet()) {
			if (minDelta == null || entry.getValue().compareTo(minDelta.getValue()) < 0)
			{
				minDelta = entry;
			}
		}
		this.model.setSelectedContour(minDelta.getKey());
	}

	public void setGuiController(GUIController guiController) {
		this.guiController = guiController;
	}
}
