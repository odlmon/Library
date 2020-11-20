<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="text"/>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://unpkg.com/papercss@1.8.1/dist/paper.min.css">
    <title><fmt:message key="title_sign_up"/></title>
</head>

<body>
<nav class="border fixed split-nav">
    <div class="nav-brand">
        <h3><a href="sign-up"><fmt:message key="library"/></a></h3>
    </div>
    <div class="collapsible">
        <input id="collapsible1" type="checkbox" name="collapsible1">
        <button>
            <label for="collapsible1">
                <div class="bar1"></div>
                <div class="bar2"></div>
            </label>
        </button>
        <div class="collapsible-body">
            <ul class="inline">
                <custom:lang path="/library/sign-up"/>
            </ul>
        </div>
    </div>
</nav>

<div class="paper container">
    <div class="row flex-center">
        <div class="col-fill col">
            <div class="text-center">
                <h1><p><fmt:message key="library"/></p></h1>
                <h2>~~~</h2>
                <h3><p><fmt:message key="sign_up"/></p></h3>
            </div>

            <form action="${pageContext.request.contextPath}/library/sign-up" method="post">
                <c:if test="${not empty requestScope.errorMessage}">
                    <div class="row flex-spaces">
                        <input class="alert-state" id="alert" type="checkbox">
                        <div class="alert alert-danger dismissible">
                                ${requestScope.errorMessage}
                            <label class="btn-close" for="alert">X</label>
                        </div>
                    </div>
                </c:if>

                <input type="hidden" name="action" value="sign-up">

                <div class="row flex-center">
                    <div class="form-group margin-right">
                        <label for="firstName"><fmt:message key="field_first_name"/></label>
                        <input type="text" name="firstName" id="firstName">
                    </div>
                    <div class="form-group margin-left">
                        <label for="login"><fmt:message key="field_login"/></label>
                        <input type="text" name="login" id="login">
                    </div>
                </div>

                <div class="row flex-center">
                    <div class="form-group margin-right">
                        <label for="lastName"><fmt:message key="field_last_name"/></label>
                        <input type="text" name="lastName" id="lastName">
                    </div>
                    <div class="form-group margin-left">
                        <label for="password"><fmt:message key="field_password"/></label>
                        <input type="password" name="password" id="password">
                    </div>
                </div>

                <div class="row flex-center">
                    <input class="paper-btn col-3" type="submit" value="Submit">
                </div>
            </form>
        </div>
    </div>
</div>
</body>

</html>
