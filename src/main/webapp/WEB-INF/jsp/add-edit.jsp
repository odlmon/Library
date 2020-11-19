<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="text"/>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://unpkg.com/papercss@1.8.1/dist/paper.min.css">
    <c:if test="${requestScope.type eq 'add'}">
        <title><fmt:message key="title_add_book"/></title>
    </c:if>
    <c:if test="${requestScope.type eq 'edit'}">
        <title><fmt:message key="title_edit_book"/></title>
    </c:if>
</head>

<body>
<header>
    <nav class="border fixed split-nav">
        <div class="nav-brand">
            <h3><a href="catalog"><fmt:message key="library"/></a></h3>
        </div>
        <div class="collapsible">
            <input id="collapsible1" type="checkbox" name="collapsible1">
            <button>
                <label for="collapsible1">
                    <div class="bar1"></div>
                    <div class="bar2"></div>
                    <div class="bar3"></div>
                    <div class="bar4"></div>
                </label>
            </button>
            <div class="collapsible-body">
                <ul class="inline">
                    <li><a href="orders"><fmt:message key="orders"/></a></li>
                    <custom:lang path="/library/catalog"/>
                    <li>
                        <form action="${pageContext.request.contextPath}/library/sign-out">
                            <button class="btn-small btn-danger-outline">
                                <fmt:message key="sign_out"/>
                            </button>
                        </form>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>

<main>
    <div class="paper container">
        <div class="row flex-center">
            <div class="col-fill col">
                <div class="text-center">
                    <c:if test="${requestScope.type eq 'add'}">
                        <h1><fmt:message key="add_book"/></h1>
                    </c:if>
                    <c:if test="${requestScope.type eq 'edit'}">
                        <h1><fmt:message key="edit_bok"/></h1>
                    </c:if>
                    <h2>~~~</h2>
                </div>

                <c:if test="${requestScope.type eq 'add'}">
                    <form action="${pageContext.request.contextPath}/library/addBook" method="get">
                </c:if>
                <c:if test="${requestScope.type eq 'edit'}">
                    <form action="${pageContext.request.contextPath}/library/editBook" method="get">
                </c:if>
                    <c:if test="${not empty requestScope.errorMessage}">
                        <div class="row flex-spaces">
                            <input class="alert-state" id="alert" type="checkbox">
                            <div class="alert alert-danger dismissible">
                                    ${requestScope.errorMessage}
                                <label class="btn-close" for="alert">X</label>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${requestScope.type eq 'edit'}">
                        <input type="hidden" name="id" value="${requestScope.book.id}">
                    </c:if>
                    <div class="row flex-center">
                        <div class="form-group">
                            <label for="title"><fmt:message key="field_title"/></label>
                            <c:if test="${requestScope.type eq 'add'}">
                                <input type="text" name="title" id="title">
                            </c:if>
                            <c:if test="${requestScope.type eq 'edit'}">
                                <input type="text" name="title" id="title" value="${requestScope.book.title}">
                            </c:if>
                        </div>
                    </div>
                    <div class="row flex-center">
                        <div class="form-group">
                            <label for="author"><fmt:message key="field_author"/></label>
                            <c:if test="${requestScope.type eq 'add'}">
                                <input type="text" name="author" id="author">
                            </c:if>
                            <c:if test="${requestScope.type eq 'edit'}">
                                <input type="text" name="author" id="author" value="${requestScope.book.author}">
                            </c:if>
                        </div>
                    </div>
                    <div class="row flex-center">
                        <div class="form-group">
                            <label for="count"><fmt:message key="field_count"/></label>
                            <c:if test="${requestScope.type eq 'add'}">
                                <input type="text" name="count" id="count">
                            </c:if>
                            <c:if test="${requestScope.type eq 'edit'}">
                                <input type="text" name="count" id="count" value="${requestScope.book.count}">
                            </c:if>
                        </div>
                    </div>
                    <div class="row flex-center">
                        <button><fmt:message key="confirm"/></button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>
</body>

</html>

