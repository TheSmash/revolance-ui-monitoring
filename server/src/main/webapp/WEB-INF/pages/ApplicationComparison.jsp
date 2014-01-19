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
        <div class="span12">
            <div class="page-title">
                <h2>Application comparison</h2>
                <h4>${refAppTag} is compared to ${newAppTag}</h4>
            </div>

            <div class="pull-right">
                <form class="form-wrapper" id="search">
                    <div class="search-icon">
                        <input class="span4" type="search" id="search-input" list="tags" placeholder="Enter a criteria + another one to filter the report" required >
                        <datalist id="tags"></datalist>
                    </div>
                </form>
            </div>
            <table id="report" class="table">
                <tbody>
                    <c:forEach var="pageComparison" items="${pageComparisons}">${pageComparison}</c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/review.js"></script>
<script src="${pageContext.request.contextPath}/js/viewer.js"></script>
<script src="${pageContext.request.contextPath}/js/viewer-search.js"></script>

<%@ include file="Footer.jsp" %>