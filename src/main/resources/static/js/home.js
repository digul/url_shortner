var app = new Vue({
	el:'#home',
	data: {
		host: 'http://digul.cf/',
		url: {},
		status: 'INPUT YOUR URL',
		afterGen : false
	},
	computed: {
		computedMessage: function() {
			return this.message + "!";
		}
	},
	updated: function() {
/*		axios.get('json').then(function(response){
				this.url = response.data
			}.bind(this).catch(function(e){
				console.error(e);
			})
		);
		app.url = {
			realUrl: 'test',
			shortUrl: 'aAbB',
			isNew: true
		};
		app.afterGen = true;
		if(app.url.isNew) {
			app.status = 'generated new shortUrl';
		} else {
			app.status = 'this url already generated';
		}
	*/
	},
	methods: {
		gen: function() {
			app.status = 'clicked';
			app.afterGen = true;
			app.url.shortUrl = 'GenedUrl';
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