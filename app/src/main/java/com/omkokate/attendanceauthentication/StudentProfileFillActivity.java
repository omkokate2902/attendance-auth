package com.omkokate.attendanceauthentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StudentProfileFillActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nameEditText, deptEditText, divisionEditText, rollEditText;
    private ImageView profileImageView;
    private Uri profileImageUri;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button teacher_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_fill);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameEditText = findViewById(R.id.name);
        deptEditText = findViewById(R.id.dept);
        divisionEditText = findViewById(R.id.div);
        rollEditText = findViewById(R.id.roll);
        profileImageView = findViewById(R.id.prof_img);

        // set default values from Google sign-in
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            nameEditText.setText(user.getDisplayName());
            profileImageUri = user.getPhotoUrl();
            Glide.with(this).load(profileImageUri).into(profileImageView);
        }

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        Button submitButton = findViewById(R.id.submit_btn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String dept = deptEditText.getText().toString();
                String division = divisionEditText.getText().toString();
                String roll = rollEditText.getText().toString();

                if (TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(dept) ||
                        TextUtils.isEmpty(division) ||
                        TextUtils.isEmpty(roll)) {
                    Toast.makeText(StudentProfileFillActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    Map<String, Object> userStudent = new HashMap<>();
                    userStudent.put("name", name);
                    userStudent.put("department", dept);
                    userStudent.put("division", division);
                    userStudent.put("roll", roll);
                    userStudent.put("email", email);
                    db.collection("user-students").document(email).set(userStudent)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(StudentProfileFillActivity.this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(StudentProfileFillActivity.this, StudentDashBoardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(StudentProfileFillActivity.this, "Failed to create profile. Please try again", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        teacher_btn=findViewById(R.id.teacher_btn);
        teacher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StudentProfileFillActivity.this,TeacherProfileFillActivity.class);
                startActivity(i);
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            Glide.with(this).load(profileImageUri).into(profileImageView);
        }
    }
}
