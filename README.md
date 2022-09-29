해당 프로젝트는 lombok을 지향하고 최대한 자바코드로 구현을 하였습니다.
연습하는 프로젝트이기에 유저 도메인 밖에 없습니다.
프로젝트에 jdbc를 이용하여 유저의 회원가입 조회 등의 간단한 로직 구현을 구현하였습니다.  


아키텍쳐  
먼저 패키지 구조는 도메인과 컨트롤러, 서비스, 글로벌한 영역을 나누어 설계를 하였습니다.  
글로벌 영역은 Exception, config, response등으로 이루어져 있습니다.  

validation  
현재 validation은 @Valid를 이용하여 request에 관하여 검사를 하였습니다.  
또한 validation관련 메세지들을 validation.properties를 통해 관리를 하게끔 설계를 하였습니다.  

Exception  
Exception은 RuntimeException을 상속받은 커스텀 클래스를 통해 관리하고자 설계를 하였습니다.  
또한 해당 오류들에 대하여 GlobalExceptionHandler을 두어 해당 예외들을 처리하고 하였습니다.  

메세지 처리  
MessageDatasource를 통해 메세지들을 따로 관리를 하고 있으며 여기에 국제화 관련 코드들을 추가할 예정입니다.  

테스트 코드  
controller에 대한 unit test, Integration test이 작성되어있습니다.  
 
환경  
현재 환경을 local과 test 환경으로 분리를 하여 관리하고 있습니다.  
그래서 테스트 코드들 실행시 실제 환경에 영향을 받지 않고자 하였고, 인메모리 디비를 통해 테스트 코드를 구현하였습니다.  

응답  
서비스 성공시 success와 data를 보내주고 오류시에 fail과 에러 메세지를 주게끔 설계하였습니다.  
응답 클래스를 제네릭을 활용하여 코드의 유연성 있게 재사용 되게끔 설계하였습니다.  
