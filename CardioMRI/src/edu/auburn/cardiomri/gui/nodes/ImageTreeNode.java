/**
 * 
 */
package edu.auburn.cardiomri.gui.nodes;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import edu.auburn.cardiomri.datastructure.DICOMImage;

public class ImageTreeNode implements TreeNode {

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
        int[] indices = { gIndex, sIndex, tIndex, iIndex };
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