package com.nasywa.newsapplication

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.nasywa.newsapplication.Service.RetrofitConfig
import com.nasywa.newsapplication.adapter.NewsAdapter
import com.nasywa.newsapplication.databinding.ActivityMainBinding
import com.nasywa.newsapplication.model.ResponseNews
import org.jetbrains.anko.progressDialog
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity(),View.OnClickListener {
  val date = getCurreDateTime()
    var refUser : DatabaseReference? = null
    var firebaseUser : FirebaseUser? = null
    lateinit var mainBinding: ActivityMainBinding

    private fun getCurreDateTime(): Date {
        return Calendar.getInstance().time

    }
    fun Date.toString(format: String,locale : Locale = Locale.getDefault()):String{
        val formatter = SimpleDateFormat(format,locale)
        return formatter.format(this)

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()
        mainBinding.apply {
            ivProfile.setOnClickListener(this@MainActivity)
            tvDateMain.text = date.toString("dd/MM/yyyy")
        }
       // mainBinding.ibProfile.setOnclickListener(this)
        //mainBinding.tvDateMain.text=date.toString("dd/mm/yyyy")
        getNews()

    }

    private fun getNews() {
        val country = "id"
        val apiKey = "be13eca672c446f58ef2e5e1384e7aa4"
        val loading = ProgressDialog.show(this,"Request Data","Loading...")
        RetrofitConfig.getInstance().getNewsHeadLines(country, apiKey).enqueue(
            object : retrofit2.Callback<ResponseNews>{
                override fun onResponse(
                    call: Call<ResponseNews>,
                    response: Response<ResponseNews>
                ) {
                    Log.d ("Response","Success" + response.body()?.articles)
                    loading.dismiss()
                    if (response.isSuccessful){
                        val status = response.body()?.status
                        if (status.equals("ok")){
                            Toast.makeText(this@MainActivity,"Data Success!",Toast.LENGTH_SHORT).show()
                            val newsData = response.body()?.articles
                            val newsAdapter = NewsAdapter(this@MainActivity,newsData)
                            mainBinding.rvMain.apply {
                                adapter = newsAdapter
                                layoutManager = LinearLayoutManager(this@MainActivity)
                                var dataHighLight = response.body()
                                Glide.with(this@MainActivity).load(dataHighLight?.articles?.component4()?.urlToImage).centerCrop().into(mainBinding.ivHighlight)
                                mainBinding.apply {
                                    tvTitleHighlight.text = dataHighLight?.articles?.component4()?.title
                                    tvDateHighlight.text = dataHighLight?.articles?.component4()?.publishedAt
                                    tvNameAuthor.text = dataHighLight?.articles?.component4()?.author
                                }
                            }

                        }else{
                            Toast.makeText(this@MainActivity,"Data Failed",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                    Log.d("Response","Failed : " + t.localizedMessage)
                    loading.dismiss()
                }


            }

        )
    }

    companion object{
        fun getLaunchService(from: Context)= Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}