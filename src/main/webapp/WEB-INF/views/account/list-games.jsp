<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 8/12/2022
  Time: 8:22 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="cybersoft.javabackend.java18.game.utils.UrlUtils" %>
<%@ page import="cybersoft.javabackend.java18.game.utils.JspUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
    <title>Games của bạn</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>

<body>

<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #e3f2fd;">
    <a class="navbar-brand font-weight-bold" href="#">Trò Chơi Đoán Số</a>
    <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link font-weight-bold" href="<%=request.getContextPath() + UrlUtils.GAME%>">Game</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link font-weight-bold" href="<%=request.getContextPath() + UrlUtils.LIST_GAME%>">List
                    game</a>
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

<div class="container col-md-10">

    <table class="table">
        <h1 class="text-center">DANH SÁCH MÀN CHƠI</h1>
        <table class="table ">
            <thead class="thead-light">
            <tr class="text-center">
                <th scope="col">No</th>
                <th scope="col">Game ID</th>
                <th scope="col">Target</th>
                <th scope="col">Start time</th>
                <th scope="col">End time</th>
                <th scope="col">Guesses</th>
                <th scope="col">Active</th>
                <th scope="col">Complete</th>
                <th scope="col">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${games}" var="game" varStatus="loop">

                <tr class="${game.isCompleted ? 'table-success' : 'table-primary'} text-center">
                    <th scope="row">${loop.index+1}</th>
                    <td>${game.id}</td>
                    <td>
                            ${game.isCompleted ? game.targetNumber : "?"}
                    </td>
                    <td>${game.getTimeFormatted(game.startTime)}</td>
                    <td>${game.endTime == null ? 'Undefined' : game.getTimeFormatted(game.endTime)}</td>
                    <td>${game.guesses.size()}</td>
                    <td>
                        <c:choose>
                            <c:when test="${game.isActive}">
                                <a class="btn btn-warning"
                                   href="<%=request.getContextPath() + UrlUtils.DEACTIVATE_GAME%>?game-id=${game.id}"
                                   role="button">Deactivate</a>
                            </c:when>
                            <c:otherwise>
                                <a class="btn btn-success"
                                   href="<%=request.getContextPath() + UrlUtils.ACTIVATE_GAME%>?game-id=${game.id}"
                                   role="button">Activate</a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${game.isCompleted ? "Completed" : "Incomplete"}</td>
                    <td>
                        <a class="btn btn-primary"
                           href="<%=request.getContextPath() + UrlUtils.VIEW_GAME%>?game-id=${game.id}"
                           role="button">View</a>
                        <c:if test="${!game.isCompleted}">
                            <a class="btn btn-success"
                               href="<%=request.getContextPath() + UrlUtils.CONTINUE_GAME%>?game-id=${game.id}"
                               role="button">Continue</a>
                        </c:if>

                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <nav aria-label="Page navigation example">
            <form action="<%=request.getContextPath() + UrlUtils.LIST_GAME%>" method="post" class="pagination">

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
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
</body>

</html>