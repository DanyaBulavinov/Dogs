package com.example.dogs.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogs.databinding.FragmentListBinding
import com.example.dogs.model.DogBreed
import com.example.dogs.repository.Repository
import com.example.dogs.viewmodel.ListViewModel
import com.example.dogs.viewmodel.ListViewModelFactory


class ListFragment : Fragment() {

    private val repository by lazy {
        Repository()
    }

    private val viewModelFactory by lazy {
        ListViewModelFactory(requireActivity().application, repository)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ListViewModel::class.java]
    }

    private val dogListAdapter = DogListAdapter(arrayListOf())

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = _binding ?: throw RuntimeException("FragmentListBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDogs()

        binding.dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogListAdapter
        }

        binding.refreshLayout.setOnRefreshListener {
            viewSetup()
            viewModel.getDogs()
            binding.refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    private fun viewSetup() {
        binding.dogsList.visibility = View.GONE
        binding.listError.visibility = View.GONE
        binding.loadingView.visibility = View.VISIBLE
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(viewLifecycleOwner) {
            binding.dogsList.visibility = View.VISIBLE
            retrieveList(it)
        }
        viewModel.dogsLoadError.observe(viewLifecycleOwner) {
            binding.listError.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun retrieveList(dogs: List<DogBreed>) {
        dogListAdapter.apply {
            addDogs(dogs)
            notifyDataSetChanged()
        }
    }
}




