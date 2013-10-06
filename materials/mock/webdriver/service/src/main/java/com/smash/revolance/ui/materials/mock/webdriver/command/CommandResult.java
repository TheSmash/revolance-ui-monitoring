package com.smash.revolance.ui.materials.mock.webdriver.command;

import java.util.Map;

/**
 * User: wsmash
 * Date: 29/09/13
 * Time: 13:30
 */
public class CommandResult
{
    private int                 httpCode;
    private Map<String, Object> payload;

    public CommandResult(Map<String, Object> payload, int httpCode)
    {
        this.payload = payload;
        this.httpCode = httpCode;
    }

    public Map<String, Object> getPayload()
    {
        return payload;
    }

}
