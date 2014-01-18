
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