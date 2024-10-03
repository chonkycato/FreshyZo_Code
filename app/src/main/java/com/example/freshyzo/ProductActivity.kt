package com.example.freshyzo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freshyzo.helper.CartManager
import com.example.freshyzo.model.DataModelProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class ProductActivity : AppCompatActivity() {

    private lateinit var mCalltoActionLayout: LinearLayout
    private lateinit var mCartIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        // Get the product from the intent
        val product = intent.getSerializableExtra("product") as? DataModelProduct
        mCalltoActionLayout = findViewById(R.id.callToActionLayout)

        // Show product details on the UI
        product?.let {
            updateProductDetailsUI(it)
        }

        if (isInternetAvailable()) {
            Toast.makeText(this, "INTERNET IS WORKING", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "NO INTERNET", Toast.LENGTH_SHORT).show()
        }

        // Handle UI components
        val addToCartBtn = findViewById<Button>(R.id.addToCartBtnProduct)
        val subscribeButton = findViewById<Button>(R.id.subscribeBtnProduct)
        val productIntent = intent.getParcelableExtra<DataModelProduct>("product")
        val cartIntent = intent.getParcelableExtra<com.example.freshyzo.model.Cart>("cart")

        try {
            if (productIntent != null) {
                updateProductDetailsUI(productIntent)

                // Only add DataModelProduct to cart
                addToCartBtn.setOnClickListener {
                    addToCart(productIntent)
                }
            } else if (cartIntent != null) {
                updateCartDetailsUI(cartIntent)
                Toast.makeText(this, "Can't add a cart item directly to the cart", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }

        // Navigating to SubscribeProductFragment
        subscribeButton.setOnClickListener {
            navigateToSubscribeProduct()
        }
    }

    private fun navigateToSubscribeProduct(){
        // Create a new instance of SubscribeProductFragment
        val subscribeFragment = SubscribeProductFragment()

        // Pass product details via arguments
        val currentProduct = intent.getSerializableExtra("product")
        val bundle = Bundle().apply {
            putSerializable("productDetails", currentProduct)
        }
        subscribeFragment.arguments = bundle

        mCalltoActionLayout.visibility = View.INVISIBLE
        // Replace the current fragment with SubscribeProductFragment
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.productFragmentContainerView, subscribeFragment)
            .addToBackStack(null)  // Add to back stack if you want to allow back navigation
            .commit()

        supportFragmentManager.addOnBackStackChangedListener{
            if (supportFragmentManager.backStackEntryCount == 0) {
                // Show the layout again when back in ProductActivity
                mCalltoActionLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun updateProductDetailsUI(product: DataModelProduct) {
        val productImageView = findViewById<ImageView>(R.id.productImageIv)
        val productTitle = findViewById<TextView>(R.id.product_title_tv)
        productTitle.text = product.title
        productImageView.setImageResource(product.image)
    }

    private fun updateCartDetailsUI(cart: com.example.freshyzo.model.Cart) {
        val productImageView = findViewById<ImageView>(R.id.productImageIv)
        val productTitle = findViewById<TextView>(R.id.product_title_tv)
        productTitle.text = cart.title
        productImageView.setImageResource(cart.image)
    }

    private fun addToCart(product: DataModelProduct) {
        CartManager.addProduct(product)
        Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show()
        Log.d("ProductActivity", "Product added to cart: ${product.title}")
    }

    private fun isInternetAvailable(): Boolean {
        return runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    val url = URL("https://www.google.com")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.connectTimeout = 3000
                    connection.connect()
                    connection.responseCode == 200
                } catch (e: Exception) {
                    Log.d("ERROR INTERNET", e.toString())
                    false
                }
            }
        }
    }
}
