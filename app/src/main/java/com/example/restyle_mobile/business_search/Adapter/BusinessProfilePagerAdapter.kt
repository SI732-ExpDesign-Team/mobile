package com.example.restyle_mobile.business_search.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.restyle_mobile.business_search.Fragment.BusinessPortfolioFragment
import com.example.restyle_mobile.business_search.Fragment.BusinessProfileFragment
import com.example.restyle_mobile.business_search.Fragment.BusinessProjectsFragment

class BusinessProfilePagerAdapter(
    fragment: FragmentActivity,
    private val businessId: Int
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BusinessProfileFragment.newInstance(businessId)    //Profile
            1 -> BusinessProjectsFragment.newInstance(businessId)   //Projects
            2 -> BusinessPortfolioFragment.newInstance(businessId)  //Portfolio
            else -> BusinessProfileFragment()
        }
    }
}
