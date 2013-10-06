(function($)
{
    const MIME_TYPE = 'text/plain';

    var downloadFile = function( fName, content )
    {
        window.URL = window.webkitURL || window.URL;

        var bb = new Blob([content], {type: MIME_TYPE});

        var a = document.createElement('a');

        a.download = fName;
        a.href = window.URL.createObjectURL(bb);
        a.dataset.downloadurl = [MIME_TYPE, a.download, a.href].join(':');
        a.draggable = true; // Don't really need, but good practice.

        a.click();

        setTimeout(function() {
            window.URL.revokeObjectURL(a.href);
        }, 1500);

    };

    $.downloadOfficialSitemap = function( )
    {
        $('#download-official-sitemap').click();
    };

    $(document).ready(function()
    {
        $('#download-official-sitemap').click(function(e){
            downloadFile( 'sitemap.json', $('#official-sitemap-report').val() );
        });
    });

})(jQuery);
