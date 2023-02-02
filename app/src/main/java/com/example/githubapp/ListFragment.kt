package com.example.githubapp

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.githubapp.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONException


class ListFragment : Fragment() {
    lateinit var name: String
    lateinit var description: String
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ItemsListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application
        val dao = ItemsDatabase.getInstance(application).itemsDao

        val viewModelFactory = ItemsListViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory)[ItemsListViewModel::class.java]
        binding.itemsListViewModel = viewModel // data binding
        binding.lifecycleOwner = viewLifecycleOwner // live data for data binding


        (activity as AppCompatActivity).supportActionBar?.title = "Local Repositories"

        binding.viewBtn.visibility=View.GONE


        val adapter = ItemsAdapter(
        )

        binding.list.adapter = adapter
        viewModel.items.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        this.context?.let {
            enableSwipe(it, view, binding.list, adapter)
        }



        return  view
    }

    private fun enableSwipe(context: Context, view: View, recyclerView: RecyclerView, adapter: ItemsAdapter) {

        class SwipeCallback: ItemTouchHelperCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemPosition = viewHolder.adapterPosition
                val item = adapter.currentList[itemPosition]
                viewModel.removeItem(item)
                Snackbar.make(
                    view,
                    getString(R.string.removed_item, item.name),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(getString(R.string.undo_link)) {
                        viewModel.addItem(item)
                        Toast.makeText(
                            activity,
                            getString(R.string.undo_remove, item.name),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .show()
            }
        }

        val swipeCallback = SwipeCallback()
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }






}