package com.example.restyle_mobile.business_search.Activity

import Beans.Business
import Interface.BusinessService
import Interface.ProjectService
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restyle_mobile.R
import com.example.restyle_mobile.business_portfolio.Activity.Portfolio
import com.example.restyle_mobile.business_search.Adapter.BusinessAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchBusinessesActivity : AppCompatActivity() {

    lateinit var businessService: BusinessService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_businesses)

        //Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //Navigation Bar
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, SearchBusinessesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_projects -> {
                    val intent = Intent(this, SearchBusinessesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_businesses -> {
                    val intent = Intent(this, SearchBusinessesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_portfolios -> {
                    val intent = Intent(this, Portfolio::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, SearchBusinessesActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        //Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.18.175:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        businessService = retrofit.create<BusinessService>(BusinessService::class.java)
        getAllBusinesses()
    }

    private fun getAllBusinesses(){
        businessService.getBusinesses().enqueue(object : Callback<List<Business>>{
            override fun onResponse(p0: Call<List<Business>>, p1: Response<List<Business>>) {
                val business = p1?.body()

                val listBusinesses = mutableListOf<Business>()

                if(business != null){
                    for (item in business){
                        listBusinesses.add(
                            Business(
                                item.id,
                                item.name,
                                item.description,
                                item.address,
                                item.city,
                                item.expertise,
                                item.image,
                                item.remodelerId
                            )
                        )
                    }
                    val recycler = findViewById<RecyclerView>(R.id.recyclerViewBusinesses)
                    recycler.layoutManager = LinearLayoutManager(applicationContext)

                    recycler.adapter = BusinessAdapter(listBusinesses) { business ->
                        val intent = Intent(this@SearchBusinessesActivity, BusinessProfileActivity::class.java)
                        intent.putExtra("BUSINESS_ID", business.id)
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(p0: Call<List<Business>>, p1: Throwable) {
                p1.printStackTrace()
            }
        })
    }
}