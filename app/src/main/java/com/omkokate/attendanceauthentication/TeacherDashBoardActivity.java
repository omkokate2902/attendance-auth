package com.omkokate.attendanceauthentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TeacherDashBoardActivity extends AppCompatActivity {

    private Button logoutButton;
    private CardView uploadattendance;
    private TextView message;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dash_board);

        uploadattendance=findViewById(R.id.upload_attendance_card_view);
        message=findViewById(R.id.message);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email=currentUser.getEmail();

        logoutButton=findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignIn.getClient(TeacherDashBoardActivity.this, gso).signOut();
                Intent intent = new Intent(TeacherDashBoardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

//
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userTeacherRef = db.collection("user-teachers").document(email);

        userTeacherRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String verification = documentSnapshot.getString("verification");

                if (verification.equals("not-verified")) {
                    // If verification is not done, hide the card view and show the text view
                    uploadattendance.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                } else if(verification.equals("verified")) {
                    // If verification is done, show the card view and hide the text view
                    uploadattendance.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                }
            }
        });

    }
}