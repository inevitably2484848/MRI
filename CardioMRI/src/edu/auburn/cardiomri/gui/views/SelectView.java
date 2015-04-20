package edu.auburn.cardiomri.gui.views;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.Observable;

import javafx.scene.control.ComboBox;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.sun.corba.se.spi.orbutil.fsm.Action;
import com.sun.org.apache.bcel.internal.generic.NEW;

import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.datastructure.Group.AxisType;
import edu.auburn.cardiomri.gui.models.SelectModel;
import edu.auburn.cardiomri.datastructure.Group;

public class SelectView extends View implements ActionListener {
	
    protected JPanel panel;
    protected SelectModel model;
    

//   private JLabel image_0;
//	private JLabel image_1;
//	private JLabel image_2;
    
	private static AxisType[] axisType = {}; 
//	private Icon[] pics = {new ImageIcon(getClass().getResource(axisTypeStrings[0])),
//			new  ImageIcon(getClass().getResource(axisTypeStrings[1])),
//			new  ImageIcon(getClass().getResource(axisTypeStrings[2]))};


    // Observer methods
    public void update(Observable obs, Object obj) {

    }

    /**
     * Returns the class' panel attribute.
     * 
     * @return The class' panel attribute.
     */
    public JPanel getPanel() {
        return this.panel;
    }

    public SelectView(Study s) {
    	super();
    	this.panel.setLayout(new GridBagLayout());
    	
    	JComboBox<AxisType> axisTypeList0 = new JComboBox<AxisType>(axisType);
    	JComboBox<AxisType> axisTypeList1 = new JComboBox<AxisType>(axisType);
    	JComboBox<AxisType> axisTypeList2 = new JComboBox<AxisType>(axisType);
    	
    	ActionListener actionListener0 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Study s = (Study)e.getSource();
				//this.model.setStudy(s);
			}
    	};
    	axisTypeList0.addActionListener(actionListener0);
    	
    	ActionListener actionListener1 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Study s = (Study)e.getSource();
			}
    	};
    	
    	axisTypeList1.addActionListener(actionListener1);
    	
    	ActionListener actionListener2 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Study s = (Study)e.getSource();
			}
    	};
    	axisTypeList2.addActionListener(actionListener2);
        
    }
    
    private AxisType selectedAxis(Group s) {
    	//String selected[] = s.getSeriesDescription();
    }

	public SelectModel getModel() {
        return model;
    }

    public void setModel(SelectModel model) {
        this.model = model;
    }
}
