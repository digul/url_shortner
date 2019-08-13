<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>URL SHORTENER</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="/css/style.css" type="test/css" rel="stylesheet" />
</head>
<body>
	<h3>INPUT YOUR URL</h3>
	<div id="main">
		{{ realUrl }} : {{ shortUrl }}
	</div>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/vue@2.6.10/dist/vue.min.js" ></script>
	<script type="text/javascript" src="/js/main.js" ></script>
</body>
</html>