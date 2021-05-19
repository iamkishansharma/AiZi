package com.heycode.aizi.dashboard.Attempt_Quiz_Section;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heycode.aizi.CommanClass;
import com.heycode.aizi.R;
import com.heycode.aizi.models.Question;
import com.heycode.aizi.models.Test;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Objects;

public class TestsActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private ListView listView;
    private TestAdapter testAdapter;
    private int lastPos = -1;

    ArrayList<Test> tests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        avLoadingIndicatorView = findViewById(R.id.loader1);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        listView = findViewById(R.id.test_listview);
        testAdapter = new TestAdapter(TestsActivity.this, tests);
        listView.setAdapter(testAdapter);
        getQues();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stopService(new Intent(TestsActivity.this, NotificationService.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getQues() {
        //addListenerForSingleValueEvent
        myRef.child("tests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Test t = new Test();
                    t.setName(snapshot.getKey());
                    t.setTime(Long.parseLong(snapshot.child("Time").getValue().toString()));
                    ArrayList<Question> ques = new ArrayList<>();
                    for (DataSnapshot qSnap : snapshot.child("Questions").getChildren()) {
                        ques.add(qSnap.getValue(Question.class));
                    }
                    t.setQuestions(ques);
                    tests.add(t);

                }
                testAdapter.dataList = tests;
                testAdapter.notifyDataSetChanged();
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read success: ", "su" + tests.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
    }

    class TestAdapter extends ArrayAdapter<Test> implements Filterable {
        private Context mContext;
        ArrayList<Test> dataList;

        public TestAdapter(Context context, ArrayList<Test> list) {
            super(context, 0, list);
            mContext = context;
            dataList = list;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if (listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.test_item, parent, false);

            ((ImageView) listItem.findViewById(R.id.item_imageView)).
                    setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_account_box));

            ((TextView) listItem.findViewById(R.id.item_textView))
                    .setText(dataList.get(position).getName() + " : " + dataList.get(position).getTime() + "Min");

            ((Button) listItem.findViewById(R.id.item_button)).setText("Attempt");

            (listItem.findViewById(R.id.item_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AttemptTest.class);
                    CommanClass.testName = dataList.get(position).getName();
                    CommanClass.Questions = dataList.get(position).getQuestions();
                    CommanClass.time = dataList.get(position).getTime();
                    startActivity(intent);
                }
            });

            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    (position > lastPos) ? R.anim.up_from_bottom : R.anim.down_from_top);
            (listItem).startAnimation(animation);
            lastPos = position;

            return listItem;
        }
    }


}
