package Interface

import Beans.Business
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BusinessService {

    @GET("business")
    fun getBusinesses(): Call<List<Business>>

    @GET("business/{id}")
    fun getBusinessById(@Path("id") businessId: Int): Call<Business>
}