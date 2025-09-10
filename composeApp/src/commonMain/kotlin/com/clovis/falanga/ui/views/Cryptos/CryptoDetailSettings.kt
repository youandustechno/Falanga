package com.clovis.falanga.ui.views.Cryptos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.clovis.falanga.PreferenceS
import com.clovis.falanga.StringsUtil
import com.clovis.falanga.StringsUtil.AVERAGE
import com.clovis.falanga.StringsUtil.BUY_AT
import com.clovis.falanga.StringsUtil.DOLLARS
import com.clovis.falanga.StringsUtil.EMPTY
import com.clovis.falanga.StringsUtil.QUANTITY
import com.clovis.falanga.StringsUtil.SELL_AT
import com.clovis.falanga.StringsUtil.SEPARATOR
import com.clovis.falanga.WatchedShares
import com.clovis.falanga.getPreferences
import com.clovis.falanga.models.CryptoUpdate
import com.clovis.falanga.ui.components.CryptoCard
import com.clovis.falanga.ui.components.DenyButton
import com.clovis.falanga.ui.components.PrecisionTextField
import com.clovis.falanga.ui.components.TextFieldWithIcons
import com.clovis.falanga.ui.getContext
import com.clovis.falanga.ui.isAndroid
import kotlinx.coroutines.launch


@Composable
fun CryptoDetailSettings (navController: NavHostController,
                          id: String?,
                          prefs: DataStore<Preferences>,
                          cryptoViewModel: CryptoViewModel = viewModel()) {

    val context = getContext()
    val storage: PreferenceS by remember { mutableStateOf(if(isAndroid()) {
        getPreferences(context)!!
    } else {
        getPreferences(prefs)!!
    })
    }

    val scope  = rememberCoroutineScope()

    val cryptoId by remember {
        mutableStateOf(id?.substringBefore(SEPARATOR)?: EMPTY)
    }
    val crypto = cryptoViewModel.cryptoUI.collectAsState()

    val name by remember {
        mutableStateOf(id?.substringAfter(SEPARATOR))
    }

    //Values
    val buyAt by storage.getBuyAt(name?:"").collectAsState("0")
    var buyAtChange by remember { mutableStateOf(buyAt) }

    var realBuyAt by remember {
        mutableStateOf(EMPTY)
    }

    val buyAtPrecision by storage.getPrecision(name?: "0", BUY_AT).collectAsState("0")
    var buyAtPrecisionChange by remember { mutableStateOf(buyAtPrecision) }

    val sellAt by storage.getSellAt(name?: "0").collectAsState(EMPTY)
    var sellAtChange by remember { mutableStateOf(sellAt) }

    var realSellAt by remember { mutableStateOf(EMPTY) }

    val sellAtPrecision by storage.getPrecision(name?: "0", SELL_AT).collectAsState("0")
    var sellAtPrecisionChange by  remember { mutableStateOf(sellAtPrecision) }

    val quantity by storage.getQuantity(name?:EMPTY).collectAsState(EMPTY)
    var quantityChange by remember { mutableStateOf(quantity) }

    var realQuantity by remember {
        mutableStateOf(EMPTY)
    }

    val quantityPrecision by storage.getPrecision(name?: "0", QUANTITY).collectAsState("0")
    var quantityPrecisionChange by remember { mutableStateOf(quantityPrecision) }

    val average by  storage.getAverage(name?: "0").collectAsState(EMPTY)

    var averageChange by remember { mutableStateOf(average) }

    var realAverage by remember {
        mutableStateOf(EMPTY)
    }
    val averagePrecision by  storage.getPrecision(name?:"0", AVERAGE).collectAsState("0")
    var averagePrecisionChange by remember { mutableStateOf(averagePrecision) }


    var thisCrypto : CryptoUpdate? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = Unit) {
        cryptoViewModel.getCryptoInfo(cryptoId)
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        item {
            if (crypto.value?.crypto != null) {
                Column (Modifier
                    .fillMaxSize()){
                    //Current Values
                    thisCrypto = crypto.value?.crypto?.get(0)
                    CryptoCard {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(8.dp)) {
                            //Crypto Name
                            Row{
                                Text(text = "Crypto Name: $name",
                                    style = TextStyle.Default
                                        .copy(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    CryptoCard {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(8.dp)) {
                            //Current Min Value
                            Text(text = "Maximum Value", style = TextStyle.Default
                                .copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
                            Text(text = DOLLARS +thisCrypto?.high.toString())
                            Spacer(Modifier.height(5.dp))
                            //Current Min Value
                            Text(text = "Minimum Value", style = TextStyle.Default
                                .copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
                            Text(text = DOLLARS +thisCrypto?.low.toString())
                            Spacer(Modifier.height(5.dp))
                            //Current value
                            Text(text = "Current Value", style = TextStyle.Default
                                .copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
                            Text(text = DOLLARS +thisCrypto?.open.toString())
                        }
                    }
                    //User Settings
                    Spacer(Modifier.height(15.dp))
                    CryptoCard {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(8.dp)) {
                            //Willing to sell at
                            realSellAt =  StringsUtil
                                .convertAmountWithPrecision(sellAtChange, sellAtPrecisionChange)
                            Text(text = "Willing To Sell At: $DOLLARS$realSellAt")
                            TextFieldWithIcons(sellAtChange,
                                Icons.Default.Close,
                                "Sell At") { str->
                                sellAtChange = str
                            }
                            Spacer(Modifier.height(5.dp))
                            Text(text = "Sell at Precision: $sellAtPrecisionChange")
                            Box(
                                Modifier
                                    .width(100.dp)
                                    .height(60.dp),
                                contentAlignment = Alignment.Center) {
                                Box (
                                    Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center),
                                    contentAlignment = Alignment.Center){
                                    PrecisionTextField(sellAtPrecisionChange) {
                                        if(it.length < 2) sellAtPrecisionChange = it
                                    }
                                }
                            }

                            realBuyAt =  StringsUtil
                                .convertAmountWithPrecision(buyAtChange, buyAtPrecisionChange)
                            //Willing to buy at
                            Text(text = "Willing To Buy At: $DOLLARS$realBuyAt")
                            TextFieldWithIcons(buyAtChange,
                                Icons.Default.Close, "Buy At") { str ->
                                buyAtChange = str
                            }
                            Spacer(Modifier.height(5.dp))
                            Text(text = "Buy at  Precision: $buyAtPrecisionChange")
                            Box(
                                Modifier
                                    .width(100.dp)
                                    .height(60.dp),
                                contentAlignment = Alignment.Center) {
                                Box (
                                    Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center),
                                    contentAlignment = Alignment.Center){
                                    PrecisionTextField(buyAtPrecisionChange) {
                                        if (it.length < 2) {
                                            buyAtPrecisionChange = it
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //History
                    Spacer(Modifier.height(15.dp))
                    CryptoCard {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(8.dp)) {

                            Column{
                                //Average
                                realAverage =  StringsUtil
                                    .convertAmountWithPrecision(averageChange,
                                        averagePrecisionChange)
                                Text(text = "Average: $DOLLARS$realAverage")
                                TextFieldWithIcons(averageChange,
                                    Icons.Default.Close,
                                    "Average") { str->
                                    averageChange = str
                                }
                                Spacer(Modifier.height(5.dp))
                                Text(text = "Average Precision: $averagePrecisionChange")
                                Spacer(Modifier.height(2.dp))
                                Box(
                                    Modifier
                                        .width(100.dp)
                                        .height(60.dp),
                                    contentAlignment = Alignment.Center) {
                                    Box (
                                        Modifier
                                            .fillMaxSize()
                                            .align(Alignment.Center),
                                        contentAlignment = Alignment.Center){
                                        PrecisionTextField(averagePrecisionChange) {
                                            if(it.length < 2) averagePrecisionChange = it
                                        }
                                    }
                                }

                                //Quantity
                                Spacer(Modifier.height(10.dp))
                                realQuantity =  StringsUtil
                                    .convertAmountWithPrecision(quantityChange,
                                        quantityPrecisionChange)
                                Text(text = "Quantity: $realQuantity")
                                TextFieldWithIcons(quantityChange,
                                    Icons.Default.Close,
                                    "Quantity") {str ->
                                    quantityChange = str
                                }
                                Spacer(Modifier.height(5.dp))
                                Text(text = "Quantity Precision: $quantityPrecisionChange")

                                Box(
                                    Modifier
                                        .width(100.dp)
                                        .height(60.dp),
                                    contentAlignment = Alignment.Center) {
                                    Box (
                                        Modifier
                                            .fillMaxSize()
                                            .align(Alignment.Center),
                                        contentAlignment = Alignment.Center){

                                        PrecisionTextField(quantityPrecisionChange) {
                                            if(it.length < 2) quantityPrecisionChange = it
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(5.dp))

                            if(thisCrypto != null) {
                                val gain = try {
                                    val current = thisCrypto?.open?:0.0
                                    val count = realQuantity.ifEmpty { "0" }.toDouble()
                                    if(realAverage.isNotEmpty()) {
                                        (current - realAverage.toDouble()) * count
                                    } else {
                                        0
                                    }

                                } catch (e: Exception) {
                                    print("cloclo "+e.message)
                                    0.0
                                }

                                val gainSellAt = try {

                                    val current = thisCrypto?.open?:0.0
                                    val count = realQuantity.ifEmpty { "0" }.toDouble()

                                    if(realSellAt.isNotEmpty()) {
                                        (realSellAt.toDouble() - current) * count
                                    } else {
                                        0
                                    }

                                } catch (e: Exception) {
                                    print("cloclo "+e.message)
                                    0.0
                                }
                                Text(text = "Current Gain : $DOLLARS$gain")
                                Spacer(Modifier.width(8.dp))
                                Text(text = "Gain if sells at $DOLLARS$sellAt : $DOLLARS$gainSellAt")
                            }
                        }
                    }
                }
            }
            else  if (crypto.value?.error!= null) {

                CryptoCard {
                    Column(Modifier.padding(8.dp)) {
                        Text(text = "Sorry! We can't comply at the request right now. Please try later")
                    }
                }

            }
            else {
                CryptoCard {
                    Column(Modifier.padding(8.dp)) {
                        Text(text = "Loading ...")
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Row {
                DenyButton("Save") {
                    scope.launch {
                        try {
                            storage.apply {
                                val list: MutableList<WatchedShares> = getWatchedShares().toMutableList()
                                name?.let {
                                    if(list.size < 11) {
                                        list.add(WatchedShares(cryptoId, it))
                                    } else {
                                        list.removeAt(0)
                                        list.add(WatchedShares(cryptoId, it))
                                    }
                                    saveWatchedShares(list)
                                    saveBuyAt(realBuyAt, it)
                                    saveSellAt(realSellAt, it)
                                    saveAverage(realAverage, it)
                                    saveQuantity(realQuantity, it)

                                    //Save precisions
                                    savePrecision(averagePrecisionChange, it, AVERAGE)
                                    savePrecision(sellAtPrecisionChange, it, SELL_AT)
                                    savePrecision(buyAtPrecisionChange, it, BUY_AT)
                                    savePrecision(quantityPrecisionChange, it, QUANTITY)
                                }
                            }
                        }
                        catch (e:Exception) {
                            print("ClocloVue "+ e.message)
                        }
                    }
                    navController.popBackStack()
                }
            }
        }
    }
}