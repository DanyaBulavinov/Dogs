package com.example.dogs.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogs.databinding.FragmentListBinding
import com.example.dogs.model.DogBreed
import com.example.dogs.repository.Repository
import com.example.dogs.utils.Status
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

        refreshDogList()

        binding.dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogListAdapter
        }
//        binding.refreshLayout.setOnRefreshListener {
//            with(binding) {
//                dogsList.visibility = View.GONE
//                listError.visibility = View.GONE
//                loadingView.visibility = View.VISIBLE
//            }
//            binding.refreshLayout.isRefreshing = false
//        }

        binding.refreshLayout.setOnRefreshListener {
            refreshDogList()
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun refreshDogList() {
        viewModel.getDogs()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogsList.observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.dogsList.visibility = View.VISIBLE
                        binding.loadingView.visibility = View.GONE
                        resource.data?.let { dogs ->
                            retrieveList(dogs)
                            viewModel.storeDogsLocally(dogs)
                        }
                    }
                    Status.ERROR -> {
                        binding.dogsList.visibility = View.VISIBLE
                        binding.loadingView.visibility = View.GONE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.loadingView.visibility = View.VISIBLE
                        binding.dogsList.visibility = View.GONE
                    }
                }
            }
        }

//        viewModel.dogs.observe(viewLifecycleOwner) {
//            it?.let {
//                binding.dogsList.visibility = View.VISIBLE
//                dogListAdapter.updateDogList(it)
//            }
//        }
//        viewModel.dogsLoadError.observe(viewLifecycleOwner) {
//            it?.let {
//                binding.listError.visibility = if (it) View.VISIBLE else View.GONE
//            }
//        }
//        viewModel.loading.observe(viewLifecycleOwner) {
//            it?.let {
//                binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
//                if (it) {
//                    binding.listError.visibility = View.GONE
//                    binding.dogsList.visibility = View.GONE
//                }
//            }
//        }
    }


    private fun retrieveList(dogs: List<DogBreed>) {
        dogListAdapter.apply {
            addDogs(dogs)
            notifyDataSetChanged()
        }
    }

}
