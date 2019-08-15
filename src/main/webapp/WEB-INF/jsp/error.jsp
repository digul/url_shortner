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
		<div class="error inner code">{{ code }}</div>
		<div class="error inner"><img alt="error image" src="/images/error.png"></div>
		<div class="error inner">{{ message }}</div>
	</div>
	<script src="https://cdn.jsdelivr.net/npm/vue@2.6.10/dist/vue.min.js" ></script>
	<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
	<script type="text/javascript" src="/js/common.js" ></script>
	<script type="text/javascript" src="/js/error.js" ></script>
	</body>
</html>