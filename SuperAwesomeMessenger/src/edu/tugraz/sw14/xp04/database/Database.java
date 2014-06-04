package edu.tugraz.sw14.xp04.database;

import java.util.ArrayList;

import edu.tugraz.sw14.xp04.R;
import edu.tugraz.sw14.xp04.contacts.Contact;
import edu.tugraz.sw14.xp04.helpers.ChatOverview;
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

	private static String me;

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		me = context.getResources().getString(R.string.a_msg_me);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String msg = String
				.format("CREATE TABLE %s ("
						+ "%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT);",
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
		if (values == null)
			return false;
		if ((String) values.get(MSG_SENDER_ID) == null)
			return false;
		if ((String) values.get(MSG_CONTENT) == null)
			return false;
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
		if (values == null)
			return false;
		if (values.get(CONTACT_USR_ID) == null)
			return false;
		if (values.get(CONTACT_NAME) == null)
			return false;
		if (contactAlreadyExists((String) values.get(CONTACT_USR_ID)))
			return false;
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
		if (email == null)
			return -1;
		String sql = "SELECT " + CONTACT_ID + " FROM " + CONTACT_TABLE
				+ " WHERE " + CONTACT_USR_ID + "='" + email + "'";
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
		if (email == null)
			return null;
		String sql = "SELECT " + CONTACT_NAME + " FROM " + CONTACT_TABLE
				+ " WHERE " + CONTACT_USR_ID + "='" + email + "'";
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
					String email = cursor.getString(cursor
							.getColumnIndex(CONTACT_USR_ID));
					String name = cursor.getString(cursor
							.getColumnIndex(CONTACT_NAME));
					String img_url = cursor.getString(cursor
							.getColumnIndex(CONTACT_IMG_URL));
					Contact c = new Contact(name, email, img_url);
					list.add(c);
				} while (cursor.moveToNext());
			}
			return list;
		} catch (SQLException ignore) {
			return list;
		}
	}

	public ArrayList<Msg> getMsgsBySender(String sender, int limit) {
		ArrayList<Msg> list = new ArrayList<Msg>();
		if (sender == null)
			return list;
		try {
			String sql = "SELECT * FROM " + MSG_TABLE + " " + "WHERE "
					+ MSG_SENDER_ID + "='" + sender + "' " + "ORDER BY "
					+ MSG_TIMESTAMP + " ASC";
			if (limit > 0)
				sql += " LIMIT " + limit;
			sql += ";";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					String content = cursor.getString(cursor
							.getColumnIndex(MSG_CONTENT));
					long timestamp = cursor.getLong(cursor
							.getColumnIndex(MSG_TIMESTAMP));
					boolean flag_own = cursor.getInt(cursor
							.getColumnIndex(MSG_FLAG_OWN)) > 0;
					boolean flag_read = cursor.getInt(cursor
							.getColumnIndex(MSG_FLAG_READ)) > 0;
					Msg m = new Msg(sender, content, timestamp, flag_own,
							flag_read);
					list.add(m);
				} while (cursor.moveToNext());
			}
			return list;
		} catch (SQLException ignore) {
			return list;
		}
	}

	public ArrayList<ChatOverview> getAllMsgs(int limit) {
		ArrayList<ChatOverview> list = new ArrayList<ChatOverview>();
		try {
			String sql = "SELECT * FROM " + MSG_TABLE + " " + "GROUP BY "
					+ MSG_SENDER_ID + " " + "ORDER BY " + MSG_TIMESTAMP
					+ " ASC";
			if (limit > 0)
				sql += " LIMIT " + limit;
			sql += ";";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					String email = cursor.getString(cursor
							.getColumnIndex(MSG_SENDER_ID));
					String title = email;
					String content = cursor.getString(cursor
							.getColumnIndex(MSG_CONTENT));
					String desc = content.length() > 30 ? content.substring(0,
							30) : content;
					String imgUrl = null;

					long timestamp = cursor.getLong(cursor
							.getColumnIndex(MSG_TIMESTAMP));
					boolean flag_own = cursor.getInt(cursor
							.getColumnIndex(MSG_FLAG_OWN)) > 0;
					boolean flag_read = cursor.getInt(cursor
							.getColumnIndex(MSG_FLAG_READ)) > 0;
					int new_msg = 0;
					if (flag_own)
						desc = me + ": " + desc;
					else {
						
						new_msg = countUnreadMsgs(email);
						if (new_msg > 1)
							desc = "(" + new_msg + ") messages";
					}
					ChatOverview c = new ChatOverview(title, desc, email,
							timestamp, imgUrl, new_msg);
					list.add(c);
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

	public int countUnreadMsgs(String id) {
		int result = 0;
		try {
			String sql = "SELECT count(*) FROM " + MSG_TABLE + " " + "WHERE \""
					+ MSG_SENDER_ID + "\" = \"" + id + "\" AND "
					+ MSG_FLAG_READ + " = 0;";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0)
				cursor.moveToFirst();
			do {
				result = cursor.getInt(0);
			} while (cursor.moveToNext());
		} catch (SQLException ignore) {
			return 0;
		}
		return result;
	}

	public void setAsRead(String id) {
		String sql = "UPDATE " + MSG_TABLE + " SET " + MSG_FLAG_READ + "=1 "
				+ "WHERE \"" + MSG_SENDER_ID + "\" = \"" + id + "\";";
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL(sql);
		} catch (Exception ignore) {
		}
	}
}
