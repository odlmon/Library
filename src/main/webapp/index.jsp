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
                <h3>
                    <a href="${pageContext.request.contextPath}/library/sign-in">Sign In</a>
                    or
                    <a href="${pageContext.request.contextPath}/library/sign-up">Sign Up</a>
                </h3>
            </div>
        </div>
    </div>
</div>
</body>

</html>
