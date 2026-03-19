package com.paymentapp.template.cart

import com.paymentapp.template.data.CartItem
import com.paymentapp.template.data.Product

data class CartState(
    val items: List<CartItem> = emptyList(),
) {
    val itemCount: Int get() = items.sumOf { it.quantity }
    val subtotal: Double get() = items.sumOf { it.subtotal }
    val subtotalFormatted: String get() = "$${"%.2f".format(subtotal)}"
    val isEmpty: Boolean get() = items.isEmpty()
}

fun CartState.addProduct(product: Product, quantity: Int = 1): CartState {
    val existing = items.indexOfFirst { it.product.id == product.id }
    val newItems = if (existing >= 0) {
        items.toMutableList().apply {
            set(existing, this@apply[existing].copy(quantity = this[existing].quantity + quantity))
        }
    } else {
        items + CartItem(product = product, quantity = quantity)
    }
    return copy(items = newItems)
}

fun CartState.removeProduct(productId: String): CartState {
    return copy(items = items.filter { it.product.id != productId })
}

fun CartState.updateQuantity(productId: String, quantity: Int): CartState {
    if (quantity <= 0) return removeProduct(productId)
    val newItems = items.map {
        if (it.product.id == productId) it.copy(quantity = quantity) else it
    }
    return copy(items = newItems)
}
