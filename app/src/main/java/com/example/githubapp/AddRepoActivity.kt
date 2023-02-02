package com.example.githubapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*

class AddRepoActivity : AppCompatActivity() {
    private lateinit var viewModel: ItemsListViewModel
    lateinit var owner : EditText
    lateinit var repo_name : EditText
    lateinit var error1 : TextView
    lateinit var error2 : TextView
    lateinit var add_repo : Button
    lateinit var name: String
    lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_repo)

        owner = findViewById(R.id.owner)
        repo_name =findViewById(R.id.repository)
        error1 =findViewById(R.id.error1)
        error2 = findViewById(R.id.error2)
        add_repo = findViewById(R.id.add_repo)

        val dao = ItemsDatabase.getInstance(application).itemsDao
        val viewModelFactory = ItemsListViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory)[ItemsListViewModel::class.java]

        add_repo.setOnClickListener {
            if (owner.text.toString().isEmpty()) {
                error1.visibility = View.VISIBLE
                error2.visibility = View.GONE
            }
            else if (repo_name.text.toString().isEmpty()) {
                error2.visibility = View.VISIBLE
                error1.visibility = View.GONE
            } else {
                error1.visibility = View.GONE
                error2.visibility = View.GONE

                val url = "https://api.github.com/user/repos"
                val params: MutableMap<String?, String?> = HashMap()
                params["name"] = owner.text.toString()
                params["description"] = repo_name.text.toString()

                val objRegData = JSONObject(params as Map<*, *>?)

                val jsObjRequest: JsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, objRegData,
                    Response.Listener { response ->
                        Log.i("responseee", response.toString())
                        Toast.makeText(
                            this@AddRepoActivity,
                            "Repository created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@AddRepoActivity, RepositoryListActivity::class.java))
                    },
                    Response.ErrorListener { error ->
                        try {
                            val charset = Charset.defaultCharset()
                            val str = String(error.networkResponse.data, charset)
                            val jsonObject = JSONObject(str)
                            val jsonArray = jsonObject.getJSONArray("errors")
                            for (i in 0 until jsonArray.length()) {
                                val jobject = jsonArray.getJSONObject(i)
                                val msg = jobject.getString("message")
                                Toast.makeText(this@AddRepoActivity, msg, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["Content-Accept"] = "application/json"
                        params["Authorization"] =
                            "Bearer " + "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"
                        return params
                    }
                }
                jsObjRequest.retryPolicy = DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )

                val requestQueue = Volley.newRequestQueue(this)
                requestQueue.cache.clear()
                requestQueue.add(jsObjRequest)


                val newItem = Item(owner.text.toString(),repo_name.text.toString())
                viewModel.addItem(newItem)


            }
        }


    }
}