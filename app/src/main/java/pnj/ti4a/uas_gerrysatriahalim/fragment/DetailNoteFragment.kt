package pnj.ti4a.uas_gerrysatriahalim.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import pnj.ti4a.uas_gerrysatriahalim.R
import pnj.ti4a.uas_gerrysatriahalim.data.TravelNote
import pnj.ti4a.uas_gerrysatriahalim.helper.TravelDatabaseHelper

class DetailNoteFragment : Fragment() {
    private lateinit var title: TextView
    private lateinit var location: TextView
    private lateinit var date: TextView
    private lateinit var desc: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var db: TravelDatabaseHelper
    private var selectedNoteId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = view.findViewById(R.id.TxtTitle)
        location = view.findViewById(R.id.TxtLocation)
        date = view.findViewById(R.id.TxtDate)
        desc = view.findViewById(R.id.TxtDesc)
        btnEdit = view.findViewById(R.id.BtnEdit)
        btnDelete = view.findViewById(R.id.BtnDelete)

        db = TravelDatabaseHelper(requireContext())

        val bundle = arguments

        // Ambil data catatan dari bundle
        val data = bundle?.getParcelable<TravelNote>("selectedNote")

        data?.let {
            selectedNoteId = it.id
            title.text = it.title
            location.text = it.location
            date.text = it.date
            desc.text = it.description
        }

        btnEdit.setOnClickListener {
            navigateToEdit(selectedNoteId)
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun navigateToEdit(selectedNoteId: Int) {
        val editNoteFragment = EditNoteFragment()

        // Kirim ID catatan yang dipilih sebagai argument ke EditNoteFragment
        val bundle = Bundle()
        bundle.putInt("selectedNoteId", selectedNoteId)
        editNoteFragment.arguments = bundle

        // Ganti fragment dengan EditNoteFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, editNoteFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showDeleteConfirmationDialog() {
        val selectedNote = arguments?.getParcelable<TravelNote>("selectedNote")

        // Buat AlertDialog Builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi")
        builder.setMessage("Apakah Anda yakin ingin menghapus catatan perjalanan ini?")

        // Tambahkan tombol "Ya" untuk menghapus catatan
        builder.setPositiveButton("Ya") { dialog, _ ->
            // Hapus catatan perjalanan dari database menggunakan ID
            selectedNote?.let {
                val rowsAffected = db.deleteTravelNote(it.id)
                if (rowsAffected > 0) {
                    Toast.makeText(requireContext(), "Catatan perjalanan dihapus", Toast.LENGTH_SHORT).show()
                    // Jika penghapusan berhasil, kembali ke halaman sebelumnya
                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(requireContext(), "Gagal menghapus catatan perjalanan", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }

        // Tambahkan tombol "Tidak" untuk batal menghapus
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        // Buat dan tampilkan AlertDialog
        val alertDialog = builder.create()
        alertDialog.show()
    }

}