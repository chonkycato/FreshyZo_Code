package com.example.freshyzo

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.freshyzo.adapter.BannerAdapter
import com.example.freshyzo.adapter.RecyclerAdapterHome
import com.example.freshyzo.api.ApiHandler
import com.example.freshyzo.model.CategoryResponse
import com.example.freshyzo.model.CustomerResponse
import com.example.freshyzo.model.ProductResponse
import com.example.freshyzo.model.WalletResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var mWalletBalanceTV: TextView
    private lateinit var mRechargeButton: TextView
    private lateinit var mBackToTopButton: LinearLayout
    private lateinit var mNestedScrollView: NestedScrollView
    private lateinit var mCategoryName: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewPager2: ViewPager2
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var mRecyclerAdapterHome: RecyclerAdapterHome
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var horizontalScrollViewContainer: LinearLayout
    private lateinit var customerName: String
    private lateinit var totalProductsCount: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        /** Bottom and top nav activities **/
        (activity as MainActivity).handleNavigationToolbar(null, true)
        val cartIcon = view.findViewById<ImageView>(R.id.cart_icon)
        cartIcon.setOnClickListener {
            (activity as MainActivity).loadFragment(CartFragment(), false, null)
        }

        /** Values and variables **/
        mWalletBalanceTV = view.findViewById(R.id.walletBalanceTV)
        mRechargeButton = view.findViewById(R.id.rechargeTV)
        mBackToTopButton = view.findViewById(R.id.backToTop)
        mNestedScrollView = view.findViewById(R.id.nestedScrollView)
        mCategoryName = view.findViewById(R.id.category_name)
        progressBar = view.findViewById(R.id.progressBarHome)
        horizontalScrollView = view.findViewById(R.id.categoryScrollView)
        horizontalScrollViewContainer = view.findViewById(R.id.category_layout)
        viewPager2 = view.findViewById(R.id.banner_carousel)
        totalProductsCount = view.findViewById(R.id.category_product_count)
        totalProductsCount.visibility = View.INVISIBLE

        /** Fetch Customer Name **/
        fetchCustomerName()

        /** Fetch Wallet Balance **/
        fetchWalletBalance()

        /** Carousel View **/
        fetchBannerImages()

        /** Fetch Categories **/
        fetchProductCategories()

        /** Fetch Products **/
        fetchProductList("")

        /**Recharge button**/
        mRechargeButton.setOnClickListener {
            (activity as MainActivity).loadFragment(WalletFragment(), false, null)
        }

        /**Back to top**/
        mNestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = mNestedScrollView.scrollY
            val scrollViewHeight = mNestedScrollView.height
            val scrollViewContentHeight = mNestedScrollView.getChildAt(0).height

            if (scrollY + scrollViewHeight >= scrollViewContentHeight){
                mBackToTopButton.visibility = View.VISIBLE
            }
            else{
                mBackToTopButton.visibility = View.GONE
            }
        }

        mBackToTopButton.setOnClickListener{
            mNestedScrollView.smoothScrollTo(0, 0, 1000)
        }

        /** Initialize recyclerView **/
        recyclerView = view.findViewById(R.id.recyclerViewHome)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        mRecyclerAdapterHome = RecyclerAdapterHome(requireContext())
        recyclerView.adapter = mRecyclerAdapterHome

        mRecyclerAdapterHome.onItemClick = {
            val intentHome = Intent(activity, ProductActivity::class.java)
            intentHome.putExtra("product", it as Parcelable)  // Since it implements Parcelable now, no cast needed
            (activity as MainActivity).startActivity(intentHome)
        }
        return view
    }

    /** Fetch Product Categories **/
    private fun fetchProductCategories(){
        val apiHandler = ApiHandler()
         apiHandler.fetchCategories(object: ApiHandler.CategoriesListCallback {
            override fun onSuccess(categories: List<CategoryResponse>) {
                categories.forEach { category ->
                    val categoryId = category.product_category_id
                    val categoryName = category.product_category_name
                    val categoryTitle = category.product_category_title
                    val subCategory = category.have_sub_category
                    val categoryStatus = category.category_status
                    val categoryImage = category.category_image
                    val outletId = category.outlet_id
                    Log.d("Category", "ID: $categoryId NAME: $categoryName TITLE: $categoryTitle SUBCAT: $subCategory STATUS: $categoryStatus IMAGE: $categoryImage OUTLETID: $outletId")
                   if (isAdded){
                       setupCategoryViews(categories)
                   } else{
                       Log.d("Error: ", "Home not attached to an activity.")
                   }
                }
            }

            override fun onFailure(errorMessage: String) {
                Log.e("Category fetchCategory() onFailure", errorMessage)
            }
        })
    }

    /** Fetch Product List **/
    private fun fetchProductList(categoryId: String?) {
        progressBar.visibility = View.VISIBLE
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val productID = ""
        val customerID = sharedPref.getString("customer_id", "").toString()
        val categoryID = categoryId ?: ""

        val apiHandler = ApiHandler()
        apiHandler.fetchProductList(productID, customerID, categoryID, object : ApiHandler.ProductListCallback {
            override fun onSuccess(products: List<ProductResponse>) {
                mRecyclerAdapterHome.setDataList(products)
                val totalProducts = (products.size).toString()
                totalProductsCount.text = getString(R.string.product_count, totalProducts)
                totalProductsCount.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

                for (product in products) {
                    Log.d("Product", "ID: ${product.productID}, Name: ${product.productName}, Price: ${product.productPrice}")
                }
            }

            override fun onFailure(errorMessage: String) {
                // Handle failure scenario
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Failed to load content!", Toast.LENGTH_SHORT).show()
                Log.e("ProductListError", errorMessage)
            }
        })
    }

    /** Fetch Banner Images **/
    private fun fetchBannerImages() {
        val apiHandler = ApiHandler()
        apiHandler.fetchBannerImages(object : ApiHandler.BannerListCallback {
            override fun onSuccess(images: List<String>) {
                if (isAdded) { // Check if the fragment is currently added to its activity
                    setupViewPager(images)
                }
            }
            override fun onFailure(errorMessage: String) {
                if (isAdded) { // Check if the fragment is currently added to its activity
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.e("BANNERONFAILURE", errorMessage)
                }
            }
        })
    }

    /** Fetch Customer Information */
    private fun fetchCustomerName() {
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val customerId = sharedPref.getString("customer_id", "").toString()
        val customerRole = sharedPref.getString("customer_role", "").toString()
        if (!customerId.isNullOrEmpty() && !customerRole.isNullOrEmpty()) {
            val apiHandler = ApiHandler()
            apiHandler.fetchCustomerDetails(customerId, customerRole,
                object : ApiHandler.CustomerListCallback {
                    override fun onSuccess(customerList: List<CustomerResponse>) {
                        if (customerList.isNotEmpty()) {
                            for (customer in customerList) {
                                customerName = customer.firstName
                                sharedPref.edit().putString("customer_name", customerName).apply()
                            }
                        }
                    }
                    override fun onFailure(message: String) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    /** Fetch Wallet Balance **/
    private fun fetchWalletBalance() {
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val customerID = sharedPref.getString("customer_id", null)
        val customerRole = sharedPref.getString("customer_role", null)
        Log.d("Customer Details Wallet", "$customerID $customerRole")

        val apiHandler = ApiHandler()
        if (customerRole != null && customerID != null) {
            apiHandler.fetchWalletBalance(customerID, customerRole, object : Callback<List<WalletResponse>> {
                override fun onResponse(call: Call<List<WalletResponse>>, response: Response<List<WalletResponse>>) {
                    val walletResponse = response.body()

                    // Check if the response body is not null and contains at least one element
                    if (response.isSuccessful && !walletResponse.isNullOrEmpty()) {
                        // Extract the balance_amount from the first item
                        val balanceAmount = walletResponse[0].balance_amount
                        setWalletBalance(balanceAmount)
                    } else {
                        Log.e("Wallet Error", "Failed to fetch Wallet Balance or empty response")
                        Toast.makeText(context, "Failed to fetch Wallet Balance", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<WalletResponse>>, t: Throwable) {
                    Log.e("Failed to fetch Wallet Balance", "Request failed: ${t.message}")
                    Toast.makeText(context, "Wallet Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.d("Wallet Balance", "Failed to fetch wallet balance. CustomerID/Role is null")
        }
    }

    /** Set wallet balance **/
    private fun setWalletBalance(amount: String) {
        val balanceAmount = getString(R.string.money, amount)
        mWalletBalanceTV.text = balanceAmount
    }

    /** Set Up Banner ViewPager **/
    private fun setupViewPager(imageUrls: List<String>) {
        bannerAdapter = BannerAdapter(imageUrls)
        viewPager2.adapter = bannerAdapter

        // Automatic sliding carousel
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager2.currentItem
                viewPager2.currentItem = (currentItem + 1) % imageUrls.size
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(runnable)
    }

    /** Set up Product Category Views **/
    private fun setupCategoryViews(categories: List<CategoryResponse>) {
        // Clear existing views if any
        horizontalScrollViewContainer.removeAllViews()

        // Add a default category with text "All"
        val defaultCategory = CategoryResponse(
            product_category_id = "0",
            product_category_name = "All Products",
            product_category_title = " ",
            have_sub_category = "no",
            category_status = "active",
            category_image = "",
            outlet_id = "1"
        )

        // Add the default category to the beginning of the list
        val updatedCategories = listOf(defaultCategory) + categories

        // Define the background tints
        val backgroundTints = listOf(
            R.color.primary_tint_light,
            R.color.secondary_tint_light,
            R.color.accent_tint_light
        )

        // Inflate views for each category
        updatedCategories.forEachIndexed { index, category ->
            // Inflate the existing layout
            val categoryView = LayoutInflater.from(requireContext())
                .inflate(R.layout.card_category, horizontalScrollViewContainer, false) as LinearLayout

            // Get references to the TextView in the inflated layout
            val textView = categoryView.findViewById<TextView>(R.id.item_text_hsv_one)

            // Break line between words
            val categoryName = category.product_category_name
            val modifiedCategoryName = categoryName.replace(" ", "\n")

            // Set the category name text
            textView.text = modifiedCategoryName
            Log.d("Category Name", "$categoryName")
            mCategoryName.text = categoryName

            // Set the background tint (cycle through the palette)
            val tintColor = backgroundTints[index % backgroundTints.size]
            categoryView.backgroundTintList = ContextCompat.getColorStateList(requireContext(), tintColor)

            // Add the OnClickListener to handle category click events
            categoryView.setOnClickListener {
                // Handle the category click event
                Log.d("CategoryClick", "Clicked category: ${category.product_category_name}")
                fetchProductList(category.product_category_id)

                // You can trigger actions, such as navigating to a new fragment or loading data
                onCategorySelected(category)
            }

            // Add the LinearLayout to the HorizontalScrollView container
            horizontalScrollViewContainer.addView(categoryView)
        }
    }

    // Define a function to handle category selection
    private fun onCategorySelected(category: CategoryResponse) {
        // Here you can handle the category selection, for example:
        // - Load products for the selected category
        // - Navigate to another fragment
        Log.d("CategorySelected", "Category selected: ${category.product_category_name}, ID: ${category.product_category_id}")
    }

    // Extension function to convert dp to pixels
    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun onSwitchActivity(){
        val intentHome = Intent(activity, ProductActivity::class.java)
        intentHome.putExtra("product", "item")
        (activity as MainActivity).startActivity(intentHome)
    }

}