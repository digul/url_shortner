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
	<h3>URL SHORTENER</h3>
	<div id="home">
		<div class="textbox">
			<label>REAL URL</label>
			<input id="realUrl" type="text" :value="url.realUrl"/>
		</div>
		<div class="btn"><button @click="gen" >GEN</button></div>
		<div class="result">
			<label>SHORT URL : {{ host }}</label>
			<input id="shortUrl"  type="text" :value="url.shortUrl"  readonly />
		</div>
		<div class="btn">
			<button v-show="afterGen" @click="copy()" >COPY</button>
		</div>
		<div class="btn">
			<button v-show="afterGen" @click="go($event, url.shortUrl)">GO</button>
		</div>
		<div class="status">
			<span v-text="status"/>
		</div>
		<input type="hidden" id="genedUrl" :value="host + url.shortUrl" />
	</div>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/vue@2.6.10/dist/vue.min.js" ></script>
	<script type="text/javascript" src="/js/home.js?ver=gg" ></script>
</body>
</html>