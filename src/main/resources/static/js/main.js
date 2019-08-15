var app = new Vue({
	el:'#main',
	data: {
		host: 'http://digul.cf/',
		url: {},
		status: 'INPUT YOUR URL',
		afterGen : false
	},
	computed: {
		generatedFullUrl: function() {
			return this.host + this.url.shortUrl;
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
				if(app.url.isNew) {
					app.status = '생성되었습니다.';
				} else {
					app.status = '기존에 생성된 url입니다.';
				}
				console.log(app.url);
				
			}).catch(function(e){
				var errResponse = e.response;
				if(errResponse.status == 405) {
					app.status = '유효한 url이 아닙니다.';
				} else {
					app.status = '알 수 없는 이유로 실패했습니다.';
				}
				app.afterGen = false;
			});
		},
		copy: function() {
			let genedUrlEl = document.querySelector('#genedUrl');
			genedUrlEl.select();

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
		},
		go: function(event, shortUrl) {
			app.status = shortUrl + ' : go button clicked.';
		}
	},
})