package ie.wit.citizenscience.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.wit.citizenscience.main.MainApp
import ie.wit.citizenscience.models.SightingModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.citizenscience.databinding.ActivitySightingListBinding
import ie.wit.citizenscience.databinding.CardSightingBinding

class SightingListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivitySightingListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySightingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager =  LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = SightingAdapter(app.sightings)
    }
}

class SightingAdapter constructor(private var sightings: List<SightingModel>) :
    RecyclerView.Adapter<SightingAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardSightingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val sighting = sightings[holder.adapterPosition]
        holder.bind(sighting)
    }

    override fun getItemCount(): Int = sightings.size

    class MainHolder(private val binding : CardSightingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sighting: SightingModel) {
            binding.sightingClassification.text = sighting.classification
            binding.sightingSpecies.text = sighting.species
        }
    }
}