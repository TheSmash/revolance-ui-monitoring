(function($,W,D){

    $(document).ready(function( )
    {

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


    });

})(jQuery, window, document);


