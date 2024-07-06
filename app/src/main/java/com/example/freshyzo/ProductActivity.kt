package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freshyzo.model.DataModelCart
import com.example.freshyzo.model.DataModelOrders
import com.example.freshyzo.model.DataModelProduct

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)


        var productImageView = findViewById<ImageView>(R.id.productImageIv)
        var productTitle = findViewById<TextView>(R.id.product_title_tv)
        var productPrice = findViewById<TextView>(R.id.product_price_tv)
        var productPriceUnit = findViewById<TextView>(R.id.price_per_unit_tv)
        var selectedSize = findViewById<TextView>(R.id.selected_size_tv)
        var productDesc = findViewById<TextView>(R.id.prod_desc_body_tv)
//        var productNutritionDesc = findViewById<TextView>(R.id.nutrition_desc_body_tv)
        val subscribeButton = findViewById<Button>(R.id.subscribeBtnProduct)
        val addToCartBtn = findViewById<Button>(R.id.addToCartBtnProduct)
        val backBtn = findViewById<Button>(R.id.back_icon_product)


        val productIntent = intent.getParcelableExtra<DataModelProduct>("product")
        val cartIntent = intent.getParcelableExtra<DataModelCart>("cart")
        val ordersIntent = intent.getParcelableExtra<DataModelOrders>("orders")

        if (productIntent != null){
            productTitle.text = productIntent.title
            productImageView.setImageResource(productIntent.image)
        }

        else if (cartIntent != null){
            productTitle.text = cartIntent.title
            productImageView.setImageResource(cartIntent.image)
        }

        else if(ordersIntent != null){
            productTitle.text = ordersIntent.itemTitle
            productImageView.setImageResource((ordersIntent.itemImage))
        }

        else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }

        /* TODO("implement a proper back navigation") */
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        addToCartBtn.setOnClickListener{
            try
            {
                MainActivity().addToCart(202)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}