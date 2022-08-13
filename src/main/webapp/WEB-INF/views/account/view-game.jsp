<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 8/12/2022
  Time: 2:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="cybersoft.javabackend.java18.game.utils.UrlUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>
    <title>View game</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #e3f2fd;">
    <a class="navbar-brand font-weight-bold" href="<%=request.getContextPath() + UrlUtils.ROOT%>">Trò Chơi Đoán Số</a>
    <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
        <ul class="navbar-nav">
            <li class="nav-item ">
                <a class="nav-link font-weight-bold" href="<%=request.getContextPath() + UrlUtils.GAME%>">Game</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link font-weight-bold"
                   href="<%=request.getContextPath() + UrlUtils.LIST_GAME%>">List game</a>
            </li>
            <li class="nav-item">
                <a class="nav-link font-weight-bold"
                   href="<%=request.getContextPath() + UrlUtils.RANK%>">Ranking</a>
            </li>
        </ul>
    </div>
    <div class="nav-item dropdown">
        <a class="nav-link dropdown-toggle font-weight-bold" href="#" role="button" data-toggle="dropdown"
           aria-expanded="false">
            ${sessionScope.currentUser.name}
        </a>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="<%=request.getContextPath() + UrlUtils.LOG_OUT%>">Đăng xuất</a></li>
        </ul>
    </div>
</nav>
<div class="container col-md-6">
    <div class="row justify-content-center mt-5">
        <table class="table">
            <h1 class="text-center">DANH SÁCH LẦN ĐOÁN</h1>
            <table class="table">
                <thead class="thead-light">
                <tr>
                    <th scope="col">No</th>
                    <th scope="col">Number</th>
                    <th scope="col">Result</th>
                    <th scope="col">Time</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${guesses}" var="guess" varStatus="loop">
                    <c:choose>
                        <c:when test="${guess.result == 0}">
                            <tr class="table-success">
                                <th scope="row">${loop.index+1}</th>
                                <td>${guess.number}</td>
                                <td>PINGO!!!!</td>
                                <td>${guess.getTimeFormatted()}</td>
                            </tr>
                        </c:when>
                        <c:when test="${guess.result == -1}">
                            <tr class="table-warning">
                                <th scope="row">${loop.index+1}</th>
                                <td>${guess.number}</td>
                                <td>Số của bạn nhỏ hơn số ngẫu nhiên</td>
                                <td>${guess.getTimeFormatted()}</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr class="table-danger">
                                <th scope="row">${loop.index+1}</th>
                                <td>${guess.number}</td>
                                <td>Số của bạn lớn hơn số ngẫu nhiên</td>
                                <td>${guess.getTimeFormatted()}</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                </tbody>
            </table>
            <nav aria-label="Page navigation example">
                <form action="<%=request.getContextPath() + UrlUtils.VIEW_GAME%>?game-id=${id}" method="post"
                      class="pagination">

                    <input type="hidden" value="${currentPage}" name="currentPage">

                    <li class="page-item">
                        <input type="submit" class="page-link btn" value="Previous"
                               name="pagination" ${currentPage == 1 ? 'disabled': ''} >
                    </li>

                    <li class="page-item">
                        <input type="submit" class="page-link btn" value="Next"
                               name="pagination" ${currentPage == totalPage ? 'disabled' : ''}>
                    </li>
                </form>
            </nav>
        </table>
    </div>
</div>
</body>
</html>
