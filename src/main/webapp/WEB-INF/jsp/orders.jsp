<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://unpkg.com/papercss@1.8.1/dist/paper.min.css">
    <title>Library: Orders</title>
</head>

<body>
<header>
    <nav class="border fixed split-nav">
        <div class="nav-brand">
            <h3><a href="catalog">Library</a></h3>
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
                    <li><a href="orders">Заказы</a></li>
                    <li><a href="eng">Eng</a></li>
                    <li><a href="rus">Рус</a></li>
                    <li>
                        <form action="${pageContext.request.contextPath}/library/sign-out">
                            <button class="btn-small btn-danger-outline">Signout</button>
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
                    <h1>Orders</h1>
                    <h2>~~~</h2>
                </div>

                <c:if test="${not empty requestScope.errorMessage}">
                    <div class="row flex-spaces">
                        <input class="alert-state" id="alert" type="checkbox">
                        <div class="alert alert-danger dismissible">
                                ${requestScope.errorMessage}
                            <label class="btn-close" for="alert">X</label>
                        </div>
                    </div>
                </c:if>

                <div class="row flex-spaces tabs">
                    <input id="tab1" type="radio" name="tabs" checked>
                    <label for="tab1">В обработке</label>

                    <input id="tab2" type="radio" name="tabs">
                    <label for="tab2">Активные</label>

                    <div class="content" id="content1">
                        <c:if test="${not empty requestScope.noProcessing}">
                            <div class="row flex-center">
                                <h4>${requestScope.noProcessing}</h4>
                            </div>
                        </c:if>

                        <c:forEach var="order" items="${requestScope.orders}">
                            <c:if test="${order.status == 'PROCESSING'}">
                                <form action="${pageContext.request.contextPath}/library/orders" method="get">
                                    <div class="card margin-bottom" style="width: 40rem;">
                                        <div class="card-body">
                                            <h4 class="card-title">${order.book.title}</h4>
                                            <h5 class="card-subtitle">${order.book.author}</h5>
                                            <c:if test="${sessionScope.user.role eq 'USER'}">
                                                <input type="hidden" name="action" value="cancel">
                                                <input type="hidden" name="bookId" value="${order.book.id}">
                                                <button class="btn-danger-outline">Отменить заказ</button>
                                            </c:if>
                                            <c:if test="${sessionScope.user.role eq 'LIBRARIAN'}">
                                                <p class="card-text">
                                                    Запрашивает ${order.user.firstName} "${order.user.login}" ${order.user.lastName}
                                                </p>
                                                <input type="hidden" name="userLogin" value="${order.user.login}">
                                                <input type="hidden" name="bookId" value="${order.book.id}">
                                                <button name="action" value="cancel" class="btn-danger-outline">
                                                    Отменить заказ
                                                </button>
                                                <button name="action" value="reading_room" class="btn-primary-outline">
                                                    Выдать в читальный зал
                                                </button>
                                                <button name="action" value="subscription" class="btn-secondary-outline">
                                                    Выдать по абонементу
                                                </button>
                                            </c:if>
                                        </div>
                                    </div>
                                </form>
                            </c:if>
                        </c:forEach>
                    </div>

                    <div class="content" id="content2">
                        <c:if test="${not empty requestScope.noActive}">
                            <div class="row flex-center">
                                <h4>${requestScope.noActive}</h4>
                            </div>
                        </c:if>

                        <c:forEach var="order" items="${requestScope.orders}">
                            <c:if test="${order.status != 'PROCESSING'}">
                                <form action="${pageContext.request.contextPath}/library/orders" method="get">
                                    <div class="card margin-bottom" style="width: 40rem;">
                                        <div class="card-body">
                                            <h4 class="card-title">${order.book.title}
                                                <c:if test="${order.status eq 'READING_ROOM'}">
                                                    <span class="badge">Читальный зал</span>
                                                </c:if>
                                                <c:if test="${order.status eq 'SUBSCRIPTION'}">
                                                    <span class="badge secondary">Абонемент</span>
                                                </c:if>
                                            </h4>
                                            <h5 class="card-subtitle">${order.book.author}</h5>
                                            <c:if test="${sessionScope.user.role eq 'USER'}">
                                                <input type="hidden" name="action" value="giveback">
                                                <input type="hidden" name="bookId" value="${order.book.id}">
                                                <button class="btn-success-outline">Вернуть книгу</button>
                                            </c:if>
                                            <c:if test="${sessionScope.user.role eq 'LIBRARIAN'}">
                                                <p class="card-text">
                                                        ${order.user.firstName} "${order.user.login}" ${order.user.lastName} имеет на руках
                                                </p>
                                            </c:if>
                                        </div>
                                    </div>
                                </form>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
</body>

</html>

