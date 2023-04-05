package com.example.ecom.view.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecom.R;
import com.example.ecom.databinding.FragmentProductDetailsBinding;
import com.example.ecom.databinding.FragmentProfileBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    ActivityResultLauncher<String> requestPermissionLauncher;
    ActivityResultLauncher<Intent> capturePictureLauncher;
    private ActivityResultLauncher<String> galleryLauncher;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> productIDs = sharedPreferences.getStringSet("Items", new HashSet<>());

        binding.chnageImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (String productId : productIDs) {
                    // Do something with the product ID
                    Toast.makeText(requireContext(), "" + productId, Toast.LENGTH_SHORT).show();
                }

            }
        });

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                capturePicture();
            } else {
                Snackbar.make(binding.rootProfileLayout, "Require camera permission", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        capturePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                assert result.getData() != null;
                Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                binding.profileCircleImage.setImageBitmap(imageBitmap);
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                    binding.profileCircleImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.profileCircleImage.setOnClickListener(v -> {
            Dialog dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.pick_image_bottomsheet_layout);

            TextView pickFromCamera = dialog.findViewById(R.id.pickFromCamera);
            TextView pickFromGallery = dialog.findViewById(R.id.pickFromGallery);

            pickFromCamera.setOnClickListener(v12 -> {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                } else {
                    // Permission is already granted, capture a picture
                    capturePicture();
                }
                dialog.dismiss();
            });

            pickFromGallery.setOnClickListener(v1 -> {
                galleryLauncher.launch("image/*");
                dialog.dismiss();
            });

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        });
    }

    private void capturePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        capturePictureLauncher.launch(intent);
    }

}