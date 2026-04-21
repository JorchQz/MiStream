package com.mistream.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mistream.app.R
import com.mistream.app.data.model.TmdbMovie
import com.mistream.app.databinding.FragmentMainBinding
import com.mistream.app.ui.detail.DetailActivity
import com.mistream.app.ui.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeViewModel()

        binding.searchButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecyclerViews() {
        val onClick: (TmdbMovie) -> Unit = { movie -> openDetail(movie) }

        binding.rvTrending.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = MediaAdapter(onClick)
        }
        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = MediaAdapter(onClick)
        }
        binding.rvSeries.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = MediaAdapter(onClick)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.trending.collect { items ->
                (binding.rvTrending.adapter as? MediaAdapter)?.submitList(items)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularMovies.collect { items ->
                (binding.rvMovies.adapter as? MediaAdapter)?.submitList(items)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularSeries.collect { items ->
                (binding.rvSeries.adapter as? MediaAdapter)?.submitList(items)
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
