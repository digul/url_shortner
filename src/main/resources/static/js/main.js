var app = new Vue({
	el:'#main',
	data: {
		url: {
			shortUrl: ''
		},
		status: 'INPUT YOUR URL',
		afterGen : false
	},
	computed: {
		host: function(){
			var requestURL = document.querySelector("#requestURL").getAttribute("value");
			var requestURI = document.querySelector("#requestURI").getAttribute("value");
			return requestURL.replace(requestURI, '/');
		} 
	},
	methods: {
		gen: function() {
			var realUrl = app.url.realUrl;
			axios.post('/', realUrl, {
				headers: {
					'Content-type': 'text/plain'
				}
			})
			.then(function(response){
				app.url = response.data;
				app.afterGen = true;
				if(response.status == 201) {
					app.status = '생성되었습니다.';
				} else {
					app.status = '기존에 생성된 url입니다.';
				}
			}).catch(function(e){
				var errResponse = e.response;
				if(errResponse.status == 405) {
					app.status = '유효한 url이 아닙니다.';
				} else {
					app.status = '알 수 없는 이유로 실패했습니다.';
				}
				app.afterGen = false;
				app.url.shortUrl = '';
			});
		},
		copy: function() {
			this.$refs.shortUrl.$refs.input.select();

			try {
				var isSuccess = document.execCommand('copy');
				if(isSuccess) {
					app.status = '복사되었습니다. 다른곳에 붙여보세요.';
				} else {
					app.status = '복사실패.';
				}
			
			} catch (err) {
				app.status = 'Unable to copy now';
			}
		}
	},
})