(function($,W,D){

    $.defaultTab = "#wizard";

    var clearActiveTab = function()
    {
        var activeTab = $(".nav > li.active").eq(0);
        if( activeTab.length > 0)
        {
            activeTab.removeAttr("class");
            clearActiveContent( $(activeTab).children().eq(0).attr("href") );
        }

    };

    var clearActiveContent = function( tab )
    {
        $("div[id=" + tab +"]").eq(0).css("display", "none");
    };

    var enableActiveTab = function()
    {
        var hash = event.srcElement.hash;
        if( hash === undefined)
        {
            hash = document.location.hash;
        }

        if( hash.length == 0 )
        {
            hash = $.defaultTab;
        }

        $.enableTab( hash );
    };

    $.enableTab = function( tab )
    {
        if( tab !== undefined )
        {
            var tabs = $(".nav >> a[href=" + tab + "]");
            if( tabs.length == 1 )
            {
                var li = tabs.parent();
                if( li.length > 0 )
                {
                    clearActiveTab();
                    li.eq(0).addClass("active");
                    enableContent( tab );
                }
            }
        }
    };

    var defineTabsBehavior = function()
    {
        $(".nav > li").click(function(){clearActiveContent(); clearActiveTab(); enableActiveTab(); enableContent();});
    };

    var enableContent = function( tab )
    {
        if( tab !== undefined )
        {
            $.each($("#content > div.page-content"), function(idx, content){
                if( $(content).attr('id') === tab )
                {
                    $(content).css("display", "block");
                }
                else
                {
                    $(content).css("display", "none");
                }
            });
        }
    }

    $(document).ready(function( ) {
        defineTabsBehavior();
        enableActiveTab();
    });

})(jQuery, window, document);

