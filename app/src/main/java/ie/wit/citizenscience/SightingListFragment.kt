package ie.wit.citizenscience

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.citizenscience.activities.SightingActivity
import ie.wit.citizenscience.activities.SightingMapsActivity
import ie.wit.citizenscience.adapters.SightingAdapter
import ie.wit.citizenscience.adapters.SightingListener
import ie.wit.citizenscience.databinding.FragmentSightingListBinding
import ie.wit.citizenscience.main.MainApp
import ie.wit.citizenscience.models.SightingModel

class SightingListFragment : Fragment(), SightingListener/*, MultiplePermissionsListener*/ {


    private var _fragBinding: FragmentSightingListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = activity?.application as MainApp

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentSightingListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_reported_sightings)

        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        //fragBinding.recyclerView.adapter = SightingAdapter(app.sightings.findAll())
        loadSightings()
        registerRefreshCallback()
        registerMapCallback()
        return root;

        //return inflater.inflate(R.layout.fragment_sighting_list, container, false)
    }

    override fun onResume() {
        super.onResume()

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SightingListFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(requireContext(), SightingFragment::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(requireContext(), SightingMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSightingClick(sighting: SightingModel) {

        val launcherIntent = Intent(requireContext(), SightingActivity::class.java)
        launcherIntent.putExtra("sighting_edit", sighting)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadSightings() }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
    private fun loadSightings() {
        showSightings(app.sightings.findAll())
    }

    fun showSightings (sightings: List<SightingModel>) {
        fragBinding.recyclerView.adapter = SightingAdapter(sightings, this)
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    }



}