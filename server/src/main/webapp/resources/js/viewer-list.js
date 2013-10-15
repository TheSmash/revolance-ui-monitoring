(function($,W,D){

    $(document).ready(function( )
    {
        $("#application-del").click(function()
        {
            event.preventDefault();
            $.each($("input[type=checkbox]"), function(idx, checkbox) {

                if($(checkbox).is(':checked'))
                {
                    $.ajax({
                        type: "DELETE",
                        url: "application/" + $(item).parent().parent().find(".tag").text(),
                        success: function(result)
                        {
                            $(item).parent().parent().remove();
                        }
                    });
                }
            });

        });
        $("#application-compare").click(function()
        {
            if($(this).parent().attr("href") == "#")
            {
                event.preventDefault();
                var refApp;
                var app;
                $.each($("input[type=checkbox]"), function(idx, checkbox) {

                    if($(checkbox).is(':checked'))
                    {
                        if( refApp === undefined )
                        {
                            refApp = $(checkbox).parent().parent().find(".tag").text();
                        }
                        else
                        {
                            app = $(checkbox).parent().parent().find(".tag").text();
                            return false;
                        }
                    }
                });

                $(this).parent().attr("href", "application/compare/" + refApp + "/" + app);
                $(this).click();
            }
        });
        $("input[type=checkbox]").change(function()
        {
            var checkedBoxCount = $("input[type=checkbox]:checked").size();

            if(checkedBoxCount > 0)
            {
                $("#application-del").attr("disabled", null);
            }
            else
            {
                $("#application-del").attr("disabled", "true");
            }

            if(checkedBoxCount == 2)
            {
                $("#application-compare").attr("disabled", null);
                $.each($("input[type=checkbox]"), function(idx, checkbox) {

                    if(!$(checkbox).is(':checked'))
                    {
                        $(checkbox).attr('disabled', 'true')
                    }
                });
            }
            else
            {
                $("#application-compare").attr("disabled", "true");
                $.each($("input[type=checkbox]"), function(idx, checkbox) {

                    if(!$(checkbox).is(':checked'))
                    {
                        $(checkbox).attr('disabled', null)
                    }
                });
            }
        });
    });

})(jQuery, window, document);


