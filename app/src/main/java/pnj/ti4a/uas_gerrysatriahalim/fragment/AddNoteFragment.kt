package pnj.ti4a.uas_gerrysatriahalim.fragment

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import pnj.ti4a.uas_gerrysatriahalim.R
import pnj.ti4a.uas_gerrysatriahalim.data.TravelNote
import pnj.ti4a.uas_gerrysatriahalim.helper.TravelDatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

class AddNoteFragment : Fragment() {

    private lateinit var btnCreateNote: Button
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etDate: EditText
    private lateinit var btnDate: Button
    private lateinit var db: TravelDatabaseHelper
    private lateinit var currentLoc: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTitle = view.findViewById(R.id.etTitle)
        etDescription = view.findViewById(R.id.etDescription)
        etDate = view.findViewById(R.id.etDate)
        btnCreateNote = view.findViewById(R.id.btnCreateNote)
        btnDate = view.findViewById(R.id.btnDate)

        currentLoc = view.findViewById(R.id.currentLocation)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        db = TravelDatabaseHelper(requireContext())

        // Mendapatkan Lokasi Saat Ini
        locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                if (locationAvailability.isLocationAvailable) {
                    Debug.getLocation()
                } else {
                    val locationText = "N/A"
                    currentLoc.text = locationText
                }
            }

            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation: Location? = locationResult.lastLocation
                lastLocation?.let { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val geocoder = Geocoder(requireContext())
                        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
                        if (addresses != null && addresses.isNotEmpty()) {
                            val address: Address = addresses[0]
                            val locationText = address.getAddressLine(0)
                            currentLoc.text = locationText
                        }
                    } else {
                        Toast.makeText(requireContext(),"Gagal mendapatkan Lokasi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val dateFormat = SimpleDateFormat("dd MMM yyyy")

            val datePickerDialog = DatePickerDialog(requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    val dateFormat = dateFormat.format(Date())
                    etDate.text = Editable.Factory.getInstance().newEditable(dateFormat)
                }, year, month, day
            )
            datePickerDialog.show()
        }

        // Tambahkan event listener untuk tombol "Buat Catatan"
        btnCreateNote.setOnClickListener {
            createTravelNote()
        }
    }

    private fun getCurrentLocation() {
        val locationRequest = LocationRequest.create().apply {
            interval = 6000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            getCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                val locationText = "N/A"
                currentLoc.text = locationText
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createTravelNote() {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val date = etDate.text.toString()
        val location = currentLoc.text.toString()

        // Buat objek TravelNote dari data yang diinputkan pengguna
        val travelNote = TravelNote(0, title, description, date, location)

        // Simpan catatan perjalanan ke database
        db.addTravelNote(travelNote)

        // Kosongkan data input setelah menyimpan catatan
        etTitle.text.clear()
        etDescription.text.clear()
        etDate.text.clear()

        // pindah ke listview
        Toast.makeText(requireContext(), "Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
    }
}