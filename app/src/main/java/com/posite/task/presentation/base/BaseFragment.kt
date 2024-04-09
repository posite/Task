package com.posite.task.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    Fragment() {
    val binding: B get() = _binding!!
    private var _binding: B? = null

    abstract fun initObserver()
    abstract fun initView()

    protected open fun navigate(navigation: Navigation) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        with(binding) {
            lifecycleOwner = this@BaseFragment
            executePendingBindings()
            return root
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}