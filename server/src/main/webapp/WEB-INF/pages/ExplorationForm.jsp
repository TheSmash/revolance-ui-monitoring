<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="Header.jsp" %>

<div class="container">
    <div class="span10">

        <form:form method="POST" class="form-horizontal" commandName="content" action="${pageContext.request.contextPath}/explorations">
            <fieldset>
                <legend>Exploration request</legend>
                <form:errors path="*" cssClass="errorblock" element="div" />
                <div class="control-group">
                    <label class="control-label" for="tag">Tag</label>
                    <div class="controls">
                      <input type="text" path="tag" name="tag" id="tag" placeholder="Enter the application tag" />
                      <form:errors path="id" cssClass="error" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="url">Url</label>
                    <div class="controls">
                      <input type="text" path="url" name="url" id="url" placeholder="Enter the application url" />
                      <form:errors path="id" cssClass="error" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="domain">Domain</label>
                    <div class="controls">
                      <input type="text" path="domain" name="domain" id="domain" placeholder="Enter the application domain" />
                      <form:errors path="id" cssClass="error" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="resolution">Resolution</label>
                    <div class="controls">
                        <label class="radio"><input name="resolution" type="radio" value="320x480" />320x480</label>
                        <label class="radio"><input name="resolution" type="radio" value="800x600" />800x600</label>
                        <label class="radio"><input name="resolution" type="radio" value="1280x800" />1280x800</label>
                        <label class="radio"><input name="resolution" type="radio" value="1600x1200" />1600x1200</label>
                        <label class="radio"><input name="resolution" type="radio" value="1920x1080" />1920x1080</label>
                        <form:errors path="id" cssClass="error" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="browserType">Browser</label>
                    <div class="controls">
                        <label class="radio"><input name="browserType" type="radio" value="Firefox" />Firefox</label>
                        <label class="radio"><input name="browserType" type="radio" value="Chrome" disabled/>Chrome</label>
                        <label class="radio"><input name="browserType" type="radio" value="IExplorer" disabled/>IExplorer</label>
                        <form:errors path="id" cssClass="error" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="options">Options</label>
                    <div class="controls" id="options">
                      <label class="checkbox"><input name="followLinks" type="checkbox" id="followButtons" />follow links</label>
                      <label class="checkbox"><input name="followButtons" type="checkbox" id="followLinks" />follow buttons</label>
                    </div>
                </div>
                <div class="form-actions">
                  <input type="submit" class="btn btn-success" value="Start exploration"/>
                  <a class="cancel" id="cancel" href="#">cancel</a>
                </div>
            </fieldset>
        </form:form>
    </div>
</div>

<%@ include file="Footer.jsp" %>