<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>URL SHORTENER</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://npmcdn.com/keen-ui@1.2.0/dist/keen-ui.min.css">
	<link href="/css/style.css?ver=${pageContext.session.creationTime }" type="text/css" rel="stylesheet" />
</head>
<body><div class="outline">
	<div id="main"><section class="page page--ui-textbox">
		<input type="hidden" id="requestURL" value="${pageContext.request.getRequestURL() }" />
		<input type="hidden" id="requestURI" value="${pageContext.request.getRequestURI() }" />
		<ui-tabs >
			<ui-tab fullwidth title="URL SHORTENER"  >
				<ui-textbox label="REAL URL" placeholder="http://" v-model="url.realUrl"  @keydown.enter="gen" ></ui-textbox>
				<div class="btn">
					<ui-button color="green" :size="20" @click="gen" >GEN</ui-button>
				</div>
				
				<ui-textbox ref="shortUrl"  readonly label="SHORT URL"  :value="host + url.shortUrl" ></ui-textbox>
				<div class="btn">
					<ui-button color="green" :size="20" v-show="afterGen" @click="copy()" >COPY</ui-button>
				</div>
				
				<div class="status">
					<ui-alert :dismissible="false" >{{ status }}</ui-alert>
				</div>
			</ui-tab>
		 </ui-tabs>
		
	</section></div>
	<script src="https://cdn.jsdelivr.net/npm/vue@2.6.10/dist/vue.min.js" ></script>
	<script src="https://npmcdn.com/keen-ui@1.2.0/dist/keen-ui.min.js" ></script>
	<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
	<script type="text/javascript" src="/js/main.js?ver=${pageContext.session.creationTime }" ></script>
</div></body>
</html>