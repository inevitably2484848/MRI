/**
 * 
 */
package edu.auburn.cardiomri.gui.views;

import javax.swing.JPanel;

import edu.auburn.cardiomri.gui.models.Model;

/**
 * Created to standardize views across the project.
 * Super class View implements this as well as ImageView.
 * ImageView is not a subclass of the super class View
 * because it is a subclass of SingleImagePanel in pixelMed
 * 
 * @author Moniz
 * 
 */
public interface ViewInterface {
    public abstract JPanel getPanel();

//    public abstract Model getModel();
//
//    public abstract void setModel(Model model);
}
