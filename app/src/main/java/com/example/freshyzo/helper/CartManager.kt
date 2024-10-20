package com.example.freshyzo.helper

import com.example.freshyzo.model.ProductResponse

object CartManager {
    private val cartItems = mutableListOf<ProductResponse>()

    fun addProduct(product: ProductResponse) {
        cartItems.add(product)
    }

    fun removeProduct(product: ProductResponse) {
        cartItems.remove(product)
    }

    fun getCartItems(): List<ProductResponse> {
        return cartItems
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getTotalItems(): Int {
        return cartItems.size
    }
}
