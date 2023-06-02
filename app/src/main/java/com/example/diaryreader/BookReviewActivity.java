package com.example.diaryreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookReviewActivity extends AppCompatActivity {

    private Book book;
    private String reviewText;
    private String reviewKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_review);

        // получаем объект Book из интента
        book = getIntent().getParcelableExtra("book");

        // находим элементы UI
        ImageView bookImage = findViewById(R.id.book_image);
        TextView bookTitle = findViewById(R.id.book_title);
        TextView bookAuthor = findViewById(R.id.book_author);
        EditText reviewEditText = findViewById(R.id.review);
        Button saveReviewButton = findViewById(R.id.save_review_button);
        TextView savedReviewText = findViewById(R.id.saved_review);

        // отображаем информацию о книге
        bookImage.setImageResource(book.getImageId());
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());

        // создаем уникальный ключ для отзыва к этой книге
        reviewKey = "review_" + book.getTitle();

        // получаем сохраненный отзыв из SharedPreferences
        SharedPreferences preferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String savedReview = preferences.getString(reviewKey, "");

        // отображаем сохраненный отзыв в TextView
        savedReviewText.setText(savedReview);

        // обработка нажатия на кнопку "Сохранить отзыв"
        saveReviewButton.setOnClickListener(v -> {
            // получаем текст отзыва из EditText
            reviewText = reviewEditText.getText().toString();

            // сохраняем отзыв в SharedPreferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(reviewKey, reviewText);
            editor.apply();

            // отображаем сохраненный отзыв в TextView
            savedReviewText.setText(reviewText);
        });

    }

}