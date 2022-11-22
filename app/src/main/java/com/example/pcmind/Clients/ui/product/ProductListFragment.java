package com.example.pcmind.Clients.ui.product;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.pcmind.Adapters.UserProductCustomAdapter;
import com.example.pcmind.Model.Product;
import com.example.pcmind.R;
import com.example.pcmind.databinding.FragmentUserProductBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ProductListFragment extends Fragment {
    ArrayList<Product> products = new ArrayList<Product>();
    ListView productListView;
    DatabaseReference db;

    private FragmentUserProductBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentUserProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        productListView = binding.lvProduct;
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        db = FirebaseDatabase.getInstance().getReference().child("Products");
        db.get()
                .addOnSuccessListener( SnapsShot ->{

                    for (DataSnapshot data : SnapsShot.getChildren()) {

                        Product  p = data.getValue(Product.class);
                        this.products.add(p);
                        Log.e("TAG", "onStart: " + p.getName() );
                    }
                    UserProductCustomAdapter ca = new UserProductCustomAdapter(getContext(),R.layout.userporductlist,products);
                    productListView.setAdapter(ca);
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "on Failer: " + e.getMessage());
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        Log.e("TAG", "Products Size: " + this.products.size());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}