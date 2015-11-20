package edu.auburn.cardiomri.popupmenu.view;

import java.awt.AWTEvent;

import javax.swing.JSeparator;


/******************************************************************************
 *  MySeparator
 *  For some reason when you add a separator to a JPopupMenu the normal way
 *  It doesn't play nice with the mouselistener and closes the menu for no
 *  reason. So when you need a separator in your menu add this 
 *  <<MENU>>.add(new MySeparator);
 *  http://stackoverflow.com/questions/19570904/mouseexited-event-triggers-when-crossing-an-inner-separator
 * @author Kullen
 *
 *****************************************************************************/
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