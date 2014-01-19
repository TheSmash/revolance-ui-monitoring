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
(function($,W,D)
{
    $.getTags = function()
    {
        var tags = Array();
        $.each($('.cloud-tag .label'), function(idx, tag)
        {
            if( $.inArray( $(tag).text(), tags ) == -1 )
            {
                tags.push( $(tag).text() );
            }
        });
        return tags;
    }

    var filterContent = function( filter )
    {
                var tr;
        var match;
        var matchCount = 0;
        var options = filter.split("+");

        // reverseOptionSelection( option );
        $.each($("#report tr"), function(idx, tr)
        {
            if( idx > 0 )
            {
                $.each( $(tr).find(".label"), function(labelIdx, label)
                {
                    match = false;
                    matchCount = 0;
                    $.each( options, function(optIdx, option)
                    {
                        if( option !== "" )
                        {
                            if( $(label).text().toLowerCase().indexOf( option.trim() ) >= 0 )
                            {
                                matchCount += 1;
                            }
                        }
                        else
                        {
                            matchCount += 1;
                        }

                        if( matchCount == options.length )
                        {
                            match = true;
                            return false;
                        }
                    });
                    if( matchCount == options.length )
                    {
                        match = true;
                        return false;
                    }
                });
                if( match )
                {
                    $(tr).css("display", "block");
                }
                else
                {
                    $(tr).css("display", "none");
                }
            }
        });
    };

    $(document).ready(function()
    {
        $("#search-submit").click(function()
        {
            console.log('Search submit');
        });
        $("#search-input").keyup(function(event)
        {
            if ( event.which == 13 )
            {
                event.preventDefault();
            }
            else
            {
                var query = $(this).val();
                filterContent( query );
            }
        });
    });



})(jQuery, window, document);
