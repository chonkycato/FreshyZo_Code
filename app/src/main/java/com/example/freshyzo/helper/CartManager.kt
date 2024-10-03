package com.example.freshyzo.helper

import com.example.freshyzo.model.DataModelProduct

object CartManager {
    private val cartItems = mutableListOf<DataModelProduct>()

    fun addProduct(product: DataModelProduct) {
        cartItems.add(product)
    }

    fun removeProduct(product: DataModelProduct) {
        cartItems.remove(product)
    }

    fun getCartItems(): List<DataModelProduct> {
        return cartItems
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getTotalItems(): Int {
        return cartItems.size
    }
}
