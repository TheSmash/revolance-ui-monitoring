package com.smash.revolance.ui.materials.mock.webdriver.command;

import com.smash.revolance.ui.materials.JsonHelper;
import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: wsmash
 * Date: 29/09/13
 * Time: 11:10
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class Command
{
    private String cmd;
    private String method;
    private String sessionId;

    private Map<String, Object> payload = new HashMap<String, Object>();

    public Command(String sessionId, String cmd, String method)
    {
        this.sessionId = sessionId;
        this.method = method.toLowerCase();
        this.cmd = cmd;
    }

    public Command(String sessionId, String cmd, String method, Map<String, Object> payload)
    {
        this( sessionId, cmd, method );
        setPayload( payload );
    }

    public Command(String sessionId, String cmd, String method, String jsonPayload)
    {
        this( sessionId, cmd, method );
        setPayload( jsonPayload );
    }

    private void setPayload(String jsonPayload)
    {
        Map<String, Object> payload = null;

        if ( jsonPayload.isEmpty() )
        {
            payload = new HashMap<String, Object>();
        } else
        {
            try
            {
                payload = (Map<String, Object>) JsonHelper.getInstance().map( HashMap.class, jsonPayload );
            }
            catch (IOException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        setPayload( payload );
    }

    public void setPayload(Map<String, Object> payload)
    {
        this.payload = payload;
    }

    public Map<String, Object> getPayload()
    {
        return payload;
    }

    public String getName()
    {
        return cmd.toString();
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public String getMethod()
    {
        return method;
    }
}
