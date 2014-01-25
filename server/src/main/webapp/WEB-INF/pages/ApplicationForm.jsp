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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="Header.jsp" %>

<div class="container">
    <div class="row">
        <div class="span12">
            <form:form method="POST" class="form-horizontal" commandName="content" action="/ui-monitoring-server/applications" >
                <fieldset>
                    <legend>Explore an application</legend>
                    <form:errors path="*" cssClass="errorblock" element="div" />
                    <div class="span5">
                        <div class="control-group">
                            <label class="control-label" for="tag">Tag</label>
                            <div class="controls">
                              <input type="text" path="tag" name="tag" id="tag" placeholder="Enter the application tag" />
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
                            <label class="control-label" for="page">Home url</label>
                            <div class="controls">
                              <input type="text" path="page" name="page" id="page" placeholder="Enter the application home page" />
                              <form:errors path="id" cssClass="error" />
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="isSecured" class="span4">Does this application requires to login ?</label>
                            <div class="controls">
                                <label class="radio"><input name="isSecured" type="radio" value="yes" id="yes" />Yes</label>
                                <label class="radio"><input name="isSecured" type="radio" value="no" id="no" checked />no</label>
                                <form:errors path="isSecured" cssClass="error" />
                            </div>
                        </div>
                        <div id="login-controls">
                            <div class="control-group">
                                <label class="control-label" for="login">Login</label>
                                <div class="controls" id="login-controls">
                                  <input type="text" path="login" name="login" id="login" placeholder="Enter the user login" />
                                  <form:errors path="login" cssClass="error" />
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="password">Password</label>
                                <div class="controls" id="password-controls">
                                  <input type="text" path="password" name="password" id="password" placeholder="Enter the passwd of the user" />
                                  <form:errors path="password" cssClass="error" />
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="applicationModel">Application model</label>
                                <div class="controls">
                                  <input type="file" path="applicationModel" name="applicationModel" id="applicationModel" />
                                  <form:errors path="applicationModel" cssClass="error" />
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="applicationClassName">Application model <br />class name</label>
                                <div class="controls">
                                  <input type="text" path="applicationClassName" name="applicationClassName" id="applicationClassName" />
                                  <form:errors path="applicationClassName" cssClass="error" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="span6">
                        <div class="control-group">
                            <label class="control-label" for="resolution">Resolution</label>
                            <div class="controls">
                                <c:forEach var="resolution" items="${resolutions}">
                                <label class="radio"><input name="resolution" type="radio" value="${resolution}" id="${resolution}" />${resolution}</label>
                                </c:forEach>
                                <form:errors path="resolution" cssClass="error" />
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="browserType">Browser</label>
                            <div class="controls">
                                <c:forEach var="browser" items="${browsers}">
                                <label class="radio"><input name="browserType" type="radio" value="${browser.name}" id="${browser.name}" />${browser.name}</label>
                                </c:forEach>
                                <form:errors path="browserType" cssClass="error" />
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="options">Options</label>
                            <div class="controls" id="options">
                              <label class="checkbox"><input name="followLinks" type="checkbox" id="followButtons" />follow links</label>
                              <label class="checkbox"><input name="followButtons" type="checkbox" id="followLinks" />follow buttons</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="span12">
                    <div class="form-actions">
                      <input type="submit" class="btn btn-success" id="start-exploration" value="Start exploration"/>
                      <a class="cancel" id="cancel" href="">cancel</a>
                    </div>
                </div>
                </fieldset>
            </form:form>
        </div>
        <!--
        <div class="span6">
            <form:form method="POST" commandName="content" action="${pageContext.request.contextPath}/applications" enctype="multipart/form-data" >
                <fieldset>
                    <legend>Add application</legend>
                    <form:errors path="*" cssClass="errorblock" element="div" />
                    <div class="control-group">
                        <label class="control-label" for="tag">Tag</label>
                        <div class="controls">
                          <input type="text" path="tag" name="tag" id="tag" placeholder="Enter the application tag" />
                          <form:errors path="id" cssClass="error" />
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="file">Select the file to upload</label>
                        <div class="controls">
                          <input type="file" path="file" name="file" id="file" />
                          <form:errors path="file" cssClass="error" />
                        </div>
                    </div>
                    <div class="form-actions">
                        <input type="submit" class="btn btn-success" value="Import"/>
                        <a class="cancel" id="cancel" href="#">cancel</a>
                    </div>
                </fieldset>
            </form:form>
        </div>
        -->
    </div>
</div>
<script>
    $("#login-controls").hide();
    $("input[type=radio][name=isSecured]").change(function(){
        if($(this).attr('id')=="yes")
        {
            $("#login-controls").show();
            $("#login-field-controls").show();
            $("#password-controls").show();
            $("#password-field-controls").show();
        }
        else
        {
            $("#login-controls").hide();
            $("#login-field-controls").hide();
            $("#password-controls").hide();
            $("#password-field-controls").hide();
        }
    });
</script>
<%@ include file="Footer.jsp" %>