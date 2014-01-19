/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ui-monitoring-server
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2014 RevoLance
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
(function($,W,D){

    var pixelRatio = screen.width/screen.height;

    $.previousVariant = function( element )
    {
        var tr = $(element).parent().parent().parent();
        var variantIdx = parseInt( $(tr).attr('data-variant-idx') ) -1;

        var refPage = $(tr).find('.ref-page .page-decorator').data('id');
        var newPage = $(tr).find('.new-page .page-decorator').data('id');
        var refPageVariant = $.getVariant( refPage, variantIdx );
        var newPageVariant = $.getVariant( newPage, variantIdx );
        $(tr).find('.ref-page').html( buildPage( refPageVariant ) );
        $(tr).find('.new-page').html( buildPage( newPageVariant ) );

        $(tr).data('variant-idx', variantIdx);

        definePageBehavior( );
        definePageElementBehavior( );
        definePageDecoratorBehavior( );
        defineTooltipBehavior( );
    }

    $.nextVariant = function( element )
    {
        var tr = $(element).parent().parent().parent();
        var variantIdx = parseInt( $(tr).attr('data-variant-idx') ) +1;

        var refPage = $(tr).find('.ref-page .page-decorator').attr('data-id');
        var newPage = $(tr).find('.new-page .page-decorator').attr('data-id');

        var refPageVariant = $.getVariant( refPage, variantIdx );
        var newPageVariant = $.getVariant( newPage, variantIdx );
        $(tr).find('.ref-page').html( buildPage( refPageVariant ) );
        $(tr).find('.new-page').html( buildPage( newPageVariant ) );

        $(tr).attr('data-variant-idx', variantIdx);

        definePageBehavior( );
        definePageElementBehavior( );
        definePageDecoratorBehavior( );
        defineTooltipBehavior( );
    }

    $.getVariant = function( pageId, variantIdx, ref)
    {
        var variant = undefined;
        var page = $.getPageById( pageId, ref );
        if( page !== undefined )
        {
            if( page.variants !== undefined )
            {
                if( page.variants.length>0 && page.variants.length>=variantIdx)
                {
                    variant = page.variants[variantIdx-1];
                }
            }
        }
        return variant;
    }

    var defineSearchTags = function( )
    {
        var options = '';
        $.each($.getTags(), function(idx, tag)
        {
            options += '<option value="' + tag + '">';
        });
        $("#tags").html( options );
    }

    var definePageBehavior = function()
    {
        $(".page").each(function(idx, page)
        {
            var w = $(this).width();
            var wReal = $(this).data("w");
            var hReal = $(this).data("h");

            var xScale = w/wReal;

            var h = pixelRatio * xScale * hReal;
            var yScale = h/hReal;


            $(page).css("width", $(page).data("w") * xScale + "px");
            $(page).css("height", $(page).data("h") * yScale + "px");
            $(page).parent().css("width", $(page).data("w") * xScale + "px");
            $(page).parent().css("height", $(page).data("h") * yScale + "px");

            $(page).css("background-image", "url('" + $(page).data('caption') + "')" );
            $(page).css("background-repeat", "no-repeat");
            $(page).css("background-size", "100% 100%");

            $(this).find(".page-element").each(function(idx, element){

                $(element).css("top",  $(element).data("y") * yScale + (-1) + "px");
                $(element).css("left", $(element).data("x") * xScale + (-1) + "px");
                $(element).css("width", $(element).data("w") * xScale + "px");
                $(element).css("height", $(element).data("h") * yScale + "px");

                $(element).css("background-image", "url('" + $(element).data('caption') + "')");
                $(element).css("background-repeat", "no-repeat");
                $(element).css("background-position", "center");
                $(element).css("background-size", "100% 100%");
            });
        });
    };

    $.getPage = function( pageId )
    {
        return $('.page-decorator[data-id="' + pageId + '"] > .page ').get( 0 );
    };

    var del = function(item, array)
    {
        var idx = array.indexOf( item );
        if(idx != -1)
        {
            array.splice(idx, 1);
        }
    };

    $(document).ready(function( )
    {
        definePageBehavior();
    });

})(jQuery, window, document);
