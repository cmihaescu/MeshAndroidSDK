package com.paymentapp.template.data

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val priceFormatted: String = "$${"%.2f".format(price)}",
)

data class CartItem(
    val product: Product,
    val quantity: Int,
) {
    val subtotal: Double get() = product.price * quantity
    val subtotalFormatted: String get() = "$${"%.2f".format(subtotal)}"
}
