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
import edu.tugraz.sw14.xp04.contacts.Contact;
import edu.tugraz.sw14.xp04.helpers.MApp;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact>{
    private final int resource;
    private final Activity activity;
 
    public ContactAdapter(Context context, int resource, List<Contact> items) {
        super(context, resource, items);
        this.resource = resource;
        this.activity = (Activity)context;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        FrameLayout itemView;
        //Inflate the view
        if(convertView==null)
        {
            itemView = new FrameLayout(getContext());
            final String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        }
        else { itemView = (FrameLayout) convertView; }

        //Get the current object
        final Contact item = this.getItem(position);

        ImageView img =(ImageView)itemView.findViewById(R.id.item_contact_image);
        TextView name =(TextView)itemView.findViewById(R.id.item_contact_title);
        TextView desc =(TextView)itemView.findViewById(R.id.item_contact_desc);
        String str_name = item.getName().toUpperCase();
        String str_desc = item.getEmail();

        if(img != null && item.getImgUrl() != null){
            img.setVisibility(View.INVISIBLE);
            item.downloadOverviewImg(getContext(), img);
        } else {
        	img.setVisibility(View.VISIBLE);
        }
        if(name != null) name.setText(str_name);
        if(desc != null) desc.setText(str_desc);

        itemView.setOnClickListener(new View.OnClickListener() {
            private String email = item.getEmail();
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(activity, ActivityMsg.class);
            	intent.putExtra(ActivityMsg.EXTRA_EMAIL, email);
                MApp.goToActivity(activity, intent, false);
            }
        });
        itemView.setBackgroundResource(R.drawable.btn_nav_item);

        return itemView;
    }

}