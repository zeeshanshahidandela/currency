package com.curreny.converter.presentation.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.curreny.converter.R
import com.curreny.converter.base.utils.ApiState
import com.curreny.converter.base.utils.Event
import com.curreny.converter.base.utils.EventObserver
import com.curreny.converter.base.utils.NetworkComponents
import com.curreny.converter.databinding.FragmentCurrencySelectorBinding
import com.curreny.converter.domain.entity.CurrencyModel
import com.curreny.converter.presentation.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConversionFragment: Fragment() {

    private val binding by lazy { FragmentCurrencySelectorBinding.inflate(layoutInflater)}
    private val mainViewModel by activityViewModels<MainViewModel>()

    private lateinit var currencies: List<CurrencyModel>

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
        callWebServiceForQuestionList()
        setupObserver()
        setupClickListener()
    }

    private fun callWebServiceForQuestionList() {
        if (NetworkComponents.DETECT_INTERNET_CONNECTION(requireContext())) {
            mainViewModel.getAllCurrencies()
        } else {
            NetworkComponents.showDialog(requireContext())
        }
    }

    private fun setupClickListener() {
        binding.baseCurrencyTv.setOnClickListener{
            settingCurrenciesList(true)
        }

        binding.convertedCurrencyTv.setOnClickListener{
            settingCurrenciesList(false)
        }

        binding.baseEt.addTextChangedListener {
            if (!TextUtils.isEmpty(binding.baseEt.text.toString()) && !TextUtils.isEmpty(binding.convertedEt.text.toString())) {
                mainViewModel.convertDetails()
            }
        }
    }

    private fun setupObserver(){
        lifecycleScope.launchWhenStarted {
            mainViewModel.currencyStateFlow.collect {
                when (it) {
                    is ApiState.Loading -> {
                        binding.detailsButton.isClickable = false
                        binding.progressBarCircle.visibility = View.VISIBLE
                    }
                    is ApiState.Failure -> {
                        binding.detailsButton.isClickable = true
                        binding.progressBarCircle.visibility = View.GONE
                        Log.e("TAG", "onCreate: " + it.msg)
                    }
                    is ApiState.Success -> {
                        binding.detailsButton.isClickable = true
                        binding.progressBarCircle.visibility = View.GONE
                        currencies = it.data!!
                        Log.e("ConversionFragment", "size: ${currencies.size}")
                    }
                    is ApiState.Empty -> {
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            mainViewModel.currencyConvertedValue.collect {
                when (it) {
                    is ApiState.Loading -> {
                        binding.progressBarCircle.visibility = View.VISIBLE
                    }
                    is ApiState.Failure -> {
                        binding.progressBarCircle.visibility = View.GONE
                    }
                    is ApiState.Success -> {
                        binding.detailsButton.visibility = View.VISIBLE
                        binding.progressBarCircle.visibility = View.GONE
                    }
                    is ApiState.Empty -> {
                    }
                }
            }
        }
        mainViewModel.loadDetails.observe(viewLifecycleOwner, launchDetailsFragment)
    }

    private val launchDetailsFragment = EventObserver<Boolean> {
        if(it) {
            findNavController().navigate(R.id.detailsFragment)
        }
    }

    private fun settingCurrenciesList(isBase: Boolean) {
        val text = if(isBase) "Base" else "Conversion"
        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builderSingle.setTitle("Select $text Currency:-")

        val arrayAdapter =
            ArrayAdapter<CurrencyModel>(requireContext(), android.R.layout.select_dialog_singlechoice)
        arrayAdapter.addAll(currencies)

        builderSingle.setNegativeButton("cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

        builderSingle.setAdapter(arrayAdapter,
            DialogInterface.OnClickListener { dialog, index ->
                val strName = arrayAdapter.getItem(index)?.title ?: ""
                Log.e("OnClickListener","$strName")
                if(isBase) {
                    mainViewModel.baseText.postValue(strName)
                } else {
                    mainViewModel.convertedText.postValue(strName)
                }
                mainViewModel.convertDetails()
            })
        builderSingle.show()
    }

}