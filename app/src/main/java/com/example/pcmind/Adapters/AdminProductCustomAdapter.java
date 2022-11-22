package com.example.pcmind.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pcmind.Model.Product;
import com.example.pcmind.Model.User;
import com.example.pcmind.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminProductCustomAdapter extends ArrayAdapter<Product> {

        private int resourceLayout;
        private Context mContext;
        FirebaseStorage storage;
        StorageReference ImagesRef;
        FirebaseDatabase database;
        DatabaseReference FavoriteRef;
    public AdminProductCustomAdapter(Context context, int resource, ArrayList<Product> items) {
            super(context, resource, items);
            this.resourceLayout = resource;
            this.mContext = context;
            storage = FirebaseStorage.getInstance();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(mContext);
                v = vi.inflate(resourceLayout, null);
            }

            Product p = getItem(position);

            if (p != null) {
                TextView tvname =  v.findViewById(R.id.textView1);
                TextView tvprice =  v.findViewById(R.id.textView2);
                ImageView ivPic = v.findViewById(R.id.imageView);
                ImageView ivDelete =  v.findViewById(R.id.ivDelete);
                if (tvname != null) {
                    tvname.setText(p.getName());
                }
                if (tvprice != null) {
                    tvprice.setText("Price : " +p.getPrice() + " $");
                }
                if (ivPic != null) {
                    ImagesRef = storage.getReference().child("Images").child(p.getImg());
                    Glide.with(ivPic.getContext()).load(ImagesRef).into(ivPic);
                }
                database = FirebaseDatabase.getInstance();
                ivDelete.setOnClickListener(view -> {
                    FavoriteRef =  database.getReference().child("Products").child(p.getId());
                    FavoriteRef.removeValue().addOnCompleteListener(task ->{
                        if (task.isSuccessful())
                        {
                          this.remove(getItem(position));
                            Toast.makeText(mContext, "Product Removed From Products", Toast.LENGTH_SHORT).show();
                        }
                    });

                });

            }

            return v;
        }



    }
