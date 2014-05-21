package edu.tugraz.sw14.xp04.msg;

import android.content.ContentValues;
import edu.tugraz.sw14.xp04.database.Database;

public class Msg {
	
	private String id;
	private String content;
	private long timestamp;
	private boolean flag_own;
	private boolean flag_read;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isFlag_own() {
		return flag_own;
	}
	public void setFlag_own(boolean flag_own) {
		this.flag_own = flag_own;
	}
	public boolean isFlag_read() {
		return flag_read;
	}
	public void setFlag_read(boolean flag_read) {
		this.flag_read = flag_read;
	}
	public Msg(String id, String content, long timestamp, boolean flag_own,
			boolean flag_read) {
		this.id = id;
		this.content = content;
		this.timestamp = timestamp;
		this.flag_own = flag_own;
		this.flag_read = flag_read;
	}
	
	
	public ContentValues toContentValues(){
        ContentValues v = new ContentValues();
        v.put(Database.MSG_SENDER_ID, this.id);
        v.put(Database.MSG_CONTENT, this.content);
        v.put(Database.MSG_TIMESTAMP, this.timestamp);
        v.put(Database.MSG_FLAG_OWN, this.flag_own);
        v.put(Database.MSG_FLAG_READ, this.flag_read);
        
        return v;
    }

}
