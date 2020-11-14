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
    <title>My Paper Site</title>
</head>

<body>
<header>
    <nav class="border fixed split-nav">
        <div class="nav-brand">
            <h3><a href="catalog.html">Library</a></h3>
        </div>
        <div class="collapsible">
            <input id="collapsible1" type="checkbox" name="collapsible1">
            <button>
                <label for="collapsible1">
                    <div class="bar1"></div>
                    <div class="bar2"></div>
                    <div class="bar3"></div>
                    <div class="bar4"></div>
                    <div class="bar5"></div>
                </label>
            </button>
            <div class="collapsible-body">
                <ul class="inline">
                    <li>[</li>
                    <li><a href="catalog.html/eng">Eng</a></li>
                    <li><a href="catalog.html/rus">Рус</a></li>
                    <li>]</li>
                    <button class="btn-small btn-danger-outline">Sign out</button>
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

                    <div class="row flex-center form-group">
                        <input type="text" placeholder="Search">
                    </div>

                    <div class="row flex-spaces">
                        <div class="card margin-bottom" style="width: 40rem;">
                            <div class="card-body">
                                <h4 class="card-title">Some cool title of a book</h4>
                                <h5 class="card-subtitle">C. Author</h5>
                                <button>Заказать</button>
                            </div>
                        </div>

                        <div class="card margin-bottom" style="width: 40rem;">
                            <div class="card-body">
                                <h4 class="card-title">Психобльница в руках пациентов</h4>
                                <h5 class="card-subtitle">Фадеева Е.Е.</h5>
                                <button>Заказать</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>
</body>

</html>
