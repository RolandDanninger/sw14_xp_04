package edu.tugraz.sw14.xp04.database;

import java.util.ArrayList;

import edu.tugraz.sw14.xp04.contacts.Contact;
import edu.tugraz.sw14.xp04.msg.Msg;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	private static final String TAG = "database";

	private static final String DATABASE_NAME = "sam.db";
	private static final int DATABASE_VERSION = 1;

	public static final String MSG_TABLE = "msg";
	public static final String MSG_ID = BaseColumns._ID;
	public static final String MSG_SENDER_ID = "msg_sender_id";
	public static final String MSG_TIMESTAMP = "msg_timestamp";
	public static final String MSG_FLAG_OWN = "msg_flag_own";
	public static final String MSG_FLAG_READ = "msg_flag_read";
	public static final String MSG_CONTENT = "msg_content";

	public static final String CONTACT_TABLE = "contact";
	public static final String CONTACT_ID = "contact_id";
	public static final String CONTACT_USR_ID = "contact_usr_id";
	public static final String CONTACT_NAME = "contact_name";
	public static final String CONTACT_IMG_URL = "contact_img_url";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String msg = String
				.format("CREATE TABLE %s ("
						+ "%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT);",
						MSG_TABLE, MSG_ID, MSG_SENDER_ID, MSG_TIMESTAMP,
						MSG_FLAG_OWN, MSG_FLAG_READ, MSG_CONTENT);
		try {
			db.execSQL(msg);
		} catch (Exception ignore) {
		}

		String contact = String
				.format("CREATE TABLE %s ("
						+ "%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT);",
						CONTACT_TABLE, CONTACT_ID, CONTACT_USR_ID,
						CONTACT_NAME, CONTACT_IMG_URL);
		try {
			db.execSQL(contact);
		} catch (Exception ignore) {
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", MSG_TABLE));
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", CONTACT_TABLE));
	}

	public boolean insertMsg(ContentValues values) {
		long result = -1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			if (db != null) {
				result = db.insertWithOnConflict(MSG_TABLE, null, values,
						SQLiteDatabase.CONFLICT_REPLACE);
			}
		} catch (SQLException ignore) {
			return false;
		}
		return result >= 0;
	}

	public boolean insertContact(ContentValues values) {
		long result = -1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			if (db != null) {
				result = db.insertWithOnConflict(CONTACT_TABLE, null, values,
						SQLiteDatabase.CONFLICT_REPLACE);
			}
		} catch (SQLException ignore) {
			return false;
		}
		return result >= 0;
	}

	public int getContactId(String email) {
		String sql = "SELECT " + CONTACT_ID + " FROM " + CONTACT_TABLE + " WHERE "
				+ CONTACT_USR_ID + "='" + email + "'";
		int id = -1;
		try {

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() == 1) {
				cursor.moveToNext();
				id = cursor.getInt(cursor.getColumnIndex(CONTACT_ID));
			}
		} catch (SQLException ignore) {
			Log.d(TAG, "error retrieving contactId");
		}

		return id;
	}

	public String getContactName(String email) {
		String sql = "SELECT " + CONTACT_NAME + " FROM " + CONTACT_TABLE + " WHERE "
				+ CONTACT_USR_ID + "='" + email + "'";
		String name = null;
		try {

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() == 1) {
				cursor.moveToNext();
				name = cursor.getString(cursor.getColumnIndex(CONTACT_NAME));
			}
		} catch (SQLException ignore) {
			Log.d(TAG, "error retrieving contactId");
		}

		return name;
	}
	
	public ArrayList<Contact> getAllContacts() {
		ArrayList<Contact> list = new ArrayList<Contact>();
		try {
			String sql = "SELECT * FROM " + CONTACT_TABLE + " ORDER BY "
					+ CONTACT_USR_ID + " ASC;";
			// + " DESC " + "LIMIT 0 OFFSET 0;";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					String email = cursor.getString(1);
					String name = cursor.getString(2);
					String img_url = cursor.getString(3);
					Contact c = new Contact(name, email, img_url);
					list.add(c);
				} while (cursor.moveToNext());
			}
			return list;
		} catch (SQLException ignore) {
			return list;
		}
	}

	public ArrayList<Msg> getMsgsBySender(String sender, int limit){
		ArrayList<Msg> list = new ArrayList<Msg>();
		try {
			String sql = "SELECT * FROM " + MSG_TABLE + " "
					+ "WHERE " + MSG_SENDER_ID + "='" + sender + "' "
					+ "ORDER BY " + MSG_TIMESTAMP + " ASC;";
			// + " LIMIT " + limit + ";";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					String content = cursor.getString(cursor.getColumnIndex(MSG_CONTENT));
					long timestamp = cursor.getLong(cursor.getColumnIndex(MSG_TIMESTAMP));
					boolean flag_own = cursor.getInt(cursor.getColumnIndex(MSG_FLAG_OWN)) > 0;
					boolean flag_read = cursor.getInt(cursor.getColumnIndex(MSG_FLAG_READ)) > 0;
					Msg m = new Msg(sender, content, timestamp, flag_own, flag_read);
					list.add(m);
				} while (cursor.moveToNext());
			}
			return list;
		} catch (SQLException ignore) {
			return list;
		}
	}
	
	public boolean contactAlreadyExists(String id) {
		try {
			String sql = "SELECT * FROM " + CONTACT_TABLE + " " + "WHERE \""
					+ CONTACT_USR_ID + "\" = \"" + id + "\";";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0)
				return true;
			else
				return false;
		} catch (SQLException ignore) {
			return false;
		}
	}
}
