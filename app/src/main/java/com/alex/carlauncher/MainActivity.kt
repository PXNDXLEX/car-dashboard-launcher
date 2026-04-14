package com.alex.carlauncher

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.alex.carlauncher.databinding.ActivityMainBinding
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private var currentSpeed = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animación de entrada estilo DUDU
        binding.root.alpha = 0f
        binding.root.scaleY = 0.9f
        binding.root.animate().alpha(1f).scaleY(1f).setDuration(900).start()

        setupMap()
        setupClock()
        requestPermissions()
        startMusicListener()
    }

    private fun setupMap() {
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        binding.mapView.controller.setZoom(18.0)
        binding.mapView.controller.setCenter(GeoPoint(37.7749, -122.4194)) // Cambia después a tu zona

        // Marcador de auto (se moverá con GPS)
        val carMarker = Marker(binding.mapView)
        carMarker.title = "Tu auto"
        carMarker.position = GeoPoint(37.7749, -122.4194)
        binding.mapView.overlays.add(carMarker)
    }

    private fun setupClock() {
        val clockHandler = Handler(Looper.getMainLooper())
        clockHandler.post(object : Runnable {
            override fun run() {
                val date = Date()
                binding.tvClock.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
                binding.tvDate.text = SimpleDateFormat("EEEE dd MMMM yyyy", Locale("es")).format(date)
                clockHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
        )

        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 100)
        } else {
            startLocationUpdates()
        }
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Se necesitan permisos de ubicación para el velocímetro", Toast.LENGTH_LONG).show()
        }
    }

    private fun startLocationUpdates() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                currentSpeed = (location.speed * 3.6).toInt() // m/s → km/h
                binding.speedometer.updateSpeed(currentSpeed)

                // Mover mapa y marcador
                val geoPoint = GeoPoint(location.latitude, location.longitude)
                binding.mapView.controller.animateTo(geoPoint)
                // Aquí puedes actualizar el marcador del auto
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener)
        }
    }

    private fun startMusicListener() {
        // El NotificationListener ya está registrado en el Manifest
        // Aquí solo mostramos que está activo
        Toast.makeText(this, "Escuchando música de cualquier app...", Toast.LENGTH_SHORT).show()
        // La lógica completa del widget está en NotificationListener.kt
    }
}