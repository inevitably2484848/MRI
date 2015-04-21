/**
 *
 */
package edu.auburn.cardiomri.gui.views;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import edu.auburn.cardiomri.gui.models.Model;

/**
 * @author Moniz
 *
 */
public abstract class View implements Observer, ViewInterface, MouseListener,
        ActionListener {
    protected JPanel panel;
    protected Model model;

    public View() {
        panel = new JPanel();
		panel.setSize(200, 200);
		panel.setLayout(new GridLayout(1, 1));
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * @see edu.auburn.cardiomri.gui.views.View#getPanel()
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }

    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub

    }

    /**
     * @see edu.auburn.cardiomri.gui.views.ViewInterface#getModel()
     */
    @Override
    public Model getModel() {
        return model;
    }

    /**
     * @see edu.auburn.cardiomri.gui.views.ViewInterface#setModel(edu.auburn.cardiomri.gui.models.Model)
     */
    @Override
    public void setModel(Model model) {
        this.model = model;
        model.addObserver(this);
    }

}
