package com.example.mp3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Social> socials;
    public FeedAdapter(Context context, ArrayList<Social> socials) {
        this.context = context;
        this.socials = socials;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.card, viewGroup, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((Item) viewHolder).eventName.setText(socials.get(i).getEventName());
        ((Item) viewHolder).poster.setText(socials.get(i).getPoster());

        String interested_string = "Currently " + socials.get(i).getInterested() + "are interested.";
        ((Item) viewHolder).interested.setText(interested_string);


        StorageReference sRef = FirebaseStorage.getInstance().getReference().child(socials.get(i).getId() + ".png");
        Glide.with(context).using(new FirebaseImageLoader()).load(sRef).into(((Item) viewHolder).img);
    }

    @Override
    public int getItemCount() {
        return socials.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView eventName;
        public TextView poster;
        public TextView interested;

        public Item(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            eventName = itemView.findViewById(R.id.eventName);
            poster = itemView.findViewById(R.id.poster);
            interested = itemView.findViewById(R.id.interested);
        }
    }
}
