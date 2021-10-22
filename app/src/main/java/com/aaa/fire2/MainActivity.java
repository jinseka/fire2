package com.aaa.fire2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseReference database;
    EditText et_user_name, et_user_email, et_user_id2;
    Button btn_save, btn_read;
    TextView read_data;
    int i = 1; //userId값 count하기 위한 변수
    ListView listView;
    TextView text1, text2;

    ArrayList<User> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_user_name = findViewById(R.id.et_user_name);
        et_user_email = findViewById(R.id.et_user_email);
        et_user_id2 = findViewById(R.id.et_user_id2);
        btn_save = findViewById(R.id.btn_save);
        btn_read = findViewById(R.id.btn_read);
        read_data = findViewById(R.id.read_data);
        read_data = findViewById(R.id.read_data);
        listView = findViewById(R.id.listView);

        database = FirebaseDatabase.getInstance().getReference("User");
        Log.d("파이어베이스>> ", database + " ");

        //db에서 가지고 오는 유저들의 목록을 넣을 공간
        arrayList = new ArrayList<>();


        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("파이어베이스 >> " , "User 아래의 자식들의 개수 : " + snapshot.getChildrenCount());
                Log.d("파이어베이스 >> ","전체 json 목록 가지고 온 것 "+snapshot.getChildren());
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    Log.d("파이어베이스 >> ","하나의 snapshot : " + snapshot1);
                    Log.d("파이어베이스 >> ","하나의 snapshot  value: " + snapshot1.getValue());
                    User user = snapshot1.getValue(User.class);
                    Log.d("파이어베이스 >>", "user 1명 : " + user);
                    arrayList.add(user);

                }
                    Log.d("파이어베이스 >> ", "user 목록 전체 : " +arrayList);
                    Log.d("파이어베이스 >> ", "user 목록 전체 : " +arrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_user_name.getText().toString();
                String email = et_user_email.getText().toString();
                String i = et_user_id2.getText().toString();
                //i++;

                User user = new User(userName,email);

                database.child(String.valueOf(i)).setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "저장을 완료했습니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "저장을 실패했습니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId = et_user_id2.getText().toString();

                database.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Log.d("파이어베이스>> ", userId + ": userId 상세정보: " + user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("파이어베이스>> ", userId + ": userId 없음");
                    }
                });
            }
        });
    } //onCreate
}//class