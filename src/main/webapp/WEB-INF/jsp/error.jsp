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
	<div id="error">
		<div class="inner code">{{ code }}</div>
		<div class="inner"><img alt="error image" src="/images/error.png"></div>
		<div class="inner">{{ message }}</div>
	</div>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js" ></script>
	<script type="text/javascript" src="/js/error.js" ></script>
</body>
</html>