package com.posite.task.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<vm : BaseViewModel?, V : ViewDataBinding>(@LayoutRes val layoutResource: Int) :
    AppCompatActivity() {

    abstract val viewModel: vm?

    private var _binding: V? = null
    protected val binding: V get() = _binding!!
    abstract fun initView()
    abstract fun initObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initView()
        initObserver()

    }

}