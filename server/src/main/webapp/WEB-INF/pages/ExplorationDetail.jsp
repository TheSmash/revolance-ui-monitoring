
<%@ include file="Header.jsp" %>

<div class="container">
    <div class="span2">
        &nbsp;
    </div>

    <div class="row">

        <div class="page-title">
            <h3>Exploration monitoring</h3>
        </div>

        <div id="log"></div>

        <br /><br />
        <a class="btn btn-success" id="applicationLink" href="" style="display:none">Review the application</a>
    </div>
</div>


    <script src="${pageContext.request.contextPath}/libs/jquery/js/jquery.updater-plugin.js"></script>
    <script>
        $('#applicationLink').hide();
        $(function(){
            $.updater({
                url: '${pageContext.request.contextPath}/explorations/${explorationId}/'+$("#log .line").size(),
                data: undefined,
                interval: 100,
                method: 'get',
                response: 'json'
            },
            function(result, response){
                var logs = $("#log .line")
                var lastLoggedLine = $(logs[logs.length-1]).text()

                $.each(result, function(idx, line){

                    if(line.indexOf('Report has been generated') > 0)
                    {
                        $.updater.stop()
                        var startIdx = line.indexOf(":");
                        var explorationId = line.substring(startIdx+1, line.length).trim();

                        $('#applicationLink').attr('href', '${pageContext.request.contextPath}/applications/'+explorationId);
                        $('#applicationLink').show();
                    }
                    else
                    {
                        if(idx === 0 && lastLoggedLine !== line)
                        {
                            $('#log').append('<p class="line">' + line + '</p>')
                        }
                        else
                        {
                            $('#log').append('<p class="line">' + line + '</p>')
                        }
                        if(idx%2 === 0)
                        {
                            var y = $('#log').position().top+$('#log').outerHeight()
                            window.scrollTo(0, y)
                            idx = 0
                        }
                    }
                });
            });
        });

    </script>

<%@ include file="Footer.jsp" %>