
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>

        <title>Revolance UI Reviewer</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Third parties CSSs -->
        <link href="${pageContext.request.contextPath}/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
        <link href="${pageContext.request.contextPath}/libs/smallipop/css/jquery.smallipop.min.css" rel="stylesheet" media="screen"/>
        <link href="${pageContext.request.contextPath}/libs/animate/css/animate.css" rel="stylesheet" media="screen"/>
        <!--<link href="libs/simplemodal/css/simplemodal.css" rel="stylesheet" media="screen"/>-->
        <link href="${pageContext.request.contextPath}/libs/smart-wizard/css/smart_wizard_vertical.css" rel="stylesheet" media="screen"></script>

        <!-- Third parties JSs -->
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>

        <script src="${pageContext.request.contextPath}/libs/bootstrap/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/libs/modernizr/js/modernizr.js"></script>
        <script src="${pageContext.request.contextPath}/libs/smallipop/js/jquery.smallipop.min.js"></script>
        <script src="${pageContext.request.contextPath}/libs/simplemodal/js/simplemodal.js"></script>
        <script src="${pageContext.request.contextPath}/libs/smart-wizard/js/jquery.smartWizard.js"></script>
        <script src="${pageContext.request.contextPath}/libs/jrumble/js/jquery.jrumble.1.3.min.js"></script>
        <script src="${pageContext.request.contextPath}/libs/js-jquery-spin/js/jQuery.Spin.js"></script>

        <!-- Revolance CSSs -->
        <link href="${pageContext.request.contextPath}/css/viewer.css" rel="stylesheet" media="screen"></script>
        <link href="${pageContext.request.contextPath}/css/viewer-page.css" rel="stylesheet" media="screen"></script>
        <link href="${pageContext.request.contextPath}/css/viewer-content-loader.css" rel="stylesheet" media="screen"></script>
        <link href="${pageContext.request.contextPath}/css/viewer-wizard.css" rel="stylesheet" media="screen"></script>
        <link href="${pageContext.request.contextPath}/css/viewer-search.css" rel="stylesheet" media="screen"></script>
        <link href="${pageContext.request.contextPath}/css/viewer-magnifier-glass.css" rel="stylesheet" media="screen"></script>

        <!-- Revolance JSs -->
        <script src="${pageContext.request.contextPath}/js/viewer.js"></script>
        <script src="${pageContext.request.contextPath}/js/viewer-list.js"></script>
        <script src="${pageContext.request.contextPath}/js/viewer-comparator.js"></script>
        <!--<script src="${pageContext.request.contextPath}/js/viewer-content-loader.js"></script>-->
        <script src="${pageContext.request.contextPath}/js/viewer-settings.js"></script>
        <!--<script src="${pageContext.request.contextPath}/js/viewer-wizard.js"></script>-->
        <script src="${pageContext.request.contextPath}/js/viewer-search.js"></script>
        <script src="${pageContext.request.contextPath}/js/viewer-downloader.js"></script>
        <!--<script src="${pageContext.request.contextPath}/js/viewer-magnifier-glass.js"></script>-->
        <script src="${pageContext.request.contextPath}/js/viewer-nav.js"></script>

        <style>
            .error {
                color: #ff0000;
            }

            .errorblock {
                color: #000;
                background-color: #ffEEEE;
                border: 3px solid #ff0000;
                padding: 8px;
                margin: 16px;
            }
        </style>
    </head>

    <body>

        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="brand" href="#">UI Viewer</a>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li><a href="${pageContext.request.contextPath}/application">Applications</a></li>
                            <li class="dropdown">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                  More<span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a href="${pageContext.request.contextPath}/licence">Licence</a></li>
                                    <li><a href="${pageContext.request.contextPath}/user-guide">User guide</a></li>
                                </ul>
                              </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="container">
            <div class="span10">

                <div class="page-title">
                    <h2>Application Comparison</h2>
                    <h4><span id="ref-tag">${refAppTag}</span> is compared to <span id="new-tag">${newAppTag}</span></h4>
                </div>

            </div>
        </div>

        ${content}

        <script>
            $.defineModalViewPageBaseElementBehavior();
        </script>

    </body>

    <footer> &#169; 2012-2013 Revo Lance. <br /> Revo Lance logo or brand are trademarks. </footer>

</html>