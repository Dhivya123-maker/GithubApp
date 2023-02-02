package com.example.githubapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONException

class RepositoryListActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    internal lateinit var repoModelList: ArrayList<RepoModel>
    lateinit var name: String
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var description: String
    lateinit var url: String
    lateinit var add:TextView
    lateinit var view_btn:Button
    private var repoAdapter: RepoAdapter? = null
    lateinit var progressbar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        val actionBar = supportActionBar
        actionBar!!.title = "Your Repositories"
        actionBar.setDisplayUseLogoEnabled(true)
       actionBar.setDisplayShowHomeEnabled(true)

        recyclerView = findViewById(R.id.recyclerview)
        swipeRefreshLayout = findViewById(R.id.refreshLayout)
        add = findViewById(R.id.add_btn)
        view_btn = findViewById(R.id.view_btn)
        progressbar = findViewById(R.id.progressBar)
        progressbar.setVisibility(View.VISIBLE)

        internet()

        swipeRefreshLayout.setOnRefreshListener(
            OnRefreshListener {
                swipeRefreshLayout.setRefreshing(false)
                internet()
            }
        )


        view_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


    }
    fun data(){
        val URL = "https://api.github.com/users/Dhivya123-maker/repos"
        val stringRequest = StringRequest(
            Request.Method.GET, URL,
            { response ->
                Log.i("responseeeeeeeee", response!!)
                repoModelList = ArrayList<RepoModel>()
                progressbar.setVisibility(View.GONE)

                try {
                    val data = JSONArray(response)
                    for (i in 0 until data.length()) {
                        if (data.length()>=1) {
                            val json = data.getJSONObject(i)
                            name = json.getString("name")
                            description = json.getString("description")
                            url = json.getString("html_url")
                            val repoModel = RepoModel()
                            repoModel.setRepoNames(name)
                            repoModel.setDescs(description)
                            repoModel.setUrls(url)
                            repoModelList.add(repoModel)


                        } else {
                            add.setVisibility(View.VISIBLE)
                        }

                    }


                    recyclerView.setLayoutManager(LinearLayoutManager(this))
                    repoAdapter = RepoAdapter(this, repoModelList)
                    recyclerView.setAdapter(repoAdapter)


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { }

        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)

    }
    fun internet() {
        val connMgr = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connMgr != null) {
            val activeNetworkInfo = connMgr.activeNetworkInfo
            if (activeNetworkInfo != null) {
                data()
                if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    data()
                }
            } else {
                val layout = findViewById<RelativeLayout>(R.id.layout)
                val snackbar = Snackbar
                    .make(
                        layout,
                        "Check your internet connection",
                        Snackbar.LENGTH_LONG
                    )
                snackbar.show()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu)
        return  true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add ->
                startActivity(Intent(this, AddRepoActivity::class.java))

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val dialog: Dialog
        dialog = Dialog(this@RepositoryListActivity)

        dialog.setContentView(R.layout.alert_dialogbox)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )


        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        val title = dialog.findViewById<TextView>(R.id.title)
        val no = dialog.findViewById<RelativeLayout>(R.id.no)
        val yes = dialog.findViewById<RelativeLayout>(R.id.yes)
        title.text = "Are you sure want to exit"
        no.setOnClickListener { dialog.dismiss() }
        yes.setOnClickListener { finishAffinity() }
    }
}