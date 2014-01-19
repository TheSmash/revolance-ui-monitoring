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


