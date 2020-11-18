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
    <title>Library: Sign In</title>
</head>

<body>
<div class="paper container">
    <div class="row flex-center">
        <div class="col-fill col">
            <div class="text-center">
                <h1>Library</h1>
                <h2>~~~</h2>
                <h3>Sign In</h3>
            </div>

            <form action="${pageContext.request.contextPath}/library/sign-in" method="post">
                <c:if test="${not empty requestScope.errorMessage}">
                    <div class="row flex-spaces">
                        <input class="alert-state" id="alert" type="checkbox">
                        <div class="alert alert-danger dismissible">
                                ${requestScope.errorMessage}
                            <label class="btn-close" for="alert">X</label>
                        </div>
                    </div>
                </c:if>

                <input type="hidden" name="action" value="sign-in">

                <div class="row flex-center">
                    <div class="form-group">
                        <label for="login">Login</label>
                        <input type="text" name="login" id="login">
                    </div>
                </div>

                <div class="row flex-center">
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" name="password" id="password">
                    </div>
                </div>

                <div class="row flex-center">
                    <input class="paper-btn" type="submit" value="Submit">
                </div>
            </form>

            <div class="row flex-center">
                <h4>Not registered? Sign up</h4>
            </div>
            <div class="row flex-center">
                <form action="${pageContext.request.contextPath}/library/sign-up">
                    <button>Sign Up</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>

</html>
