package com.mistream.app.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mistream.app.data.model.TmdbMovie
import com.mistream.app.databinding.FragmentSearchBinding
import com.mistream.app.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        observeViewModel()
        binding.etSearch.requestFocus()
    }

    private fun setupRecyclerView() {
        binding.rvResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchResultsAdapter { movie -> openDetail(movie) }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: ""
                if (query.length >= 2) viewModel.search(query)
                else if (query.isEmpty()) viewModel.clearResults()
            }
        })
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.results.collect { results ->
                (binding.rvResults.adapter as? SearchResultsAdapter)?.submitList(results)
                binding.tvEmpty.visibility =
                    if (results.isEmpty() && viewModel.hasSearched) View.VISIBLE else View.GONE
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { loading ->
                binding.progressSearch.visibility = if (loading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun openDetail(movie: TmdbMovie) {
        val mediaType = movie.mediaType.ifEmpty {
            if (movie.title.isNotEmpty()) "movie" else "tv"
        }
        startActivity(
            Intent(requireContext(), DetailActivity::class.java)
                .putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.id)
                .putExtra(DetailActivity.EXTRA_MEDIA_TYPE, mediaType)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
