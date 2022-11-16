package com.curreny.converter.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curreny.converter.base.utils.ApiState
import com.curreny.converter.base.utils.MutableEvent
import com.curreny.converter.data.repository.CurrencyRepository
import com.curreny.converter.domain.entity.ConversionTransaction
import com.curreny.converter.domain.entity.CurrencyModel
import com.curreny.converter.domain.entity.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {

    var _currencyStateFlow : MutableStateFlow<ApiState<List<CurrencyModel>?>> = MutableStateFlow(ApiState.Empty)

    var currencyStateFlow: StateFlow<ApiState<List<CurrencyModel>?>> = _currencyStateFlow

    val _currencyConvertedValue: MutableStateFlow<ApiState<ConversionTransaction?>> = MutableStateFlow(ApiState.Empty)

    var baseText: MutableLiveData<String> = MutableLiveData("click to select")

    var convertedText: MutableLiveData<String> = MutableLiveData("to convert")

    var baseField: MutableLiveData<String> = MutableLiveData("1")

    var convertedField: MutableLiveData<String> = MutableLiveData("")

    val currencyConvertedValue: StateFlow<ApiState<ConversionTransaction?>> = _currencyConvertedValue

    val transactions: MutableLiveData<List<ConversionTransaction>> = MutableLiveData()

    private val currentBase: MutableLiveData<String> = MutableLiveData<String>("")

    fun getAllCurrencies() = viewModelScope.launch {
        _currencyStateFlow.value = ApiState.Loading
        currencyRepository.getAllCurrencies().catch { e ->
            _currencyStateFlow.value = ApiState.Empty
        }.collect {
            _currencyStateFlow.value = ApiState.Success(it)
        }
    }

    fun getHistory() = viewModelScope.launch(Dispatchers.IO) {
        val list = currencyRepository.getHistory()
        transactions.postValue(list)
    }

    var _topCurrencyStateFlow : MutableStateFlow<ApiState<List<ConversionTransaction>?>> = MutableStateFlow(ApiState.Empty)

    var topCurrencyStateFlow: StateFlow<ApiState<List<ConversionTransaction>?>> = _topCurrencyStateFlow

    fun getTopCurrencies() = viewModelScope.launch {

        _topCurrencyStateFlow.value = ApiState.Empty
        val popularCurrencies = "USD,EUR,JPY,GBP,AUD,CAD,CHF,CNH,SEK,NZD"
        currencyRepository.getTopCurrency(currentBase.value!!, popularCurrencies).catch { e ->
            _topCurrencyStateFlow.value = ApiState.Failure(e)
        }.collect {
            when(it) {
                is ApiState.Success -> {
                    _topCurrencyStateFlow.value = ApiState.Success(it.data)
                }
                is ApiState.Loading -> {
                    _topCurrencyStateFlow.value = ApiState.Loading
                }
                is ApiState.Failure -> {
                    _topCurrencyStateFlow.value = ApiState.Failure(it.msg)
                }
                else -> {
                    _topCurrencyStateFlow.value = ApiState.Empty
                }
            }
        }
    }

    private fun getConversion(base: String, conversions: String) = viewModelScope.launch {
        _currencyConvertedValue.value = ApiState.Loading
        currencyRepository.getConvertedCurrency(base, conversions).catch { e ->
            _currencyConvertedValue.value = ApiState.Failure(e)
        }.collect {
            when(it) {
                is ApiState.Success -> {
                    val base = baseField.value?.toDouble() ?: 1.0
                    currentBase.postValue(it.data.baseCurrency)
                    convertedField.postValue("${calculateRate(base, it.data.rate)}")
                    _currencyConvertedValue.value = it
                }
                is ApiState.Loading -> {
                    _currencyConvertedValue.value = ApiState.Loading
                }
                is ApiState.Failure -> {
                    _currencyConvertedValue.value = ApiState.Failure(it.msg)
                }
                else -> {
                    _currencyConvertedValue.value = ApiState.Empty
                }
            }
        }
    }

    fun convertDetails() {
        if(!baseText.value.isNullOrBlank()
            && baseText.value != "click to select"
            && !convertedText.value.isNullOrBlank()
            && convertedText.value != "to convert"
        ) {
            getConversion(baseText.value!!, convertedText.value!!)
        }
    }

    val loadDetails = MutableEvent<Boolean>(false)
    fun getDetails() {
        if(!currentBase.value.isNullOrBlank()) {
            loadDetails.postEvent(true)
        }
    }

    fun calculateRate(base: Double, convertRate: Double): Double {
        return base * convertRate
    }
}