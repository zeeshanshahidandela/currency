package com.curreny.converter.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curreny.converter.data.repository.CurrencyRepository
import com.curreny.converter.base.utils.ApiState
import com.curreny.converter.domain.entity.ConversionTransaction
import com.curreny.converter.domain.entity.CurrencyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) : ViewModel() {

    private val _currencyStateFlow: MutableLiveData<List<CurrencyModel>> =
        MutableLiveData(emptyList())

    val currencyStateFlow: LiveData<List<CurrencyModel>> = _currencyStateFlow


    val _currencyConvertedValue: MutableLiveData<ConversionTransaction?> =
        MutableLiveData()

    val currencyConvertedValue: LiveData<ConversionTransaction?> = _currencyConvertedValue

    fun getAllCurrencies() = viewModelScope.launch {
        currencyRepository.getAllCurrencies().catch { e ->
            _currencyStateFlow.value = emptyList()
        }.collect {
            _currencyStateFlow.value = it
        }
    }


    fun getConversion(base: String, converions: String) = viewModelScope.launch {
        currencyRepository.getConvertedCurrency(base, converions).catch { e ->
            _currencyConvertedValue.value = null
        }.collect {
//            when(it){
//                is ApiState.Success -> {
//                    _currencyStateFlow.value = it.data.rates
//                }
//            }

        }
    }


}