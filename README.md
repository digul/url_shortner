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
* Vue.js 2.6
* mongodb 


### GENERATING ALGORITHM
* mongodb에 저장된 object id (hex) 를 문자열로 변환
* 변환된 문자열 -> byte array -> int형으로 변환
* base62로 변환

### SERVICE STRATEGY
* http://hostname/ : short url 생성을 위한 화면 제공 - 간단한 media query 적용
* 유효한 url string(200 return)만 생성 가능
* 생성 성공 시 201 code로 short url return
* 중복 생성 시도 시 200 code로 기 생성된 short url return
* http://hostname/SHORTURL : 입력된 shorturl에 해당하는 화면으로 redirect (없는경우 404 return)
* 2시간단위로 변경사항 확인하여 war packging 후 cloud에 빌드/배포/통합테스트 자동수행([jenkins](http://jenkins.digul.cf), bash script) => 클라우드서버 메모리 부족 문제로 포기..
* 무중단배포 (두 개의 서비스를 띄워 nginx 로드밸런싱 설정)
* 서비스 환경에서 static resource는 nginx가 로딩

### SERVICE ENVIRONMENT
* [http://digul.cf/](http://digul.cf/) : 서버 memory 부족으로 실행되지 않을 수 있습니다..
* NCP cloud
* CentOS 7.3 64bit
* nginx 1.16 
* mongodb 4.0
* 야심차게 준비했는데 오늘 갑자기 서버가 죽어서 안뜸...ㅠㅠ 공짜서버에 너무많은것을 올린듯..

### LOCAL ENVIRONMENT
* mvn clean package 로 war build 
* java -jar [패키징된 war] 로 실행
* http://localhost:8080 
* 단위테스트 실행 시 -Dunit-test=true 옵션 필요
* 로컬환경 실행 시 embedded db로 실행됩니다.

