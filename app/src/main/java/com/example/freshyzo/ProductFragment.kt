package com.example.freshyzo
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.example.freshyzo.model.BottomNavVisibilityListener
//import com.example.freshyzo.model.DataModelProduct
//
///**
// * A simple [Fragment] subclass.
// * Use the [ProductFragment. newInstance] factory method to
// * create an instance of this fragment.
// */
//@Suppress("DEPRECATION")
//class ProductFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_product, container, false)
//        (activity as BottomNavVisibilityListener).setBottomNavVisibility(false)
//
//
//        val productId = 202
//
//        var productImageView = view.findViewById<ImageView>(R.id.productImageIv)
//        var productTitle = view.findViewById<TextView>(R.id.product_title_tv)
//        var productPrice = view.findViewById<TextView>(R.id.product_price_tv)
//        var productPriceUnit = view.findViewById<TextView>(R.id.price_per_unit_tv)
//        var selectedSize = view.findViewById<TextView>(R.id.selected_size_tv)
//        var productDesc = view.findViewById<TextView>(R.id.prod_desc_body_tv)
//        var productNutritionDesc = view.findViewById<TextView>(R.id.nutrition_desc_body_tv)
//        val subscribeButton = view.findViewById<Button>(R.id.subscribeBtnProduct)
//        val addToCartBtn = view.findViewById<Button>(R.id.addToCartBtnProduct)
//        val backIcon = view.findViewById<ImageView>(R.id.back_icon_prod)
//
//
//        val productIntent = (activity as MainActivity).intent.getParcelableExtra<DataModelProduct>("product")
//
//        if (productIntent != null){
//            productTitle.text = productIntent.title
//            productImageView.setImageResource(productIntent.image)
//        }
//        else{
//            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
//        }
//
//        /* TODO("implement a proper back navigation") */
//        backIcon.setOnClickListener {
//            try {
////                findNavController().navigate(R.id.action_productFragment_to_homeFragment)
//                (activity as MainActivity).loadFragment(HomeFragment(), false)
//            }
//            catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
//
//        addToCartBtn.setOnClickListener{
//            try
//            {
//                (activity as MainActivity).addToCart(productId)
//            }catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
//
//        return view
//    }
//
//    override fun onResume() {
//        super.onResume()
//        (activity as BottomNavVisibilityListener).setBottomNavVisibility(false)
//    }
//
//    override fun onPause() {
//        super.onPause()
//    }
//
//
//
//}