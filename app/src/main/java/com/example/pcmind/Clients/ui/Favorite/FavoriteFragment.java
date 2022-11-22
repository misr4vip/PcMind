package com.example.pcmind.Clients.ui.Favorite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pcmind.Adapters.UserFavCustomAdapter;
import com.example.pcmind.Adapters.UserProductCustomAdapter;
import com.example.pcmind.Model.Product;
import com.example.pcmind.Model.User;
import com.example.pcmind.R;
import com.example.pcmind.databinding.FragmentUserFavoriteBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    ArrayList<Product> products = new ArrayList<Product>();
    ListView productListView;
    DatabaseReference ref;
    UserFavCustomAdapter ca;
    private FragmentUserFavoriteBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        productListView = binding.lvProduct;
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        ref = FirebaseDatabase.getInstance().getReference().child("Favorites").child(User.getId());

        ref.get()
                .addOnSuccessListener( SnapsShot ->{
                    for (DataSnapshot data : SnapsShot.getChildren()) {
                        Product  p = data.getValue(Product.class);
                        this.products.add(p);
                        Log.e("TAG", "onStart: " + p.getName() );
                    }
                     ca = new UserFavCustomAdapter(getContext(), R.layout.userfavlist,products);
                    productListView.setAdapter(ca);
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "on Failer: " + e.getMessage());
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ca.notifyDataSetChanged();
                productListView.deferNotifyDataSetChanged();

            }
        });
        Log.e("TAG", "Products Size: " + this.products.size());

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}