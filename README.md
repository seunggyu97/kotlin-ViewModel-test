# kotlin-ViewModel-test
### 뷰모델을 사용해야 하는 이유?
안드로이드의 생명주기를 보면 
## 라이브 데이터 활용한 단순한 +- 계산기

build gradle 종속성 추가
```kotlin
    // ViewModel과 LiveData의 lifecycle_version의 버전 정의
    def lifecycle_version = "2.5.0-alpha05"

    // 뷰모델 - 라이프사이클 관련
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")

    // 라이브 데이터 - 옵저버 패턴 관련 (데이터의 변경 사항을 알 수 있다)
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
```

### myNumberViewModel Class
```kotlin
private val _currentValue = MutableLiveData<Int>()
```
다른 클래스에서 접근하지 못하도록 private으로 설정

