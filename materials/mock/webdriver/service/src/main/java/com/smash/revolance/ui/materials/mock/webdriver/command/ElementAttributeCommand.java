package com.smash.revolance.ui.materials.mock.webdriver.command;

/**
 * User: wsmash
 * Date: 01/10/13
 * Time: 23:39
 */
public class ElementAttributeCommand extends ElementCommand
{
    private String attribute;

    public ElementAttributeCommand(String sessionId, String cmd, String method, String payload, String elementId, String attr)
    {
        super( sessionId, cmd, method, payload, elementId );
        setAttribute( attr );
    }

    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }

    public String getAttribute()
    {
        return attribute;
    }
}
