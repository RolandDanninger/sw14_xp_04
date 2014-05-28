package edu.tugraz.sw14.xp04.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.tugraz.sw14.xp04.ActivityMsg;
import edu.tugraz.sw14.xp04.R;
import edu.tugraz.sw14.xp04.helpers.DateTime;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.msg.Msg;

import java.util.List;

public class MsgAdapter extends ArrayAdapter<Msg> {
	private final int resource;
	private final Activity activity;

	public MsgAdapter(Context context, int resource, List<Msg> items) {
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
		final Msg item = this.getItem(position);

		LinearLayout background = (LinearLayout) itemView
				.findViewById(R.id.item_msg_lin_item);
		TextView msg = (TextView) itemView.findViewById(R.id.item_msg_tv_msg);
		LinearLayout info = (LinearLayout) itemView
				.findViewById(R.id.item_msg_lin_info);
		TextView sender = (TextView) itemView
				.findViewById(R.id.item_msg_tv_sender);
		TextView date = (TextView) itemView.findViewById(R.id.item_msg_tv_date);
		String str_msg = item.getContent();
		String str_date = "";
		if (DateTime.isToday(item.getTimestamp()))
			str_date += activity.getResources().getString(R.string.today)
					.toUpperCase();
		else
			str_date += DateTime.dateFromStamp(item.getTimestamp());

		str_date += ", " + DateTime.timeFromStamp(item.getTimestamp());

		String str_sender = item.isFlag_own() ? activity.getResources()
				.getString(R.string.a_msg_me) : item.getId();

		
		
		if (background != null) {
			int bg;
			if (item.isFlag_own())
				bg = R.drawable.bg_msg_own;
			else
				bg = R.drawable.bg_msg_sender;
			
			int dpValue = 48;
			float d = activity.getResources().getDisplayMetrics().density;
			int margin = (int) (dpValue * d);

			LayoutParams params = (LayoutParams) background.getLayoutParams();
			if (item.isFlag_own())
				params.setMargins(margin, 0, 0, 0);
			else
				params.setMargins(0, 0, margin, 0);
			background.setLayoutParams(params);
			background.setBackgroundResource(bg);
		}
		if (msg != null)
			msg.setText(str_msg);
		if(info != null){
			int color;
			if (item.isFlag_own())
				color = R.color.bg_msg_own;
			else
				color = R.color.bg_msg_sender;
			info.setBackgroundResource(color);
		}
		if (sender != null)
			sender.setText(str_sender);

		if (date != null)
			date.setText(str_date);

		return itemView;
	}

}