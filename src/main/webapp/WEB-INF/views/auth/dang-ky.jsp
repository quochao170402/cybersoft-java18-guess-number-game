<%@ page import="cybersoft.javabackend.java18.game.utils.UrlUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <title>Đăng ký</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<div>
    <div class="container">
        <div class="col-md-6 offset-md-3">
            <nav class="navbar navbar-expand-lg navbar-dark bg-dark justify-content-center">
                <a class="navbar-brand" href="<%=request.getContextPath() + UrlUtils.GAME%>">Trò chơi</a>
                <a class="navbar-brand" href="<%=request.getContextPath() + UrlUtils.RANK%>">Xếp hạng</a>
                <a class="navbar-brand" href="<%=request.getContextPath() + UrlUtils.SIGN_IN%>">Đăng nhập</a>
            </nav>
        </div>
        <h1 class="text-center text-primary mt-5">ĐĂNG KÝ</h1>
        <div class="row">
            <div class="col-md-4 offset-md-4">
                <form action="<%=request.getContextPath() + UrlUtils.SIGN_UP%>" method="post">
                    <c:if test="${errors != null}">
                        <div class="alert alert-danger" role="alert">
                                ${errors}
                        </div>
                    </c:if>
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input type="text" class="form-control" name="name" id="name" aria-describedby="helpId"
                               required value="${name}">
                        <small id="helpId" class="form-text text-muted">Tên thật có dấu</small>
                    </div>
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" class="form-control" name="username" id="username"
                               aria-describedby="usernameHelp"
                               required value="${username}">
                        <small id="usernameHelp" class="form-text text-muted">Username</small>
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" name="password" id="password"
                               aria-describedby="passwordHelp"
                               required value="${password}">
                        <small id="passwordHelp" class="form-text text-muted">Mật khẩu</small>
                    </div>
                    <button type="submit" class="btn btn-primary">Đăng ký</button>
                </form>
            </div>
        </div>
    </div>
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