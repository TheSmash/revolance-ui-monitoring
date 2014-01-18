(function($,W,D){

    $(document).ready(function( )
    {
        $(".del-application").click(function()
        {
            event.preventDefault();
            $.each($("input[type=checkbox]:checked"), function(idx, checkbox) {
                $.ajax({
                    type: "DELETE",
                    url: "/ui-monitoring-server/applications/" + $(checkbox).parent().parent().find(".tag").text(),
                    success: function(result)
                    {
                        $(checkbox).attr("checked", false);
                        $(checkbox).parent().parent().remove();
                    }
                });
            });

            if($("input[type=checkbox]:checked").size()===0)
            {
                $(".del-application").addClass("disabled");
            }

        });

        $(".add-application").click(function(){
            event.preventDefault();
            var link = $("#link")[0];
            $(link).attr("href", "/ui-monitoring-server/applications/declare");
            link.click();
        });

        $("input[type=checkbox]").change(selectApplication);

/*
$.each($("tbody tr"), function(idx, item)
{
    var appId = $(item).find('.tag').text();
    $.getJSON('applications/'+appId+'/status',
        function(result) {

    });
});
*/
    });

    var selectApplication = function()
    {
        event.stopPropagation();
        var checkedBoxCount = $("input[type=checkbox]:checked").size();

        if(checkedBoxCount > 0)
        {
            $(".del-application").removeClass("disabled");
        }
        else
        {
            $(".del-application").addClass("disabled");
        }
    }

})(jQuery, window, document);


