/**
 * 
 */
package edu.auburn.cardiomri.gui.nodes;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import edu.auburn.cardiomri.datastructure.Group;

public class GroupTreeNode implements TreeNode {

    private Group group;
    private int gIndex;

    public TreeNode getChildAt(int childIndex) {
        int[] indices = { gIndex, childIndex };
        return new SliceTreeNode(this.group.getSlices().get(childIndex),
                indices);
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