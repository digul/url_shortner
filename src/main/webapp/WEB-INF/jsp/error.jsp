<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>ERROR</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="/css/style.css" type="text/css" rel="stylesheet" />
</head>
<body>
	<div id="main">
		<div class="error inner code">${ error.code }</div>
		<div class="error inner"><img alt="error image" src="/images/error.png"></div>
		<div class="error inner">${ error.message }</div>
	</div>
	</body>
</html>