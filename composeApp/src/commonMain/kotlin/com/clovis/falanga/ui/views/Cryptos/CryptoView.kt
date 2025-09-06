package com.clovis.falanga.ui.views.Cryptos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.clovis.falanga.StringsUtil.BASE_URL
import com.clovis.falanga.StringsUtil.EMPTY
import com.clovis.falanga.StringsUtil.SEPARATOR
import com.clovis.falanga.WatchedShares
import com.clovis.falanga.getPreferences
import com.clovis.falanga.ui.Screen
import com.clovis.falanga.ui.components.CryptoButton
import com.clovis.falanga.ui.components.CryptoCard
import com.clovis.falanga.ui.components.SearchView
import com.clovis.falanga.ui.getContext
import com.clovis.falanga.ui.isAndroid


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CryptosView(navController: NavHostController,
                helpName: String?,
                prefs: DataStore<Preferences>,
                cryptoViewModel: CryptoViewModel = viewModel()) {

    val cryptos = cryptoViewModel.cryptoListUI.collectAsState()
    val context = getContext()
    BASE_URL =   "" //stringResource(id = R.string.main_part)
    var called  by remember { mutableStateOf(false) }
    var list by remember { mutableStateOf( emptyList<WatchedShares>()) }

    LaunchedEffect(Unit) {
        list = if(isAndroid()) {
            getPreferences(context)?.getWatchedShares()?.toMutableList()?: emptyList()
        }
        else getPreferences(prefs)?.getWatchedShares()?.toMutableList() ?: emptyList()
    }

    LaunchedEffect(called) {
        if(!called) {
            called = true
            cryptoViewModel.getListOfCryptoSymbols(BASE_URL)
        }
    }

    var textState: String by remember { mutableStateOf(EMPTY) }

    Column(Modifier.padding(horizontal = 16.dp)) {
        //NotificationPermissionScreen()
        if(list.isNotEmpty()) {
            Spacer(modifier = Modifier.height(40.dp))
            Text("Saved Cryptos")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .zIndex(4F)) {

            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {

                list.forEach { money ->
                    Column(Modifier.wrapContentSize()) {
                        Spacer(Modifier
                            .height(8.dp)
                        )
                        CryptoButton(value = money.name ) {
                            navController.navigate(Screen.CryptoDetailsRoute.route
                                    + "/${money.id}$SEPARATOR${money.name}")
                        }
                    }
                    Spacer(Modifier
                        .width(15.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(text = "Look Up Crypto Below")
                Spacer(modifier = Modifier.height(20.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            SearchView(state = textState ) {
                textState = it
            }
            Spacer(modifier = Modifier.height(20.dp))
//            Spacer(modifier = Modifier.height(5.dp))
//            Row {
//                DenyButton("Go To Settings!") {
//                    navController.navigate(Screen.AccountRoute.route+ "/route")
//                }
//            }
//            Spacer(modifier = Modifier.height(15.dp))
        }

        if (cryptos.value?.cryptoList != null) {

            var filteredItems = cryptos.value?.cryptoList?.filter {
                it.name.contains(textState, true)
            }?:emptyList()

            filteredItems = filteredItems.ifEmpty {
                cryptos.value?.cryptoList ?:emptyList()
            }

            LazyColumn {

                itemsIndexed(filteredItems) { index, item ->
                    CryptoCard {
                        Column (
                            Modifier
                                .fillMaxWidth()
                                .heightIn(80.dp, 100.dp)
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    navController.navigate(Screen.CryptoDetailsRoute.route + "/${item.id}$SEPARATOR${item.name}")
                                }) {

                            Row(Modifier.padding(vertical = 5.dp)) {
                                Text(text = ( index + 1 ).toString())
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = item.name, style = TextStyle.Default
                                    .copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
                            }
                            Column (
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp)){
                                Text(text = item.is_active.toString())
                                Text(text = item.is_new.toString())
                            }
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
        else  if (cryptos.value?.error!= null) {
            CryptoCard {
                Column(Modifier.padding(5.dp)) {
                    Text(text = "Sorry! We can't comply at the request right now. Please try later")
                }
            }
        }
        else {
            CryptoCard {
                Column(Modifier.padding(5.dp)) {
                    Text(text = "Loading ...")
                }
            }
        }
    }
}