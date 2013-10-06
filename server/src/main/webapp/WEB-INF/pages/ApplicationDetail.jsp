
<%@ include file="Header.jsp" %>

<div class="container">
    <div class="span10">

        <div class="page-title">
            <h2>Application details</h2>
        </div>

        <div class="span10" id="text-area-options">
            <c:forEach var="page" items="${pages}">
                ${page}
            </c:forEach>
        </div>

    </div>
</div>

<script>
    $.definePageBehavior();
    $.definePageElementBehavior();
</script>

<%@ include file="Footer.jsp" %>