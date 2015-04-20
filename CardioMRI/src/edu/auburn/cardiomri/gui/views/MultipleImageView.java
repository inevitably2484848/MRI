package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class MultipleImageView extends View {

	
	public MultipleImageView()
	{
		this.panel = new JPanel();
		this.panel.setSize(200, 200);
		this.panel.setLayout(new GridLayout(1, 1));
		this.panel.setBackground(Color.YELLOW);
		this.panel.setOpaque(true);
		this.panel.setVisible(true);
	}
}
