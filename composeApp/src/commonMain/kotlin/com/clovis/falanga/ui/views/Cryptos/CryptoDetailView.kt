package com.clovis.falanga.ui.views.Cryptos

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.clovis.falanga.StringsUtil.DOLLARS
import com.clovis.falanga.StringsUtil.EMPTY
import com.clovis.falanga.StringsUtil.SEPARATOR
import com.clovis.falanga.ui.Screen
import com.clovis.falanga.ui.components.ConfirmButton
import com.clovis.falanga.ui.components.CryptoCard
import com.clovis.falanga.ui.components.DenyButton

@Composable
fun CryptoDetails(navController: NavHostController,
                  id: String?,
                  cryptoViewModel: CryptoViewModel = viewModel()) {

    val cryptoId by remember {
        mutableStateOf(id?.substringBefore(SEPARATOR)?: EMPTY)
    }
    val crypto = cryptoViewModel.cryptoUI.collectAsState()

    val name by remember {
        mutableStateOf(id?.substringAfter(SEPARATOR))
    }

    //Values
    var buyAt by remember {
        mutableStateOf(EMPTY)
    }

    var sellAt by remember {
        mutableStateOf(EMPTY)
    }

    var quantity by remember {
        mutableStateOf(EMPTY)
    }

    var average by remember {
        mutableStateOf(EMPTY)
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        PreferenceSUtil(context).apply {
            name?.let {
                try {
                    buyAt = getBuyAt(it)
                    sellAt =  getSellAt(it)
                    average =  getAverage(it)
                    quantity = getQuantity(it)
                } catch (e: Exception) {
                    Log.d("PreferenceUtils", e.message.toString())
                }
            }
        }

        cryptoViewModel.getCryptoInfo(cryptoId, BASE_URL)
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        item {
            if (crypto.value?.crypto != null) {
                Column (Modifier
                    .fillMaxSize()){
                    //Current Values
                    val thisCrypto = crypto.value?.crypto?.get(0)
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
                            Spacer(Modifier.height(10.dp))
                            Column{
                                Text(text = "Date Open: ${thisCrypto?.time_open}")
                                Text(text = "Date Close: ${thisCrypto?.time_close}")
                                Text(text = "Volume: ${thisCrypto?.volume}")
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
                            Text(text = "Maximum Value",style = TextStyle.Default
                                .copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
                            Text(text = DOLLARS+thisCrypto?.high.toString())
                            Spacer(Modifier.height(5.dp))
                            //Current Min Value
                            Text(text = "Minimum Value", style = TextStyle.Default
                                .copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
                            Text(text = DOLLARS+thisCrypto?.low.toString())
                            Spacer(Modifier.height(5.dp))
                            //Current value
                            Text(text = "Current Value", style = TextStyle.Default
                                .copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
                            Text(text = DOLLARS+thisCrypto?.open.toString())
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
                            Text(text = "Willing To Sell At: $DOLLARS$sellAt")

                            //Willing to buy at
                            Text(text = "Willing To Buy At: $DOLLARS$buyAt")
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

                            Row {
                                Text(text = "Average: $DOLLARS$average")
                                Spacer(Modifier.width(8.dp))
                                Text(text = "Quantity: $quantity")
                            }
                            if(thisCrypto != null) {
                                val gain = try {
                                    val yourValue = average.ifEmpty { "0" }.toDouble()
                                    val current = thisCrypto?.open?:0.0
                                    val count = quantity.ifEmpty { "0" }.toDouble()

                                    (current - yourValue) * count

                                } catch (e: Exception) {
                                    Log.d("cloclo", ""+e.message)
                                    0.0
                                }

                                val gainSellAt = try {
                                    val toSell = sellAt.ifEmpty { "0" }.toDouble()
                                    val current = thisCrypto.open
                                    val count = quantity.ifEmpty { "0" }.toDouble()

                                    (toSell - current) * count

                                } catch (e: Exception) {
                                    Log.d("cloclo", ""+e.message)
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
                ConfirmButton("Update values") {
                    navController.navigate(Screen.CryptosSettingsRoute.route + "/${cryptoId}$SEPARATOR$name")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row {
                DenyButton("Back To Dashboard") {
                    navController.navigate(Screen.CryptosRoute.route+ "/crypto")
                }
            }
        }
    }
}