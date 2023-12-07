package com.omkokate.attendanceauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Toast.makeText(this, "User found"+currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            // Check if user exists in "user-students" collection
            db.collection("user-students")
                    .document(userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Intent intent = new Intent(this, StudentDashBoardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

            // Check if user exists in "user-teachers" collection
            db.collection("user-teachers")
                    .document(userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Intent intent = new Intent(this, TeacherDashBoardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

            // Check if user exists in "user-admins" collection
            db.collection("user-admins")
                    .document(userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Intent intent = new Intent(this, AdminDashBoardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });



            // Check if the user exists in the user-students collection
            db.collection("user-students").document(userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // User exists in user-students collection
                                // Do something here
                            } else {
                                // User does not exist in user-students collection
                                // Check if the user exists in the user-teachers collection
                                db.collection("user-teachers").document(userEmail)
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                DocumentSnapshot document1 = task1.getResult();
                                                if (document1.exists()) {
                                                    // User exists in user-teachers collection
                                                    // Do something here
                                                } else {
                                                    // User does not exist in user-teachers collection
                                                    // Check if the user exists in the user-admins collection
                                                    db.collection("user-admins").document(userEmail)
                                                            .get()
                                                            .addOnCompleteListener(task2 -> {
                                                                if (task2.isSuccessful()) {
                                                                    DocumentSnapshot document2 = task2.getResult();
                                                                    if (document2.exists()) {
                                                                        // User exists in user-admins collection
                                                                        // Do something here
                                                                    } else {
                                                                        // User does not exist in any collection
                                                                        // Redirect to login activity
                                                                        mAuth.signOut();
                                                                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                                                .requestIdToken(getString(R.string.default_web_client_id))
                                                                                .requestEmail()
                                                                                .build();
                                                                        GoogleSignIn.getClient(SplashScreenActivity.this, gso).signOut();
                                                                        Intent intent = new Intent(this, LoginActivity.class);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                } else {
                                                                    Log.d("TAG", "get failed with ", task2.getException());
                                                                }
                                                            });
                                                }
                                            } else {
                                                Log.d("TAG", "get failed with ", task1.getException());
                                            }
                                        });
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    });



        }

        if(currentUser==null){
            Toast.makeText(this, "no user", Toast.LENGTH_SHORT).show();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            GoogleSignIn.getClient(SplashScreenActivity.this, gso).signOut();
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }
}