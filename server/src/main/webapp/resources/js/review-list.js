(function($,W,D){

    $(document).ready(function( )
    {

        $("input[type=checkbox]").change(selectCheckbox);

        $("#del-review").click(function()
        {
            event.preventDefault();
            $.each($("input[type=checkbox]"), function(idx, checkbox) {

                if($(checkbox).is(':checked'))
                {
                    $.ajax({
                        type: "DELETE",
                        url: "/ui-monitoring-server/reviews/" + $(checkbox).parent().parent().find(".id").text(),
                        success: function(result)
                        {
                            $(checkbox).parent().parent().remove();
                        }
                    });
                }
            });

            if($("input[type=checkbox]:checked").size()=== 0)
            {
                $("#del-review").addClass("disabled");
            }

        });
        $(".add-review").click(function(){
            event.preventDefault();
            var link = $("#link")[0];
            $(link).attr("href", "/ui-monitoring-server/reviews/declare");
            link.click();
        });
        $(".progress-bar").knob();

    });

    var selectCheckbox = function()
    {
        var checkedBoxCount = $("input[type=checkbox]:checked").size();

        if(checkedBoxCount > 0)
        {
            $(".del-review").removeClass("disabled");
        }
        else
        {
            $(".del-review").addClass("disabled");
        }
    }

})(jQuery, window, document);


