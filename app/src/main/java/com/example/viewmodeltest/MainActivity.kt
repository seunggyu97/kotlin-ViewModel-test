package com.example.viewmodeltest

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.viewmodeltest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener{
    lateinit var binding: ActivityMainBinding

    companion object{
        const val TAG: String = "로그"
    }

    // 나중에 값이 설정될거라고 lateinit 으로 설정
    lateinit var myNumberViewModel: MyNumberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myNumberViewModel = ViewModelProvider(this).get(MyNumberViewModel::class.java)

        myNumberViewModel.currentValue.observe(this, Observer {
            Log.d(TAG, "MainActivity - myNumberViewModel - currentValue 라이브 데이터 값 변경 : $it")
            binding.tvNumber.text = it.toString()
        })

        binding.btnPlus.setOnClickListener(this)
        binding.btnMinus.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val userInput =  binding.etUserinput.text.toString().toInt()

        // 뷰모델에 라이브데이터 값을 변경하는 메소드 실행
        when(view){
            binding.btnPlus ->
                myNumberViewModel.updateValue(actionType = ActionType.PLUS, userInput)
            binding.btnMinus ->
                myNumberViewModel.updateValue(actionType = ActionType.MINUS, userInput)
        }
    }
}