
<%@ include file="Header.jsp" %>

<div class="span10">

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

    <div class="span10">
        <table id="report" class="table">
            <tbody>
                <c:forEach var="pageComparison" items="${pageComparisons}">${pageComparison}</c:forEach>
            </tbody>
        </table>
    </div>


</div>

<script>
    $.definePageBehavior();
    $.definePageElementBehavior();
</script>

<%@ include file="Footer.jsp" %>