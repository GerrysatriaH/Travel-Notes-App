package pnj.ti4a.uas_gerrysatriahalim.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import pnj.ti4a.uas_gerrysatriahalim.R
import pnj.ti4a.uas_gerrysatriahalim.data.TravelNote

class TravelNoteAdapter(context: Context, notes: List<TravelNote>) :
    ArrayAdapter<TravelNote>(context, 0, notes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val viewHolder: ViewHolder

        // Cek apakah convertView sudah ada atau belum
        if (itemView == null) {
            // Jika belum, inflasikan layout untuk list item
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_travel_note, parent, false)

            // Buat ViewHolder untuk menyimpan referensi elemen UI di dalam list item
            viewHolder = ViewHolder()
            viewHolder.tvTitle = itemView.findViewById(R.id.tvTitle)
            viewHolder.tvDescription = itemView.findViewById(R.id.tvDescription)
            viewHolder.tvDate = itemView.findViewById(R.id.tvDate)

            // Tandai viewHolder sebagai tag pada view item
            itemView.tag = viewHolder
        } else {
            // Jika convertView sudah ada, ambil viewHolder dari tag yang disimpan pada view item
            viewHolder = itemView.tag as ViewHolder
        }

        // Ambil objek TravelNote dari data di posisi tertentu
        val travelNote = getItem(position)

        // Tampilkan data TravelNote ke elemen UI di dalam list item
        viewHolder.tvTitle?.text = travelNote?.title
        viewHolder.tvDescription?.text = travelNote?.description
        viewHolder.tvDate?.text = travelNote?.date

        return itemView!!
    }

    // ViewHolder untuk menyimpan referensi elemen UI di dalam list item
    private class ViewHolder {
        var tvTitle: TextView? = null
        var tvDescription: TextView? = null
        var tvDate: TextView? = null
    }
}