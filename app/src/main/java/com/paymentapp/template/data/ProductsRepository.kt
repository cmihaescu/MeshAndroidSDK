package com.paymentapp.template.data

object ProductsRepository {
    val products: List<Product> = listOf(
        Product(
            id = "1",
            name = "Wireless Earbuds Pro",
            description = "Premium sound, 24h battery, active noise cancellation.",
            price = 129.99,
        ),
        Product(
            id = "2",
            name = "Smart Watch Series X",
            description = "Health tracking, GPS, 7-day battery life.",
            price = 249.99,
        ),
        Product(
            id = "3",
            name = "Portable Power Bank 20K",
            description = "Fast charging, dual USB-C, compact design.",
            price = 49.99,
        ),
    )

    fun productById(id: String): Product? = products.find { it.id == id }
}
