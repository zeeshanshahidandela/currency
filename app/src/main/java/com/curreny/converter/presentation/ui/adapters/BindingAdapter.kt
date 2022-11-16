package com.curreny.converter.presentation.ui.adapters

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LiveData
import com.curreny.converter.domain.entity.CurrencyModel

@BindingAdapter(value = ["currency", "selectedCurrency", "selectedCurrencyAttrChanged"], requireAll = false)
fun setUsers(spinner: Spinner, currencies: List<CurrencyModel>?, selectedUser: CurrencyModel?, listener: InverseBindingListener) {
    if (currencies == null) return
    spinner.adapter = CurrencyAdapter(spinner.context, androidx.transition.R.layout.support_simple_spinner_dropdown_item, currencies)
    setCurrentSelection(spinner, selectedUser)
    setSpinnerListener(spinner, listener)
}


@InverseBindingAdapter(attribute = "selectedCurrency")
fun getSelectedUser(spinner: Spinner): CurrencyModel {
    Toast.makeText(
        spinner.context,
        (spinner.selectedItem as CurrencyModel).title,
        Toast.LENGTH_LONG
    )
        .show()
    return spinner.selectedItem as CurrencyModel
}

private fun setSpinnerListener(spinner: Spinner, listener: InverseBindingListener) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)  = listener.onChange()
        override fun onNothingSelected(adapterView: AdapterView<*>) = listener.onChange()
    }
}

private fun setCurrentSelection(spinner: Spinner, selectedItem: CurrencyModel?): Boolean {
    for (index in 0 until spinner.adapter.count) {
        if (spinner.getItemAtPosition(index) == selectedItem?.title) {
            spinner.setSelection(index)
            return true
        }
    }
    return false
}