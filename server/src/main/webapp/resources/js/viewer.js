(function($,W,D){

    $.wip = false;

    var pixelRatio = screen.width/screen.height;

    var definePageElementBehavior = function()
    {
        $(".page > .page-element").each(function()
        {
            var page = $(this).parent().eq(0);

            var w = $(page).parent().width();
            var h = $(page).parent().height();

            var wReal = page.attr("data-w");
            var hReal = page.attr("data-h");

            var xScale = w/wReal;
            var yScale = h/hReal;

            $(this).css("top",  $(this).attr("data-y") * yScale + (-1) + "px");
            $(this).css("left", $(this).attr("data-x") * xScale + (-1) + "px");
            $(this).css("width", $(this).attr("data-w") * xScale + "px");
            $(this).css("height", $(this).attr("data-h") * yScale + "px");

            $(this).css("background-image", "url('" + $(this).attr('data-caption') + "')");
            $(this).css("background-repeat", "no-repeat");
            $(this).css("background-position", "center");
            $(this).css("background-size", "100% 100%");
        });
    };

    $.definePageElementBehavior = function()
    {
        definePageElementBehavior();
    }

    var buildElementTooltip = function( comparison )
    {
        /*
        var refPageId = "";
        if(refPage !== undefined)
        {
            refPageId = refPage.id;
        }
        var pageId = "";
        if(page !== undefined)
        {
            pageId = page.id;
        }
        var refElementId = "";
        if(refElement !== undefined)
        {
            refElementId = refElement.internalId;
        }
        var elementId = "";
        if(element !== undefined)
        {
            elementId = element.internalId;
        }
        */

        var tooltip = '';
        if( comparison.pageElementDiffTypes.length == 0 )
        {
            return tooltip;
        }
        else
        {
            tooltip += '<form class="element-diff-form form-horizontal">';
            // tooltip += buildSwitchRawModeButton( refPageId, refElementId, pageId, elementId );
            // tooltip += '<input type="hidden" data-ref-page-ref="' + refPageId + '" data-element-ref-id="' + refElementId + '" data-new-page-id="' + pageId + '" data-element-id="' + elementId + '" />';
            tooltip += "<fieldset>";

            if(comparison.diffType === "BASE")
            {

                tooltip += buildBadge( "label-important target", "target", $.inArray("TARGET", comparison.pageElementDiffTypes) >= 0 );
                tooltip += buildBadge( "label-important impl", "impl", $.inArray("IMPL", comparison.pageElementDiffTypes) >= 0 );
                tooltip += buildBadge( "label-warning value", "value", $.inArray("VALUE", comparison.pageElementDiffTypes) >= 0 );
                tooltip += buildBadge( "label-warning position", "position", $.inArray("POS", comparison.pageElementDiffTypes) >= 0 );
                tooltip += buildBadge( "label-info look", "look", $.inArray("LOOK", comparison.pageElementDiffTypes) >= 0 );

                // tooltip += buildSubmit( "apply", "Confirm element changes" );
            }
            else if( comparison.diffType === "ADDED" )
            {
                tooltip += buildBadge( "label-info added", "ADDED", true );
                // tooltip += buildSubmit( "add", "Confirm element creation" );
            }
            else if( comparison.diffType === "DELETED" )
            {
                tooltip += buildBadge( "label-important deleted", "DELETED", true );
                // tooltip += buildSubmit( "del", "Confirm element deletion" );
            }

            tooltip += "</fieldset>";
            tooltip += '</form>';
            /*
            tooltip += '<form class="element-data-form form-horizontal" style="display: none;">';
            tooltip += '<h4>Raw element</h4>';
            tooltip += '<fieldset>';
            tooltip += getJSON( refElement );
            tooltip += '</fieldset>';
            tooltip += '</form>';
            */
            return '<span class="smallipopHint">' + tooltip + '</span>';
        }
    };

    $.previousVariant = function( element )
    {
        var tr = $(element).parent().parent().parent();
        var variantIdx = parseInt( $(tr).attr('data-variant-idx') ) -1;

        var refPage = $(tr).find('.ref-page .page-decorator').attr('data-id');
        var newPage = $(tr).find('.new-page .page-decorator').attr('data-id');
        var refPageVariant = $.getVariant( refPage, variantIdx );
        var newPageVariant = $.getVariant( newPage, variantIdx );
        $(tr).find('.ref-page').html( buildPage( refPageVariant ) );
        $(tr).find('.new-page').html( buildPage( newPageVariant ) );

        $(tr).attr('data-variant-idx', variantIdx);

        definePageBehavior( );
        definePageElementBehavior( );
        definePageDecoratorBehavior( );
        defineTooltipBehavior( );
    }

    $.nextVariant = function( element )
    {
        var tr = $(element).parent().parent().parent();
        var variantIdx = parseInt( $(tr).attr('data-variant-idx') ) +1;

        var refPage = $(tr).find('.ref-page .page-decorator').attr('data-id');
        var newPage = $(tr).find('.new-page .page-decorator').attr('data-id');

        var refPageVariant = $.getVariant( refPage, variantIdx );
        var newPageVariant = $.getVariant( newPage, variantIdx );
        $(tr).find('.ref-page').html( buildPage( refPageVariant ) );
        $(tr).find('.new-page').html( buildPage( newPageVariant ) );

        $(tr).attr('data-variant-idx', variantIdx);

        definePageBehavior( );
        definePageElementBehavior( );
        definePageDecoratorBehavior( );
        defineTooltipBehavior( );
    }

    $.getVariant = function( pageId, variantIdx, ref)
    {
        var variant = undefined;
        var page = $.getPageById( pageId, ref );
        if( page !== undefined )
        {
            if( page.variants !== undefined )
            {
                if( page.variants.length>0 && page.variants.length>=variantIdx)
                {
                    variant = page.variants[variantIdx-1];
                }
            }
        }
        return variant;
    }

    var buildPageVariant = function( idx, variant )
    {
        return buildPage( variant, "variant", "", "", "" );
    }

    $.activeSaveMode = function( link )
    {
        var saveButton = '<a href="#" onclick="$.activeEditMode( this );"><i class="icon-ok icon-white"></i> [save]</a>';
        var cancelButton = '<a href="#" class="cancel" data-element-json="' + $(link).parent().parent().parent().find(".element-data").val() + '" onclick="$.cancelChanges( this );"><i class="icon-remove icon-white"></i> [cancel]</a>';
        var txtArea = '<textarea class="element-data" class="span3" rows="15">' + $(link).parent().parent().parent().find(".element-data").val() + '</textarea>';
        $(link).parent().css("display", "none");
        $(link).parent().parent().find('.element-data-form fieldset').html( '<div class="row span5">' + saveButton + cancelButton + '</div>' + txtArea );
        $(link).parent().parent().find('.element-data-form').css('display', 'block');
        $(link).smallipop('hide');
    };

    $.activeEditMode = function( link, data )
    {
        if(data === undefined)
        {
            data = $(link).parent().parent().parent().find( ".element-data-form textarea" ).val();
        }
        $(link).parent().html( '<a href="#" onclick="$.activeSaveMode( this );"><i class="icon-pencil icon-white"></i> [edit]</a><textarea class="element-data" class="span3" disabled rows="15">' + data + '</textarea>' );
    };

    $.cancelChanges = function( link )
    {
        $.activeEditMode( link, $(link).attr("data-element-json") );
    };

    var getJSON = function( element )
    {
        // var tooltipForm = $(element).parent().parent();
        // tooltipForm.css("display", "none");
        // var pageRefId = $(element).attr("data-page-ref-id");
        // var pageRefElementId = $(element).attr("data-element-ref-id");
        // var elementObject = $.getPageElementById( pageRefId, pageRefElementId, false );
        var elementObject = element;
        var page = elementObject.page;
        var caption  = elementObject.caption;
        elementObject.page = undefined;
        elementObject.caption = undefined;
        var txtArea = '<textarea class="element-data" class="span3" disabled rows="15">' + JSON.stringify( elementObject, null, 4 ) + '</textarea>';
        elementObject.page = page;
        elementObject.caption = caption;
        return txtArea;
        // definePageElementTooltip( element );
    };

    var defineSearchTags = function( )
    {
        var options = '';
        $.each($.getTags(), function(idx, tag)
        {
            options += '<option value="' + tag + '">';
        });
        $("#tags").html( options );
    }

    var definePageBehavior = function()
    {
        $(".page").each(function()
        {
            var w = $(this).width();
            var wReal = $(this).attr("data-w");
            var hReal = $(this).attr("data-h");

            var xScale = w/wReal;

            var h = pixelRatio * xScale * hReal;
            var yScale = h/hReal;


            $(this).css("width", $(this).attr("data-w") * xScale + "px");
            $(this).css("height", $(this).attr("data-h") * yScale + "px");
            $(this).parent().css("width", $(this).attr("data-w") * xScale + "px");
            $(this).parent().css("height", $(this).attr("data-h") * yScale + "px");

            $(this).css("background-image", "url('" + $(this).attr('data-caption') + "')" );
            $(this).css("background-repeat", "no-repeat");
            $(this).css("background-size", "100% 100%");
        });
    };

    $.definePageBehavior = function()
    {
        definePageBehavior();
    }

    $.getPage = function( pageId )
    {
        return $('.page-decorator[data-id="' + pageId + '"] > .page ').get( 0 );
    };

    $.revertChanges = function( button )
    {
        askForConfirmation( 'Work in progress will be permanently lost! Do you want to continue?' );
        if( !$.wip )
        {
            $(window).spin();

            document.location.reload();

            $(window).spin();
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
            var rowIdx = parseInt($("#dialog").attr('data-row-id'))+1;
            if(rowIdx <= $("#report tr").size() )
            {
                $(window).spin();

                var refPageIdx = $('#report tr[data-id=' + rowIdx + '] .ref-page .page-decorator').attr("data-id");
                var pageIdx = $('#report tr[data-id=' + rowIdx + '] .new-page .page-decorator').attr("data-id");
                var content = buildDialodContent(rowIdx, refPageIdx, pageIdx);
                $('a.simplemodal-close').click();
                $.modal( content );
                defineModalViewPageBaseElementBehavior();

                $(window).spin();
            }
        }
    };

    var askForConfirmation = function( question, updateWip )
    {
        var ans = false;
        if($.wip)
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
            var rowIdx = parseInt($("#dialog").attr('data-row-id'))-1;
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
        if( askForConfirmation( 'Are you sure you want to commit?', false ) )
        {
            $(window).spin();

            var newContent = Array();

            var refPageId = $(button).attr("data-ref-page-id");
            var pageId = $(button).attr("data-new-page-id");

            $.each($(".merge .page-element"), function(idx, element)
            {
                newContent.push( $(element).attr("data-id") );
            });

            $.ajax({
                type: "POST",
                url: "/ui-explorer-viewer/application/merge/" + $("#ref-tag").text() + "/" + $("#new-tag").text() + "/" + refPageId + "/" + pageId,
                data: JSON.stringify( newContent ),
                contentType: 'application/json',
                success: function(result)
                {
                    $(window).spin();
                    alert("Changes have been saved!");
                },
                error: function()
                {
                    $(window).spin();
                    alert("Changes could not be saved!");
                }
            });


        }
        else
        {
            alert('There is no current changes to be commited');
        }
    };

    var del = function(item, array)
    {
        var idx = array.indexOf( item );
        if(idx != -1)
        {
            array.splice(idx, 1);
        }
    };

    $.handleDrop = function( page )
    {
        window.event.preventDefault();

        $.wip = true;
        $("#commit-button").removeClass("disabled");
        $("#revert-button").removeClass("disabled");

        var elementDropped = $('#dialog div[data-id="' + window.event.dataTransfer.getData("Text") + '"]').get(0);

        $.end = true;
        $(elementDropped).removeClass("hover");
        $(".page-element[data-id="+$(elementDropped).attr("data-match-element-id")+"]").removeClass("hover");

        $(elementDropped).remove( );
        $(page).find(".page").append( elementDropped );

        updateElementCount();
        definePageElement( elementDropped );



    };

    $.handleTrashDrop = function( page )
    {
        window.event.preventDefault();

        $.wip = true;
        $("#commit-button").removeClass("disabled");
        $("#revert-button").removeClass("disabled");

        var elementDropped = $('#dialog div[data-id="' + window.event.dataTransfer.getData("Text") + '"]').get(0);
        $(elementDropped).remove( );
    };

    $.dragStart = function( event )
    {
       event.dataTransfer.effectAllowed='move';
       event.dataTransfer.setData("Text", event.target.getAttribute('data-id'));
       event.dataTransfer.setDragImage(event.target,$(event.target).width()/2,$(event.target).height()/2);

       return true;
    };

    $.defineModalViewPageBaseElementBehavior = function()
    {
        defineModalViewPageBaseElementBehavior();
    }

    $.end = false;

    var defineModalViewPageBaseElementBehavior = function()
    {
        var width = $("#dialog").width()/3 -75;
        $("div[dropzone]").on({
            dragover: function(evt){
                evt.preventDefault();
            }
        });

        $(".page-element").hover(function(){
            $(".page-element[data-id="+$(this).attr("data-match-element-id")+"]").addClass("hover");
        },
        function(){
            $(".page-element[data-id="+$(this).attr("data-match-element-id")+"]").removeClass("hover");
        });

        $(".page-decorator").each(function()
        {
            $(this).parent().css("float", "left");

            var w = ((screen.width)/3)-100;
            var wReal = $(this).find(".page").attr("data-w");

            var xScale = w / wReal;
            var yScale = pixelRatio * xScale;

            $(this).css("width", $(this).find(".page").attr("data-w") * xScale + "px");
            $(this).css("height", $(this).find(".page").attr("data-h") * yScale + "px");

        });
        $(".page > .page-element").each(function()
        {
            definePageElement( $(this) );
            definePageElementTooltip( $(this) );
        });
        $(".page").each(function()
        {
            $(this).find(".page-element").attr("draggable", "true");
            $(this).css("background-image", "url('" + $(this).attr('data-caption') + "')" );
            $(this).css("background-repeat", "no-repeat");
            $(this).css("background-position", "center");
            $(this).css("background-size", "100% 100%");
        });
        $("#page-mark").html( parseInt($('#dialog').attr('data-row-id'))+1 + ' / ' + parseInt($("#report tr").size()-1) );
        $("#trash").css("display", "block");
        updateElementCount();
    };

    var updateElementCount = function()
    {
        $("#ref-element-left-count").html( "Remaining element(s): " + parseInt($("#merger .ref-page .page-element").size()) );
        $("#element-left-count").html( "Remaining elements(s): " + parseInt($("#merger .new-page .page-element").size()) );
    }

    var definePageElement = function( element )
    {
        var page = $(element).parent();
        var wReal = page.attr("data-w");
        var hReal = page.attr("data-h");

        var pageDecorator = page.parent();

        var w = pageDecorator.width()
        var h = pageDecorator.height()

        $(element).attr("draggable", "true");
        $(element).attr("ondragstart", "return $.dragStart(event)");

        var xScale = w / wReal;
        var yScale = h / hReal;

        $(element).css("top",  $(element).attr("data-y") * yScale + (-1) + "px");
        $(element).css("left", $(element).attr("data-x") * xScale + (-1) + "px");
        $(element).css("width", $(element).attr("data-w") * xScale + "px");
        $(element).css("height", $(element).attr("data-h") * yScale + "px");

        $(element).css("background-image", "url('" + $(element).attr('data-caption') + "')");
        $(element).css("background-repeat", "no-repeat");
        $(element).css("background-position", "center");
        $(element).css("background-size", "100% 100%");
    }

    var definePageElementTooltip = function( element )
    {
        element.smallipop({
            preferredPosition: 'right',
            theme: 'black revolanceTip',
            popupOffset: 0,
            invertAnimation: true,
            hideOnPopupClick: false,
            triggerOnClick: true,
            popupYOffset: -10,
            cssAnimations: {
                enabled: true,
                show: 'animated flipInX',
                hide: 'animated flipOutX'
            }
        });
    }

    $(document).ready(function( )
    {
        definePageElementBehavior( );
        definePageBehavior();
    });

})(jQuery, window, document);
