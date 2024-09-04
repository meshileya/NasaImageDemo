package com.nasa.demo.presentation.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.nasa.demo.common.hide
import com.nasa.demo.common.show
import com.nasa.demo.common.toast
import com.nasa.demo.databinding.FragmentNasaMainBinding
import com.nasa.demo.presentation.ui.adapter.NasaFavoriteAdapter
import com.nasa.demo.presentation.ui.adapter.NasaImageAdapter
import com.nasa.demo.presentation.viewmodel.NasaImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentNasaMain : Fragment() {

    @Inject
    lateinit var viewModel: NasaImageViewModel

    private var _binding: FragmentNasaMainBinding? = null
    private val binding get() = _binding!!

    private val imagesAdapter: NasaImageAdapter by lazy {
        NasaImageAdapter { image ->
            val action = if (image.isFavorite) "add to" else "remove from"
            val message = "Are you sure you want to $action your favorites?"

            AlertDialog.Builder(requireContext())
                .setTitle("Confirm Action")
                .setMessage(message)
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.toggleFavorite(image)
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private val favoriteAdapter by lazy {
        NasaFavoriteAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNasaMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()
        setupRefreshLayout()
        observeLoadState()
    }

    private fun setupRecyclerViews() {
        with(binding) {
            recyclerView.adapter = imagesAdapter
            favoriteRecyclerView.adapter = favoriteAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.images.collectLatest { pagingData ->
                    imagesAdapter.submitData(pagingData)
                }
            }
        }

        viewModel.favoriteImages.observe(viewLifecycleOwner) { favorites ->
            if(favorites.isEmpty()){
                binding.favoriteRecyclerView.hide()
            }else{
                binding.favoriteRecyclerView.show()
            }
            favoriteAdapter.updateList(favorites)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.exceptionMessage.observe(viewLifecycleOwner) { message ->
            requireActivity().toast(message)
        }
    }

    private fun setupRefreshLayout() {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            imagesAdapter.loadStateFlow.collectLatest { loadStates ->
                handleEmptyState(loadStates)
                handleErrorState(loadStates)
            }
        }
    }

    private fun handleEmptyState(loadStates: CombinedLoadStates) {
        val isListEmpty = loadStates.refresh is LoadState.NotLoading && imagesAdapter.itemCount == 0
        binding.emptyStateTextView.isVisible = isListEmpty
    }

    private fun handleErrorState(loadStates: CombinedLoadStates) {
        val errorState = loadStates.source.refresh as? LoadState.Error
            ?: loadStates.append as? LoadState.Error
            ?: loadStates.prepend as? LoadState.Error

        errorState?.let { error ->
            viewModel.onError(error.error)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear binding reference to avoid memory leaks
    }
}
