(function($,W,D){

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
/*
    var buildElementWithContent = function(elem, content, tooltip, refElementId, newElementId)
    {
        if(elem === undefined)
        {
            return;
        }
        else
        {
            // Tooltip class
            var style = " ";
            if(tooltip)
            {
                style += "smallipopHorizontal ";
            }

            var element = '<div class="page-element' + style + '" data-id="' + elem.internalId + '" data-ref-element-id="' + refElementId + '" data-new-element-id="' + newElementId + '" data-caption="' + elem.caption + '" data-w="' + elem.w + '" data-h="' + elem.h + '" data-x="' + elem.x + '" data-y="' + elem.y + '" >';
            if( content !== undefined && content !== "")
            {
                element += content;
            }
            element += '</div>';
            return element;
        }
    };

    var buildUndefinedPage = function( tooltip, isRef )
    {
        var style= "undef";
        if(tooltip !== "")
        {
            style += " smallipopHorizontal";
        }

        return '<div class="page-decorator ' + style + '" >' + tooltip + '</div>';
    };

    var buildPage = function(page, style, refPageId, newPageId, tooltip, content)
    {
        if( page === undefined )
        {
            return buildUndefinedPage( tooltip, false );
        }
        else
        {
            if( content === undefined )
            {
                return buildPageWithContent( page, refPageId, newPageId, buildPageContent( page.content ), style, tooltip, "" );
            }
            else
            {
                return buildPageWithContent( page, refPageId, newPageId, content, style, tooltip, "" );
            }
        }
    };

    var buildPageContent = function(content, tooltip)
    {
        var pageContent = "";
        if( content !== undefined )
        {
            for(contentIdx in content)
            {
                if(tooltip)
                {
                    pageContent += buildElementWithContent( content[contentIdx], tooltip );
                }
                else
                {
                    pageContent += buildElementWithContent( content[contentIdx] );
                }
            }
        }
        return pageContent;
    };

    var buildPageWithContent = function(pageObj, refPageId, newPageId, content, style, tooltip, pageAttrs)
    {
        var hasTooltip = false;
        if(tooltip !== undefined)
        {
            hasTooltip = true;
        }
        // Tooltip class
        if(hasTooltip)
        {
            style += " smallipopHorizontal";
        }

        var page = '';

        // page decorator bloc
        page += '<div class="page-decorator ' + style + '" data-ref-page-id="' + refPageId + '" data-new-page-id="' + newPageId + '" ';
        if( pageAttrs !== "" )
        {
            page += pageAttrs;
        }

        page += ' data-id="' + pageObj.id + '">';

        page += '<div class="page" data-w="' + pageObj.w + '" data-h="' + pageObj.h + '" data-caption="' + pageObj.caption + '">';
        page += content;
        page += '</div>';

        page += '</div>'; // end of page decorator
        return page;

    };

    var defineTooltipBehavior = function()
    {
        $(".smallipopHorizontal").smallipop({
            preferredPosition: 'right',
            theme: 'black revolanceTip',
            popupOffset: 0,
            invertAnimation: true,
            hideOnPopupClick: false,
            triggerOnClick: true,
            cssAnimations: {
                enabled: true,
                show: 'animated flipInX',
                hide: 'animated flipOutX'
            }
        });
    };

    $.renderReports = function()
    {
        var officialSitemapReport = $.getOfficialSitemapReport( );
        var testedSitemapReport = $.getTestedSitemapReport( );
        if( officialSitemapReport !== "" && testedSitemapReport !== "")
        {
            $("#comparison-sitemap-report").val( $.compareSitemapToJSON(testedSitemapReport, officialSitemapReport) );
            return renderReports(officialSitemapReport, $.loadDifferencies(), testedSitemapReport);
        }
    };

    var buildReportRow = function(idx, ref, tested, diffType, pageDiffTypes)
    {
        var refPageClass = null;
        var testedPageClass = null;
        if(diffType === "BASE")
        {
            refPageClass = "base";
            testedPageClass = "";
            if( $.inArray("CONTENT", pageDiffTypes) >= 0 )
            {
                testedPageClass = "error";
            }
            else if( pageDiffTypes.length > 0 )
            {
                testedPageClass = "warn";
            }
        }
        else if(diffType === "ADDED")
        {
            refPageClass = "undef";
            testedPageClass = "added";
        }
        else if(diffType === "DELETED")
        {
            refPageClass = "removed";
            testedPageClass = "undef";
        }

        var refPageId = "";
        if( ref !== undefined )
        {
            refPageId = ref.id;
        }

        var testedPageId = "";
        if( tested !== undefined )
        {
            testedPageId = tested.id;
        }

        var refPage = buildPage(ref, refPageClass, refPageId, testedPageId);
        var testedPage = buildPage(tested, testedPageClass, refPageId, testedPageId );

        var row = '';
        row += '<tr data-id="' + idx + '" data-variant-idx=0>';
        row += '    <td colspan=2>';
        row += '    <div class="span1"><a href="#" onClick="$.previousVariant( this );"><i class="icon-chevron-left"></i></a></div>';
        row += '    <div class="span4 ref-page">' + refPage + '</div>';
        row += '    <div class="span4 new-page">' + testedPage + '</div>';
        row += '    <div class="span1"><a href="#" onclick="$.nextVariant( this );"><i class="icon-chevron-right"></i></a></div>';
        row += '    <div class="data-container span10">';
        row += '    <p class="data-container-title">Tags</p>';
        row += buildPageTooltip( idx, ref, tested, diffType, pageDiffTypes );
        row += '    </div>';

        row += '    </td>';
        row += '</tr>';

        return row;
    };
*/
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
/*
    var buildMetaInf = function( refPage, page, pageDiffTypes )
    {
        // page meta-data
        var inf = '';
        inf += '<div class="cloud-tag span10">';

        if( refPage !== undefined )
        {
            inf += '    <div class="span10"><span class="label label-info">o:title: <em>' + refPage.title + '</em></span></div>';
        }
        if( page !== undefined )
        {
            inf += '    <div class="span10"><span class="label label-info">n:title: <em>' + page.title + '</em></span></div>';
        }
        if( refPage !== undefined )
        {
            inf += '    <div class="span10"><span class="label label-info">o:url: <em>' + refPage.url + '</em></span></div>';
        }
        if( page !== undefined )
        {
            inf += '    <div class="span10"><span class="label label-info">n:url: <em>' + page.url + '</em></span></div>';
        }

        inf += '</div>';

        return inf;
    }

    var getLinksCount = function( page )
    {
        return countElementByImpl( page.content, "Link" );
    };

    var getButtonsCount = function( page )
    {
        return countElementByImpl( page.content, "Button" );
    };

    var countElementByImpl = function( content, impl )
    {
        var count = 0;
        $.each(content, function(idx, elem){
            if( elem.impl === impl )
            {
                count += 1;
            }
        });
        return count;
    };

    var buildPageTooltip = function( idx, pageRef, page, diffType, pageDiffTypes )
    {
        var pageId = "";
        if(page !== undefined)
        {
            pageId = page.id;
        }
        var pageRefId = "";
        if(pageRef !== undefined)
        {
            pageRefId = pageRef.id;
        }

        var tooltip = '';

        tooltip += '        <form class="page-diff-form form-horizontal tooltip-form">';
        tooltip += '            <input type="hidden" data-ref-page-ref="' + pageRefId + '" data-new-page-id="' + pageId + '" />';
        tooltip += buildMetaInf( pageRef, page, pageDiffTypes );

        if(diffType === "BASE")
        {

            tooltip += '<div class="cloud-tag span10">';
            if( pageDiffTypes.length === 0 )
            {
                tooltip += buildBadge("label-success no-changes", "change: none", true);
            }
            else
            {
                tooltip += buildBadge( "label-important content", "change: content", $.inArray("CONTENT", pageDiffTypes) >= 0 );
                tooltip += buildBadge( "label-warning layout", "change: layout", $.inArray("LAYOUT", pageDiffTypes) >= 0 );
                tooltip += buildBadge( "label-warning look", "change: look", $.inArray("LOOK", pageDiffTypes) >= 0 );
                tooltip += buildBadge( "label-info title", "change: title", $.inArray("TITLE", pageDiffTypes) >= 0 );
                tooltip += buildBadge( "label-info url", "change: url", $.inArray("URL", pageDiffTypes) >= 0 );
            }

            tooltip += '</div>';

            if( pageDiffTypes.length !== 0 )
            {
                tooltip += '<div class="pull-right">';
                tooltip += buildInspectPageButton( idx, pageRefId, pageId );
                tooltip += buildSubmit( "apply", "Accept all" );
                tooltip += '</div>';
            }

            tooltip += '</div>';
        }
        else if( diffType === "ADDED" )
        {
            tooltip += buildBadge( "label-info added", "ADDED", true );
            tooltip += '<div class="pull-right">';
            tooltip += buildInspectPageButton( idx, pageRefId, pageId );
            tooltip += buildSubmit( "add", "Confirm page creation" );
            tooltip += '</div>';
        }
        else if( diffType === "DELETED" )
        {
            tooltip += buildBadge( "label-important deleted", "DELETED", true );
            tooltip += '<div class="pull-right">';
            tooltip += buildInspectPageButton( idx, pageRefId, pageId );
            tooltip += buildSubmit( "del", "Confirm page deletion" );
            tooltip += '</div>';
        }

        tooltip += '    </div>';
        tooltip += '</form>';
        return tooltip;
    };

    var buildSubmit = function( id, label, pageId, pageRefId )
    {
        return '<button type="submit" id="' + id + '" class="btn btn-success btn-small" data-new-page-id="' + pageId + '" data-ref-page-id="' + pageRefId + '" onclick="$.applyChanges(this);">' + label + '</button>' ;
    };

    var buildInspectPageButton = function( idx, pageRefId, pageId )
    {
        return '<input type="button" class="btn btn-info btn-small" value="Inspect" href="#" data-row-id="' + idx + '" data-new-page-id="' + pageId + '" data-ref-page-id="' + pageRefId + '" onclick="$.buildDialog( this );" />';
    };

    var buildSwitchRawModeButton = function( refPageId, refElementId, pageId, elementId )
    {
        return '<a href="#" onclick="$.activeSaveMode( this );"><i class="icon-pencil icon-white"></i> [edit]</a>';
    };
*/
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
/*
    var buildBadge = function( extraClass, label, state )
    {
        if(state)
        {
            return '<span class="label ' + extraClass + '">' + label + '</span> ';
        }
        else
        {
            return "";
        }
    };

    var renderReports = function(ref, diff, tested)
    {
        if( ref === undefined || diff === undefined ||tested === undefined )
        {
            return;
        }
        if( ref === "" || diff === "" ||tested === "" )
        {
            return;
        }
        var report = '';

        report += '<table id="report" class="table"><thead><th></th><th>Official page</th><th>Tested page</th><th></th></thead><tbody>';
        $.each(diff.pageComparisons, function(comparisonIdx, pageComparison)
        {
            report += buildReportRow(comparisonIdx, pageComparison.refPage, pageComparison.page, pageComparison.diffType, pageComparison.pageDiffTypes);
        });
        report += '</tbody></table>';
        $("#report").html( report );
        defineSearchTags();
        definePageBehavior( );
        definePageElementBehavior( );
        definePageDecoratorBehavior( );
        defineTooltipBehavior( );
    };
*/
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
/*
    $.buildDialog = function( val )
    {
        $(val).smallipop('hide');
        var rowIdx = $(val).attr('data-row-id');
        var pageId = $(val).attr('data-new-page-id');
        var pageRefId = $(val).attr('data-ref-page-id');

        $.modal( buildDialodContent(rowIdx, pageRefId, pageId) );
        defineModalViewPageBaseElementBehavior();
    };

    var buildDialodContent = function(rowIdx, pageRefId, pageId)
    {
        var page = $.getPageById( pageId, false );
        var refPage = $.getPageById( pageRefId, true );

        var comparison = $.comparePage( page, refPage );
        comparison.page = page;
        comparison.refPage = refPage;

        var width = 0.9 * W.screen.availWidth;
        var height = pixelRatio*width/3 +90;
        return '<div id="dialog" data-row-id="' + rowIdx + '" style="width:' + width + 'px;height:' + height + 'px;" >' + buildComparisonReport( comparison ); + '</div>';
    };

    var buildNewPageForMerge = function( comparison, content )
    {
        var style = "";
        if(comparison.diffType === "BASE")
        {
            if( $.inArray("CONTENT", comparison.pageDiffTypes) >= 0 )
            {
                style = "error";
            }
            else if( comparison.pageDiffTypes.length > 0 ) // Nothing related to the data
            {
                style = "warn";
            }
        }
        else if(comparison.diffType === "ADDED")
        {
            style = "added";
        }
        else if(comparison.diffType === "DELETED")
        {
            style = "undef";
        }

        var refPageId = "";
        if( comparison.refPage !== undefined )
        {
            refPageId = comparison.refPage.id;
        }

        return buildPage( comparison.page, style, refPageId, comparison.page.id, "", content );
    };

    var buildRefPageForMerge = function( comparison, content )
    {
        var style = "";
        if(comparison.diffType === "BASE")
        {
            style = "base";
        }
        else if(comparison.diffType === "ADDED")
        {
            style = "undef";
        }
        else if(comparison.diffType === "DELETED")
        {
            style = "removed";
        }

        var newPageId = "";
        if( comparison.page !== undefined )
        {
            newPageId = comparison.page.id;
        }

        return buildPage( comparison.refPage, style, comparison.refPage.id, newPageId, "", content );
    };

    var buildContentForMerge = function( comparisons )
    {
        var refMergePageContent = "";
        var newMergePageContent = "";
        var mergePageContent = "";

        var element = undefined;
        $.each(comparisons.contentComparisons, function(idx, comparison)
        {
            if(!isCommon( comparison ))
            {
                if( comparison.diffType === "BASE" )
                {
                    element = $.findPageElementById( comparisons.page.content, comparison.element );
                    if( element !== undefined )
                    {
                        // console.log( element.internalId );
                        newMergePageContent += buildElementWithContent( element, buildElementTooltip( comparison ), true, comparison.refElement, comparison.element );
                    }
                    element = $.findPageElementById( comparisons.refPage.content, comparison.refElement );
                    if( element !== undefined )
                    {
                        refMergePageContent += buildElementWithContent( element, buildElementTooltip( comparison ), true, comparison.refElement, comparison.element );
                    }
                }
                else if( comparison.diffType === "ADDED" )
                {
                    element = $.findPageElementById( comparisons.page.content, comparison.element );
                    if( element !== undefined )
                    {
                        console.log( element.internalId );
                        newMergePageContent += buildElementWithContent( element, buildElementTooltip( comparison ), true, "", comparison.element );
                    }
                }
                else if( comparison.diffType === "DELETED" )
                {
                    element = $.findPageElementById( comparisons.refPage.content, comparison.refElement );
                    if( element !== undefined )
                    {
                        refMergePageContent += buildElementWithContent( element, buildElementTooltip( comparison ), true, comparison.refElement, "" );
                    }
                }
            }
            else
            {
                element = $.findPageElementById( comparisons.refPage.content, comparison.refElement );
                mergePageContent += buildElementWithContent( element, buildElementTooltip( comparison ), true, comparison.refElement, comparison.element );
            }
        });

        var pageWidth = 0;
        var pageHeight = 0;
        // var pageCaption = "";
        var refPageId = "";
        var pageId = "";

        if(comparisons.diffType === "ADDED" )
        {
            pageHeight = comparisons.page.h;
            pageWidth = comparisons.page.w;
            // pageCaption = comparisons.page.caption;
            //refPageId = "";
            //refContent = Array();
            content = comparisons.page.content;
            pageId = comparisons.page.id
        }
        else if( comparisons.diffType === "BASE" )
        {
            pageHeight = comparisons.refPage.h;
            pageWidth = comparisons.refPage.w;
            // pageCaption = comparisons.refPage.caption;
            refPageId = comparisons.refPage.id;
            refContent = comparisons.refPage.content;
            content = comparisons.page.content;
            pageId = comparisons.page.id
        }

        var content = Object();
        content.refPageContent = refMergePageContent;
        content.pageContent = newMergePageContent;
        content.mergePageContent = mergePageContent;

        content.id = "merged";
        content.w = pageWidth;
        content.h = pageHeight;
        content.caption = '';
        content.refPageId = refPageId;
        content.newPageId = pageId;
        return content;
    };

    var buildComparisonReport = function( comparison )
    {
        var mergePage = buildContentForMerge( comparison );
        var refPage = buildRefPageForMerge(comparison, content.refPageContent);
        var page = buildNewPageForMerge(comparison, content.pageContent);

        var mergedPage = buildPageWithContent( mergePage, mergePage.refPageId, mergePage.newPageId, mergePage.mergePageContent, '', '', 'ondrop="$.handleDrop(this);" dropzone="move string:text/plain"' );

        var pageId = '';
        if( comparison.page !== undefined )
        {
            pageId = comparison.page.id;
        }

        var refPageId = '';
        if( comparison.refPage !== undefined )
        {
            refPageId = comparison.refPage.id;
        }

        return '<table class="table" id="merger"><tbody><tr><td></td><td><div class="counter" id="ref-element-left-count"></div></td><td><div class="toolbar"><button class="btn btn-success disabled" id="commit-button" onclick="$.applyChanges( this );" data-ref-page-id="' + refPageId + '" data-new-page-id="' + pageId + '" ><i class="icon-share"></i></button><button class="btn btn-warning disabled" onclick="$.revertChanges( this );" id="revert-button" data-ref-page-id="' + refPageId + '" data-new-page-id="' + pageId + '"><div class="revert"></div></button></td><td></div><div class="counter" id="element-left-count"></div></td><td></td></tr><tr><td style="vertical-align:middle;"><a href="#" onclick="$.previous(this)" ><i class="icon-chevron-left"></i></a></td><td class="ref-page" style="margin-top:18px">' + refPage + '</td><td class="merge" style="margin-top:18px">' + mergedPage + '</div></div></td><td class="new-page" style="margin-top:18px">' + page + '</td><td style="vertical-align:middle;"><a href="#" onclick="$.next( this );"><i class="icon-chevron-right"></i></a></td></tr><tr><td></td><td colspan=3><div class="row trash" ondrop="$.handleTrashDrop(this);" dropzone="move string:text/plain"><i class="icon-trash"></i></td><td></td><tr><td></td><td></td><td id="page-mark" style="text-align:center"></td><td></td><td></td></tr></tbody><table>';
    };

    var removeCommonContent = function( page, commonContentIds )
    {
        var content = Array();

        $.each(page.content, function(idx, pageContent)
        {
            var common = false;
            $.each(commonContentIds, function(idx, commonContentId)
            {
                if( pageContent.internalId === commonContentId )
                {
                    common = true;
                    return false;
                }
            });
            if( !common )
            {
                content.push( pageContent );
            }
        });

        page.content = content; // update the page content
        return page;
    };

    var buildMergePageContent = function( refPage, page, contentComparisons )
    {
        var mergedContent = Array();
        $.each(contentComparisons, function(idx, comparison)
        {
            if(isCommon( comparison ))
            {
                mergedContent.push( $.getPageElementById( refPage.id, comparison.refElement, true) );
            }
        });
        return buildPageContent( mergedContent );
    };

    var isCommon = function( comparison )
    {
        return comparison.diffType === "BASE" && comparison.pageElementDiffTypes.length === 0;
    };

    var getCommonContent = function( contentComparisons )
    {
        var commonContent = Array();

        if( contentComparisons !== undefined )
        {
            $.each(contentComparisons, function(idx, comparison)
            {
                if(isCommon( comparison ))
                {
                    if( comparison.element !== undefined )
                    {
                        commonContent.push( comparison.element );
                    }
                    if( comparison.refElement !== undefined )
                    {
                        commonContent.push( comparison.refElement );
                    }
                }
            });
        }

        return commonContent;
    };
*/
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
/*
    var buildComparisonReportContent = function( pageContentComparison )
    {
        var content = "";

        $.each(pageContentComparison, function(idx, comparison){
            content += buildElementComparison( comparison );
        });

        return content;
    };

    var buildElementComparison = function( comparison )
    {
        var tooltip = buildElementTooltip( comparison.refPage, comparison.refElement, comparison.page, comparison.element, comparison.diffTypes, comparison.pageElementDiffTypes);
        return buildElementWithContent( comparison.refElement, tooltip, tooltip );
    }
*/
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

            var w = ((screen.width)/3)-18;
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
