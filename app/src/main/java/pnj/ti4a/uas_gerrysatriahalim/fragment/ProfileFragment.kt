package pnj.ti4a.uas_gerrysatriahalim.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pnj.ti4a.uas_gerrysatriahalim.R

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email   = getEmail()
        val nim     = getNIM()
        val nama    = getNama()
        val kelas   = getKelas()

        val emailTextView = view.findViewById<TextView>(R.id.TxtEmail)
        val nimTextView = view.findViewById<TextView>(R.id.TxtNIM)
        val namaTextView = view.findViewById<TextView>(R.id.TxtName)
        val kelasTextView = view.findViewById<TextView>(R.id.TxtKelas)

        emailTextView.text = email
        nimTextView.text = nim
        namaTextView.text = nama
        kelasTextView.text = kelas
    }

    private fun getEmail(): String? {
        val sharedPreferences = activity?.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("email", null)
    }

    private fun getNIM(): String? {
        val sharedPreferences = activity?.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("Nim", null)
    }

    private fun getNama(): String? {
        val sharedPreferences = activity?.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("Nama", null)
    }

    private fun getKelas(): String? {
        val sharedPreferences = activity?.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("Kelas", null)
    }
}