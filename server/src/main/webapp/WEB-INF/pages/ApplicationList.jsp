<%@ include file="Header.jsp" %>

    <div class="container">
        <div class="row">
            <div class="page-title">
                <h2>Application List</h2>
            </div>
            <div class="page-toolbar">
                <a href="${pageContext.request.contextPath}/applications/declare"><button class="btn" id="add">Add</button></a>
                <a href="#"><button class="btn" id="del" disabled>Remove</button></a>
                <a href="#"><button class="btn" id="application-compare" disabled>Compare</button></a>
            </div>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>&nbsp;&nbsp;</th>
                        <th>Tag</th>
                        <th>Domain</th>
                        <th>Browser Type</th>
                        <th>Browser Width</th>
                        <th>Browser Height</th>
                        <th>Exploration Start</th>
                        <th>Exploration End</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${contentList}">
                        <tr>
                            <td><input type="checkbox"></td>
                            <td><div class="tag"><a href="${pageContext.request.contextPath}/applications/${item.tag}">${item.tag}</a></div></td>
                            <td><div class="domain">${item.user.domain}</div></td>
                            <td><div class="browserType">${item.user.browserType}</div></td>
                            <td><div class="browserWidth">${item.user.browserWidth}</div></td>
                            <td><div class="browserHeight">${item.user.browserHeight}</div></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/application-list.js"></script>

<%@ include file="Footer.jsp" %>