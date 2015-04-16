/**
 * 
 */
package edu.auburn.cardiomri.gui.views;

import javax.swing.JPanel;

import edu.auburn.cardiomri.gui.models.Model;

/**
 * @author Moniz
 *
 */
public interface ViewInterface {
    public abstract JPanel getPanel();

    public abstract Model getModel();

    public abstract void setModel(Model model);
}
