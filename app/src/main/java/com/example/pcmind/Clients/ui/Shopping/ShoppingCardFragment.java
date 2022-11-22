package com.example.pcmind.Clients.ui.Shopping;

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
import com.example.pcmind.Adapters.UserOrderCustomAdapter;
import com.example.pcmind.Model.Product;
import com.example.pcmind.Model.User;
import com.example.pcmind.R;
import com.example.pcmind.databinding.FragmentUserShoppingcardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShoppingCardFragment extends Fragment {
    ArrayList<Product> products = new ArrayList<Product>();
    ListView productListView;
    DatabaseReference ref;
    UserOrderCustomAdapter ca;
    double total = 0;

    private FragmentUserShoppingcardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserShoppingcardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        productListView = binding.lvProduct;
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        ref = FirebaseDatabase.getInstance().getReference().child("Orders").child(User.getId());

        ref.get()
                .addOnSuccessListener( SnapsShot ->{
                    for (DataSnapshot data : SnapsShot.getChildren()) {
                        Product  p = data.getValue(Product.class);
                        this.total +=Double.valueOf(p.getPrice()) ;
                        this.products.add(p);
                        Log.e("TAG", "onStart: " + p.getName() );
                    }
                    ca = new UserOrderCustomAdapter(getContext(), R.layout.userorderlist,products);
                    productListView.setAdapter(ca);
                    binding.tvTotal.setText("Total Price :" +String.valueOf(total));
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "on Failer: " + e.getMessage());
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        productListView.setOnItemClickListener((adapterView, view, i, l) -> {
            ca.notifyDataSetChanged();
            productListView.deferNotifyDataSetChanged();

        });

        Log.e("TAG", "Products Size: " + this.products.size());

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}