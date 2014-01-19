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

    <div class="row">
        <div class="page-title">
            <h2>Application List</h2>
        </div>
        <div class="applications-count">
            <h4>
                <span id="applications-count">${fn:length(applications)}</span>
                <c:if test="${fn:length(applications) eq 1}">application</c:if>
                <c:if test="${fn:length(applications) ne 1}">applications</c:if>
            </h4>
        </div>
        <div class="page-toolbar pull-right">
            <div id="add-application" class="add-application" ></div>
            <div id="del-application" class="del-application disabled"></div>
            <a id="link" hidden=true href="#"></a>
        </div>
        <table id="application-list" class="table table-hover">
            <thead>
                <tr>
                    <th>&nbsp;&nbsp;</th>
                    <th>Tag</th>
                    <th>Pages count</th>
                    <th>Logs</th>
                    <th>Browser</th>
                    <th>Resolution</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="application" items="${applications}">
                    <tr>
                        <td><input type="checkbox"></td>
                        <td><a class="tag" href="/ui-monitoring-server/applications/${application.tag}">${application.tag}</a></td>
                        <td><div class="pages-count">${application.pagesCount}</div></td>
                        <td><a href="/ui-monitoring-server/applications/${application.tag}/logs"><div class="log-application"></div></a></td>
                        <td><div class="browser-type">${application.user.browserType}</div></td>
                        <td><div class="resolution-application">${application.user.browserWidth}x${application.user.browserHeight}</div></td>
                        <td><div class="date">${application.date}</div></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <script src="${pageContext.request.contextPath}/js/application-list.js"></script>

<%@ include file="Footer.jsp" %>