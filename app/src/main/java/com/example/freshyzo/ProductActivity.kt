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
import com.bumptech.glide.Glide
import com.example.freshyzo.helper.CartManager
import com.example.freshyzo.model.ProductResponse

class ProductActivity : AppCompatActivity() {

    private lateinit var mCalltoActionLayout: LinearLayout
    private lateinit var mCartIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        // Get the product from the intent (now using Parcelable)
        val product = intent.getParcelableExtra<ProductResponse>("product")
        mCalltoActionLayout = findViewById(R.id.callToActionLayout)

        // Show product details on the UI
        product?.let {
            updateProductDetailsUI(it)
        }

        // Handle UI components
        val addToCartBtn = findViewById<Button>(R.id.addToCartBtnProduct)
        val subscribeButton = findViewById<Button>(R.id.subscribeBtnProduct)

        try {
            if (product != null) {
                updateProductDetailsUI(product)

                // Only add com.example.freshyzo.model.ProductResponse to cart
                addToCartBtn.setOnClickListener {
                    addToCart(product)
                }
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
        val currentProduct = intent.getParcelableExtra<ProductResponse>("product")
        val bundle = Bundle().apply {
            putParcelable("productDetails", currentProduct)
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

    private fun updateProductDetailsUI(product: ProductResponse) {
        val productImageView = findViewById<ImageView>(R.id.productImageIv)
        val productTitle = findViewById<TextView>(R.id.product_title_tv)
        productTitle.text = product.productName

        // Load the image using Glide
        Glide.with(this)
            .load("https://freshyzo.com/admin/uploads/product_image/${product.productImage}")
            .into(productImageView)
    }

    private fun addToCart(product: ProductResponse) {
        CartManager.addProduct(product)
        Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show()
        Log.d("ProductActivity", "Product added to cart: ${product.productName}")
    }
}
