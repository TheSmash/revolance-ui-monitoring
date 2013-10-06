package com.smash.revolance.ui.materials.mock.webdriver.command;

/**
 * User: wsmash
 * Date: 02/10/13
 * Time: 00:14
 */
public class ElementCSSCommand extends ElementCommand
{
    private String cssProperty;

    public ElementCSSCommand(String sessionId, String path, String method, String payload, String elementId, String cssProperty)
    {
        super( sessionId, path, method, payload, elementId );
        setCssProperty( cssProperty );
    }

    public String getCssProperty()
    {
        return cssProperty;
    }

    public void setCssProperty(String cssProperty)
    {
        this.cssProperty = cssProperty;
    }

}
