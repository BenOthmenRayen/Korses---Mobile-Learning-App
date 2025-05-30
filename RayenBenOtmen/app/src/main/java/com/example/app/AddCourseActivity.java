package com.example.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCourseActivity extends AppCompatActivity {

    EditText etTitle, etDescription, etUrl;
    Button btnAddCourse;

    DatabaseReference coursesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etUrl = findViewById(R.id.etUrl);
        btnAddCourse = findViewById(R.id.btnAddCourse);

        coursesRef = FirebaseDatabase.getInstance().getReference("courses");

        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String url = etUrl.getText().toString().trim();

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(url)) {
                    Toast.makeText(AddCourseActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }


                String courseId = coursesRef.push().getKey();

                Course course = new Course(title, description, url);
                coursesRef.child(courseId).setValue(course);

                Toast.makeText(AddCourseActivity.this, "Cours ajouté avec succès !", Toast.LENGTH_SHORT).show();


                etTitle.setText("");
                etDescription.setText("");
                etUrl.setText("");
            }
        });
    }


    public static class Course {
        public String title, description, url;

        public Course() {
        }

        public Course(String title, String description, String url) {
            this.title = title;
            this.description = description;
            this.url = url;
        }
    }

}
