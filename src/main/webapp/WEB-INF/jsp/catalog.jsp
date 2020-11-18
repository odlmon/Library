<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://unpkg.com/papercss@1.8.1/dist/paper.min.css">
    <title>Library: Catalog</title>
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
                    <li><a href="catalog/eng">Eng</a></li>
                    <li><a href="catalog/rus">Рус</a></li>
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
                <form>
                    <div class="text-center">
                        <h1>Catalog</h1>
                        <h2>~~~</h2>
                    </div>

                    <form action="${pageContext.request.contextPath}/library/catalog" method="get">
                        <c:if test="${sessionScope.user.role eq 'USER'}">
                            <div class="row flex-center form-group">
                                <input type="text" placeholder="Search" name="search">
                            </div>
                        </c:if>
                        <c:if test="${sessionScope.user.role eq 'LIBRARIAN'}">
                            <div class="row flex-center">
                                <button name="action" value="add">Добавить книгу</button>
                            </div>
                        </c:if>
                    </form>

                    <c:if test="${not empty requestScope.errorMessage}">
                        <div class="row flex-spaces">
                            <input class="alert-state" id="alert" type="checkbox">
                            <div class="alert alert-danger dismissible">
                                    ${requestScope.errorMessage}
                                <label class="btn-close" for="alert">X</label>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty requestScope.noElements}">
                        <div class="row flex-center">
                            <h4>${requestScope.noElements}</h4>
                        </div>
                    </c:if>

                    <div class="row flex-spaces">
                        <c:forEach var="book" items="${requestScope.books}">
                            <c:if test="${sessionScope.user.role eq 'USER'}">
                                <form action="${pageContext.request.contextPath}/library/orders" method="get">
                            </c:if>
                            <c:if test="${sessionScope.user.role eq 'LIBRARIAN'}">
                                <form action="${pageContext.request.contextPath}/library/catalog" method="get">
                            </c:if>
                                <div class="card margin-bottom" style="width: 40rem;">
                                    <div class="card-body">
                                        <h4 class="card-title">${book.title}</h4>
                                        <h5 class="card-subtitle">${book.author}</h5>
                                        <input type="hidden" name="bookId" value="${book.id}">
                                        <c:if test="${sessionScope.user.role eq 'USER'}">
                                            <c:if test="${book.count gt 0}">
                                                <button>Заказать</button>
                                            </c:if>
                                            <c:if test="${book.count eq 0}">
                                                <label class="paper-btn" for="modal-1">Заказать</label>
                                                <input class="modal-state" id="modal-1" type="checkbox">
                                                <div class="modal">
                                                    <label class="modal-bg" for="modal-1"></label>
                                                    <div class="modal-body">
                                                        <label class="btn-close" for="modal-1">X</label>
                                                        <h4 class="modal-title">Уууупс:)</h4>
                                                        <p class="modal-text">Кажется экземпляров этой книги не
                                                            осталось</p>
                                                        <label for="modal-1" class="paper-btn">Окей</label>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${sessionScope.user.role eq 'LIBRARIAN'}">
                                            <button class="btn-danger-outline" name="action" value="delete">
                                                Удалить книгу
                                            </button>
                                            <button name="action" value="edit">
                                                Отредактировать книгу
                                            </button>
                                        </c:if>
                                    </div>
                                </div>
                            </form>
                        </c:forEach>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>
</body>

</html>
