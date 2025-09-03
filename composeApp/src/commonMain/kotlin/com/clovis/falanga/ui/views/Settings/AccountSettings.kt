package com.clovis.falanga.ui.views.Settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.clovis.falanga.ui.components.CryptoCard


@Composable
fun AccountSettings (
    navController: NavHostController,
    settingsName: String?,
    cryptoViewModel: CryptoViewModel = viewModel()
) {
    Column {
        //Current Values
        CryptoCard {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                //Crypto Name
                Row{
                    Text(text = "Crypto Name ")
                }

                //Current Min Value
                Text(text = "Maximum Value")
                Text(text = "Current Value")
                //Current Min Value
                Text(text = "Minimum Value")
                Text(text = "Current Value")
                //Current Max value
                Text(text = "Maximum Value")
                Text(text = "Current Value")
            }
        }
        //User Settings
        Spacer(Modifier.height(15.dp))
        CryptoCard {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                //Willing to sell at
                Text(text = "Willing To Sell At: ")
                //Willing to buy at
                Text(text = "Willing To Buy At")
            }
        }
        //History
        Spacer(Modifier.height(15.dp))
        CryptoCard {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {

                Row {
                    Text(text = "average: $2.56")
                    Text(text = "Quantity: 234")
                }
                Text(text = "Gain: 234")
            }
        }
    }
}