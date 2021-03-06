package com.erdiansyah.mystory.presenter

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.erdiansyah.mystory.R
import com.erdiansyah.mystory.databinding.FragmentMapsStoryBinding
import com.erdiansyah.mystory.data.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsStoryFragment : Fragment(), OnMapReadyCallback{

    private val storyViewModel: StoryViewModel by activityViewModels()
    private var _binding: FragmentMapsStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsStoryBinding.inflate(inflater, container, false)
        storyViewModel.getStoryLocation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        storyViewModel.getStoryLocationResponse.observe(viewLifecycleOwner){ result ->
            when(result) {
                is Result.Success -> {
                    result.data?.listStory?.forEach{
                        val lat: Double = it.lat
                        val lon: Double = it.lon
                        googleMap.addMarker(
                            MarkerOptions().position(
                                LatLng(lat, lon)
                            )
                                .title(it.name)
                                .snippet(it.description)
                        )
                        val zoom = 12.0f
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon),zoom))
                    }
                    googleMap.uiSettings.isZoomControlsEnabled = true
                    googleMap.uiSettings.isCompassEnabled = true
                    googleMap.uiSettings.isMapToolbarEnabled = true
                    setMapStyle(googleMap)
                }
            }
        }
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        try {
            val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),R.raw.map_style))
            if (!success){
                Log.e(TAG, "Style Error")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Style Not Found. Error: ", e)
        }
    }
    companion object {
        private val TAG = "MapStoryFragment"
    }
}