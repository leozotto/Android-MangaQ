package com.example.mangaq.activity.util;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ImageManager {

    public static void renderizarImagemPorUri(Uri uri, ImageView imageView) {
        Picasso.get().load(uri).into(imageView);
    }

    public static void carregarImagemFirestoreEmImageViewPorUrl(FirebaseStorage storage, String url, ImageView imageView, Activity activity) {
        StorageReference storageReference = storage.getReferenceFromUrl(url);
        storageReference.getDownloadUrl().addOnSuccessListener(
                uri -> renderizarImagemPorUri(uri, imageView)
        ).addOnFailureListener(
                e -> Toast.makeText(activity, "Erro ao carregar imagem!", Toast.LENGTH_SHORT).show()
        );
    }
}
