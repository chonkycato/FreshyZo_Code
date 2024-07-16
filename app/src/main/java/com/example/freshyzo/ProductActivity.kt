package com.example.freshyzo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freshyzo.model.DataModelCart
import com.example.freshyzo.model.DataModelProduct
import java.net.HttpURLConnection
import java.net.URL

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        if(isInternetAvailable()){
            Toast.makeText(this, "INTERNET IS WORKING", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "NO INTERNET", Toast.LENGTH_SHORT).show()
        }


        var productImageView = findViewById<ImageView>(R.id.productImageIv)
        var productTitle = findViewById<TextView>(R.id.product_title_tv)
        var productPrice = findViewById<TextView>(R.id.product_price_tv)
        var productPriceUnit = findViewById<TextView>(R.id.price_per_unit_tv)
//        var selectedSize = findViewById<TextView>(R.id.selected_size_tv)
        var productDesc = findViewById<TextView>(R.id.prod_desc_body_tv)
//        var productNutritionDesc = findViewById<TextView>(R.id.nutrition_desc_body_tv)
        val subscribeButton = findViewById<Button>(R.id.subscribeBtnProduct)
        val addToCartBtn = findViewById<Button>(R.id.addToCartBtnProduct)

        /** Handle back navigation **/
        val backNavIcon = findViewById<Button>(R.id.back_icon_product)
        backNavIcon.setOnClickListener { finish() }

        /** Add to subscriptions **/
//        subscribeButton.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }


        val productIntent = intent.getParcelableExtra<DataModelProduct>("product")
        val cartIntent = intent.getParcelableExtra<DataModelCart>("cart")

        try {
            if (productIntent != null){
                productTitle.text = productIntent.title
                productImageView.setImageResource(productIntent.image)
            }

            else if (cartIntent != null){
                productTitle.text = cartIntent.title
                productImageView.setImageResource(cartIntent.image)
            }

            else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }



        addToCartBtn.setOnClickListener{
            try {
              MainActivity().addToCart(202)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun isInternetAvailable(): Boolean {
        return try {
            val url = URL("https://www.google.com")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 3000
            connection.connect()
            connection.responseCode == 200
        } catch (e: Exception) {
            Log.d("ERROR INTERNET", e.toString(), )
            false
        }
    }
}