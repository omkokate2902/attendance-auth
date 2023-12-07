package com.omkokate.attendanceauthentication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class WhitelistAdapter extends RecyclerView.Adapter<WhitelistAdapter.ViewHolder> {
    private Context context;
    private List<WhitelistModel> whitelistModelList;

    public WhitelistAdapter(Context context, List<WhitelistModel> whitelistModelList) {
        this.context = context;
        this.whitelistModelList = whitelistModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_teacher_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WhitelistModel whitelistModel = whitelistModelList.get(position);
        holder.nameTextView.setText(whitelistModel.getName());
        holder.deptTextView.setText(whitelistModel.getDept());
        holder.emailTextView.setText(whitelistModel.getEmail());
        holder.verifyButton.setOnClickListener(v -> {
            onVerify(position);
        });
    }

    @Override
    public int getItemCount() {
        return whitelistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView deptTextView;
        public TextView emailTextView;
        public Button verifyButton;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.display_name_textview);
            deptTextView = view.findViewById(R.id.department_textview);
            emailTextView = view.findViewById(R.id.email_textview);
            verifyButton = view.findViewById(R.id.verify_button);
        }
    }

    public void onVerify(int position) {
        WhitelistModel whitelistModel = whitelistModelList.get(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user-teachers")
                .document(whitelistModel.getId())
                .update("verification", "verified")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "UserTeacher verified", Toast.LENGTH_SHORT).show();
                    whitelistModelList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, whitelistModelList.size());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to verify UserTeacher", Toast.LENGTH_SHORT).show();
                    Log.e("UserTeacherAdapter", "Error verifying UserTeacher: ", e);
                });
    }

}
