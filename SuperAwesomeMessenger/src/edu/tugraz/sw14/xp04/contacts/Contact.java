package edu.tugraz.sw14.xp04.contacts;

import edu.tugraz.sw14.xp04.R;
import edu.tugraz.sw14.xp04.database.Database;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MDownloads;
import edu.tugraz.sw14.xp04.stubs.ContactStub;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

public class Contact {
	
	private String name;
	private String email;
	private String imgUrl;
    private Bitmap img;
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public Contact(String name, String email, String imgUrl) {
		this.name = name;
		this.email = email;
		this.imgUrl = imgUrl;
	}

	public Contact(ContactStub contact_stub) {
		this.name = contact_stub.getName();
		this.email = contact_stub.getEmail();
		this.imgUrl = contact_stub.getImgUrl();
	}

	// Methods
    public void downloadOverviewImg(Context context, ImageView target){
        if(this.img != null){
            target.setImageBitmap(this.img);
            target.setVisibility(View.VISIBLE);
        }
        else if(this.imgUrl != null){
        	new DownloadOverviewImgTask(context, target, this.getImgUrl()).execute((Void[]) null);
        }
    }

    // Tasks
    private class DownloadOverviewImgTask extends AsyncTask<Void, Void, Bitmap> {
        private Context context;
        private ImageView target;
        private String url = null;

        public DownloadOverviewImgTask(Context context, ImageView target, String url) {
            this.context = context;
            this.target = target;
            this.url = url;
        }

        protected Bitmap doInBackground(Void... args) {
           if(!MApp.isNetworkAvailable(context)) return null;
           return MDownloads.downloadBitmapFromUrl(this.context, this.url);
        }

        protected void onPostExecute(Bitmap result) {
            if(result == null) target.setImageResource(R.drawable.ic_launcher);
            else {
                img = result;
                target.setImageBitmap(result);
            }

            target.setVisibility(View.VISIBLE);
        }
    }

    public ContentValues toContentValues(){
    	if(this.name == null || this.email == null) return null;
    	
        ContentValues v = new ContentValues();
        v.put(Database.CONTACT_USR_ID, this.email);
        v.put(Database.CONTACT_NAME, this.name);
        v.put(Database.CONTACT_IMG_URL, this.imgUrl);
        
        return v;
    }

	
    @Override
	public boolean equals(Object o) {
    	
    	if (!(o instanceof Contact))
            return false;
        if (o == this)
            return true;
    	
		boolean result = true;
		Contact c = (Contact) o;
		
		result &= this.name.equals(c.name);
		result &= this.email.equals(c.email);
		
		return result;
	}

    
}