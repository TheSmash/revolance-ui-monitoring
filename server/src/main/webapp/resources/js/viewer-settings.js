(function($,W,D){

    $.wip = false;
    /*
    $.getPageDecoratorWidth = function()
    {
        return $("#page-decorator-width").val();
    };

    $.getPageDecoratorHeight = function()
    {
        return $("#page-decorator-height").val();
    }

    $.getMergedSitemapReport = function()
    {
        return loadMergedSitemapReport();
    }

    $.getOfficialSitemapReport = function()
    {
        return loadOfficialSitemapReport();
    };

    var getMergedSitemapReport = function()
    {
        return $("#merged-sitemap-report").val();
    };

    var getOfficialSitemapReport = function()
    {
        return $("#official-sitemap-report").val();
    };

    $.getTestedSitemapReport = function()
    {
        return loadTestedSitemapReport();
    };

    var getTestedSitemapReport = function()
    {
        return $("#new-sitemap-report").val();
    };

    var loadOfficialSitemapReport = function()
    {
        return loadSitemap( getOfficialSitemapReport() );
    };

    var loadTestedSitemapReport = function()
    {
        return loadSitemap( getTestedSitemapReport() );
    };

    var loadSitemap = function( sitemap )
    {
        var report = "";
        if( sitemap !== undefined && sitemap !== "" )
        {
            report = JSON.parse( sitemap );
            var user = report.user;
            $.each(report.sitemap, function(i, page)
            {
                page.user = user;
            });
        }
        return report;
    };

    $.loadDifferencies = function()
    {
        var diffReport = JSON.parse( $("#comparison-sitemap-report").val() );
        $.each(diffReport.pageComparisons, function(i, pageComparison)
        {
            if( pageComparison.refPage !== undefined )
            {
                pageComparison.refPage = $.getPageById( pageComparison.refPage, true );
            }
            if( pageComparison.page !== undefined )
            {
                pageComparison.page = $.getPageById( pageComparison.page, false );
            }
            $.each(pageComparison.contentComparisons, function(j, contentComparison)
            {
                if( contentComparison.element !== undefined )
                {
                    contentComparison.element = $.getPageElementById( pageComparison.page.id, contentComparison.element, false );
                }
                if( contentComparison.refElement !== undefined )
                {
                    contentComparison.refElement = $.getPageElementById( pageComparison.refPage.id, contentComparison.refElement, true );
                }
            });
        });
        return diffReport;
    }

    $.getSitemapPageIdx = function( report, pageId )
    {
        var idx = undefined;
        $.each(report.sitemap, function(i, page)
        {
            if(page.id === pageId)
            {
                idx = i;
                return false;
            }
        });
        return idx;
    };

    $.updateRefSitemapPage = function( pageId, pageObj )
    {
        $.wip = false;
        if( pageObj !== undefined )
        {
            var report = loadOfficialSitemapReport();
            var pageIdx = $.getSitemapPageIdx( report, pageId );

            if( pageIdx === undefined )
            {
                // If the page has not yet been updated we add a new entry for it
                pageIdx = pageObj.url;
            }

            report.sitemap[pageIdx] = pageObj;
            $("#official-sitemap-report").val( JSON.stringify( report, null, 4 ) );
        }
    };

    $.updateNewSitemapPage = function( pageId, pageObj )
    {
        $.wip = false;
        if( pageObj !== undefined )
        {
            var report = loadTestedSitemapReport();
            var pageIdx = $.getSitemapPageIdx( report, pageId );

            if( pageIdx === undefined )
            {
                // If the page has not yet been updated we add a new entry for it
                pageIdx = pageObj.url;
            }

            report.sitemap[pageIdx] = pageObj;
            $("#new-sitemap-report").val( JSON.stringify( report, null, 4 ) );
        }
    };

    $.addRefSitemapPage = function( pageId, pageObj )
    {
        var report = loadOfficialSitemapReport();
        report.sitemap[pageId] = pageObj;
        $("#official-sitemap-report").val( JSON.stringify( report, null, 4 ) );
    }

    $.delRefSitemapPage = function( pageId, pageObj )
    {
        var report = loadOfficialSitemapReport();
        del( pageObj, report.sitemap );
        $("#official-sitemap-report").val( JSON.stringify( report, null, 4 ) );
    }

    var del = function(item, array)
    {
        var idx = array.indexOf( item );
        if(idx != -1)
        {
            array.splice(idx, 1);
        }
    };

    $(document).ready(function( )
    {
        $("#apply-settings").click(function(){
            $.renderReports( );
        });
    });
    */
})(jQuery, window, document);

