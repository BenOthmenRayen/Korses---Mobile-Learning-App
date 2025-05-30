package com.example.app;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CourseDetailActivity extends AppCompatActivity {

    TextView titleView, descView;
    WebView videoWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        titleView = findViewById(R.id.titleView);
        descView = findViewById(R.id.descView);
        videoWebView = findViewById(R.id.videoWebView);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String url = getIntent().getStringExtra("url");

        titleView.setText(title);
        descView.setText(description);

        String videoId = extractYoutubeId(url);
        String html = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe>";
        videoWebView.getSettings().setJavaScriptEnabled(true);
        videoWebView.loadData(html, "text/html", "utf-8");
    }

    private String extractYoutubeId(String url) {
        String pattern = "(?<=v=|be/|embed/)[^&#?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        return matcher.find() ? matcher.group() : "";
    }
}

