package com.example.mp3;

import android.content.Intent;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Social> socials;
    public FeedAdapter(Context context, ArrayList<Social> socials) {
        // passes in a context and list of social objects
        this.context = context;
        this.socials = socials;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // our custom cell layout (card.xml)
        View row = inflater.inflate(R.layout.card, viewGroup, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Social s = socials.get(i);

        // sets values in the cell to the data in our socials list for each social
        ((Item) viewHolder).eventName.setText(s.getEventName());
        ((Item) viewHolder).poster.setText(s.getPoster());

        String interested_string = s.getInterested() + " people are interested!";
        ((Item) viewHolder).interested.setText(interested_string);

        String strDate = "on " + s.getDate();
        ((Item) viewHolder).date.setText(strDate);

        //StorageReference sRef = FirebaseStorage.getInstance().getReference().child(socials.get(i).getId() + ".png");
        Glide.with(context).using(new FirebaseImageLoader()).load(FirebaseUtils.getImageStorageRef(socials.get(i).getId())).into(((Item) viewHolder).img);
    }

    @Override
    public int getItemCount() {
        return socials.size();
    }


    // class that holds all cell values in recycler view
    public class Item extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView eventName;
        public TextView poster;
        public TextView interested;
        public TextView date;

        public Item(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            eventName = itemView.findViewById(R.id.eventName);
            poster = itemView.findViewById(R.id.poster);
            interested = itemView.findViewById(R.id.interested);
            date = itemView.findViewById(R.id.date);

            // this allows the cell to be clicked
            itemView.setOnClickListener(this);
        }

        // handles when cell is clicked on
        @Override
        public void onClick(View v) {
            Social social = socials.get(getAdapterPosition());
            Intent intent = new Intent(context, SocialDetail.class);
            intent.putExtra("id", social.getId());
            context.startActivity(intent);
        }
    }
}
