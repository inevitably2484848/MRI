package edu.auburn.cardiomri.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.pixelmed.display.SingleImagePanel;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.datastructure.Landmark.LandmarkType;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Vector3d;
import edu.auburn.cardiomri.gui.ConstructImage;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.Model;
import edu.auburn.cardiomri.popupmenu.view.ContourContextMenu;
import edu.auburn.cardiomri.util.Mode;
import edu.auburn.cardiomri.datastructure.ControlPoint;

public class ImageView extends SingleImagePanel implements ActionListener,
        ViewInterface, Observer {
    protected Model model;
    protected JPanel imageContourPanel, panel, cPanel;
    private static final long serialVersionUID = -6920775905498293695L;
    private boolean lmrkMode = false;
    private int cPointD = -1; //int values to act as mode flags for dragging and store dragging index value
    private int tPointD = -1;
    private Vector<Shape> visibleShapes = new Vector<Shape>();
    private ContourControlView contourPanel; // testing
    
    protected JPopupMenu ccm;// = ContourContextMenu.popupContextMenu(); //kw
    private int ccmIndex = 0;
    
    /**
     * Redraws the DICOMImage, updates the selected contour's control points,
     * and updates the set of visible contours.
     */
    public void update(Observable obs, Object obj) {
        if (obj.getClass() == DICOMImage.class) {
            DICOMImage dImage = getImageModel().getImage();
//            System.out.println("\n\nCall ContourControlView");
//            ContourControlView contourPanel = new ContourControlView(dImage);  //KW
//            JPanel temp = contourPanel.getPanel(); //kw
//            updateControlPanel(temp,dImage); //kw

            dirtySource(new ConstructImage(dImage));
            visibleShapes.clear();
            updateSelectedContour(getImageModel().getSelectedContour());
            updateVisibleContours(getImageModel().getVisibleContours());
            updateVisibleLandmarks(getImageModel().getVisibleLandmarks());
            updateTensionPoints(getImageModel().getSelectedContour());
            this.setPreDefinedShapes(visibleShapes);
            refresh();
        }
    }

    /**
     * Updates the set of control points that should be drawn onto the screen.
     * Control points are drawn as 2x2 ellipses.
     * 
     * @param contour The currently selected contour
     */
    private void updateSelectedContour(Contour contour) {
        
        if (contour != null) {
            for (ControlPoint controlPoint : contour.getControlPoints()) {
            	Ellipse2D ellipse = new Ellipse2D.Double(controlPoint.getX(),
                        controlPoint.getY(), 2, 2);
                visibleShapes.add(ellipse);
            }
        }
    }
    
    private void updateTensionPoints(Contour contour) {
    	
    	if(contour != null) {
    		for (ControlPoint controlPoint : contour.getControlPoints()) {
    			Ellipse2D ellipse = new Ellipse2D.Double(controlPoint.getTension1().getX(), controlPoint.getTension1().getY(), 2, 2);
    			Ellipse2D ellipse2 = new Ellipse2D.Double(controlPoint.getTension2().getX(), controlPoint.getTension2().getY(), 2, 2);
    			visibleShapes.add(ellipse);
    			visibleShapes.add(ellipse2);
    		}
        }   
    }
    

    private void updateVisibleLandmarks(Vector<Landmark> landmarks){
    	for (Landmark l:landmarks){
    		double x = l.getCoordinates().getX();
    		double y = l.getCoordinates().getY();
    		GeneralPath cross = new GeneralPath();
    		//horizontal component
    		cross.moveTo(x-1, y);
    		cross.lineTo(x+1, y);
    		//vertical component
    		cross.moveTo(x,y-1);
    		cross.lineTo(x,y+1);
    		visibleShapes.add(cross);
    	}
    }
    

    /**
     * Updates the list of contours to be drawn onto the screen
     * 
     * @param contours List of Contour objects
     */
    private void updateVisibleContours(Vector<Contour> contours) {
        this.setSelectedDrawingShapes(contours);
        //contourPanel.refreshView(contours); //KW
    }


    /**
     * This is copy/pasted from the View class.
     */
    public void setModel(Model model) {
        this.model = model;
        this.model.addObserver(this);
    }

    protected static ImageModel imageModel; //kw
    
    public ImageModel getImageModel() {
    	imageModel = (ImageModel) this.model;
        return (ImageModel) this.model;
    }
    
    public static ImageModel getImageModelStatic(){ //kw
    	return imageModel;
    }

    public Model getModel() {
        return this.model;
    }

    /**
     * Returns a JPanel with this ImageView as its only element
     */
    public JPanel getPanel() {
        panel = new JPanel();
        panel.setSize(200, 200);
        panel.setLayout(new GridLayout(1, 1));
        panel.setBackground(Color.BLACK);
        panel.add(this);
        panel.setFocusable(true);
        addKeyBindings(panel);
        return panel;
    }
    

    public void refresh() {
        this.revalidate();
        this.repaint();
    }

    public ImageView(ConstructImage sImg) {
        super(sImg);
    }

    /**
     * Handle mouse click events. Left clicks add a control point. Right clicks
     * select the nearest visible contour.
     */
    public void mouseClicked(MouseEvent e) {

    	int mode = Mode.getMode(); //kw
    	
    	System.out.println("MODE : " + mode);
    	
    	java.awt.geom.Point2D mouseClick = 
    			getImageCoordinateFromWindowCoordinate(e.getX(), e.getY());
    	
    	if(mode == 1){ // adding contour mode  --------------------------------
    		
    		//left click add contour point
    		if(SwingUtilities.isLeftMouseButton(e)){
	    		if (!getImageModel().addControlPoint(mouseClick.getX(),mouseClick.getY())) {
	                System.err.println("currentContour is null");
	            }
    		}
    		//RC context menu if contour selected
    			// Delete
    			// lock tension points
    			// properties
    		else if(SwingUtilities.isRightMouseButton(e)){
    			//getImageModel().setSelectedContour(null);
    			if(ccmIndex == 1){
    				ccm.removeAll();
    			}
    			ccmIndex = 1;
    			ccm = ContourContextMenu.popupContextMenu();
   			 	ccm.setLocation(MouseInfo.getPointerInfo().getLocation());
   			 	ccm.setVisible(true);
    			
    		}
    	} 
    	else if (mode == 2){ //landmark mode ----------------------------------
    		//left click add landMark
    		if (SwingUtilities.isLeftMouseButton(e)) {
    			getImageModel().setLandmarkCoordinates(mouseClick.getX(), mouseClick.getY());
    		}
    		else if(SwingUtilities.isRightMouseButton(e)){
    			
    		}
    		
    	} //-------------------------------------------------------------------
    	else { //select mode --------------------------------------------------
    		
    		//leftClick select closest contour or landMark
    		 if (SwingUtilities.isLeftMouseButton(e)) {
    	            getImageModel().selectContour(mouseClick.getX(), mouseClick.getY());
    	     } 
    		// rightClick context menu
    		 else {
    			 getImageModel().deleteSelectedContour();

    		 }
    		 
    		 
    		 
    	} //-------------------------------------------------------------------
    	
//        if (SwingUtilities.isRightMouseButton(e)) {
//            getImageModel().selectContour(mouseClick.getX(), mouseClick.getY());
//        } 
//        else {
//            if (!lmrkMode){
//            	if (!getImageModel().addControlPoint(mouseClick.getX(),
//                    mouseClick.getY())) {
//                System.err.println("currentContour is null");
//            	}
//            }
//            else {
//            	getImageModel().setLandmarkCoordinates(mouseClick.getX(), mouseClick.getY());
//            	lmrkMode = false;
//            }
//        }

        this.panel.requestFocusInWindow();
    }
    
    public void mouseDragged(MouseEvent e) {
    	java.awt.geom.Point2D mouseClick = getImageCoordinateFromWindowCoordinate(e.getX(), e.getY());
    	
        if(!lmrkMode) {
        	if(cPointD >= 0) {
        		getImageModel().getSelectedContour().moveContourPoint(mouseClick.getX(), mouseClick.getY(), cPointD);
        	}
        	else if(tPointD >= 0) {
        		getImageModel().getSelectedContour().moveTensionPoint(mouseClick.getX(), mouseClick.getY(), tPointD);	
        	}
        	else {
        		super.mouseDragged(e);
        		//add this code to the landmark drag when created
        	}
        	
        	// Forces updating of control and tension points during dragging
        	DICOMImage dImage = getImageModel().getImage();
            dirtySource(new ConstructImage(dImage));
            visibleShapes.clear();
            updateSelectedContour(getImageModel().getSelectedContour());
            updateVisibleContours(getImageModel().getVisibleContours());
            updateVisibleLandmarks(getImageModel().getVisibleLandmarks());
            updateTensionPoints(getImageModel().getSelectedContour());
            this.setPreDefinedShapes(visibleShapes);
            refresh();
        }
        this.panel.requestFocusInWindow();
    }

    /**
     * This is so every time a mouse event is processed through the Image
     * panels, the arrow keys will work as well.
     * 
     */
    public void mouseReleased(MouseEvent e) {
    	cPointD = -1;
    	tPointD = -1;
        this.panel.requestFocusInWindow();
    }

    public void mousePressed(MouseEvent e) {
    	int temp1;
    	int temp2;
    	java.awt.geom.Point2D mouseClick = getImageCoordinateFromWindowCoordinate(e.getX(), e.getY());
    	
    	temp1 = getImageModel().getSelectedContour().findControlPoint(mouseClick.getX(), mouseClick.getY());
    	temp2 = getImageModel().getSelectedContour().findTensionPoint(mouseClick.getX(), mouseClick.getY());
    	if(temp1 >= 0) {
    		cPointD = temp1;
    	}
    	else if(temp2 >= 0) {
    		tPointD = temp2;
    	}
    	
    	this.panel.requestFocusInWindow();
    }
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals("CONTOUR_MODE")){
        	Mode.setMode(Mode.contourMode());
        }
        else if (actionCommand.equals("LANDMARK_MODE")){
        	Mode.setMode(Mode.landmarkMode());
        }
        else if (actionCommand.equals("Default Type")) {
        	getImageModel().addContourToImage(new Contour(Type.DEFAULT));
        } 
        else if (actionCommand.equals("LV EPI")) {
        	Mode.setMode(Mode.contourMode());
            getImageModel().addContourToImage(new Contour(Type.LV_EPI));
        } 
        else if (actionCommand.equals("LV ENDO")) {
        	Mode.setMode(Mode.contourMode());
            getImageModel().addContourToImage(new Contour(Type.LV_ENDO));
        } 
        else if (actionCommand.equals("LA EPI")) {
        	Mode.setMode(Mode.contourMode());
            getImageModel().addContourToImage(new Contour(Type.LA_EPI));
        } 
        else if (actionCommand.equals("LA ENDO")) {
        	Mode.setMode(Mode.contourMode());
            getImageModel().addContourToImage(new Contour(Type.LA_ENDO));
        } 
        else if (actionCommand.equals("RV EPI")) {
        	Mode.setMode(Mode.contourMode());
            getImageModel().addContourToImage(new Contour(Type.RV_EPI));
        } 
        else if (actionCommand.equals("RV ENDO")) {
        	Mode.setMode(Mode.contourMode());
            getImageModel().addContourToImage(new Contour(Type.RV_ENDO));

        } else if (actionCommand.equals("RA EPI")) {
        	Mode.setMode(Mode.contourMode());
            getImageModel().addContourToImage(new Contour(Type.RA_EPI));

        } else if (actionCommand.equals("RA ENDO")) {
        	Mode.setMode(Mode.contourMode());
            getImageModel().addContourToImage(new Contour(Type.RA_ENDO));

        } else if (actionCommand.equals("ARV")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.ARV));
        	lmrkMode = true;
        } else if (actionCommand.equals("IRV")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.IRV));
        	lmrkMode = true;
        } else if (actionCommand.equals("MS")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.MS));
        	lmrkMode = true;
        } else if (actionCommand.equals("LVAPEX")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVAPEX));
        	lmrkMode = true;
        } else if (actionCommand.equals("LVSEPTALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVSEPTALBASE));
        	lmrkMode = true;
        } else if (actionCommand.equals("LVLATERALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVLATERALBASE));
        	lmrkMode = true;
        } else if (actionCommand.equals("LVANTERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVANTERIORBASE));
        	lmrkMode = true;
        } else if (actionCommand.equals("LVINFERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVINFERIORBASE));
        	lmrkMode = true;
        } else if (actionCommand.equals("RVAPEX")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVAPEX));
        	lmrkMode = true;
        } else if (actionCommand.equals("RVLATERALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVLATERALBASE));
        	lmrkMode = true;
        } else if (actionCommand.equals("RVSEPTALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVSEPTALBASE));
        	lmrkMode = true;
        } else if (actionCommand.equals("RVANTERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVANTERIORBASE));
        	lmrkMode = true;
        } else if (actionCommand.equals("RVINFERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVINFERIORBASE));
        	lmrkMode = true;
        } else if (actionCommand.equals("Delete Contour")) {
            if (getImageModel().getSelectedContour() == null) {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "Please select a contour to delete.");
            } else {
                getImageModel().deleteSelectedContour();
            }
        } else if (actionCommand.equals("Hide Contour")) {
            if (getImageModel().getSelectedContour() == null) {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "Please select a contour to hide.");
            } else {
                getImageModel().hideSelectedContour();
            }

        } else if (actionCommand.equals("Select Contour")) {
            Vector<Contour> visibleContours = getImageModel()
                    .getVisibleContours();
            if (visibleContours.size() > 0) {
                Contour[] contours = new Contour[visibleContours.size()];
                visibleContours.toArray(contours);

                Contour c = (Contour) JOptionPane.showInputDialog(
                        imageContourPanel, "Select Contour: ", "Contours",
                        JOptionPane.PLAIN_MESSAGE, null, contours, "ham");

                getImageModel().setSelectedContour(c);
            } else {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "There are no visible contours to select.");
            }
        } else if (actionCommand.equals("Hide Contours")) {
            if (getImageModel().getContours() == null
                    || getImageModel().getContours().size() == 0) {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "There are no contours to hide.");
            } else {
                getImageModel().hideAllContours();
            }
        } else if (actionCommand.equals("Show Contours")) {
            if (getImageModel().getHiddenContours() == null
                    || getImageModel().getHiddenContours().size() == 0) {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "There are no contours to show.");
            } else {
                getImageModel().showAllContours();
            }
        } else if (actionCommand.equals("Delete All Contours")) {
            if (getImageModel().getContours() == null
                    || getImageModel().getContours().size() == 0) {
                JOptionPane.showMessageDialog(imageContourPanel,
                        "There are no contours to delete.");
            } else {
                getImageModel().deleteAllContours();
            }
        }
        this.panel.requestFocusInWindow();
    }

    private void addKeyBindings(JPanel panel) {
        panel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
        panel.getActionMap().put("left", this.new ArrowKeyAction("left"));

        panel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
        panel.getActionMap().put("right", this.new ArrowKeyAction("right"));

        panel.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
        panel.getActionMap().put("down", this.new ArrowKeyAction("down"));

        panel.getInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
        panel.getActionMap().put("up", this.new ArrowKeyAction("up"));

        int commandKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        panel.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_E, commandKey), "LV ENDO");
        panel.getActionMap().put("LV ENDO",
                this.new ControlKeyAction("LV ENDO", this));

        panel.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, commandKey), "LV EPI");
        panel.getActionMap().put("LV EPI",
                this.new ControlKeyAction("LV EPI", this));

        panel.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_D, commandKey),
                "Delete Contour");
        panel.getActionMap().put("Delete Contour",
                this.new ControlKeyAction("Delete Contour", this));

        panel.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_D, commandKey
                        | InputEvent.SHIFT_MASK), "Delete Contours");
        panel.getActionMap().put("Delete Contours",
                this.new ControlKeyAction("Delete All Contours", this));

        panel.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_G, commandKey
                        | InputEvent.SHIFT_MASK), "Hide Contours");
        panel.getActionMap().put("Hide Contours",
                this.new ControlKeyAction("Hide Contours", this));

        panel.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_G, commandKey),
                "Hide Contour");
        panel.getActionMap().put("Hide Contour",
                this.new ControlKeyAction("Hide Contour", this));

        panel.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F, commandKey),
                "Show Contours");
        panel.getActionMap().put("Show Contours",
                this.new ControlKeyAction("Show Contours", this));
    }

    /**
     * Action key event for all of the arrow buttons. Has to be sent over to
     * workspaceView
     * 
     * @author Ben Gustafson
     *
     */
    public class ArrowKeyAction extends AbstractAction {
        private static final long serialVersionUID = 6612132766001531904L;
        private String comand;

        public ArrowKeyAction(String cmd) {
            this.comand = cmd;
        }

        public void actionPerformed(ActionEvent e) {
            getImageModel().arrowAction(
                    new ActionEvent(panel, (int) ActionEvent.ACTION_PERFORMED,
                            this.comand));
        }
    }

    /**
     * Action key event for all of the arrow buttons. Has to be sent over to
     * workspaceView
     * 
     * @author Ben Gustafson
     *
     */
    public class ControlKeyAction extends AbstractAction {
        private static final long serialVersionUID = 6612132766001531904L;
        private String comand;
        private ImageView imageView;

        public ControlKeyAction(String cmd, ImageView imageView) {
            this.comand = cmd;
            this.imageView = imageView;
        }

        public void actionPerformed(ActionEvent e) {
            imageView.actionPerformed(new ActionEvent(panel,
                    (int) ActionEvent.ACTION_PERFORMED, this.comand));
        }
    }

}