<%--
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ui-monitoring-server
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  Copyright (C) 2012 - 2014 RevoLance
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the 
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public 
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  --%>

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