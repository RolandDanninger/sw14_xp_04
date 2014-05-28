package edu.tugraz.sw14.xp04.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import edu.tugraz.sw14.xp04.ActivityMsg;
import edu.tugraz.sw14.xp04.R;
import edu.tugraz.sw14.xp04.helpers.ChatOverview;
import edu.tugraz.sw14.xp04.helpers.DateTime;
import edu.tugraz.sw14.xp04.helpers.MApp;

import java.util.List;

public class ChatOverviewAdapter extends ArrayAdapter<ChatOverview> {
	private final int resource;
	private final Activity activity;

	public ChatOverviewAdapter(Context context, int resource,
			List<ChatOverview> items) {
		super(context, resource, items);
		this.resource = resource;
		this.activity = (Activity) context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FrameLayout itemView;
		// Inflate the view
		if (convertView == null) {
			itemView = new FrameLayout(getContext());
			final String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, itemView, true);
		} else {
			itemView = (FrameLayout) convertView;
		}

		// Get the current object
		final ChatOverview item = this.getItem(position);

		ImageView img = (ImageView) itemView
				.findViewById(R.id.item_chat_overview_image);
		TextView name = (TextView) itemView
				.findViewById(R.id.item_chat_overview_title);
		TextView desc = (TextView) itemView
				.findViewById(R.id.item_chat_overview_desc);
		TextView time = (TextView) itemView
				.findViewById(R.id.item_chat_overview_time);
		TextView date = (TextView) itemView
				.findViewById(R.id.item_chat_overview_date);
		View notification = itemView
				.findViewById(R.id.item_chat_overview_notification);
		String str_name = item.getTitle().toUpperCase();
		String str_desc = item.getDesc();
		String str_time = DateTime.timeFromStamp(item.getTimestamp());
		String str_date;
		if (DateTime.isToday(item.getTimestamp()))
			str_date = activity.getResources().getString(R.string.today)
					.toUpperCase();
		else
			str_date = DateTime.dateFromStamp(item.getTimestamp());

		if (img != null && item.getImgUrl() != null) {
			img.setVisibility(View.INVISIBLE);
			item.downloadOverviewImg(getContext(), img);
		} else {
			img.setVisibility(View.VISIBLE);
		}
		if (name != null)
			name.setText(str_name);
		if (desc != null)
			desc.setText(str_desc);
		if (time != null)
			time.setText(str_time);
		if (date != null)
			date.setText(str_date);

		if (item.getNewMsg() > 0) {
			if (notification != null)
				notification.setVisibility(View.VISIBLE);
		} else {
			if (notification != null)
				notification.setVisibility(View.INVISIBLE);
		}

		itemView.setOnClickListener(new View.OnClickListener() {
			private String id = item.getId();

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, ActivityMsg.class);
            	intent.putExtra(ActivityMsg.EXTRA_EMAIL, id);
                MApp.goToActivity(activity, intent, false);
			}
		});
		itemView.setBackgroundResource(R.drawable.btn_nav_item);

		return itemView;
	}

}