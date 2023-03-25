package com.example.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.R
import com.example.dogs.model.DogBreed
import com.example.dogs.utils.getProgressDrawable
import com.example.dogs.utils.loadImage

class DogListAdapter(private val dogList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogListAdapter.DogViewHolder>() {
    class DogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.name)
        val tvLifeSpan: TextView = view.findViewById(R.id.lifespan)
        val ivDogImage: ImageView = view.findViewById(R.id.iv_dog_item)
    }

//    fun updateDogList(newDogList: List<DogBreed>) {
//        dogList.clear()
//        dogList.addAll(newDogList)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.tvName.text = dogList[position].dogBreed
        holder.tvLifeSpan.text = dogList[position].lifeSpan
        holder.itemView.setOnClickListener {
            it.findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToDetainFragment())
        }
        holder.ivDogImage.loadImage(
            dogList[position].imgUrl,
            getProgressDrawable(holder.itemView.context)
        )
    }

    override fun getItemCount() = dogList.size

    fun addDogs(dogs: List<DogBreed>) {
        this.dogList.apply {
            clear()
            addAll(dogs)
        }
    }

}