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
    <title>Library: Sign Up</title>
</head>

<body>
<div class="paper container">
    <div class="row flex-center">
        <div class="col-fill col">
            <div class="text-center">
                <h1>Library</h1>
                <h2>~~~</h2>
                <h3>Sign Up</h3>
            </div>

            <form action="${pageContext.request.contextPath}/library" method="post">
                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="row flex-spaces">
                        <input class="alert-state" id="alert-5" type="checkbox">
                        <div class="alert alert-danger dismissible">
                                ${sessionScope.errorMessage}
                            <label class="btn-close" for="alert-5">X</label>
                        </div>
                    </div>
                </c:if>

                <input type="hidden" name="action" value="sign-up">

                <div class="row flex-center">
                    <div class="form-group margin-right">
                        <label for="firstName">First Name</label>
                        <input type="text" name="firstName" id="firstName">
                    </div>
                    <div class="form-group margin-left">
                        <label for="login">Login</label>
                        <input type="text" name="login" id="login">
                    </div>
                </div>

                <div class="row flex-center">
                    <div class="form-group margin-right">
                        <label for="lastName">LastName</label>
                        <input type="text" name="lastName" id="lastName">
                    </div>
                    <div class="form-group margin-left">
                        <label for="password">Password</label>
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
