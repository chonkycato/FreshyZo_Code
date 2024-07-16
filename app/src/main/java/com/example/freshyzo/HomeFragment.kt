package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.freshyzo.model.BannerAdapter
import com.example.freshyzo.model.BottomNavVisibilityListener
import com.example.freshyzo.model.ButtonClickListener
import com.example.freshyzo.model.DataModelProduct
import com.example.freshyzo.model.RecyclerAdapterHome

//import com.synnapps.carouselview.CarouselView
//import com.synnapps.carouselview.ImageListener


class HomeFragment : Fragment(), ButtonClickListener {

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
        (activity as MainActivity).showBottomNav()

        /** Carousel View **/

        viewPager2 = view.findViewById(R.id.banner_carousel)
        val bannerImages = listOf(
            R.drawable.carousel_one,
            R.drawable.carousel_two,
            R.drawable.carousel_three,
            R.drawable.carousel_four
        )

        bannerAdapter = BannerAdapter(bannerImages)
        viewPager2.adapter = bannerAdapter

        //Automatic sliding carousel
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager2.currentItem
                viewPager2.currentItem = (currentItem + 1) % bannerImages.size
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(runnable)

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
        productDataList.add(DataModelProduct("Ghee","FreshyZo Ghee Butter", "100% Pure desi ghee","250 gms","₹350",R.drawable.img_ghee))
        productDataList.add(DataModelProduct("Milk","FreshyZo Cow Milk", "100% pure desi cow milk","200 ml","₹25",R.drawable.img_milk))
        productDataList.add(DataModelProduct("Milk","FreshyZo Buffalo Milk", "100% pure buffalo milk","200 ml","₹25",R.drawable.img_milk_buff))
        productDataList.add(DataModelProduct("Dahi","FreshyZo Malai Dahi", "Fresh and creamy malai dahi","250 ml","₹35",R.drawable.img_dahi))
        productDataList.add(DataModelProduct("Paneer","FreshyZo Paneer", "Fresh and soft paneer","250 gms","₹250",R.drawable.img_paneer))

        /** Initialize recyclerView **/
        recyclerView = view.findViewById(R.id.recyclerViewHome)
        recyclerView.layoutManager = GridLayoutManager(context,2)
//        recyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerAdapterHome = RecyclerAdapterHome(requireContext())
        recyclerView.adapter = mRecyclerAdapterHome

        mRecyclerAdapterHome.onItemClick = {
            val intentHome = Intent(activity, ProductActivity::class.java)
            intentHome.putExtra("product", it)
            (activity as MainActivity).startActivity(intentHome)
        }

        mRecyclerAdapterHome.setDataList(productDataList)

        /** Recharge Button**/
        mRechargeButton.setOnClickListener {
            (activity as MainActivity).loadFragment(WalletFragment(), false, null)
        }

        mLogOutButton.setOnClickListener{
//            (activity as MainActivity).changeLoginState(false)
////            findNavController().navigate(R.id.action_homeFragment_to_productFragment)
//            (activity as MainActivity).loadFragment(LoginFragment(), false, null)
            val intent = Intent(requireContext(), PaymentActivity::class.java)
//            intent.putExtra("status", "failure")
            (activity as MainActivity).startActivity(intent)
        }
        return view
    }

    override fun onButtonClicked(position : Int, data: DataModelProduct){
        Log.d("Position", position.toString())
    }

    override fun onResume() {
        super.onResume()
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)
    }

    private fun onSwitchActivity(){
        val intentHome = Intent(activity, ProductActivity::class.java)
        intentHome.putExtra("product", "item")
        (activity as MainActivity).startActivity(intentHome)
    }

//    private var imageListener = ImageListener { position, imageView -> imageView.setImageResource(carouselArray[position]) }
}