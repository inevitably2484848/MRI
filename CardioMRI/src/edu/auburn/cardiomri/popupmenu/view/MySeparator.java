package edu.auburn.cardiomri.popupmenu.view;

import java.awt.AWTEvent;

import javax.swing.JSeparator;

public class MySeparator extends JSeparator{
	
	public MySeparator( )
    {
        super( JSeparator.HORIZONTAL );
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    /**
     * Returns the name of the L&F class that renders this component.
     *
     * @return the string "PopupMenuSeparatorUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    public String getUIClassID()
    {
        return "PopupMenuSeparatorUI";

    }

}
