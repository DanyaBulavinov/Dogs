package com.example.dogs.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.dogs.databinding.FragmentDetailBinding
import com.example.dogs.utils.getProgressDrawable
import com.example.dogs.utils.loadImage
import com.example.dogs.viewmodel.DetailViewModel


class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentDetailBinding == null")

    private var dogUuid: Int = 0

    private val viewModel by lazy {
        ViewModelProvider(this)[DetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dogUuid = args.dogUuid
        viewModel.getDogInformation(dogUuid)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(viewLifecycleOwner) {
            it?.let {
//                with(binding) {
//                    dogName.text = it.dogBreed
//                    dogLifespan.text = it.lifeSpan
//                    dogPurpose.text = it.bredFor
//                    dogTemperament.text = it.temperament
//                    dogImage.loadImage(
//                        it.imgUrl,
//                        getProgressDrawable(requireContext())
//                    )
//                }
                binding.dog = it
            }
        }
    }


}