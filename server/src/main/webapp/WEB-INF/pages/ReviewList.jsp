<%@ include file="Header.jsp" %>

    <div class="row">
        <div class="page-title">
            <h2>Review List</h2>
        </div>
        <div class="reviews-count">
            <h4>
                <span id="reviews-count">${fn:length(reviews)}</span>
                <c:if test="${fn:length(reviews) eq 1}">review</c:if>
                <c:if test="${fn:length(reviews) ne 1}">reviews</c:if>
            </h4>
        </div>
        <div class="page-toolbar pull-right">
            <div id="add-review" class="add-review" ></div>
            <div id="del-review" class="del-review disabled"></div>
            <a id="link" hidden=true href="#"></a>
        </div>
        <table id="review-list" class="table table-hover">
            <thead>
                <tr>
                    <th>&nbsp;&nbsp;</th>
                    <th>Id</th>
                    <th>Status</th>
                    <th>Application</th>
                    <th>Reference Application</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="review" items="${reviews}">
                    <tr>
                        <td><input type="checkbox"></td>
                        <td><a class="id" href="/ui-monitoring-server/compare/${review.referenceApplication}/${review.application}?reviewId=${review.id}">${review.id}</a></td>
                        <td>
                            <div class="status">
                                <input class="progress-bar" value="${review.progress}" data-fgcolor="#222222" data-anglearc="250" data-readOnly=true data-angleoffset="-125" data-width="50" data-height="30">
                            </div>
                            <span class="status-badge badge">${review.status}</span>
                        </td>
                        <td><a class="tag" href="/ui-monitoring-server/applications/${review.application}">${review.application}</a></td>
                        <td><a class="tag" href="/ui-monitoring-server/applications/${review.referenceApplication}">${review.referenceApplication}</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <script src="${pageContext.request.contextPath}/libs/knob/js/knob.js"></script>
    <link href="${pageContext.request.contextPath}/libs/knob/css/knob.css" rel="stylesheet" media="screen"></script>
    <script src="${pageContext.request.contextPath}/js/review-list.js"></script>

<%@ include file="Footer.jsp" %>