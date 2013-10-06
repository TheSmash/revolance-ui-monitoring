(function($,W,D){

    $.selectCount = 0;
    $.checkboxes = Array();

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
        $("#application-del").click(function()
        {
            event.preventDefault();
            $.each($.checkboxes, function(idx, item)
            {
                $.ajax({
                    type: "DELETE",
                    url: "application/" + $(item).parent().parent().find(".tag").text(),
                    success: function(result)
                    {
                        $(item).parent().parent().remove();
                    }
                });
            });

        });
        $("#application-compare").click(function()
        {
            if($(this).parent().attr("href") == "#")
            {
                event.preventDefault();
                var refApp;
                var app;
                $.each($.checkboxes, function(idx, item)
                {
                    if( refApp === undefined )
                    {
                        refApp = $(item).parent().parent().find(".tag").text();
                    }
                    else
                    {
                        app = $(item).parent().parent().find(".tag").text();
                        return false;
                    }
                });

                $(this).parent().attr("href", "application/compare/" + refApp + "/" + app);
                $(this).click();
            }
        });
        $("input[type=checkbox]").change(function()
        {
            if($(this).is(':checked'))
            {
                $.checkboxes.push( $(this) );
                $.selectCount += 1;
            }
            else
            {
                del($(this), $.checkbox);
                $.selectCount -= 1;
            }

            if($.selectCount > 0)
            {
                $("#application-del").attr("disabled", null);
            }
            else
            {
                $("#application-del").attr("disabled", "true");
            }

            if( $.selectCount == 2 )
            {
                $("#application-compare").attr("disabled", null);
            }
            else
            {
                $("#application-compare").attr("disabled", "true");
            }
        });
    });

})(jQuery, window, document);


