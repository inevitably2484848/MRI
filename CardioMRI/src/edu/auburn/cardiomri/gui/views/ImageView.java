package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.GeometryOfSliceFromAttributeList;
import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.geometry.GeometryOfSlice;
import com.pixelmed.geometry.LocalizerPoster;
import com.pixelmed.geometry.LocalizerPosterFactory;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.datastructure.Landmark.Type;
import edu.auburn.cardiomri.datastructure.Point;
import edu.auburn.cardiomri.datastructure.TensionPoint;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.gui.ConstructImage;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.Model;
import edu.auburn.cardiomri.popupmenu.view.ContourContextMenu;
import edu.auburn.cardiomri.popupmenu.view.LandmarkContextMenu;
import edu.auburn.cardiomri.popupmenu.view.SelectContextMenu;
import edu.auburn.cardiomri.util.Mode;
import edu.auburn.cardiomri.datastructure.ControlPoint;

public class ImageView extends SingleImagePanel implements ActionListener,
        ViewInterface, Observer, KeyListener {
    protected Model model;
    protected JPanel imageContourPanel, panel, cPanel;
    private static final long serialVersionUID = -6920775905498293695L;
    private boolean lmrkMode = false;
    private boolean controlPressed = false;
    private Point clickedPoint = null;
    private Vector<Shape> redShapes = new Vector<Shape>();
    private Vector<Shape> orangeShapes = new Vector<Shape>();
    private Vector<Shape> blueShapes = new Vector<Shape>();
    private Vector<Shape> whiteShapes = new Vector<Shape>();
    
    public ContourContextMenu contourCM;// = ContourContextMenu.popupContextMenu(); //kw
    public LandmarkContextMenu landmarkCM; //LandmarkContextMenu()
    public SelectContextMenu selectCM ; //SelectContextMenu();
    
//    private ImageView mainImageView = null;
//    private ImageView twoChamberView = null;
//    private ImageView fourChamberView = null;
//    
    

    
    /**
     * Redraws the DICOMImage, updates the selected contour's control points,
     * and updates the set of visible contours.
     */
    public void update(Observable obs, Object obj) {
        if (obj.getClass() == DICOMImage.class) {
            this.redraw();
        }
    }
    
    public void redraw() {


    	DICOMImage dImage = getImageModel().getImage();

        dirtySource(new ConstructImage(dImage));
        
        clearShapes();
        
        updateContours(getImageModel().getContours());
        updateLandmarks(getImageModel().getLandmarks());
        
    	//addSliceLines();
        
        colorShapes();
        
        refresh();
    }
    
    private void clearShapes() {
    	this.redShapes.clear();
    	this.blueShapes.clear();
    	this.orangeShapes.clear();
    	this.whiteShapes.clear();
    }
    
    private void colorShapes() {

    	this.setSelectedDrawingShapes(redShapes);
    	this.setPreDefinedShapes(blueShapes);
    	this.setPersistentDrawingShapes(orangeShapes);
    	this.setLocalizerShapes(whiteShapes);
    }
    
    private void updateLandmarks(Vector<Landmark> landmarks) {
    	for (Landmark landmark: landmarks) {
    		if (landmark != null) {	
    			if (landmark.isVisible()) {
    				double x = landmark.getX();
	        		double y = landmark.getY();
	        		GeneralPath cross = new GeneralPath();
	        		//horizontal component
	        		cross.moveTo(x-1, y);
	        		cross.lineTo(x+1, y);
	        		//vertical component
	        		cross.moveTo(x,y-1);
	        		cross.lineTo(x,y+1);
	        		
	        		colorShape(cross, landmark.getColor());
    			}
        	}
    	}
    }
    

    private void updateContours(Vector<Contour> contours) {
    	for (Contour contour: contours) {
    		updateContour(contour);
    	}
    }
    
    private void updateContour(Contour contour) {
    	if (contour != null) {
    		if (contour.isVisible()) {
    			
    			colorShape(contour, contour.getColor());
    		
	    		if (contour.isSelected()) {
		            
		            for(int i = 0; i < contour.getControlPoints().size(); i++) {
		            	
		            	ControlPoint controlPoint = contour.getControlPoints().get(i);
		            	
		            	Ellipse2D controlPointEllipse = new Ellipse2D.Double(controlPoint.getX(),
		                        controlPoint.getY(), 2, 2);
		            	
		            	colorShape(controlPointEllipse, controlPoint.getColor());
		            	
		            	TensionPoint tensionPoint1 = controlPoint.getTension1();
		            	TensionPoint tensionPoint2 = controlPoint.getTension2();
		            	
		            	if ((Mode.getMode() == Mode.contourMode()) || 
		            		(controlPoint.isSelected() || tensionPoint1.isSelected() || tensionPoint2.isSelected()))
		            	{
			            
			            	Ellipse2D tensionPoint1Ellipse = new Ellipse2D.Double(tensionPoint1.getX(), tensionPoint1.getY(), 2, 2);
			    			Ellipse2D tensionPoint2Ellipse = new Ellipse2D.Double(tensionPoint2.getX(), tensionPoint2.getY(), 2, 2);
			    			
			            	if(contour.getControlPoints().size() > 1) {
			            		//check to not draw tension points of the final curve if the bezier curve is open
			            		if(i == 0) {
			            			if(contour.isClosedCurve()) {
			            				colorShape(tensionPoint1Ellipse, tensionPoint1.getColor());
			            			}
			            			else {
			            				tensionPoint1.isVisible(false);
			            			}
			            			colorShape(tensionPoint2Ellipse, tensionPoint2.getColor());
			            			tensionPoint2.isVisible(true);
			            		} 
			            		else if(i == (contour.getControlPoints().size() - 1)) {
			            			if(contour.isClosedCurve()) {
				            			colorShape(tensionPoint2Ellipse, tensionPoint2.getColor());
			            			}
			            			else {
			            				tensionPoint2.isVisible(false);
			            			}
			            			colorShape(tensionPoint1Ellipse, tensionPoint1.getColor());
			            			tensionPoint1.isVisible(true);
			            		}
			            		else {
		            				colorShape(tensionPoint1Ellipse, tensionPoint1.getColor());
		            				colorShape(tensionPoint2Ellipse, tensionPoint2.getColor());
		            				tensionPoint1.isVisible(true);
		            				tensionPoint2.isVisible(true);
			            		}
			            	}
		            	}
			        }
	    		}
    		}
        }
    }
    
    private void colorShape(Shape shape, Color color) {
    	if (color == Color.BLUE) {
    		this.blueShapes.add(shape);
    	}
    	else if (color == Color.ORANGE) {
    		this.orangeShapes.add(shape);
    	}
    	else if (color == Color.RED) {
    		this.redShapes.add(shape);
    	}
    	else if (color == Color.WHITE) {
    		this.whiteShapes.add(shape);
    	}
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
        panel.addKeyListener(this);
        return panel;
    }
    

    public void refresh() {
        this.revalidate();
        this.repaint();
    }

    public ImageView(ConstructImage sImg) {
        super(sImg);
    }

    /**************************************************************************
     * Handle mouse click events. 
     * Depending on which ever mode you are in the mouse click will react
     * differently.
     * Mode 1: ContourMode
     * Mode 2: LandmarkMode
     * Mode 3: SelectMode
     *************************************************************************/
    public void mouseClicked(MouseEvent e) {

    	int mode = Mode.getMode(); //kw
    	closeOpenMenus();
    	//System.out.println("MODE : " + mode);
    	
    	java.awt.geom.Point2D mouseClick =  getImageCoordinateFromWindowCoordinate(e.getX(), e.getY());
    	
    	if(mode == 1){ 
    		// CONTOUR MODE
    		if(SwingUtilities.isLeftMouseButton(e)){
	    		if (!getImageModel().addControlPoint(mouseClick.getX(),mouseClick.getY())) {
	                System.err.println("currentContour is null");
	            }
    		}
    		else if(SwingUtilities.isRightMouseButton(e)){
    			
    			contourCM = new ContourContextMenu(getImageModel()); //open context menu

    		}
    	} 
    	else if (mode == 2){ 
    		//landmark mode
    		if (SwingUtilities.isLeftMouseButton(e)) {    			
    			Mode.setMode(Mode.selectMode());
            	getImageModel().addLandmarkToImage(new Landmark(Mode.getNextLandmarkType(), mouseClick.getX(), mouseClick.getY()));
            	getImageModel().setActiveLandmark(null);
            	GridControlView.depressToggles();
    		}
    		else if(SwingUtilities.isRightMouseButton(e)){
    	    	landmarkCM = new LandmarkContextMenu(getImageModel());
    		}
    		
    	}
    	else { 
    		// SELECT MODE
    		 if (SwingUtilities.isLeftMouseButton(e)) {
    			 //getImageModel().selectClosestAnnotationWithinRange(mouseClick.getX(), mouseClick.getY(), 30);
    	     } 
    		 else if(SwingUtilities.isRightMouseButton(e)){
    			 selectCM = new SelectContextMenu(getImageModel());
    		 }

    	} 
        this.panel.requestFocusInWindow();
    } // END MOUSE CLICK
    
    /**************************************************************************
     *  CLOSE ALL OPEN MENUS
     *************************************************************************/
    public void closeOpenMenus(){
  
    	if(selectCM != null && selectCM.isVisible()){
			selectCM.setVisible(false);
    		selectCM= null;
		 }
    	if(landmarkCM != null && landmarkCM.isVisible()){
			 landmarkCM.setVisible(false);
			 landmarkCM = null;
		 }
    	if(contourCM != null && contourCM.isVisible()){
			 contourCM.setVisible(false);
			 contourCM = null;
		 }
    }
    
    /**************************************************************************
     * MouseMoved checks and see if cursor is in the contextmenus.
     * if mouse is not the in the same range as a menu close menu
     * @param MouseEvent
     *************************************************************************/
    public void mouseMoved(MouseEvent e){
    	
    	if(contourCM != null && !(contourCM.isInBox())){
    		contourCM.setVisible(false);
    	}
    	else if( landmarkCM != null && !(landmarkCM.isInBox())){
    		landmarkCM.setVisible(false);
    	}
    	else if( selectCM != null && !(selectCM.isInBox())){
    		selectCM.setVisible(false);
    	}
    }
    
    
    
    public void mouseDragged(MouseEvent e) {
    	java.awt.geom.Point2D mouseClick = getImageCoordinateFromWindowCoordinate(e.getX(), e.getY());
    	
    	
        if(!lmrkMode) {
        	if (clickedPoint != null && clickedPoint.getClass() == ControlPoint.class) {
        		if (this.controlPressed) {	// translate entire contour with control+drag
        			getImageModel().moveContour(mouseClick.getX(), mouseClick.getY(), clickedPoint);
        		}
        		else {	// move single control point without control pressed
        			getImageModel().getSelectedContour().moveContourPoint(mouseClick.getX(), mouseClick.getY(), (ControlPoint)clickedPoint);
        		}
        	}
        	else if (clickedPoint != null && clickedPoint.getClass() == TensionPoint.class) {
        		if (this.controlPressed) {	// translate entire contour with control+drag
        			getImageModel().moveContour(mouseClick.getX(), mouseClick.getY(), clickedPoint);
        		}
        		else {	// move single tension point without control pressed
        			getImageModel().getSelectedContour().moveTensionPoint(mouseClick.getX(), mouseClick.getY(), (TensionPoint)clickedPoint);
        		}
        	}
        	else if (clickedPoint != null && clickedPoint.getClass() == Landmark.class) {
        		((Landmark) clickedPoint).moveLandmark(mouseClick.getX(),mouseClick.getY());
        	}
        	else {
        		super.mouseDragged(e);
        	}
        	
        	// Forces updating of control and tension points during dragging
        	this.redraw();
        }
        this.panel.requestFocusInWindow();
    }

    /**
     * This is so every time a mouse event is processed through the Image
     * panels, the arrow keys will work as well.
     * 
     */
    public void mouseReleased(MouseEvent e) {
    	clickedPoint = null;
        this.panel.requestFocusInWindow();
    }

 
  
    
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

    public void mousePressed(MouseEvent e) {

    	if (SwingUtilities.isLeftMouseButton(e)) {
    		java.awt.geom.Point2D mouseClick = getImageCoordinateFromWindowCoordinate(e.getX(), e.getY());
        	clickedPoint = getImageModel().findNearestPointWithinRange(mouseClick.getX(), mouseClick.getY(), 3);
        	
        	if (Mode.getMode() == Mode.selectMode()) {
    	    	getImageModel().selectClosestAnnotationWithinRange(mouseClick.getX(), mouseClick.getY(), 15);
        	}
	    } 
		else if(SwingUtilities.isRightMouseButton(e)){
			java.awt.geom.Point2D mouseClick = getImageCoordinateFromWindowCoordinate(e.getX(), e.getY());
			clickedPoint = getImageModel().findNearestPointWithinRange(mouseClick.getX(), mouseClick.getY(), 10);

			if (clickedPoint != null) {
				if (clickedPoint.getClass() == Landmark.class) {
					if (Mode.getMode() == Mode.selectMode()) {
						getImageModel().selectClosestAnnotationWithinRange(mouseClick.getX(), mouseClick.getY(), 15);
					}
				}
				
				if (clickedPoint.getClass() == ControlPoint.class) {
					if (Mode.getMode() == Mode.selectMode()) {
						getImageModel().selectClosestAnnotationWithinRange(mouseClick.getX(), mouseClick.getY(), 15);
					}
				}
				else if (clickedPoint.getClass() == TensionPoint.class) {
					if (Mode.getMode() == Mode.selectMode()) {
						getImageModel().selectClosestAnnotationWithinRange(mouseClick.getX(), mouseClick.getY(), 15);
					}
				}
				
			}
			else {
				
			}
			
		}
    	
    	
    	super.mousePressed(e);
    	
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
        
        panel.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F, commandKey),
                "Show Contours");
        panel.getActionMap().put("Show Contours",
                this.new ControlKeyAction("Show Contours", this));
    }
    
    
    public void keyTyped(KeyEvent e) {
    }

    
    public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            this.controlPressed = true;
        }
    }

    
    public void keyReleased(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            this.controlPressed = false;
        }
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