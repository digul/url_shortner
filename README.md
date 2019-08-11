# URL SHORTNER
URL 을 입력받아 짧게 줄여주고, Shortening 된 URL 을 입력하면 원래 URL 로 리다이렉트하는 URL
Shortening Service


### 요구사항
* webapp 으로 개발하고 URL 입력폼 제공 및 결과 출력
* URL Shortening Key 는 8 Character 이내로 생성되어야 합니다.
* 동일한 URL 에 대한 요청은 동일한 Shortening Key 로 응답해야 합니다.
* Shortening 된 URL 을 요청받으면 원래 URL 로 리다이렉트 합니다.
* Shortening Key 생성 알고리즘은 직접 구현해야 합니다. (라이브러리 사용 불가)
* Unit Test 코드 작성
* Database 사용은 필수 아님 (선택)


### SPEC
* java 1.8
* spring boot 2.1.4.RELEASE
* mongodb 4.0


### STRATEGY
* 세팅중
* TDD : controller / service / repository TestCase 작성, 빌드배포 시 자동수행
* jar packging 후 cloud에 빌드배포(jenkins, bash script)
* 무중단배포 (두 개의 서비스를 띄워 nginx 로드밸런싱 설정)

### 실행환경
* [http://digul.cf/](http://digul.cf/)
* NCP cloud 
* CentOS 7.3 64bit
* nginx 1.16 

