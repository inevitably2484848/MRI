package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class ContourControlView extends View {
	
	public ContourControlView()
	{
		super();
		this.panel = new JPanel();
	    this.panel.setSize(200, 200);
	    this.panel.setLayout(new GridLayout(1, 1));
	    this.panel.setBackground(Color.BLUE);
	    this.panel.setOpaque(true);
	    this.panel.setVisible(true);
	}
	
}
