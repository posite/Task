package com.posite.task.presentation.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.posite.task.R
import com.posite.task.databinding.ActivitySplashBinding
import com.posite.task.presentation.base.BaseActivity
import com.posite.task.presentation.regist.RegistUserActivity
import com.posite.task.presentation.splash.vm.SplashViewModelImpl
import com.posite.task.presentation.todo.TaskActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel by viewModels<SplashViewModelImpl>()
    override fun initView() {

    }

    override fun onResume() {
        super.onResume()
        viewModel.checkRegist()
    }

    override fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isRegisted.collect {
                    if (it) {
                        Handler(Looper.getMainLooper()).postDelayed({

                            // 일정 시간이 지나면 MainActivity로 이동
                            val intent = Intent(this@SplashActivity, TaskActivity::class.java)
                            startActivity(intent)

                            // 이전 키를 눌렀을 때 스플래스 스크린 화면으로 이동을 방지하기 위해
                            // 이동한 다음 사용안함으로 finish 처리
                            finish()

                        }, 1000)
                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@SplashActivity, RegistUserActivity::class.java)
                            startActivity(intent)
                            finish()

                        }, 1000)
                    }
                }
            }
        }
    }

}