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
