package com.example.freshyzo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.model.BottomNavVisibilityListener
import com.example.freshyzo.model.DataModelNotification
import com.example.freshyzo.model.RecyclerAdapterNot

@Suppress("DEPRECATION")
class NotificationsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapterNot
    private var dataListNot = mutableListOf<DataModelNotification>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)

        val mBackIcon = view.findViewById<ImageView>(R.id.back_icon_cart)
        val mSearchIcon = view.findViewById<ImageView>(R.id.search_icon_not)
        val mCartIcon = view.findViewById<ImageView>(R.id.cart_icon_not)


        /* TODO("implement a proper back navigation") */
        mBackIcon.setOnClickListener{
            (activity as MainActivity).loadFragment(HomeFragment(), false)
        }


        setRecyclerView(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)

    }

    private fun setRecyclerView(view: View){
        dataListNot.add(DataModelNotification("New", "Today", resources.getString(R.string.notification_title), resources.getString(R.string.notification_body)))
        dataListNot.add(DataModelNotification("New", "Yesterday", resources.getString(R.string.notification_title_one), resources.getString(R.string.notification_body_one)))

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerViewNot)
        recyclerAdapter = RecyclerAdapterNot()

        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.setDataList(dataListNot)
    }



}
