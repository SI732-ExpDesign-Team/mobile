package Persistence

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_FULL_NAME = "full_name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_IS_REMODELER = "is_remodeler"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USER_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_FULL_NAME TEXT,"
                + "$COLUMN_EMAIL TEXT,"
                + "$COLUMN_PASSWORD TEXT,"
                + "$COLUMN_IS_REMODELER INTEGER)")
        db.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(fullName: String, email: String, password: String, isRemodeler: Boolean): Boolean {
        val db = this.writableDatabase

        // Check if email already exists
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_EMAIL=?", arrayOf(email))
        val emailExists = cursor.count > 0
        cursor.close()

        if (emailExists) {
            // Email is already in use
            return false
        }

        val contentValues = ContentValues().apply {
            put(COLUMN_FULL_NAME, fullName)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_IS_REMODELER, if (isRemodeler) 1 else 0)
        }

        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result != -1L
    }

    fun checkUser(email: String, password: String): Pair<Boolean, Boolean?> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_IS_REMODELER FROM $TABLE_NAME WHERE $COLUMN_EMAIL=? AND $COLUMN_PASSWORD=?",
            arrayOf(email, password)
        )

        return if (cursor.moveToFirst()) {
            val isRemodeler = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_REMODELER)) == 1
            cursor.close()
            Pair(true, isRemodeler)
        } else {
            cursor.close()
            Pair(false, null)
        }
    }
}
