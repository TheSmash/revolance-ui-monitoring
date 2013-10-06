package com.smash.revolance.ui.model.helper;

/*
        This file is part of Revolance UI Suite.

        Revolance UI Suite is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        Revolance UI Suite is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with Revolance UI Suite.  If not, see <http://www.gnu.org/licenses/>.
*/

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: wsmash
 * Date: 27/01/13
 * Time: 09:49
 */
public class ImageHelper
{
    private static final Map<File, Map<File, Boolean>> cache            = new HashMap<File, Map<File, Boolean>>();
    public static final  String                        BASE64_IMAGE_PNG = "data:image/png;base64,";


    public static BufferedImage cropImage(final BufferedImage img, int x, int y, int w, int h, double xScale, double yScale)
    {
        x = (int) ( x * xScale );
        w = (int) ( w * xScale );

        y = (int) ( y * yScale );
        h = (int) ( h * yScale );


        if ( img.getHeight() < ( y + h ) )
        {
            h = img.getHeight() - y;
        }
        if ( img.getWidth() < ( x + w ) )
        {
            w = img.getWidth() - x;
        }

        return img.getSubimage( x, y, w, h );
    }

    public static BufferedImage eraseRect(BufferedImage image, int x, int y, int w, int h, double xScale, double yScale)
    {
        x = (int) ( x * xScale );
        w = (int) ( w * xScale );

        y = (int) ( y * yScale );
        h = (int) ( h * yScale );

        Graphics2D g = image.createGraphics();
        g.setColor( new Color( 0, 0, 0, 0 ) );
        g.fillRect( x, y, w, h );
        g.dispose();
        return image;
    }

    public static BufferedImage scaleImage(BufferedImage image, double widthPerCent, double heightPerCent)
    {
        int w = (int) ( image.getWidth() * widthPerCent );
        int h = (int) ( image.getHeight() * heightPerCent );
        int t = image.getType();

        Image scaledImage = image.getScaledInstance( w, h, Image.SCALE_SMOOTH );

        BufferedImage newImg = new BufferedImage( w, h, t );
        newImg.getGraphics().drawImage( scaledImage, 0, 0, null );

        return newImg;
    }

    public static String encodeToString(BufferedImage image) throws IOException
    {
        String imageString = "";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try
        {
            Base64 encoder = new Base64();

            ImageIO.write( image, "png", bos );
            byte[] bytes = bos.toByteArray();

            imageString = BASE64_IMAGE_PNG + encoder.encodeToString( bytes );

        }
        finally
        {
            IOUtils.closeQuietly( bos );
        }
        return imageString;
    }


    public static BufferedImage decodeToImage(String imageString) throws Exception
    {
        imageString = imageString.split( BASE64_IMAGE_PNG )[1];
        BufferedImage image = null;
        byte[] imageByte;

        Base64 decoder = new Base64();
        imageByte = decoder.decode( imageString );
        ByteArrayInputStream bis = new ByteArrayInputStream( imageByte );
        try
        {
            image = ImageIO.read( bis );
        }
        finally
        {
            IOUtils.closeQuietly( bis );
        }

        return image;
    }

    public static boolean imgEquals(String imgRef, String img) throws IOException
    {
        File imgRefFile = new File( imgRef );
        File imgFile = new File( img );
        if ( imgRefFile.exists() && imgFile.exists() )
        {
            return imgEquals( imgRefFile, imgFile );
        } else
        {
            return true;
        }
    }

    public static boolean imgEquals(File imgRef, File img) throws IOException
    {
        if ( isCached( imgRef, img ) )
        {
            return getComparisons( imgRef ).get( img ).booleanValue();
        } else if ( isCached( img, imgRef ) )
        {
            return getComparisons( img ).get( imgRef ).booleanValue();
        } else
        {
            BufferedImage refBI = ImageIO.read( imgRef );
            BufferedImage imgBi = ImageIO.read( img );

            int width = imgBi.getWidth();
            int height = imgBi.getHeight();

            if ( refBI.getWidth() == width && refBI.getHeight() == height )
            {
                for ( int x = 0; x < width; x++ )
                {
                    for ( int y = 0; y < height; y++ )
                    {
                        int refIntensity = refBI.getRGB( x, y );
                        int imgIntensity = imgBi.getRGB( x, y );
                        if ( refIntensity != imgIntensity )
                        {
                            getComparisons( imgRef ).put( img, false );
                            return false;
                        }
                    }
                }
            }

            getComparisons( imgRef ).put( img, true );
            return true;
        }
    }

    /**
     * diff = (img2.rgb(x,y) != img1.rgb(x,y)) ? img1.rgb() : 0
     *
     * @param img1
     * @param img2
     * @return
     * @throws IOException
     */
    public static BufferedImage diffImg(BufferedImage img1, BufferedImage img2) throws IOException
    {
        int w = img1.getWidth();
        int h = img1.getHeight();
        int t = img1.getType();

        BufferedImage diff = new BufferedImage( w, h, t );

        if ( img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight() )
        {
            for ( int x = 0; x < w; x++ )
            {
                for ( int y = 0; y < h; y++ )
                {
                    int refIntensity = img1.getRGB( x, y );
                    int imgIntensity = img2.getRGB( x, y );
                    if ( refIntensity != imgIntensity )
                    {
                        diff.setRGB( x, y, imgIntensity );
                    } else
                    {
                        diff.setRGB( x, y, 0 );
                    }
                }
            }
        }

        return diff;
    }

    private static boolean isCached(File imgRef, File img)
    {
        if ( cache.containsKey( imgRef ) )
        {
            Map<File, Boolean> comparisons = cache.get( imgRef );
            if ( comparisons.containsKey( img ) )
            {
                return true;
            }
        }
        return false;
    }

    private static Map<File, Boolean> getComparisons(File imgRef)
    {
        Map<File, Boolean> comparisons;
        if ( cache.containsKey( imgRef ) )
        {
            comparisons = cache.get( imgRef );
        } else
        {
            comparisons = new HashMap<File, Boolean>();
            cache.put( imgRef, comparisons );
        }
        return comparisons;
    }

}
