<%@ page import="cybersoft.javabackend.java18.game.utils.UrlUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
    <title>Bảng xếp hạng</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>

<body>
<div class="container col-md-6">
    <div style="margin-bottom: 20px;">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark justify-content-center">
            <a class="navbar-brand" href="<%=request.getContextPath() + UrlUtils.GAME%>">Trò chơi</a>
            <a class="navbar-brand" href="<%=request.getContextPath() + UrlUtils.RANK%>">Xếp hạng</a>
        </nav>
    </div>

    <table class="table">
        <h1 class="text-center">Bảng xếp hạng người chơi</h1>
        <table class="table">
            <thead class="thead-light">
            <tr>
                <th scope="col">Thứ tự</th>
                <th scope="col">ID</th>
                <th scope="col">Người chơi</th>
                <th scope="col">Số lần đoán</th>
                <th scope="col">Thời gian (phút)</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${games}" var="game" varStatus="loop">
                <tr class="table-success">
                    <th scope="row">${loop.index+1}</th>
                    <td>${game.id}</td>
                    <td>${game.username}</td>
                    <td>${game.guesses.size()}</td>
                    <td>${game.getTimeFormatted()}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
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