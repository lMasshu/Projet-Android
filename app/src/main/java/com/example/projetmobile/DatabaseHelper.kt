package com.example.projetmobile

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class ScoreEntry(
    val id: Int,
    val name: String,
    val score: Int,
    val date: String
)

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quiz_scores.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_SCORES = "scores"
        private const val COL_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_SCORE = "score"
        private const val COL_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_SCORES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT NOT NULL,
                $COL_SCORE INTEGER NOT NULL,
                $COL_DATE TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SCORES")
        onCreate(db)
    }

    fun insertScore(name: String, score: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, name)
            put(COL_SCORE, score)
            put(COL_DATE, java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                .format(java.util.Date()))
        }
        db.insert(TABLE_SCORES, null, values)
        db.close()
    }

    fun getTop10Scores(): List<ScoreEntry> {
        val scores = mutableListOf<ScoreEntry>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_SCORES,
            arrayOf(COL_ID, COL_NAME, COL_SCORE, COL_DATE),
            null, null, null, null,
            "$COL_SCORE DESC",
            "10"
        )
        with(cursor) {
            while (moveToNext()) {
                scores.add(
                    ScoreEntry(
                        id = getInt(getColumnIndexOrThrow(COL_ID)),
                        name = getString(getColumnIndexOrThrow(COL_NAME)),
                        score = getInt(getColumnIndexOrThrow(COL_SCORE)),
                        date = getString(getColumnIndexOrThrow(COL_DATE))
                    )
                )
            }
            close()
        }
        db.close()
        return scores
    }
}
