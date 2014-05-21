package edu.tugraz.sw14.xp04.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import edu.tugraz.sw14.xp04.R;

public class ChatOverview {
	private String title;
	private String desc;
	private String id;
	private long timestamp;
	private String imgUrl;
	private Bitmap img;
	private int new_msg;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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

	public int getNewMsg() {
		return new_msg;
	}

	public void setNewMsg(int new_msg) {
		this.new_msg = new_msg;
	}

	public ChatOverview(String title, String desc, String email,
			long timestamp, String imgUrl, int new_msg) {
		this.title = title;
		this.desc = desc;
		this.id = email;
		this.timestamp = timestamp;
		this.imgUrl = imgUrl;
		this.new_msg = new_msg;
	}

	// Methods
	public void downloadOverviewImg(Context context, ImageView target) {
		if (this.img != null) {
			target.setImageBitmap(this.img);
			target.setVisibility(View.VISIBLE);
		} else if (this.imgUrl != null) {
			new DownloadOverviewImgTask(context, target, this.getImgUrl())
					.execute((Void[]) null);
		}
	}

	// Tasks
	private class DownloadOverviewImgTask extends AsyncTask<Void, Void, Bitmap> {
		private Context context;
		private ImageView target;
		private String url = null;

		public DownloadOverviewImgTask(Context context, ImageView target,
				String url) {
			this.context = context;
			this.target = target;
			this.url = url;
		}

		protected Bitmap doInBackground(Void... args) {
			if (!MApp.isNetworkAvailable(context))
				return null;
			return MDownloads.downloadBitmapFromUrl(this.context, this.url);
		}

		protected void onPostExecute(Bitmap result) {
			if (result == null)
				target.setImageResource(R.drawable.ic_launcher);
			else {
				img = result;
				target.setImageBitmap(result);
			}

			target.setVisibility(View.VISIBLE);
		}
	}
}
