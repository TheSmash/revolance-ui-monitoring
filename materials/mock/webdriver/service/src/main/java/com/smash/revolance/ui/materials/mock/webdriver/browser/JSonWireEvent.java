package com.smash.revolance.ui.materials.mock.webdriver.browser;

import java.util.HashMap;
import java.util.Map;

/**
 * User: wsmash
 * Date: 29/09/13
 * Time: 13:38
 */
public class JSonWireEvent
{
    private Map<String, Object> properties = new HashMap<String, Object>();

    public void setProp(String k, Object v)
    {
        properties.put( k, v );
    }

    public Object getProp(String k)
    {
        return properties.get( k );
    }

}
