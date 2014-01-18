package java.com.smash.revolance.ui.itest;

/**
 * Created by ebour on 13/01/14.
 */
public class RestUtil
{
    private final String url;

    public RestUtil(String host, String port)
    {
        this.url = "http://"+host+":"+port;
    }

    public static String buildApplicationRequestBody(String tag, String domain, String page, String resolution, String browserType, boolean followLinks, boolean followButtons)
    {
        String body = "";

        body += "tag="+tag;
        body += "&domain="+domain;
        body += "&page="+page;
        body += "&resolution"+resolution;
        body += "&browserType"+browserType;
        if(followLinks)
        {
            body += "&followLinks=on";
        }
        if(followButtons)
        {
            body += "&followButtons=on";
        }

        return body;
    }



}
