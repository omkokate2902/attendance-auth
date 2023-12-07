package com.omkokate.attendanceauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class StudentProfileActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();

    FirebaseUser user = auth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

            String email = user.getEmail();
            // get user data from Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("user-students").document(email);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        // get user data
                        String name = documentSnapshot.getString("name");
                        String dept = documentSnapshot.getString("department");
                        String division = documentSnapshot.getString("division");
                        String roll = documentSnapshot.getString("roll");
                        // update UI with user data
                        TextView nameTextView = findViewById(R.id.nameTextView);
                        nameTextView.setText(name);
                        TextView emailTextView = findViewById(R.id.emailTextView);
                        emailTextView.setText(email);
                        TextView deptTextView = findViewById(R.id.deptTextView);
                        deptTextView.setText(dept);
                        TextView divisionTextView = findViewById(R.id.divisionTextView);
                        divisionTextView.setText(division);
                        TextView rollTextView = findViewById(R.id.rollTextView);
                        rollTextView.setText(roll);
                        ImageView photoImageView = findViewById(R.id.photoImageView);
                        String photoUrl = user.getPhotoUrl().toString();
                        Picasso.get().load(photoUrl).into(photoImageView);
                    } else {
                        // user document does not exist
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // error retrieving user data
                }
            });

    }
}