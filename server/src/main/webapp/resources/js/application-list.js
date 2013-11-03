(function($,W,D){

    $.refApp = undefined,
    $.app = undefined,

    $(document).ready(function( )
    {
        $("#del").click(function()
        {
            event.preventDefault();
            $.each($("input[type=checkbox]"), function(idx, checkbox) {

                if($(checkbox).is(':checked'))
                {
                    $.ajax({
                        type: "DELETE",
                        url: "applications/" + $(checkbox).parent().parent().find(".tag").text(),
                        success: function(result)
                        {
                            $(checkbox).parent().parent().remove();
                        }
                    });
                }
            });

            if($(".tag").length === 0)
            {
                $("#del").css("disabled", "true");
            }

        });

        $("#application-compare").click(function(){
            if($(this).parent().attr("href") == "#")
            {
                $(this).parent().attr("href", "applications/compare/" + $.refApp + "/" + $.app);
                $(this).parent().click();
            }
        });

        $("input[type=checkbox]").change(function()
        {
            event.preventDefault();
            var checkedBoxCount = $("input[type=checkbox]:checked").size();

            if(checkedBoxCount > 0)
            {
                $("#del").attr("disabled", null);
            }
            else
            {
                $("#del").attr("disabled", "true");
                $.refApp = undefined;
                $.app = undefined;
            }

            if(checkedBoxCount == 1)
            {
                $("#application-compare").attr("disabled", "true");
                $.each($("input[type=checkbox]:checked"), function(idx, checkbox) {
                    $.refApp = $(checkbox).parent().parent().find(".tag").text();
                });
            }
            else if(checkedBoxCount == 2)
            {
                $("#application-compare").attr("disabled", null);
                $.each($("input[type=checkbox]:checked"), function(idx, checkbox) {
                    var tag = $(checkbox).parent().parent().find(".tag").text();
                    if($.refApp !== tag){
                        $.app =tag;
                    }
                });
            }
            else
            {
               $("#application-compare").attr("disabled", "true");
            }
        });
    });

})(jQuery, window, document);


