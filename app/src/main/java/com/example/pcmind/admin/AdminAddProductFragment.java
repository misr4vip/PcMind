package com.example.pcmind.admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.pcmind.Model.Product;
import com.example.pcmind.R;
import com.example.pcmind.databinding.FragmentAdminAddProductBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AdminAddProductFragment extends Fragment {
    public static final int Galary_perm_Code = 101;
    public static final int Galary_Request_Code = 102;
    ImageView ivCamera;
    Button buttonSaveAddProduct;
    EditText productName,productPrice;
    String imageFileName ,proName ,proPrice ;
    StorageReference storageRef ;
    DatabaseReference dbRef;
    Uri contentUri;

    private FragmentAdminAddProductBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentAdminAddProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("Products");
        storageRef = storage.getReference().child("Images/");
        productName = binding.etProductName;
        productPrice = binding.etProductPrice;
        ivCamera = binding.ivCamera;
        buttonSaveAddProduct = binding.buttonSaveAddProduct;
        ivCamera.setOnClickListener(view -> askGalaryPermission());
        buttonSaveAddProduct.setOnClickListener(view -> {
            proName = productName.getText().toString();
            proPrice = productPrice.getText().toString();
            if (proName != null && proPrice != null && contentUri != null && imageFileName != null)
            {

                String id = UUID.randomUUID().toString();
                Product product = new Product(id,proName,proPrice,imageFileName);
                storageRef.child(imageFileName).putFile(contentUri)
                        .addOnSuccessListener(taskSnapshot -> {

                            dbRef.child(product.getId()).setValue(product)
                                    .addOnSuccessListener(task ->{

                                        Toast.makeText(getContext(),"Product Added Successfully",Toast.LENGTH_LONG).show();
                                        productName.setText("");
                                        productPrice.setText("");
                                        ivCamera.setImageResource(R.drawable.ic_camera);
                                    })
                                    .addOnFailureListener(e -> {
                                        storageRef.child(imageFileName).delete().addOnSuccessListener(removeimgTask ->{
                                            Toast.makeText(getContext(), "Image Removed From Database "+ e.getMessage(), Toast.LENGTH_LONG).show();

                                        });
                                        Toast.makeText(getContext(), "Unable To Save Product To Database Check your Internet "+ e.getMessage(), Toast.LENGTH_LONG).show();
                                    });

                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Unable To Upload File To Database Check your Internet "+ e.getMessage(), Toast.LENGTH_LONG).show();
                        });

            }
        });

        return root;
    }

    private void askGalaryPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, Galary_perm_Code);
        }else
        {
            Intent galary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galary, Galary_Request_Code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Galary_perm_Code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galary, Galary_Request_Code);
            } else {
                Toast.makeText(getContext(), "Media Permission Required ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Galary_Request_Code) {
            if (resultCode == getActivity().RESULT_OK) {
                contentUri = data.getData();
                Log.d("TAG", "onActivityResult: Content Uri " + contentUri.toString());
                ivCamera.setImageURI(contentUri);
                // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imageFileName = getFileName(contentUri);

            }


        }
    }
    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        Log.d("TAG", "getFileName: " + result);
        return result;
    }
    private String getFileExt(Uri content)
    {
        ContentResolver c = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(content));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}