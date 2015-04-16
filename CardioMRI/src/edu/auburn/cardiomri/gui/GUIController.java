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
import edu.auburn.cardiomri.gui.models.StudyStructureModel;
import edu.auburn.cardiomri.gui.views.GridView;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.StudyStructureView;
import edu.auburn.cardiomri.util.SerializationManager;

public class GUIController implements java.awt.event.ActionListener,
MouseListener {


	private int gIndex = 0, sIndex = 0, tIndex = 0, iIndex = 0;

	private StudyStructureModel studyStructModel;
	private GridModel gridModel;
	private ImageModel mainImageModel;
	private ImageModel imageModelSmallLeft;
	private ImageModel imageModelSmallRight;

	private StudyStructureView studyStructView;
	private GridView gridView;
	private ImageView mainImageView;
	private ImageView imageViewSmallLeft;
	private ImageView imageViewSmallRight;

	// Default constructor
	public GUIController() {
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		// System.out.println("GUIController : GUIController()");
	}

}