package com.smash.revolance.ui.materials.mock.webdriver;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 03/10/13
 * Time: 21:57
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class MockedWebPage
{

    private int h, w;
    private String url     = "";
    private String title   = "";
    private String caption = "";

    @JsonDeserialize(contentAs = MockedWebElement.class, as = ArrayList.class)
    private List<MockedWebElement> content = new ArrayList<MockedWebElement>();


    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    public String getCaption()
    {
        return caption;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public int getH()
    {
        return h;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public int getW()
    {
        return w;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setContent(List<MockedWebElement> content) throws Exception
    {
        this.content = content;
    }

    public List<MockedWebElement> getContent()
    {
        return content;
    }


    public List<String> findElements(String xpath)
    {
        List<String> elements = new ArrayList<String>();

        for ( MockedWebElement e : content )
        {
            elements.add( e.getInternalId() );
        }

        return elements;
    }

    private MockedWebElement getElement(String elementId)
    {
        for ( MockedWebElement e : content )
        {
            if ( e.getInternalId().contentEquals( elementId ) )
            {
                return e;
            }
        }
        return null;
    }

    public Point getElementLocation(String elementId)
    {
        MockedWebElement element = getElement( elementId );
        return new Point( element.getX(), element.getY() );
    }

    public Dimension getElementSize(String elementId)
    {
        MockedWebElement element = getElement( elementId );
        return new Dimension( element.getH(), element.getW() );
    }

    public String getElementTag(String elementId)
    {
        MockedWebElement element = getElement( elementId );
        return element.getTag();
    }

    public String getElementText(String elementId)
    {
        MockedWebElement element = getElement( elementId );
        return element.getText();
    }

    public String getElementAttribute(String elementId, String attr)
    {
        MockedWebElement element = getElement( elementId );
        if ( attr.contentEquals( "class" ) )
        {
            return element.getClazz();
        }
        if ( attr.contentEquals( "href" ) )
        {
            return element.getHref();
        }
        if ( attr.contentEquals( "type" ) )
        {
            return element.getType();
        }
        return "";
    }

    public boolean isElementDisplayed(String elementId)
    {
        return getElement( elementId ).isDisplayed();
    }

    public String getCssAttribute(String elementId, String cssAttribute)
    {
        if ( cssAttribute.contentEquals( "background-image" ) )
        {
            return getElement( elementId ).getBg();
        }
        return "none";
    }
}
