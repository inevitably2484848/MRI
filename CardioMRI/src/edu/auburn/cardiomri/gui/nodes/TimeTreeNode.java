/**
 * 
 */
package edu.auburn.cardiomri.gui.nodes;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Time;
import edu.auburn.cardiomri.datastructure.DICOMImage.ImageComparator;

public class TimeTreeNode implements TreeNode {

    private Time time;
    private int gIndex;
    private int sIndex;
    private int tIndex;

    // TreeNode methods
    public TreeNode getChildAt(int childIndex) {
        int[] indices = { gIndex, sIndex, tIndex, childIndex };
        ImageTreeNode iTN = new ImageTreeNode(this.time.getImages().get(
                childIndex), indices);
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
            ImageTreeNode imageNode = ((ImageTreeNode) node);
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
        int[] indices = { gIndex, sIndex, tIndex };
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