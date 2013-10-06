(function($,W,D){

    $.getPageById = function( pageId, ref )
    {
        if(ref)
        {
            return $.findPageById($.getOfficialSitemapReport().sitemap, pageId);
        }
        else
        {
            return $.findPageById($.getTestedSitemapReport().sitemap, pageId);
        }
    };

    $.findPageById = function( pages, pageId )
    {
        var pageFound = undefined;
        $.each(pages, function(idx, page){
            if( page.id === pageId )
            {
                pageFound = page;
                return false;
            }
        });
        return pageFound;
    };

    $.findPageElementById = function( elements, elementId )
    {
        var elementFound = undefined;
        $.each(elements, function(idx, element){
            if( element.internalId === elementId )
            {
                elementFound = element;
                return false;
            }
        });
        return elementFound;
    }

    $.getPageElementById = function( pageId, elementId, ref )
    {
        var page = $.getPageById(pageId, ref);
        if(page === undefined)
        {
            return;
        }
        else
        {
            return $.findPageElementById(page.content, elementId);
        }
    };

    var writeFileContentToTarget = function(content, target)
    {
        $(target).val( content );
    };

    $.loadSitemap = function(src, target)
    {
        var reader = new FileReader();
        reader.onerror = errorHandler;
        reader.onload = (function(theFile) {
            return function(e){
                writeFileContentToTarget(e.target.result, target);
            }
        })(src);
        var content = reader.readAsText(src);
        return content;
    };

    function errorHandler(evt)
    {
        switch(evt.target.error.code)
        {
          case evt.target.error.NOT_FOUND_ERR:
            alert('File Not Found!');
            break;
          case evt.target.error.NOT_READABLE_ERR:
            alert('File is not readable');
            break;
          case evt.target.error.ABORT_ERR:
            break; // noop
          default:
            alert('An error occurred reading this file.');
        };
    };

    var endsWith = function(string, pattern)
    {
        var d = string.length - pattern.length;
        return d >= 0 && string.lastIndexOf(pattern) === d;
    };

    var exportFileWithContent = function( content )
    {
        window.webkitRequestFileSystem(window.TEMPORARY, 1024*1024, function(fs) {
            fs.root.getFile('sitemap.json', {create: true}, function(fileEntry) {
                fileEntry.createWriter(function(fileWriter) {

                    var blob = new Blob([content]);

                    fileWriter.addEventListener("writeend", function() {
                    // navigate to file, will download
                    location.href = fileEntry.toURL();
                    }, false);

                    fileWriter.write(blob);
                }, function() {});
            }, function() {});
        }, function() {});
    };

    $.exportOfficialSitemap = function( )
    {
        exportFileWithContent( $("#official-sitemap-report").val() );
    }

    $(document).ready(function( ) {

    });

})(jQuery, window, document);
