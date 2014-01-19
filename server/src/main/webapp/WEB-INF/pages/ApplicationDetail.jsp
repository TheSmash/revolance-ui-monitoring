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
                <h2>${applicationId} details</h2>
            </div>
            <div class="pages-count">
                <h4>
                    <span id="pages-count">${pagesCount}</span>
                    <c:if test="${pagesCount eq 1}">page</c:if>
                    <c:if test="${pagesCount ne 1}">pages</c:if>
                </h4>
            </div>
        </div>

        ${pages}

</div>

<script src="${pageContext.request.contextPath}/js/review.js"></script>
<script src="${pageContext.request.contextPath}/js/viewer.js"></script>
<script src="${pageContext.request.contextPath}/js/viewer-search.js"></script>

<%@ include file="Footer.jsp" %>