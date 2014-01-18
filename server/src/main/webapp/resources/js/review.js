(function($,W,D){

    var pixelRatio = screen.width/screen.height;

    $.wip = false;

    $.revertChanges = function( button )
    {
        askForConfirmation( 'Work in progress will be permanently lost! Do you want to continue?' );
        if( !$.wip )
        {
            document.location.reload();
        }
        else
        {
            alert('There is no current changes to be canceled');
        }
    };

    $.next = function( link )
    {
        askForConfirmation( 'Work in progress will be permanently lost! Do you want to continue?' );
        if( !$.wip )
        {
            var rowIdx = parseInt($("#dialog").data('row-id'))+1;
            if(rowIdx <= $("#report tr").size() )
            {
                var refPageIdx = $('#report tr[data-id=' + rowIdx + '] .ref-page .page-decorator').attr("data-id");
                var pageIdx = $('#report tr[data-id=' + rowIdx + '] .new-page .page-decorator').attr("data-id");
                var content = buildDialodContent(rowIdx, refPageIdx, pageIdx);
                $('a.simplemodal-close').click();
                $.modal( content );
                defineModalViewPageBaseElementBehavior();
            }
        }
    };

    var askForConfirmation = function( question, updateWip )
    {
        var ans = false;
        if( $.wip )
        {
            if( confirm( question ) )
            {
                if( updateWip === undefined )
                {
                    $.wip = false;
                }
                ans = true;
            }
        }
        return ans;
    }

    $.previous = function( link )
    {
        askForConfirmation( 'Work in progress will be permanently lost! Do you want to continue?' );
        if( !$.wip )
        {
            var rowIdx = parseInt($("#dialog").data('row-id'))-1;
            if(rowIdx >= 0)
            {
                var refPageIdx = $('#report tr[data-id=' + rowIdx + '] .ref-page .page-decorator').attr("data-id");
                var pageIdx = $('#report tr[data-id=' + rowIdx + '] .new-page .page-decorator').attr("data-id");
                var content = buildDialodContent(rowIdx, refPageIdx, pageIdx);
                $('a.simplemodal-close').click();
                $.modal( content );
                defineModalViewPageBaseElementBehavior();
            }
        }
    };

    $.applyChanges = function( button )
    {
        if(!$("#commit-button").hasClass("disabled"))
        {
            if( askForConfirmation( 'Are you sure you want to commit?', false ) )
            {
                var pageId = $(button).data("new-page-id");
                var reviewId = $(button).data("review-id");
                if(pageId !== undefined)
                {
                    $.ajax({
                        type: "POST",
                        url: "/ui-monitoring-server/reviews/"+reviewId+"/pages/"+pageId,
                        data: "",
                        contentType: 'application/json',
                        success: function(result)
                        {
                            alert("Changes have been saved!");
                        },
                        error: function()
                        {
                            alert("Changes could not be saved! Sorry for the inconvenience.");
                        }
                    });
                }
                else
                {
                    $.each($(".page"), function(idx, page){
                        var pageId = $(page).data('id');
                        $.ajax({
                            type: "POST",
                            url: "/ui-monitoring-server/reviews/"+reviewId+"/pages/"+pageId,
                            data: "",
                            contentType: 'application/json',
                            success: function(result)
                            {
                                alert("Changes have been saved!");
                            },
                            error: function()
                            {
                                alert("Changes could not be saved! Sorry for the inconvenience.");
                            }
                        });
                    });
                }
            }
            else
            {
                alert('There is no changes to be commited');
            }
        }
    };

    var defineModalViewPageBaseElementBehavior = function()
    {
        $(".page-element").hover(function(){
            $(".page-element[data-id="+$(this).data("match-element-id")+"]").addClass("hover");
        },
        function(){
            $(".page-element[data-id="+$(this).data("match-element-id")+"]").removeClass("hover");
        });

        $(".page-decorator").each(function()
        {
            //$(this).parent().css("float", "left");

            var w = $(this).parent().width()/2-40;
            var wReal = $(this).find(".page").attr("data-w");

            var xScale = w / wReal;
            var yScale = pixelRatio * xScale;

            $(this).css("width", $(this).find(".page").data("w") * xScale + "px");
            $(this).css("height", $(this).find(".page").data("h") * yScale + "px");

        });
        $(".page-element").each(function()
        {
            definePageElement( $(this) );
        });
        $(".ref .page-element, .new .page-element[data-diff-type=ADDED], .new .page-element[data-diff-type=BASE]").each(function()
        {
            definePageElementTooltip( $(this) );
        });
        $(".new .page-element[data-diff-type=CHANGED]").each(function()
        {
            definePageChangedElementTooltip( $(this) );
        });
        $(".page").each(function()
        {
            $(this).css("background-image", "url('" + $(this).attr('data-caption') + "')" );
            $(this).css("background-repeat", "no-repeat");
            $(this).css("background-position", "center");
            $(this).css("background-size", "100% 100%");
        });
        $("#page-mark").html( parseInt($('#dialog').attr('data-row-id'))+1 + ' / ' + parseInt($("#report tr").size()-1) );
        updateElementCount();
    };

    var listAllElements = function()
    {
        var selectors = [".new .page-element[data-diff-type=ADDED]", ".ref .page-element[data-diff-type=DELETED]", ".new .page-element[data-diff-type=CHANGED"];
        var elements = new Array();
        $.each(selectors, function(idx, selector)
        {
            $.each($(selector), function(idx, element)
            {
                elements.push(element);
            });
        });
        return elements;
    }

    var updateElementCount = function()
    {
        var differencyCount = listAllElements().length;
        if(differencyCount == 0)
        {
            $("#commit-button").addClass("disabled");
        }
        var comment = differencyCount+' ';
        if(differencyCount === 1)
        {
            comment += "differency";
        }
        else
        {
            comment += "differencies"
        }
        $("#differency-count").html( comment );
    }

    var definePageElement = function( element )
    {
        var page = $(element).parent();
        var wReal = page.attr("data-w");
        var hReal = page.attr("data-h");

        var pageDecorator = page.parent();

        var w = pageDecorator.width()
        var h = pageDecorator.height()

        var xScale = w / wReal;
        var yScale = h / hReal;

        $(element).css("top",  $(element).data("y") * yScale + (-1) + "px");
        $(element).css("left", $(element).data("x") * xScale + (-1) + "px");
        $(element).css("width", $(element).data("w") * xScale + "px");
        $(element).css("height", $(element).data("h") * yScale + "px");

        $(element).css("background-image", "url('" + $(element).attr('data-caption') + "')");
        $(element).css("background-repeat", "no-repeat");
        $(element).css("background-position", "center");
        $(element).css("background-size", "100% 100%");
    }

    var buildContentChange = function()
    {
        return '<span class="label label-important content">content</span>';
    }

    var buildPosChange = function()
    {
        return '<span class="label label-important pos">pos</span>';
    }

    var buildTargetChange = function()
    {
        return '<span class="label label-warning target">target</span>';
    }

    var buildLookChange = function()
    {
        return '<span class="label label-warning look">look</span>';
    }

    var definePageChangedElementTooltip = function(element)
    {
        var app = $("#app").text();
        var refApp = $("#ref-app").text();

        var elementId = $(element).data('id')
        var pageId = $(element).parent().data('id');
        var refElement = $(".page-element[data-match-element-id="+elementId+"]");
        var refElementId = $(refElement).data('id');
        var refPageId = $(refElement).parent().data('id');

        var refElementTooltip = buildTooltip(refElement, 'Reference element details');
        var newElementTooltip = buildTooltip(element, 'New element details');
        var tooltip = new Array();


        $.getJSON( "/ui-monitoring-server/compare/" + refApp + "/" + app + "/" + refPageId + "/" + pageId + "/" + refElementId + "/" + elementId, function( data ) {
            $.each(data, function(idx, change){
                tooltip.push('<div class="comparison">');

                if(change === 'POS')
                {
                    tooltip.push(buildPosChange())
                }
                if(change === 'CONTENT')
                {
                    tooltip.push(buildContentChange())
                }
                if(change === 'TARGET')
                {
                    tooltip.push(buildTargetChange())
                }
                if(change === 'LOOK')
                {
                    tooltip.push(buildLookChange())
                }

                tooltip.push('<br />');
                tooltip.push('<br />');

                tooltip.push('<a class="btn btn-info" onclick=$.confirmChanges("'+$(element).data('id')+'")>Mark as reviewed</a>');
                tooltip.push('</div>');

                $(element).data('powertipjq', $(tooltip.join('\n')));
                $(element).powerTip({
                    placement: 's',
                    smartPlacement: true,
                    mouseOnToPopup: true
                });
            });
        });
    }

    $.confirmAddition = function(elementId)
    {
        event.preventDefault();
        var element = $(".new .page-element[data-id="+elementId+"]")[0];
        $.powerTip.hide();
        $(element).attr("data-diff-type", "BASE");
        $.wip = true;
        $("#revert-button").removeClass("disabled");
        updateElementCount();
        definePageElementTooltip(element);
    }

    $.confirmDeletion = function(elementId)
    {
        event.preventDefault();
        var element = $(".ref .page-element[data-id="+elementId+"]")[0];
        $.powerTip.hide();
        $(element).attr("data-diff-type", "BASE");
        $.wip = true;
        $("#revert-button").removeClass("disabled");
        updateElementCount();
        definePageElementTooltip(element);
    }

    $.confirmChanges = function(elementId)
    {
        event.preventDefault();
        var element = $(".new .page-element[data-id="+elementId+"]")[0];
        $.powerTip.hide();
        $(element).attr("data-diff-type", "BASE");
        $(element).find(".comparisons").remove();
        $.wip = true;
        $("#revert-button").removeClass("disabled");
        updateElementCount();
        definePageElementTooltip(element);
    }

    $.confirmAllChanges = function(button)
    {
        event.preventDefault();
        if(!$("#confirm-all-button").hasClass("disabled"))
        {
            var elements = listAllElements();
            $.each(elements, function(idx, element)
            {
                $(element).attr("data-diff-type", "BASE");

                updateElementCount();
                definePageElementTooltip(element);
                $(element).find(".comparisons").remove();
            });

            if(elements.length>0)
            {
                $.wip = true;
                $("#commit-button").removeClass("disabled");
                $("#revert-button").removeClass("disabled");
            }
        }
    }

    var buildTooltip = function(element, title)
    {
        var x = parseInt($(element).data('x'))+'px';
        var y = parseInt($(element).data('y'))+'px';
        var h = parseInt($(element).data('h'))+'px';
        var w = parseInt($(element).data('w'))+'px';
        var impl = $(element).data('impl');
        var href = $(element).data('href');
        var bg = $(element).data('bg');

        var tooltip = new Array();

        if(title === undefined)
            tooltip.push('<p><b>Element details:</b></p>');
        else
            tooltip.push('<p><b>'+title+'</b></p>');

        tooltip.push('<p>pos: {x: '+x+', y: '+y+'}</p>');
        tooltip.push('<p>dim: {w: '+w+', h: '+h+'}</p>');
        if(impl !== undefined && impl !== '')
        {
            tooltip.push('<p>impl: '+impl+'</p>');
        }
        if(href !== undefined && href !== '')
        {
            tooltip.push('<p>href: '+href+'</p>');
        }
        if(bg !== undefined && bg !== '')
        {
            tooltip.push('<p>bg: '+bg+'</p>');
        }

        return tooltip;
    }

    var buildAdditionTooltip = function(element)
    {
        var tooltip = buildTooltip(element);
        tooltip.push('<a class="btn btn-warning" onclick=$.confirmAddition("'+$(element).data('id')+'")>Mark as reviewed</a>');
        return tooltip;
    }

    var buildDeletionTooltip = function(element)
    {
        var tooltip = buildTooltip(element);
        tooltip.push('<a class="btn btn-danger" onclick=$.confirmDeletion("'+$(element).data('id')+'")>Mark as reviewed</a>');
        return tooltip;
    }

    var definePageElementTooltip = function( element )
    {
        var x = $(element).data('x')+'px';
        var y = $(element).data('y')+'px';
        var h = $(element).data('h')+'px';
        var w = $(element).data('w')+'px';

        var diffType = $(element).attr('data-diff-type');

        if(diffType === 'DELETED')
        {
            $(element).data('powertipjq', $(buildDeletionTooltip(element).join('\n')));
        }
        if(diffType === 'ADDED')
        {
            $(element).data('powertipjq', $(buildAdditionTooltip(element).join('\n')));
        }
        if(diffType === 'BASE' || diffType === 'CHANGED')
        {
            $(element).data('powertipjq', $(buildTooltip(element).join('\n')));
        }

        $(element).powerTip({
            placement: 's',
            smartPlacement: true,
            mouseOnToPopup: true
        });
    }

    var definePageTooltips = function()
    {
        $.each($('.page-decorator[data-page-reviewed=false] .page'), function(idx, page){
            buildPageTooltip(page);
        });
    }

    var buildPageTooltip = function(page)
    {
        var tooltip = '<a class="btn btn-warning" onclick=$.markPageAsReviewed("'+$(page).data('id')+'")>Mark as reviewed</a>';

        $(page).parent().data('powertipjq', $(tooltip));
        $(page).parent().powerTip({
            placement: 's',
            smartPlacement: true,
            mouseOnToPopup: true
        });
    }

    $.markPageAsReviewed = function(pageId)
    {
        event.preventDefault();
        $.powerTip.hide();
        $.powerTip.destroy($(".page[data-id="+pageId+"]").parent());
        $.wip = true;
        $(".page[data-id="+pageId+"]").parent().attr("data-page-reviewed", "true");
        updatePagesCount();
    }

    var updatePagesCount = function()
    {
        var pagesCount = parseInt($("#pages-count").text())-1;
        var comment = ' pages';
        if(pagesCount===1)
        {
            comment = ' page';
        }
        if(pagesCount===0)
        {
            $("#commit-button").removeClass('disabled');
        }
        return $(".pages-count").html('<h4><span id="pages-count">'+pagesCount+'</span>'+comment+'</h4>');
    }

    $(document).ready(function( )
    {
        if(!document.URL.indexOf('reviewId=')>0)
        {
            $.reviewId = document.URL.split('reviewId=')[1];
        }
        if($($(".page-title h2")[0]).text()==='Detailed application comparison')
        {
            defineModalViewPageBaseElementBehavior();
        }
        else if($($(".page-title h2")[0]).text().indexOf('details')>0)
        {
            definePageTooltips();
        }

    });

})(jQuery, window, document);
