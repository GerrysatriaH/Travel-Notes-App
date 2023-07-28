package pnj.ti4a.uas_gerrysatriahalim.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pnj.ti4a.uas_gerrysatriahalim.data.TravelNote

class TravelDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TravelDiary.db"
        private const val TABLE_NAME = "travel_notes"

        // Kolom-kolom tabel
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_LOCATION = "location"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_TITLE TEXT,"
                + "$COLUMN_DESCRIPTION TEXT,"
                + "$COLUMN_DATE TEXT,"
                + "$COLUMN_LOCATION TEXT)")

        db.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Menyimpan catatan perjalanan ke database
    fun addTravelNote(travelNote: TravelNote) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COLUMN_TITLE, travelNote.title)
        values.put(COLUMN_DESCRIPTION, travelNote.description)
        values.put(COLUMN_DATE, travelNote.date)
        values.put(COLUMN_LOCATION, travelNote.location)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllTravelNotes(): ArrayList<TravelNote> {
        val travelNotesList = ArrayList<TravelNote>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                val location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION))

                val travelNote = TravelNote(id, title, description, date, location)
                travelNotesList.add(travelNote)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return travelNotesList
    }

    fun getTravelNoteById(id: Int): TravelNote? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, null,
            "$COLUMN_ID=?", arrayOf(id.toString()),
            null, null, null
        )

        var travelNote: TravelNote? = null
        if (cursor != null && cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
            val location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION))
            travelNote = TravelNote(id, title, description, date, location)
            cursor.close()
        }

        db.close()
        return travelNote
    }

    fun updateTravelNote(travelNote: TravelNote): Int {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COLUMN_TITLE, travelNote.title)
        values.put(COLUMN_DESCRIPTION, travelNote.description)
        values.put(COLUMN_DATE, travelNote.date)
        values.put(COLUMN_LOCATION, travelNote.location)

        // Kolom ID untuk kondisi WHERE
        val id = travelNote.id
        // Perbarui catatan perjalanan berdasarkan ID
        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()

        return rowsAffected
    }

    fun deleteTravelNote(id: Int): Int {
        val db = this.writableDatabase

        // Hapus catatan perjalanan berdasarkan ID
        val rowsAffected = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()

        return rowsAffected
    }
}