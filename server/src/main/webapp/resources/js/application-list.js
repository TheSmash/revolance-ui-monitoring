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


