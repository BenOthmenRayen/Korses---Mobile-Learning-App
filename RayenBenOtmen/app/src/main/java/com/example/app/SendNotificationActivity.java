package com.example.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendNotificationActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private DatabaseReference notificationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        notificationsRef = FirebaseDatabase.getInstance().getReference("notifications");

        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(message)) {
                String notificationId = notificationsRef.push().getKey();
                if (notificationId != null) {
                    notificationsRef.child(notificationId).setValue(message)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Notification sent!", Toast.LENGTH_SHORT).show();
                                messageEditText.setText("");
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Failed to send notification", Toast.LENGTH_SHORT).show()
                            );
                }
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
