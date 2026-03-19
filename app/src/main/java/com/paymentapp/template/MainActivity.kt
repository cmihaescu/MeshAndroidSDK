package com.paymentapp.template

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meshconnect.link.entity.AccessTokenPayload
import com.meshconnect.link.entity.DelayedAuthPayload
import com.meshconnect.link.entity.LinkConfiguration
import com.meshconnect.link.entity.LinkEvent
import com.meshconnect.link.entity.TransferFinishedErrorPayload
import com.meshconnect.link.entity.TransferFinishedSuccessPayload
import com.meshconnect.link.store.LinkEvents
import com.meshconnect.link.store.LinkPayloads
import com.meshconnect.link.ui.LinkResult.LinkExit
import com.meshconnect.link.ui.LinkResult.LinkSuccess
import com.meshconnect.link.ui.contract.LaunchLink
import com.paymentapp.template.cart.CartViewModel
import com.paymentapp.template.ui.cart.CartScreen
import com.paymentapp.template.ui.products.ProductsScreen
import com.paymentapp.template.ui.theme.PaymentAppTheme
import kotlinx.coroutines.launch

private const val TAG = "MeshSDK"

// Replace with a real linkToken obtained from your backend via /api/v1/linktoken
private const val LINK_TOKEN = "YOUR_LINK_TOKEN_HERE"

class MainActivity : ComponentActivity() {

    private val linkLauncher = registerForActivityResult(LaunchLink()) { result ->
        when (result) {
            is LinkSuccess -> {
                Log.d(TAG, "LinkSuccess received with ${result.payloads.size} payload(s)")
                result.payloads.forEach { payload ->
                    when (payload) {
                        is AccessTokenPayload -> Log.d(
                            TAG,
                            "AccessTokenPayload: broker=${payload.brokerType}, " +
                                "accountId=${payload.accountId}"
                        )
                        is DelayedAuthPayload -> Log.d(
                            TAG,
                            "DelayedAuthPayload: broker=${payload.brokerType}"
                        )
                        is TransferFinishedSuccessPayload -> Log.d(
                            TAG,
                            "TransferFinishedSuccessPayload: symbol=${payload.symbol}, " +
                                "amount=${payload.amount}, txId=${payload.txId}"
                        )
                        is TransferFinishedErrorPayload -> Log.d(
                            TAG,
                            "TransferFinishedErrorPayload: error=${payload.errorMessage}"
                        )
                        else -> Log.d(TAG, "Unknown payload type: ${payload::class.simpleName}")
                    }
                }
            }
            is LinkExit -> {
                Log.d(TAG, "LinkExit received: errorMessage=${result.errorMessage ?: "none"}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        observeMeshEvents()

        setContent {
            PaymentAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PaymentAppNav(onLaunchMesh = { launchMeshLink() })
                }
            }
        }
    }

    private fun launchMeshLink() {
        val configuration = LinkConfiguration(token = LINK_TOKEN)
        Log.d(TAG, "Launching Mesh Link flow")
        linkLauncher.launch(configuration)
    }

    private fun observeMeshEvents() {
        lifecycleScope.launch {
            LinkEvents.collect { event ->
                when (event) {
                    is LinkEvent.Loaded -> Log.d(TAG, "LinkEvent: Loaded — Link UI is ready")
                    is LinkEvent.Close -> Log.d(TAG, "LinkEvent: Close — user closed the Link UI")
                    is LinkEvent.Done -> Log.d(TAG, "LinkEvent: Done — flow completed successfully")
                    is LinkEvent.ShowClose -> Log.d(TAG, "LinkEvent: ShowClose — show close button")
                    is LinkEvent.TrueAuth -> Log.d(TAG, "LinkEvent: TrueAuth — link=${event.link}")
                    is LinkEvent.Payload -> Log.d(
                        TAG,
                        "LinkEvent: Payload — type=${event.payload::class.simpleName}"
                    )
                    else -> Log.d(TAG, "LinkEvent: unknown event type=${event::class.simpleName}")
                }
            }
        }

        lifecycleScope.launch {
            LinkPayloads.collect { payload ->
                Log.d(TAG, "LinkPayload received: type=${payload::class.simpleName}")
            }
        }
    }
}

@Composable
fun PaymentAppNav(
    cartViewModel: CartViewModel = viewModel(),
    onLaunchMesh: () -> Unit,
) {
    val navController = rememberNavController()
    val cartState by cartViewModel.cartState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "products",
    ) {
        composable("products") {
            ProductsScreen(
                cartItemCount = cartState.itemCount,
                onCartClick = { navController.navigate("cart") },
                onAddToCart = { cartViewModel.addToCart(it) },
            )
        }
        composable("cart") {
            CartScreen(
                items = cartState.items,
                subtotalFormatted = cartState.subtotalFormatted,
                onBack = { navController.popBackStack() },
                onRemove = { cartViewModel.removeFromCart(it) },
                onContinueShopping = { navController.popBackStack() },
                onCheckout = onLaunchMesh,
            )
        }
    }
}
