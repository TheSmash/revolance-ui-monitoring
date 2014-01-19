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
<%@ include file="Header.jsp" %>

<div class="container">
    <div class="row">
        <div class="span12">
            <form method="POST" class="form-horizontal" commandName="content" action="/ui-monitoring-server/reviews" >
                <fieldset>
                    <legend>Review creation</legend>
                    <form:errors path="*" cssClass="errorblock" element="div" />
                    <div class="control-group">
                        <label class="control-label" for="applicationId">Target application</label>
                        <div class="controls">
                            <select id="applicationId" class="span4" path="applicationId" name="applicationId">
                                <option></option>
                                <c:forEach var="application" items="${applications}">
                                    <option>${application}</option>
                                </c:forEach>
                            </select>
                            <form:errors path="applicationId" cssClass="error" />
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="firstVersion" class="span4">Is this the first version ?</label>
                        <div class="controls">
                            <label class="radio"><input name="firstVersion" type="radio" value="yes" id="yes" checked />Yes</label>
                            <label class="radio"><input name="firstVersion" type="radio" value="no" id="no" />no</label>
                            <form:errors path="firstVersion" cssClass="error" />
                        </div>
                    </div>
                    <div class="control-group" id="previous-applications" style="display:none">
                        <label class="control-label" for="referenceApplicationId">Reference application</label>
                        <div class="controls">
                            <select id="referenceApplicationId" class="span4" path="referenceApplicationId" name="referenceApplicationId">
                                <option></option>
                                <c:forEach var="application" items="${applications}">
                                    <option>${application}</option>
                                </c:forEach>
                            </select>
                            <form:errors path="referenceApplicationId" cssClass="error" />
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="reviewer">Reviewer</label>
                        <div class="controls">
                          <input type="text" class="span4" path="reviewer" name="reviewer" id="reviewer" placeholder="Enter your name" />
                          <form:errors path="reviewer" cssClass="error" />
                        </div>
                    </div>
                    <div class="form-actions">
                      <input type="submit" class="btn btn-success" id="create-review" value="Create review"/>
                      <a class="cancel" id="cancel" href="">cancel</a>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
</div>
<script>
    $("input[type=radio]").change(function(){
        if($(this).attr('id')=="yes")
        {
            $("#previous-applications").hide();
            $("#referenceApplicationId").val($("#applicationId").val());
        }
        else
        {
            $("#previous-applications").show();
        }
    });
</script>
<%@ include file="Footer.jsp" %>