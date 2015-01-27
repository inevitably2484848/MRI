package edu.auburn.cardiomri.gui;

import edu.auburn.cardiomri.datastructure.DICOMImage.ImageComparator;
import edu.auburn.cardiomri.datastructure.Group;
import edu.auburn.cardiomri.datastructure.Slice;
import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Time;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


public class StudyStructureView implements java.util.Observer {
	
	private JButton newStudyButton;
	private JButton loadExistingButton;
	private JButton loadSingleButton;
	
	private StudyTreeNode rootNode;
	private JTree tree;
	
	private JPanel panel;
	
	private MouseListener mouseListener;
	
	// Observer methods
	public void update(Observable obs, Object obj) {
//System.out.println("StudyStructureView : updated");
		
		// New Indices
		if (obj.getClass() == int[].class) {
			int[] indices = (int[])obj;
			
			int gIndex = indices[0];
			int sIndex = indices[1];
			int tIndex = indices[2];
			int iIndex = indices[3];
			
//System.out.println("StudyStructureView : " + gIndex + " " + sIndex + " " + tIndex + " " + iIndex);
			
			if (this.tree.getSelectionPath() != null) {
				this.tree.removeSelectionPath(this.tree.getSelectionPath());
			}
			
			
			TreePath tPath = new TreePath(this.rootNode);
			// Group
			tPath = tPath.pathByAddingChild(rootNode.getChildAt(gIndex));
			// Slice
			tPath = tPath.pathByAddingChild(rootNode.getChildAt(gIndex).getChildAt(sIndex));
			// Time
			tPath = tPath.pathByAddingChild(rootNode.getChildAt(gIndex).getChildAt(sIndex).getChildAt(tIndex));
			// Image
			tPath = tPath.pathByAddingChild(rootNode.getChildAt(gIndex).getChildAt(sIndex).getChildAt(tIndex)
					.getChildAt(iIndex));
			
//System.out.println("Length : " + tPath.getPathCount());
			
			this.tree.setSelectionPath(tPath);
		}
		
		// Study
		else if (obj.getClass() == Study.class) {
			
			this.panel.removeAll();
			this.panel.setLayout(new GridLayout(1,1));
			
			this.rootNode = new StudyTreeNode(((Study)obj));
			
			this.tree = new JTree(this.rootNode);
			this.tree.setSize(this.panel.getSize());
			for (int i = 0; i < this.tree.getRowCount(); i++) {
				this.tree.expandRow(i);
			}
			//tree.setPreferredSize(new Dimension(150, 600));
			
			JScrollPane treeContainer = new JScrollPane(this.tree);
			
			treeContainer.setPreferredSize(new Dimension(150, 300));
			treeContainer.setSize(150, 300);
			
			this.tree.addMouseListener(this.mouseListener);
			
			this.panel.add(treeContainer);
			
			//this.panel.setVisible(true);
			
			this.panel.revalidate();
		}
	}
	
	// Setters
	/*
	 * Sets the class' mouseListener attribute.
	 * 
	 *  @param	mL : MouseListener object that is used by the class as its
	 *  mouseListener attribute.
	 */
	public void setMouseListener(MouseListener mL) {
		this.mouseListener = mL;
	}
	
	// Getters
	/*
	 * Returns the class' panel attribute.
	 * 
	 *  @return		The class' panel attribute.
	 */
	public JPanel getPanel() { return this.panel; }
	
	/*
	 * Returns the class' tree attribute.
	 * 
	 *  @return		The class' tree attribute.
	 */
	public JTree getTree() { return this.tree; }
	
	// Button
	/*
	 * Sets the initial buttons' (Create New Study, Load Existing Study,
	 * Load Single Dicom Image) ActionListener attributes to the given 
	 * AttributeListener.
	 * 
	 * @param controller : ActionListener object that is set up to handle
	 * ActionEvents from the three initial buttons.
	 */
	public void addController(ActionListener controller) {
//System.out.println("StudyStructureView : adding controller");
		newStudyButton.addActionListener(controller);
		loadExistingButton.addActionListener(controller);
		loadSingleButton.addActionListener(controller);
	}
	
	// Constructor
	public StudyStructureView() {
//System.out.println("StudyStructureView()");
		
		this.panel = new JPanel();
		
		this.newStudyButton 	= new JButton("Create New Study");
		this.loadExistingButton = new JButton("Load Existing Study");
		this.loadSingleButton 	= new JButton("Load Single DICOM");
		
		this.newStudyButton.setSize(100, 100);
		this.loadExistingButton.setSize(100, 100);
		this.loadSingleButton.setSize(100, 100);

		this.panel.add(newStudyButton);		
		this.panel.add(loadExistingButton);
		this.panel.add(loadSingleButton);
		
		this.panel.setSize(300, 300);
		
	}

}

class StudyTreeNode implements TreeNode {
	
	private Study study;
	
	// TreeNode methods
	public TreeNode getChildAt(int childIndex) {
		int[] indices = {childIndex};
		return new GroupTreeNode(this.study.getGroups().get(childIndex), indices);
	}

	public int getChildCount() {
		return this.study.getGroups().size();
	}

	public TreeNode getParent() {
		return null;
	}

	public int getIndex(TreeNode node) {
		return 0;
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public boolean isLeaf() {
		return false;
	}

	public Enumeration<String> children() {
		return null;
	}
	
	// To String
	public String toString() {
		return new String("Study : " + this.study.getStudyID());
	}
	
	// Constructors
	public StudyTreeNode(Study s) {
		this.study = s;
	}
}

class GroupTreeNode implements TreeNode {
	
	private Group group;
	private int gIndex;
	
	public TreeNode getChildAt(int childIndex) {
		int[] indices = {gIndex, childIndex};
		return new SliceTreeNode(this.group.getSlices().get(childIndex), indices);
	}

	public int getChildCount() {
		return this.group.getSlices().size();
	}

	public TreeNode getParent() {
		return null;
	}

	public int getIndex(TreeNode node) {
		return 0;
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public boolean isLeaf() {
		return false;
	}

	public Enumeration<String> children() {
		return null;
	}
	
	// To String
	public String toString() {
		return new String("Group : " + this.group.getSeriesDescription());
	}

	// Constructors
	public GroupTreeNode(Group g, int[] indices) {
		this.group = g;
		this.gIndex = indices[0];
	}	
}

class SliceTreeNode implements TreeNode {
	
	private Slice slice;
	private int gIndex;
	private int sIndex;
	
	// TreeNode methods
	public TreeNode getChildAt(int childIndex) {
		int[] indices = {gIndex, sIndex, childIndex};
		TimeTreeNode tTN = new TimeTreeNode(this.slice.getTimes().get(childIndex), indices);
		return tTN;
	}

	public int getChildCount() {
		return this.slice.getTimes().size();
	}

	public TreeNode getParent() {
		return null;
	}

	public int getIndex(TreeNode node) {
		return 0;
	}

	public boolean getAllowsChildren() {
		return true;
	}


	public boolean isLeaf() {
		return false;
	}

	public Enumeration<String> children() {
		return null;
	}
	
	// To String
		public String toString() {
			return new String("Slice : " + this.sIndex);
		}
		
	// Setters
	public void setIndices(int[] indices) {
		gIndex = indices[0];
		sIndex = indices[1];
	}
	
	// Constructors
	public SliceTreeNode(Slice s, int[] indices) {
		this.slice = s;
		this.gIndex = indices[0];
		this.sIndex = indices[1];
	}
}

class TimeTreeNode implements TreeNode {
	
	private Time time;
	private int gIndex;
	private int sIndex;
	private int tIndex;
	
	// TreeNode methods
	public TreeNode getChildAt(int childIndex) {
		int[] indices = {gIndex, sIndex, tIndex, childIndex};
		ImageTreeNode iTN = new ImageTreeNode(this.time.getImages().get(childIndex), indices);
		return iTN;
	}

	public int getChildCount() {
		return this.time.getImages().size();
	}

	public TreeNode getParent() {
		return null;
	}

	public int getIndex(TreeNode node) {
		if (node.getClass() == ImageTreeNode.class) {
			ImageTreeNode imageNode = ((ImageTreeNode)node);
			ImageComparator iC = new ImageComparator();
			for (int i = 0; i < this.time.getImages().size(); i++) {
				DICOMImage image = this.time.getImages().get(i);
				if (iC.compare(imageNode.getDicomImage(), image) == 0) {
					return i;
				}
			}
		}
		return -1;
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public boolean isLeaf() {
		return false;
	}

	public Enumeration<String> children() {
		return null;
	}
	
	// To String
	public String toString() {
		return new String("Time : " + this.time.getTriggerTime());
	}
	
	// Setters
	public void setIndices(int[] indices) {
		gIndex = indices[0];
		sIndex = indices[1];
		tIndex = indices[2];
	}
	
	// Getters
	public int[] getIndices() {
		int[] indices = {gIndex, sIndex, tIndex};
		return indices;
	}
	
	// Constructors
		public TimeTreeNode(Time t, int[] indices) {
			this.time = t;
			this.gIndex = indices[0];
			this.sIndex = indices[1];
			this.tIndex = indices[2];
		}
}

class ImageTreeNode implements TreeNode {
	
	private DICOMImage image;
	private int gIndex;
	private int sIndex;
	private int tIndex;
	private int iIndex;
	
	// TreeNode methods
	public boolean getAllowsChildren() {
		return false;
	}

	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	public int getChildCount() {
		return 0;
	}

	public TreeNode getParent() {
		return null;
	}

	public int getIndex(TreeNode node) {
		return 0;
	}

	public boolean isLeaf() {
		return true;
	}

	public Enumeration<String> children() {
		return null;
	}
	
	// To String
	public String toString() {
		return new String("Image : " + this.image.getInstanceNumber());
	}
	
	// Setters
	public void setIndices(int[] indices) {
		gIndex = indices[0];
		sIndex = indices[1];
		tIndex = indices[2];
		iIndex = indices[3];
	}
	
	// Getters
	public DICOMImage getDicomImage() {
		return image;
	}
	
	public int[] getIndices() {
		int[] indices = {gIndex, sIndex, tIndex, iIndex};
		return indices;
	}
	
	// Constructors
	public ImageTreeNode(DICOMImage i, int[] indices) {
		this.image = i; 
		this.gIndex = indices[0];
		this.sIndex = indices[1];
		this.tIndex = indices[2];
		this.iIndex = indices[3];
	}
	
}