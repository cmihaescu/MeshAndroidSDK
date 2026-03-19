package com.paymentapp.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paymentapp.template.cart.CartViewModel
import com.paymentapp.template.ui.cart.CartScreen
import com.paymentapp.template.ui.products.ProductsScreen
import com.paymentapp.template.ui.theme.PaymentAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaymentAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PaymentAppNav()
                }
            }
        }
    }
}

@Composable
fun PaymentAppNav(
    cartViewModel: CartViewModel = viewModel(),
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
            )
        }
    }
}
