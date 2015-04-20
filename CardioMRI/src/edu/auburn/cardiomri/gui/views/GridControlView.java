package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GridControlView extends View implements ChangeListener {

	
	public GridControlView()
	{
		super();
	       this.panel = new JPanel();
	       this.panel.setSize(200, 200);
	       this.panel.setLayout(new GridLayout(1, 1));
	       this.panel.setBackground(Color.ORANGE);
	       this.panel.setOpaque(true);
	       this.panel.setVisible(true);
		  
	       JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL,0, 20, 1);
	       framesPerSecond.addChangeListener(this);
	       
	       this.panel.add(framesPerSecond);
	}
	
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            int fps = (int) source.getValue();
            System.out.println(fps);
        }
    }
}
