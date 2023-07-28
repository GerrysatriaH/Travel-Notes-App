package pnj.ti4a.uas_gerrysatriahalim.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.FragmentTransaction
import pnj.ti4a.uas_gerrysatriahalim.R
import pnj.ti4a.uas_gerrysatriahalim.adapter.TravelNoteAdapter
import pnj.ti4a.uas_gerrysatriahalim.data.TravelNote
import pnj.ti4a.uas_gerrysatriahalim.helper.TravelDatabaseHelper

class ListNoteFragment : Fragment() {
    lateinit var db: TravelDatabaseHelper
    lateinit var lvTravelNotes: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lvTravelNotes = view.findViewById(R.id.lvTravelNotes)
        db = TravelDatabaseHelper(requireContext())

        lvTravelNotes.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            var selectedNote = parent.getItemAtPosition(position) as TravelNote
            navigateToDetail(selectedNote)
        })

        showTravelNotes()
    }

    private fun showTravelNotes() {
        // Ambil semua catatan perjalanan dari database
        val travelNotesList: ArrayList<TravelNote> = db.getAllTravelNotes()

        // Buat adapter untuk menampilkan daftar catatan perjalanan dalam ListView
        val adapter = TravelNoteAdapter(requireContext(), travelNotesList)

        // Tampilkan daftar catatan perjalanan dalam ListView
        lvTravelNotes.adapter = adapter
    }

    private fun navigateToDetail(selectedNote: TravelNote) {
        // Buat instance dari halaman DetailNoteFragment (ganti dengan nama Fragment yang sesuai)
        val detailNoteFragment = DetailNoteFragment()

        // Kirim data catatan yang dipilih sebagai argument ke halaman DetailNoteFragment
        val bundle = Bundle()
        bundle.putParcelable("selectedNote", selectedNote)
        detailNoteFragment.arguments = bundle

        // Ganti fragment dengan DetailNoteFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, detailNoteFragment)
            .addToBackStack(null) // Untuk bisa kembali ke ListNoteFragment jika diperlukan
            .commit()
    }
}