package com.smash.revolance.ui.materials.mock.webdriver.command;


/**
 * User: wsmash
 * Date: 01/10/13
 * Time: 23:32
 */
public class ElementCommand extends Command
{
    private String elementId;

    public ElementCommand(String sessionId, String cmd, String method, String payload, String elementId)
    {
        super( sessionId, cmd, method, payload );
        setElementId( elementId );
    }

    public void setElementId(String elementId)
    {
        this.elementId = elementId.substring( elementId.indexOf( "{" ) + 1, elementId.lastIndexOf( "}" ) );
    }

    public String getElementId()
    {
        return elementId;
    }
}
