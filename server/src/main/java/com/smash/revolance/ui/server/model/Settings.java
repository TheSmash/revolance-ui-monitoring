package com.smash.revolance.ui.server.model;

import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;

/**
 * User: wsmash
 * Date: 08/06/13
 * Time: 18:27
 */
@Service
public class Settings implements ISettings
{
    @Range(min = 100, max = 500)
    int decoratorWidth = 300;

    @Range(min = 100, max = 500)
    int decoratorHeight = 400;

    public Settings()
    {
    }

    @Override
    public int getDecoratorWidth()
    {
        return decoratorWidth;
    }

    @Override
    public void setDecoratorWidth(int width)
    {
        this.decoratorWidth = width;
    }

    @Override
    public int getDecoratorHeight()
    {
        return decoratorHeight;
    }

    @Override
    public void setDecoratorHeight(int height)
    {
        this.decoratorHeight = height;
    }
}
