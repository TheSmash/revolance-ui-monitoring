<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="Header.jsp" %>

<div class="container">
    <legend>
        <h1>Settings</h1>
    </legend>

    <form:form method="POST" commandName="content" action="${pageContext.request.contextPath}/settings">

        <form:errors path="*" cssClass="errorblock" element="div" />
        <div class="span3">
            <div class="span3"><h3>UI</h3></div>
                <h5>Page decorator width:</h5>
                <input type="text" id="page-decorator-width" name="page-decorator-width" path="settings.width" placeholder="Enter the page decorator width" value="${settings.decoratorWidth}">
                <form:errors path="settings.width" cssClass="error span10" />

                <h5>Page decorator height: </h5>
                <input type="text" id="page-decorator-height" name="page-decorator-height" path="settings.height" placeholder="Enter the page decorator height" value="${settings.decoratorHeight}">
                <form:errors path="file" cssClass="error span10"/>
                <input type="submit" class="btn btn-success" />
            </div>
        </div>
    </form:form>
</div>

<%@ include file="Footer.jsp" %>