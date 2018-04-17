package com.example.jhomasinas.mshopping.Config

import com.example.jhomasinas.mshopping.Model.Approved
import com.example.jhomasinas.mshopping.Model.Cart
import com.example.jhomasinas.mshopping.Model.Customer
import com.example.jhomasinas.mshopping.Model.Product
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by JhomAsinas on 4/10/2018.
 */


interface ProductApi{

    @GET("admin/showSelprod/All")
    abstract fun getProduct(): Observable<List<Product>>

    @FormUrlEncoded
    @POST("admin/getCartdata")
    abstract fun getCart(@Field("user") user:String): Observable<List<Cart>>

    @FormUrlEncoded
    @POST("admin/signCustomer")
    abstract fun signUp(@Field("username") user    :  String,
               @Field("pass")     pass    :  String,
               @Field("fname")    fname   :  String,
               @Field("address")  add     :  String,
               @Field("contact")  contact : String): Observable<ProductResponse>


    @FormUrlEncoded
    @POST("admin/login")
    abstract fun logIn(@Field("logusername")user : String,
                       @Field("logpass")    pass : String): Observable<List<Customer>>

    @FormUrlEncoded
    @POST("admin/addtoCart")
    abstract fun addtoCart(@Field("code") code :String,
                           @Field("items")items:String,
                           @Field("user") user :String): Observable<ProductResponse>

    @FormUrlEncoded
    @POST("admin/deleteCartData")
    abstract fun deleteCart(@Field("code") code : String,
                            @Field("user") user : String ): Observable<ProductResponse>

    @FormUrlEncoded
    @POST("admin/transactProd")
    abstract fun transactProd(@Field("code")    code    : String,
                              @Field("address") address : String,
                              @Field("name")    name    : String,
                              @Field("contact") contact : String,
                              @Field("user")    user    : String,
                              @Field("payment") payment : String): Observable<ProductResponse>

    @FormUrlEncoded
    @POST("admin/updateAddress")
    abstract fun updateAddress(@Field("user")    user    : String,
                               @Field("address") address : String ): Observable<ProductResponse>


    @FormUrlEncoded
    @POST("admin/getApprovedProd")
    abstract fun getTransact(@Field("user") user : String): Observable<List<Approved>>

    companion object {
        val BASE_URL = "http://192.168.1.50/mobilecom/"
        fun create(): ProductApi{
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(
                        OkHttpClient().newBuilder().addInterceptor(
                            HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY }
                        ).build()
                    )
                    .build()
            return retrofit.create(ProductApi::class.java)
        }
    }
}