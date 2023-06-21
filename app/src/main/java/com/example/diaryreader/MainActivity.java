package com.example.diaryreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import Models.User;

public class MainActivity extends AppCompatActivity {

    Button btnSign, btnReg;
    FirebaseAuth auth; //для авторизации
    FirebaseDatabase db; //для подключения к базе данных
    DatabaseReference users; //для работы с таблицами внутри базы данных

    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSign = findViewById(R.id.btnSign);
        btnReg = findViewById(R.id.btnReg);

        root = findViewById(R.id.root_element);

        auth = FirebaseAuth.getInstance(); //запуск авторизации
        db = FirebaseDatabase.getInstance(); //подключение к базе данных
        users = db.getReference("Users"); //с какой таблицей работать

        // Получаем SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);

        // Получаем email и пароль из SharedPreferences
        String email = preferences.getString("EMAIL", "");
        String pass = preferences.getString("PASS", "");

        // Проверяем, есть ли сохраненные данные авторизации
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            // Если есть, производим автоматическую авторизацию
            auth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // Если авторизация прошла успешно, переходим на экран списка книг
                            startActivity(new Intent(MainActivity.this, BookListActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Если произошла ошибка при авторизации, выводим сообщение
                            Snackbar.make(root, "Ошибка авторизации!" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
        }

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegWindow();
            }
        });
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignWindow();
            }
        });
    }

    private void showSignWindow(){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Войти");

            LayoutInflater inflater = LayoutInflater.from(this); //создаём шаблон
            View signWindow = inflater.inflate(R.layout.sign_window, null); //помещаем шаблон
            dialog.setView(signWindow); //шаблон для всплывающего окна

            MaterialEditText email = signWindow.findViewById(R.id.emailField);
            MaterialEditText pass = signWindow.findViewById(R.id.passField);

            dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });
            dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    if (TextUtils.isEmpty(email.getText().toString())) {
                        Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    if (pass.getText().toString().length() < 4) {
                        Snackbar.make(root, "Введите пароль, состоящий более чем из 4 символов", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    startActivity(new Intent(MainActivity.this, BookListActivity.class));
                                    finish();

                                    SharedPreferences preferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("EMAIL", email.getText().toString());
                                    editor.putString("PASS", pass.getText().toString());
                                    editor.apply();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(root, "Ошибка авторизации!" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            dialog.show();
    }
    private void showRegWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");

        LayoutInflater inflater = LayoutInflater.from(this); //создаём шаблон
        View regWindow = inflater.inflate(R.layout.reg_window, null); //помещаем шаблон
        dialog.setView(regWindow); //шаблон для всплывающего окна

        MaterialEditText email = regWindow.findViewById(R.id.emailField);
        MaterialEditText pass = regWindow.findViewById(R.id.passField);
        MaterialEditText name = regWindow.findViewById(R.id.nameField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pass.getText().toString().length() < 4) {
                    Snackbar.make(root, "Введите пароль, состоящий более чем из 4 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // Рег пользлвателя
                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPass(pass.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() { //ключ для идентефикации пользователя
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(root, "Пользователь добавлен", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(root, "Ошибка регистрации!" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        dialog.show();
    }
}