package com.clovis.falanga.ui.views.Cryptos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CryptoViewModel(): ViewModel() {

    var cryptoListUI: MutableStateFlow<CryptoUI?> = MutableStateFlow(null)
        private set

    var cryptoUI: MutableStateFlow<CryptoDetail?> = MutableStateFlow(null)
        private set
    //CryptoUpdate

    fun getListOfCryptoSymbols(baseUrl: String) {
        viewModelScope.launch {
            //cryptoApi.getCryptos()
            CryptoApiCaller(CryptoRetrofitClient
                .setInterceptor()
                .getApiService(baseUrl))
                .getCryptoList()
                .catch {
                    cryptoListUI.value = CryptoUI(error = it.message)
                }
                .collect {
                    cryptoListUI.value = CryptoUI(cryptoList = it)
                }
        }
    }

    fun getCryptoInfo(id: String, baseUrl: String) {
        viewModelScope.launch {
            //cryptoApi.getCryptos()
            CryptoApiCaller(CryptoRetrofitClient
                .setInterceptor()
                .getApiService(baseUrl))
                .getCrypto(id)
                .catch {
                    cryptoUI.value =  CryptoDetail(error = it.message)
                }
                .collect {
                    cryptoUI.value =  CryptoDetail( it)
                }
        }
    }
}

data class CryptoUI(
    val cryptoList: List<Crypto>? = null,
    var error : String? = null
)

data class CryptoDetail(
    val crypto: List<CryptoUpdate>? = null,
    var error : String? = null
)