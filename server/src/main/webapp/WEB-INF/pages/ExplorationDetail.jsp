
<%@ include file="Header.jsp" %>

<div class="container">
    <div class="span2">
        &nbsp;
    </div>
    <div class="span8">

        <div class="page-title">
            <h2>Exploring application at: ${applicationUrl}</h2>

            <div id="log"></div>

            <a class="btn btn-success" id="applicationsListLink" href="${pageContext.request.contextPath}/applications/${explorationId}" style="display:none"></a>
        </div>

    </div>
</div>


    <script src="${pageContext.request.contextPath}/libs/jquery/js/jquery.updater-plugin.js"></script>
    <script>
        $(function(){
            $.updater({
                url: 'explorations/${explorationTag}/'+$("#log .line").size(),
                data: undefined,
                interval: 100,
                method: 'get',
                response: 'json'
            },
            function(result, response){
                var logs = $("#log .line")
                var lastLoggedLine = $(logs[logs.length-1]).text()
                var idx = 0

                $.each(result, function(idx, line){

                    if(idx === 0 && lastLoggedLine !== line)
                    {
                        $('#log').append('<p class="line">' + line + '</p>')
                    }
                    if(idx%2 === 0)
                    {
                        var y = $('#log').position().top+$('#log').outerHeight()
                        window.scrollTo(0, y)
                        idx = 0
                    }

                    idx ++
                    if(line === 'Report has been generated!')
                    {
                        $.updater.stop()
                        $('#applicationsListLink').css('display', 'block')
                    }
                });
            });
        });

    </script>

<%@ include file="Footer.jsp" %>