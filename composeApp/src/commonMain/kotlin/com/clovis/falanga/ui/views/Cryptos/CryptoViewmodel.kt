package com.clovis.falanga.ui.views.Cryptos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clovis.falanga.models.Crypto
import com.clovis.falanga.models.CryptoApiClient
import com.clovis.falanga.models.CryptoRepository
import com.clovis.falanga.models.CryptoUpdate
import com.clovis.falanga.models.platformHttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class CryptoViewModel(): ViewModel() {

    private val httpClient = platformHttpClient()
    private val api = CryptoApiClient(client = httpClient)
    private val repo = CryptoRepository(api)

    var cryptoListUI: MutableStateFlow<CryptoUI?> = MutableStateFlow(null)
        private set

    var cryptoUI: MutableStateFlow<CryptoDetail?> = MutableStateFlow(null)
        private set
    //CryptoUpdate

    fun getListOfCryptoSymbols(baseUrl: String) {
        viewModelScope.launch {
            //cryptoApi.getCryptos()
            repo.getCryptoList()               // Flow<List<Crypto>>
                //.onStart { cryptoListUI.value = CryptoUI(loading = true) }
                .catch { e -> cryptoListUI.value = CryptoUI(error = e.message ?: "Unknown error") }
                .collect { list ->
                    cryptoListUI.value = CryptoUI(cryptoList = list)
                }
//            CryptoApiCaller(CryptoRetrofitClient
//                .setInterceptor()
//                .getApiService(baseUrl))
//                .getCryptoList()
//                .catch {
//                    cryptoListUI.value = CryptoUI(error = it.message)
//                }
//                .collect {
//                    cryptoListUI.value = CryptoUI(cryptoList = it)
//                }
        }
    }

    fun getCryptoInfo(id: String) {
        viewModelScope.launch {
            //cryptoApi.getCryptos()
            repo.getCrypto(id)              // Flow<List<Crypto>>
                //.onStart { cryptoListUI.value = CryptoUI(loading = true) }
                .catch { e -> cryptoUI.value =  CryptoDetail(error = e.message)}
                .collect { crypto ->
                    cryptoUI.value =  CryptoDetail( crypto)
                }
        }
    }
}

@Serializable
data class CryptoUI(
    val cryptoList: List<Crypto>? = null,
    var error : String? = null
)

@Serializable
data class CryptoDetail(
    val crypto: List<CryptoUpdate>? = null,
    var error : String? = null
)