package com.curreny.converter.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.curreny.converter.base.utils.ApiState
import com.curreny.converter.databinding.FragmentDetailsBinding
import com.curreny.converter.domain.entity.ConversionTransaction
import com.curreny.converter.presentation.ui.adapters.HistoryAdapter
import com.curreny.converter.presentation.ui.adapters.TopCurrencyAdapter
import com.curreny.converter.presentation.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsFragment: Fragment() {

    private val binding by lazy { FragmentDetailsBinding.inflate(layoutInflater)}
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = mainViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupObserver()
        mainViewModel.getHistory()
        mainViewModel.getTopCurrencies()
    }

    // adapter methods:-----------------------------------------------------------------------------
    private fun setAdapter(list: List<ConversionTransaction>) {
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HistoryAdapter(list)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupObserver() {

        lifecycleScope.launchWhenStarted {
            mainViewModel.topCurrencyStateFlow.collect {
                when (it) {
                    is ApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ApiState.Failure -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is ApiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        setTopCurrencyAdapter(it.data)
                    }
                    is ApiState.Empty -> {
                    }
                }
            }
        }

        mainViewModel.transactions.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
    }

    private fun setTopCurrencyAdapter(list: List<ConversionTransaction>?){
        list?.let {
            binding.rvTopCurrencies.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = TopCurrencyAdapter(list)

                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }
    }


}