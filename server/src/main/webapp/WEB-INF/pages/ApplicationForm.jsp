<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="Header.jsp" %>

<div class="container">
    <div class="span10">
        <div class="page-title">
            <h2>Add application</h2>
        </div>

        <form:form method="POST" commandName="content" action="${pageContext.request.contextPath}/application" enctype="multipart/form-data">
            <form:errors path="*" cssClass="errorblock" element="div" />
            <table>
                <tr>
                    <td>Select a tag for this file :</td>
                    <td><input type="text" path="tag" name="tag"/></td>
                    <td><form:errors path="tag" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>Select the file to upload :</td>
                    <td><input type="file" name="file" /></td>
                    <td><form:errors path="file" cssClass="error" /></td>
                </tr>
                <tr>
                    <td colspan="3"><input type="submit" class="btn btn-success" /></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>

<%@ include file="Footer.jsp" %>