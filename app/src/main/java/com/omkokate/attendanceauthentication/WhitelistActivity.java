package com.omkokate.attendanceauthentication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class WhitelistActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WhitelistAdapter adapter;
    private List<WhitelistModel> whitelistModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_teacher);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        whitelistModelList = new ArrayList<>();
        adapter = new WhitelistAdapter(this, whitelistModelList);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user-teachers")
                .whereEqualTo("verification", "not-verified")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        WhitelistModel whitelistModel = documentSnapshot.toObject(WhitelistModel.class);
                        whitelistModel.setId(documentSnapshot.getId());
                        whitelistModelList.add(whitelistModel);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to retrieve data from Firestore", Toast.LENGTH_SHORT).show();
                    Log.e("UserTeacherActivity", "Error getting documents: ", e);
                });
    }

}
