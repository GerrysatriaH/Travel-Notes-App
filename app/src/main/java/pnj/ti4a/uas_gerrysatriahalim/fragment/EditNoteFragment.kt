package pnj.ti4a.uas_gerrysatriahalim.fragment

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import pnj.ti4a.uas_gerrysatriahalim.R
import pnj.ti4a.uas_gerrysatriahalim.data.TravelNote
import pnj.ti4a.uas_gerrysatriahalim.helper.TravelDatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

class EditNoteFragment : Fragment() {

    private lateinit var etLocation: EditText
    private lateinit var etDate: EditText
    private lateinit var etTitle: EditText
    private lateinit var etDesc: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: Button
    private lateinit var btnEditDate: Button
    private lateinit var db: TravelDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etLocation = view.findViewById(R.id.editLocation)
        etDate = view.findViewById(R.id.editDate)
        etTitle = view.findViewById(R.id.editTitle)
        etDesc = view.findViewById(R.id.editDescription)
        btnSave = view.findViewById(R.id.btnSave)
        btnBack = view.findViewById(R.id.btnBack)
        btnEditDate = view.findViewById(R.id.btnEditDate)

        db = TravelDatabaseHelper(requireContext())

        val selectedNoteId = arguments?.getInt("selectedNoteId") ?: 0
        val selectedNote = db.getTravelNoteById(selectedNoteId)

        selectedNote?.let {
            etTitle.setText(it.title)
            etDesc.setText(it.description)
            etDate.setText(it.date)
            etLocation.setText(it.location)
        }

        btnEditDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val dateFormat = SimpleDateFormat("dd MMM yyyy")

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    // Membuat instance Calendar baru untuk menyimpan tanggal yang dipilih pengguna
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, monthOfYear)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }

                    // Format tanggal yang dipilih pengguna menjadi string
                    val formattedDate = dateFormat.format(selectedCalendar.time)

                    // Set Editable untuk EditText etDate dengan tanggal yang telah diformat
                    etDate.text = Editable.Factory.getInstance().newEditable(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }


        btnSave.setOnClickListener {
            val updatedTitle = etTitle.text.toString()
            val updatedDescription = etDesc.text.toString()
            val updatedDate = etDate.text.toString()
            val updatedLocation = etLocation.text.toString()

            selectedNote?.let {
                // Membuat object travelNote
                val updatedNote = TravelNote(it.id, updatedTitle, updatedDescription, updatedDate, updatedLocation)

                // Update note menggunakan db helper
                val rowsAffected = db.updateTravelNote(updatedNote)

                if (rowsAffected > 0) {
                    Toast.makeText(requireContext(), "Berhasil diupdate", Toast.LENGTH_SHORT).show()
                    // Jika update berhasil, pindah ke detail fragment
                    navigateToDetail(updatedNote)
                } else {
                    Toast.makeText(requireContext(), "Gagal mengupdate catatan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnBack.setOnClickListener{
            navigateBackToDetail()
        }
    }

    private fun navigateToDetail(updatedNote: TravelNote) {
        val detailNoteFragment = DetailNoteFragment()
        val bundle = Bundle()
        bundle.putParcelable("selectedNote", updatedNote)
        detailNoteFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.container, detailNoteFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateBackToDetail() {
        val detailNoteFragment = DetailNoteFragment()
        val bundle = Bundle()
        val selectedNote = arguments?.getParcelable<TravelNote>("selectedNote")
        bundle.putParcelable("selectedNote", selectedNote)
        detailNoteFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.container, detailNoteFragment)
            .commit()
    }
}
