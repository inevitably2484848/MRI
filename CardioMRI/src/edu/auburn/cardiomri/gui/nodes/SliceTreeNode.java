/**
 * 
 */
package edu.auburn.cardiomri.gui.nodes;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import edu.auburn.cardiomri.datastructure.Slice;

public class SliceTreeNode implements TreeNode {

    private Slice slice;
    private int gIndex;
    private int sIndex;

    // TreeNode methods
    public TreeNode getChildAt(int childIndex) {
        int[] indices = { gIndex, sIndex, childIndex };
        TimeTreeNode tTN = new TimeTreeNode(this.slice.getTimes().get(
                childIndex), indices);
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