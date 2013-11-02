(function($,W,D){

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
                        url: "explorations/" + $(checkbox).parent().parent().find(".tag").text(),
                        success: function(result)
                        {
                            $(item).parent().parent().remove();
                        }
                    });
                }
            });

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
        });


    });

})(jQuery, window, document);


