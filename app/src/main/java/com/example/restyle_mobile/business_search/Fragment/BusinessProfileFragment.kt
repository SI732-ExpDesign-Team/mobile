package com.example.restyle_mobile.business_search.Fragment

import Beans.Business
import Interface.BusinessService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.restyle_mobile.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BusinessProfileFragment : Fragment() {

    private var businessId: Int = 0
    private lateinit var businessService: BusinessService

    private var businessNameTextView: TextView? = null
    private var businessDescriptionTextView: TextView? = null
    private var businessImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        businessId = arguments?.getInt("BUSINESS_ID") ?: 0

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.18.175:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        businessService = retrofit.create<BusinessService>(BusinessService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_business_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        businessNameTextView = view.findViewById<TextView>(R.id.businessCardName)
        businessDescriptionTextView = view.findViewById<TextView>(R.id.businessCardDescription)
        businessImageView = view.findViewById<ImageView>(R.id.businessCardImage)

        loadBusinessProfile(businessId)
    }

    companion object {
        fun newInstance(businessId: Int): BusinessProfileFragment {
            val fragment = BusinessProfileFragment()
            val args = Bundle()
            args.putInt("BUSINESS_ID", businessId)
            fragment.arguments = args
            return fragment
        }
    }

    private fun loadBusinessProfile(businessId: Int){
        businessService.getBusinessById(businessId).enqueue(object : Callback<Business> {
            override fun onResponse(call: Call<Business>, response: Response<Business>) {
                val business = response.body()

                if (business != null) {
                    businessNameTextView?.text = business.name
                    businessDescriptionTextView?.text = business.description

                    Picasso.get()
                        .load(business.image)
                        .into(businessImageView)
                }
            }
            override fun onFailure(call: Call<Business>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}