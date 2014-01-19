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
                <h2>Exploration List</h2>
            </div>
            <div class="page-toolbar">
                <a href="${pageContext.request.contextPath}/explorations/declare"><button class="btn" id="add">Add</button></a>
                <a href="#"><button class="btn" id="del" disabled>Remove</button></a>
            </div>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>&nbsp;&nbsp;</th>
                        <th>Date</th>
                        <th>Tag</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="exploration" items="${explorations}">
                        <tr>
                            <td><input type="checkbox"></td>
                            <td><div class="date">${exploration.value}</a></div></td>
                            <td><div class="tag"><a href="${pageContext.request.contextPath}/explorations/${exploration.key}">${exploration.key}</a></div></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/exploration-list.js"></script>

<%@ include file="Footer.jsp" %>