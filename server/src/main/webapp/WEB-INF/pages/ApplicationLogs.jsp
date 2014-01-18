
<%@ include file="Header.jsp" %>

<div class="container">
    <div class="row">

        <div class="page-title">
            <h3>${applicationId} exploration logs</h3>
        </div>

        <div id="log"></div>

        <br /><br />
        <a id="view-application" href="" style="display:none"></a>
    </div>
</div>


    <script src="${pageContext.request.contextPath}/libs/jquery/js/jquery.updater-plugin.js"></script>
    <script>
        $.stop = false;
        $('#view-application').hide();
        setInterval(function() {
            if(!$.stop)
            {
                $.getJSON('${pageContext.request.contextPath}/applications/${applicationId}/logs/?fromLine='+$("#log .line").size(),
                function(result) {
                    var logs = $("#log .line")
                    var lastLoggedLine = $(logs[logs.length-1]).text()

                    $.each(result, function(idx, line){

                        if(line.indexOf('Report has been generated') > 0)
                        {
                            $.stop = true;

                            $('#view-application').attr('href', '${pageContext.request.contextPath}/applications/${applicationId}');
                            $('#view-application').show();
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
            }
        }, 500);

    </script>

<%@ include file="Footer.jsp" %>