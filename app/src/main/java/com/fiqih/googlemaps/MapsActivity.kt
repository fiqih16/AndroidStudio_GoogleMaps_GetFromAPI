package com.fiqih.googlemaps

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation : LatLng
    private lateinit var currentAddress:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var locationList = mutableMapOf<String, Double>(
            "latloc1" to 0.0,
            "lngloc1" to 0.0,
            "latloc2" to 0.0,
            "lngloc2" to 0.0
        )
        var addresList = mutableMapOf<String, String>("Address1" to "", "Address2" to "")

        val jakarta = LatLng(-6.2088, 106.8456)
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 16f))

        mMap.setOnCameraIdleListener {
            currentLocation = mMap.cameraPosition.target
            val geocoder = Geocoder(this)
            mMap.clear()
            var geocoderResult = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
            currentAddress = geocoderResult[0].getAddressLine(0)
            mMap.addMarker(MarkerOptions().position(currentLocation).title("Posisi"))


            btnSet.setOnClickListener {
                if (locationList.get("latloc1") == 0.0) {
                    locationList.put("latloc1", currentLocation.latitude)
                    locationList.put("lngloc1", currentLocation.longitude)
                    addresList.put("Address1", currentAddress)
                } else {
                    locationList.put("latloc2", currentLocation.latitude)
                    locationList.put("lngloc2", currentLocation.longitude)
                    addresList.put("Address2", currentAddress)
                }
                lok1Lat.text = "${locationList.get("latloc1")} - ${locationList.get("lngloc1")}"
                lok2Lat.text = "${locationList.get("latloc2")} - ${locationList.get("lngloc2")}"
                lok1Long.text = addresList.get("Address1").toString()
                lok2Long.text = addresList.get("Address2").toString()

            }

            btnJrk.setOnClickListener {
                val loc1 = LatLng(locationList.get("latloc1")!!, locationList.get("lngloc1")!!)
                val location1 = Location("")
                location1.latitude = locationList.get("latloc1")!!
                location1.longitude = locationList.get("lngloc1")!!

                val loc2 = LatLng(locationList.get("latloc2")!!, locationList.get("lngloc2")!!)
                val location2 = Location("")
                location2.latitude = locationList.get("latloc2")!!
                location2.longitude = locationList.get("lngloc2")!!

                mMap.addMarker(MarkerOptions().position(loc1).title("Posisi"))
                mMap.addMarker(MarkerOptions().position(loc2).title("Posisi"))

                mMap.addPolyline(
                    PolylineOptions()
                        .clickable(true)
                        .add(loc1, loc2)
                        .color(R.color.purple_700)
                        .width(16f)
                )
                val distance1 = location1.distanceTo(location2)
                hitJar.text = "${distance1 / 1000} KM"
            }

            btnReset.setOnClickListener {
                locationList.putAll(
                    setOf(
                        "latloc1" to 0.0,
                        "lngloc1" to 0.0,
                        "latloc2" to 0.0,
                        "lngloc2" to 0.0
                    )
                )
                lok1Lat.text = ""
                lok1Long.text = ""
                lok2Lat.text = ""
                lok2Long.text = ""
                hitJar.text = ""
                addresList.clear()
                mMap.clear()
            }

        }


//        mMap.uiSettings.isZoomControlsEnabled = true
//        mMap.uiSettings.isMapToolbarEnabled = true

    }
}
