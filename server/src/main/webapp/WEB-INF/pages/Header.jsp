<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>

    <head>

        <title>Revolance UI Monitoring</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- JQuery -->
        <script src="${pageContext.request.contextPath}/libs/jquery/js/jquery.min.js"></script>

        <!-- Powertip -->
        <link href="${pageContext.request.contextPath}/libs/powertip/css/powertip.min.css" rel="stylesheet" media="screen"/>
        <script src="${pageContext.request.contextPath}/libs/powertip/js/powertip.min.js"></script>

        <!-- Twitter Bootstrap-->
        <link href="${pageContext.request.contextPath}/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
        <script src="${pageContext.request.contextPath}/libs/bootstrap/js/bootstrap.min.js"></script>

        <!-- Revolance CSSs -->
        <link href="${pageContext.request.contextPath}/css/viewer.css" rel="stylesheet" media="screen"></script>
        <link href="${pageContext.request.contextPath}/css/viewer-page.css" rel="stylesheet" media="screen"></script>
        <link href="${pageContext.request.contextPath}/css/viewer-search.css" rel="stylesheet" media="screen"></script>



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
                    <div class="row">
                        <div class="span12">
                            <a class="brand" href="#">RevoLance UI Monitoring</a>
                            <div class="nav-collapse collapse pull-right">
                                <ul class="nav">
                                    <li><a href="/ui-monitoring-server/applications">Applications</a></li>
                                    <li><a href="/ui-monitoring-server/reviews">Reviews</a></li>
                                    <li class="dropdown">
                                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                          More<span class="caret"></span>
                                        </a>
                                        <ul class="dropdown-menu">
                                            <li><a href="/licence">Licence</a></li>
                                            <li><a href="/user-guide">User guide</a></li>
                                        </ul>
                                      </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="content" class="container">
