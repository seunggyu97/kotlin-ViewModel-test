# kotlin-ViewModel-test
### 뷰모델을 사용해야 하는 이유?

ViewModel의 종류에는 두가지가 있다.

1. MVVM 패턴의 ViewModel

- View와 Model 사이의 매개체 역할을 하고 View에 보여지게 되는 데이터를 가공하는 역할을 한다.

2. AAC(Android Architecture Components)의 ViewModel

- 앱의 Lifecycle을 고려하여 UI 관련 데이터를 저장하고 관리하는 역할을 한다.


구글은 MVVM 패턴을 사용하여 앱을 만들 것을 권장하고 있고

VVM의 ViewModel을 구현할 때 AAC ViewModel을 사용해서 구현하는 것이 좋다.

이번에는 AAC의 ViewModel을 다룰것이다.

## AAC ViewModel

![image](https://user-images.githubusercontent.com/74666576/164272764-43a9ff03-8f2b-4d00-b0ec-294664bc75d1.png)

안드로이드의 생명주기를 보면 화면회전이 되면 Activity가 onDestroy 콜백을 호출하고 새롭게 onCreate가 호출된다.

이 말은 즉 기존에 Activity에 존재하던 데이터는 초기화된다는 것.

반면에 ViewModel Scope는 Finished 될 때 까지 유지된다.

데이터를 ViewModel에 담으면 화면회전이 일어나도 데이터가 유실되지 않는다.

# 라이브 데이터 활용한 단순한 +- 계산기

## build gradle 종속성 추가
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

## myNumberViewModel Class
```kotlin
enum class ActionType{
    PLUS, MINUS
}
```
- enum class생성

```kotlin
private val _currentValue = MutableLiveData<Int>()
```
다른 클래스에서 접근하지 못하도록 private으로 설정

```kotlin
val currentValue: LiveData<Int>
        get() = _currentValue
```
- 변경되지 않는 데이터를 가져 올 때 이름을 _ 언더스코어 없이 설정
- 공개적으로 가져오는 변수는 private이 아닌 public으로 외부에서도 접근가능하도록 설정
- 하지만 값을 직접 라이브데이터에 접근하지 않고 뷰모델을 통해 가져올 수 있도록 설정

### MutableLiveData : 값 변경 가능
### LiveData : 값 변경 불가능

```kotlin
init{
     Log.d(TAG, "MyNumberViewModel : 생성자 호출 ")
     _currentValue.value = 0
}
```
- 변경 가능한 _currentValue의 값을 0으로 초기화

```kotlin
    fun updateValue(actionType: ActionType, input: Int){
        when(actionType){
            ActionType.PLUS ->
                _currentValue.value = _currentValue.value?.plus(input)
            ActionType.MINUS ->
                _currentValue.value = _currentValue.value?.minus(input)
        }
    }
```
- when문은 JAVA의 switch문
- actionType이 Plus일 때, 변경가능한 _currentValue의 값에 파라미터로 전달받은 input값을 더한다.
- actionType이 Minus일 때, 변경가능한 _currentValue의 값에 파라미터로 전달받은 input값을 뺀다.

## MainActivity class

```kotlin
lateinit var myNumberViewModel: MyNumberViewModel
```
- 나중에 값이 설정될거라고 lateinit 으로 설정

### onCreate단
```kotlin
myNumberViewModel = ViewModelProvider(this).get(MyNumberViewModel::class.java)
```
- ViewModelProvider을 이용하여 myNumberViewModel 초기화

```kotlin
myNumberViewModel.currentValue.observe(this, Observer {
            Log.d(TAG, "MainActivity - myNumberViewModel - currentValue 라이브 데이터 값 변경 : $it")
            binding.tvNumber.text = it.toString()
        })
```
- 옵저버는 값의 변동을 관찰하는 관찰자 역할을 한다.
- 값의 변동이 발생하면 내부 로직을 실행한다.

```kotlin
binding.btnPlus.setOnClickListener(this)
binding.btnMinus.setOnClickListener(this)
```
- 더하기 빼기 버튼에 클릭 리스너를 설정한다.

### onClick

```kotlin
when(view){
    binding.btnPlus ->
        myNumberViewModel.updateValue(actionType = ActionType.PLUS, userInput)
    binding.btnMinus ->
        myNumberViewModel.updateValue(actionType = ActionType.MINUS, userInput)
}
```
- 뷰모델에 라이브데이터 값을 변경하는 메소드 실행

## 정리

1. _currentValue의 초기값은 0
2. MainActivity에서 수를 입력하고 버튼 클릭 이벤트가 발생하면 
3. myNumberViewModel 클래스의 updateValue 메소드 호출 (이때 해당하는 actionType과 입력한 수를 인수로 전달)
4. myNumberViewModel updateValue 메소드에서 인자로 받은 actionType에 따라 [변경가능한]_currentValue의 값에 입력한 수를 더하거나 뺌
5. 다시 MainActivity의 옵저버가 값이 변경된 것을 캐치하고 TextView에 변경된 값을 출력



# 이번 학습은 유튜버 "개발하는 정대리"님의 강의를 보고 진행하였습니다.
