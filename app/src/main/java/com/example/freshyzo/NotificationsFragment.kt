package com.example.freshyzo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.adapter.RecyclerAdapterNot
import com.example.freshyzo.model.DataModelNotification

@Suppress("DEPRECATION")
class NotificationsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapterNot
    private var dataList = mutableListOf<DataModelNotification>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        /** Handle top and bottom nav**/
        (activity as MainActivity).handleNavigationToolbar("Notifications", true)


        //Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        val mBackIcon = view.findViewById<Button>(R.id.back_icon_not)
//        val mSearchView = view.findViewById<SearchView>(R.id.searchView_not)
        val mCartIcon = view.findViewById<ImageView>(R.id.cart_icon_not)

        mBackIcon.setOnClickListener{
            (activity as MainActivity).backNavigation()
        }
        setRecyclerView(view)

        return view
    }


    private fun setRecyclerView(view: View){
        dataList.add(DataModelNotification("1 day ago", "Hurray!", resources.getString(R.string.notification_body)))
        dataList.add(DataModelNotification("4 days ago", "Credit Successful",  resources.getString(R.string.notification_body_one)))
        dataList.add(DataModelNotification("7 days ago", "Hurray!", resources.getString(R.string.notification_body)))
        dataList.add(DataModelNotification("15 days ago", "Credit Successful",  resources.getString(R.string.notification_body_one)))
        dataList.add(DataModelNotification("18 days ago", "Hurray!", resources.getString(R.string.notification_body)))
        dataList.add(DataModelNotification("21 days ago", "Credit Successful",  resources.getString(R.string.notification_body_one)))
        dataList.add(DataModelNotification("22 days ago", "Hurray!", resources.getString(R.string.notification_body)))
        dataList.add(DataModelNotification("26 days ago", "Credit Successful",  resources.getString(R.string.notification_body_one)))
        dataList.add(DataModelNotification("28 days ago", "Hurray!", resources.getString(R.string.notification_body)))
        dataList.add(DataModelNotification("29 days ago", "Credit Successful",  resources.getString(R.string.notification_body_one)))


        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerViewNot)
        recyclerAdapter = RecyclerAdapterNot()

        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.setDataList(dataList)
    }



}
