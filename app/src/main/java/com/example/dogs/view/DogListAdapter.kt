package com.example.dogs.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.databinding.ItemDogBinding
import com.example.dogs.model.DogBreed

class DogListAdapter(private val dogList: ArrayList<DogBreed>) : DogClickListener,
    RecyclerView.Adapter<DogListAdapter.DogViewHolder>() {
    class DogViewHolder(private val binding: ItemDogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //        val tvName: TextView = view.name
//        val tvLifeSpan: TextView = view.lifespan
//        val ivDogImage: ImageView = view.ivDogItem
//        var dog: DogBreed = view.dog
        fun bind(dog: DogBreed, position: Int, listener: DogClickListener) {
            binding.dog = dog
            binding.position = position
            binding.listener = listener
            binding.executePendingBindings()
        }
    }

//    fun updateDogList(newDogList: List<DogBreed>) {
//        dogList.clear()
//        dogList.addAll(newDogList)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater
//        inflater.inflate(R.layout.item_dog, parent, false)
        val binding = ItemDogBinding.inflate(inflater, parent, false)
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
//        holder.tvName.text = dogList[position].dogBreed
//        holder.tvLifeSpan.text = dogList[position].lifeSpan
//        holder.itemView.setOnClickListener {
//            val action = ListFragmentDirections.actionListFragmentToDetailFragment()
//            action.dogUuid = dogList[position].uuid
//            it.findNavController()
//                .navigate(action)
//        }
//        holder.ivDogImage.loadImage(
//            dogList[position].imgUrl,
//            getProgressDrawable(holder.itemView.context)
//        )
        holder.bind(dogList[position], position, this)
    }

    override fun getItemCount() = dogList.size

    fun addDogs(dogs: List<DogBreed>) {
        this.dogList.apply {
            clear()
            addAll(dogs)
        }
    }

    override fun onDogClicked(view: View) {
        Log.d("CLICKING_TEST", "It was clicked")
        val position = view.tag.toString().toInt()
        val action = ListFragmentDirections.actionListFragmentToDetailFragment()
        action.dogUuid = dogList[position].uuid
        Navigation.findNavController(view).navigate(action)
    }
}