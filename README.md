# Image-Collector
### 카카오 이미지, 동영상 검색 API를 활용해 이미지를 검색하고 보관합에 수집하는 안드로이드 앱입니다.
</br>

## *****Features*****
#### 앱을 시작하면 검색 탭으로 시작합니다. 검색어를 입력하고 검색 버튼을 누르면 
#### 이미지 검색 API와 동영상 검색 API를 최신순으로 첫 번째 페이지를 호출하고, 두 데이터를 합쳐 최신부터 나타나도록 정렬한 후 검색 탭에 바로 보여줍니다.
#### 그리고 유저가 앱을 사용하는 동안 백그라운드에서 호출할 수 있는 모든 데이터를 호출해 전체 리스트를 갱신해 페이징해서 보여주고,
#### 스크롤을 통해 다음 페이지를 호출할 수 있습니다. 또한 검색된 이미지를 클릭하면 하트가 애니메이션과 함께 표시되고, 해당 이미지가 내 보관함에 저장됩니다.
#### 다시 클릭하면 하트 표시가 사라짐과 동시에 보관함에서 제거할 수 있습니다. 다시 검색하더라도 내 보관함에 있는 이미지라면 하트 표시의 북마크가 유지됩니다.
#### 하단 내 보관함 탭을 누르면 앞서 저장했던 이미지들이 보관한 순서대로 보이고, 보관한 이미지들은 앱 재시작 후에도 다시 확인할 수 있습니다.
#### 또한 이미지를 클릭하면 삭제 확인 다이얼로그가 뜨고, 확인을 누르면 내 보관함의 리스트에서 이미지가 사라짐과 동시에 검색 탭에 해당 이미지가 있다면, 하트 표시가 사라집니다.
</br>

## *****Skill Set*****
| 구분 | Skill |
|:---|:---------------------------------------------------------------------------|
| Language | Kotlin |
| Networking | Retrofit, Okhttp, Moshi |
| Asynchronous | Coroutine, Flow |
| DI | Hilt |
| Test | JUnit, Mockito |
| ETC |Navigation, DataBinding, Glide, Paging3, DiffUtil, Lottie |
</br>

## *****Architecture & Modularization*****
#### https://developer.android.com/topic/architecture를 참고하여 멀티 모듈로 앱을 디자인했고,
#### 관심사의 분리를 통해 코드의 복잡성을 줄일 수 있었고, Hilt를 통한 의존성 주입 구현을 통해 클래스 간 의존 관계를 분리해 유지 보수가 편해졌습니다.
#### 검색 데이터의 경우 단방향 데이터 흐름(UDF) 원칙을 최대한 지켜 데이터의 일관성을 보장했습니다.
<img src="https://user-images.githubusercontent.com/76638683/229058547-4400fd13-0973-4197-a622-2fdb9f9a830a.png"  width="510" height="300"/>
</br>


## *****Description of each module*****
#### app : DI, Application, MainActivity 클래스와 필요한 리소스들을 포함하고, 전체 모듈이 모여 앱이 빌드되는 모듈입니다.
#### buildSrc : 빌드 관련 변수들을 모아놓은 모듈입니다.
#### core : 각 모듈에서 공통으로 필요한 클래스와 리소스를 모아놓은 모듈입니다. 
#### data : 네트워크로부터 데이터를 받아 가공하는 비즈니스 로직이 포함되며,  가공 후 도메인 계층에 검색 데이터를 전달하는 Data Layer 모듈입니다.
#### domain : Use Case를 분리하고, Data Layer 모듈과 UI Layer 모듈의 의존성을 분리하는 Domain Layer 모듈입니다.
#### feature-search : 검색 탭 관련 UI 클래스와 레이아웃을 모아놓은 UI Layer 모듈입니다.
#### feature-bookmarks : 내 보관함 탭 관련 UI 클래스와 레이아웃을 모아놓은 UI Layer 모듈입니다.
</br>

## *****Testing*****
#### JUnit와 Mockito 라이브러리를 이용해 SharedPreferences를 사용하는 Util 파일의 함수들에 대해
#### UtilTest 클래스에 테스트 코드를 작성했습니다. 테스트 함수의 네이밍 컨벤션을 지키고,
#### 테스트 함수 내부를 Given, When, Then 세 부분으로 나누어 읽기 편하도록 작성했습니다.
