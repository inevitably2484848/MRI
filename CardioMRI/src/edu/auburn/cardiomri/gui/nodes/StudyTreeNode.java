/**
 * 
 */
package edu.auburn.cardiomri.gui.nodes;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import edu.auburn.cardiomri.datastructure.Study;

public class StudyTreeNode implements TreeNode {

    private Study study;

    // TreeNode methods
    public TreeNode getChildAt(int childIndex) {
        int[] indices = { childIndex };
        return new GroupTreeNode(this.study.getGroups().get(childIndex),
                indices);
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