<%@ include file="Header.jsp" %>

    <script src="${pageContext.request.contextPath}/js/review.js"></script>

    <div class="page-title">
        <h2>Detailed application comparison</h2>
        <h4><a href="/ui-monitoring-server/compare/${refAppTag}/${newAppTag}?reviewId=${reviewId}"><span id="ref-app">${refAppTag}</span> is compared to <span id="app" >${newAppTag}</span></a></h4>
    </div>

    ${content}



<%@ include file="Footer.jsp" %>