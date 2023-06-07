package com.example.diaryreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    private ArrayList<Book> books;
    private GridView gridView;
    private BookAdapter bookAdapter;

    private ArrayList<Book> filteredBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        books = new ArrayList<>();
        books.add(new Book(R.drawable.book1, "Война и мир", "Лев Толстой"));
        books.add(new Book(R.drawable.book2, "1984", "Джордж Оруэлл"));
        books.add(new Book(R.drawable.book3, "Мастер и Маргарита", "Михаил Булгаков"));
        books.add(new Book(R.drawable.book4, "Гарри Поттер и философский камень", "Джоан Роулинг"));
        books.add(new Book(R.drawable.book5, "Унесенные ветром", "Маргарет Митчелл"));
        books.add(new Book(R.drawable.book6, "Властелин колец. Хранители кольца", "Толкин Джон Рональд Руэл"));
        books.add(new Book(R.drawable.book7, "Гордость и предубеждение", "Джейн Остин"));
        books.add(new Book(R.drawable.book8, "Гроздья гнева", "Джон Стейнбек"));

        gridView = (GridView) findViewById(R.id.gridview);
        BookAdapter adapter = new BookAdapter(this, books);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(BookListActivity.this, BookReviewActivity.class);
                intent.putExtra("book", filteredBooks.get(position)); // Здесь используем filteredBooks
                startActivity(intent);
            }
        });
        // Находим EditText и Button для поиска
        EditText searchEditText = findViewById(R.id.search_edittext);
        Button searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().toLowerCase();
                filteredBooks = new ArrayList<>();

                // Ищем книги, удовлетворяющие поисковому запросу
                for (Book book : books) {
                    if (book.getTitle().toLowerCase().contains(query)
                            || book.getAuthor().toLowerCase().contains(query)) {
                        filteredBooks.add(book);
                    }
                }

                // Создаем новый адаптер, если bookAdapter равен null
                if (bookAdapter == null) {
                    bookAdapter = new BookAdapter(BookListActivity.this, filteredBooks);
                    gridView.setAdapter(bookAdapter);
                } else {
                    // Обновляем адаптер с отфильтрованными книгами
                    bookAdapter.updateBooks(filteredBooks);
                }
            }
        });
    }
}