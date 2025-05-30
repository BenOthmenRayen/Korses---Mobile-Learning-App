 package com.example.app;

 import android.content.Intent;
 import android.graphics.Color;
 import android.graphics.Typeface;
 import android.os.Bundle;
 import android.widget.Button;
 import android.widget.LinearLayout;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;

 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;

 public class MyCoursesActivity extends AppCompatActivity {

    private LinearLayout coursesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycoursesactivity);

        coursesContainer = findViewById(R.id.coursesContainer);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("courses");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    if (course != null) {
                        addCourseToLayout(course);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyCoursesActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCourseToLayout(Course course) {
        LinearLayout courseLayout = new LinearLayout(this);
        courseLayout.setOrientation(LinearLayout.VERTICAL);
        courseLayout.setPadding(0, 0, 0, 60);

        TextView titleView = new TextView(this);
        titleView.setText(course.title);
        titleView.setTextSize(18);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setPadding(0, 10, 0, 10);

        TextView descView = new TextView(this);
        descView.setText(course.description);
        descView.setTextSize(14);
        descView.setPadding(0, 0, 0, 10);

        Button startBtn = new Button(this);
        startBtn.setText("Commencer");
        startBtn.setBackgroundColor(Color.parseColor("#2196F3"));
        startBtn.setTextColor(Color.WHITE);

        startBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MyCoursesActivity.this, CourseDetailActivity.class);
            intent.putExtra("title", course.title);
            intent.putExtra("description", course.description);
            intent.putExtra("url", course.url);
            startActivity(intent);
        });

        courseLayout.addView(titleView);
        courseLayout.addView(descView);
        courseLayout.addView(startBtn);

        coursesContainer.addView(courseLayout);
    }
}
