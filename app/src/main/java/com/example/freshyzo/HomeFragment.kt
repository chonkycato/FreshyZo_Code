package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.freshyzo.adapter.BannerAdapter
import com.example.freshyzo.adapter.RecyclerAdapterHome
import com.example.freshyzo.api.ApiHandler
import com.example.freshyzo.login.SignupFragment
import com.example.freshyzo.model.DataModelProduct


class HomeFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var  mRecyclerAdapterHome: RecyclerAdapterHome
    private var productDataList = mutableListOf<DataModelProduct>()

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

        /** Carousel View **/
        //fetchBannerImages()

        /** Values and variables **/

        val mLogOutButton = view.findViewById<Button>(R.id.logoutButton)
        val mWalletBalanceTV = view.findViewById<TextView>(R.id.walletBalanceTV)
        val mRechargeButton = view.findViewById<Button>(R.id.rechargeButton)

        val mCategoryAll = view.findViewById<LinearLayout>(R.id.categoryAllProducts)
        val mCategoryMilk = view.findViewById<LinearLayout>(R.id.categoryMilk)
        val mCategoryMilkProd = view.findViewById<LinearLayout>(R.id.categoryMilkProd)
        val mCategoryBread = view.findViewById<LinearLayout>(R.id.categoryBread)
        val mbackToTopButton = view.findViewById<LinearLayout>(R.id.backToTop)
        val mNestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollView)

        viewPager2 = view.findViewById(R.id.banner_carousel)


        /**Back to top**/

        mNestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = mNestedScrollView.scrollY
            val scrollViewHeight = mNestedScrollView.height
            val scrollViewContentHeight = mNestedScrollView.getChildAt(0).height

            if (scrollY + scrollViewHeight >= scrollViewContentHeight){
                mbackToTopButton.visibility = View.VISIBLE
            }
            else{
                mbackToTopButton.visibility = View.GONE
            }
        }

        mbackToTopButton.setOnClickListener{
            mNestedScrollView.smoothScrollTo(0, 0, 1000)
        }



        /** Categories **/

        mCategoryAll.setOnClickListener{
            (activity as MainActivity).loadFragment(CategoriesFragment(), false, null)
        }

        mCategoryMilk.setOnClickListener{
            (activity as MainActivity).loadFragment(CategoriesFragment(), false, "Milk")
        }

        mCategoryMilkProd.setOnClickListener{
            (activity as MainActivity).loadFragment(CategoriesFragment(), false, "Milk Products")
        }

        mCategoryBread.setOnClickListener{
            (activity as MainActivity).loadFragment(CategoriesFragment(), false, "Bread")
        }

        /** Add data to recyclerView **/
        productDataList.add(DataModelProduct("Ghee","FreshyZo Ghee Butter", "100% Pure desi ghee","2","250gms","₹350",R.drawable.img_ghee))
        productDataList.add(DataModelProduct("Milk","FreshyZo Cow Milk", "100% pure desi cow milk","1","250gms","₹350",R.drawable.img_milk))
        productDataList.add(DataModelProduct("Milk","FreshyZo Buffalo Milk", "100% pure buffalo milk","2","250gms","₹350",R.drawable.img_milk_buff))
        productDataList.add(DataModelProduct("Dahi","FreshyZo Malai Dahi", "Fresh and creamy malai dahi","4","250gms","₹350",R.drawable.img_dahi))
        productDataList.add(DataModelProduct("Paneer","FreshyZo Paneer", "Fresh and soft paneer","2","250gms","₹350",R.drawable.img_paneer))


        /** Initialize recyclerView **/
        recyclerView = view.findViewById(R.id.recyclerViewHome)
        recyclerView.layoutManager = GridLayoutManager(context,2)
//        recyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerAdapterHome = RecyclerAdapterHome(requireContext())
        recyclerView.adapter = mRecyclerAdapterHome

        mRecyclerAdapterHome.onItemClick = {
            val intentHome = Intent(activity, ProductActivity::class.java)
            intentHome.putExtra("product", it as Parcelable)
            (activity as MainActivity).startActivity(intentHome)
        }

        mRecyclerAdapterHome.setDataList(productDataList)

        /** Recharge Button**/
        mRechargeButton.setOnClickListener {
            (activity as MainActivity).loadFragment(WalletFragment(), false, null)
        }

        mLogOutButton.setOnClickListener{
//            (activity as MainActivity).changeLoginState(false)
//            findNavController().navigate(R.id.action_homeFragment_to_productFragment)
            (activity as MainActivity).loadFragment(SignupFragment(), false, null)
//            val intent = Intent(requireContext(), PaymentActivity::class.java)
////            intent.putExtra("status", "failure")
//            (activity as MainActivity).startActivity(intent)
        }
        return view
    }

    /** Fetch banner images **/
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
                }
            }
        })
    }


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

    private fun onSwitchActivity(){
        val intentHome = Intent(activity, ProductActivity::class.java)
        intentHome.putExtra("product", "item")
        (activity as MainActivity).startActivity(intentHome)
    }

    //    private var imageListener = ImageListener { position, imageView -> imageView.setImageResource(carouselArray[position]) }
}