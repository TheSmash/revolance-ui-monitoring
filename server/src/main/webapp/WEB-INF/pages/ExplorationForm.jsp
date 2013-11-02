<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="Header.jsp" %>

<div class="container">
    <div class="span10">
        <div class="page-title">
            <h2>Exploration request</h2>
        </div>

        <form:form method="POST" commandName="content" action="${pageContext.request.contextPath}/explorations">
            <form:errors path="*" cssClass="errorblock" element="div" />
            <table>
                <tr>
                    <td>Exploration tag:</td>
                    <td><input type="text" path="tag" name="tag"/></td>
                    <td><form:errors path="id" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>Enter the url:</td>
                    <td><input type="text" name="url" /></td>
                    <td><form:errors path="url" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>Enter the domain:</td>
                    <td><input type="text" name="domain" /></td>
                    <td><form:errors path="domain" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>Enter the resolution:</td>
                    <td><input type="text" name="resolution" /></td>
                    <td><form:errors path="resolution" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>Enter the browser:</td>
                    <td><input type="text" name="browserType" /></td>
                    <td><form:errors path="browserType" cssClass="error" /></td>
                </tr>
                <tr>
                    <td colspan="3"><input type="submit" class="btn btn-success" /></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>

<%@ include file="Footer.jsp" %>