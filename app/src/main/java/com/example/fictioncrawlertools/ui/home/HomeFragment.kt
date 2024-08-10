package com.example.fictioncrawlertools.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fictioncrawlertools.R
import com.example.fictioncrawlertools.databinding.FragmentHomeBinding
import com.example.fictioncrawlertools.services.FctController
import com.example.fictioncrawlertools.services.models.SearchModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.test1.setOnClickListener(::test1ClickHandle)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.searchList.setLayoutManager(linearLayoutManager)

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun test1ClickHandle(view: View) {
        GlobalScope.launch {
            val searchList = FctController().searchFiction("赤心");
            if (searchList !== null) {
                withContext(Dispatchers.Main) {
                    val mMyAdapter = SearchListAdapter(searchList)
                    binding.searchList.setAdapter(mMyAdapter)
                }
            }
        }
    }

    class SearchListAdapter(private val data: MutableList<SearchModel>) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = View.inflate(parent.context, R.layout.home_search_list_item, null);
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            println(data[position])
            holder.text.text = data[position].title
        }

        override fun getItemCount(): Int {
            return data.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var text: TextView = itemView.findViewById(R.id.textView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}