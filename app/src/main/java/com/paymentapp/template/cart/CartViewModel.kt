package com.paymentapp.template.cart

import androidx.lifecycle.ViewModel
import com.paymentapp.template.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {

    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    fun addToCart(product: Product, quantity: Int = 1) {
        _cartState.update { it.addProduct(product, quantity) }
    }

    fun removeFromCart(productId: String) {
        _cartState.update { it.removeProduct(productId) }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        _cartState.update { it.updateQuantity(productId, quantity) }
    }
}
