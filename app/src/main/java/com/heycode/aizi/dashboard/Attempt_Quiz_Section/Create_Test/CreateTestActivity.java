package com.heycode.aizi.dashboard.Attempt_Quiz_Section.Create_Test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.heycode.aizi.R;
import com.heycode.aizi.models.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


public class CreateTestActivity extends AppCompatActivity {

    EditText question;
    EditText aText;
    EditText bText;
    EditText cText;
    EditText dText;
    RadioButton aRadio;
    RadioButton bRadio;
    RadioButton cRadio;
    RadioButton dRadio;

    int currentQuestion = 1;
    int previousQuestion = 1;
    TextView questionNumber;

    ArrayList<Question> ques;
    JSONArray jsonArray;
    String selectedOption = "";


    AlertDialog alertDialog;
    private View dialogView;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference myRef;
    CardView nextBtn, prevBtn, saveBtn;
    private EditText explanationText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        jsonArray = new JSONArray();
        setContentView(R.layout.activity_create_test);
        question = findViewById(R.id.questionView);
        question = findViewById(R.id.questionView);
        aText = findViewById(R.id.aText);
        bText = findViewById(R.id.bText);
        cText = findViewById(R.id.cText);
        dText = findViewById(R.id.dText);
        explanationText = findViewById(R.id.explanationView);
        questionNumber = findViewById(R.id.questionNumber);
        aRadio = findViewById(R.id.aRadio);

        bRadio = findViewById(R.id.bRadio);
        cRadio = findViewById(R.id.cRadio);
        dRadio = findViewById(R.id.dRadio);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        selectedOption = "";
        currentQuestion = 1;
        setListeners();

        ques = new ArrayList<>();

        alertDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_custom, null);


        nextBtn = findViewById(R.id.nextfab);
        saveBtn = findViewById(R.id.fab2);//save button
        prevBtn = findViewById(R.id.pre_card);

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (previousQuestion > 1) {
                    previousQuestion--;
                    setAllData(previousQuestion);
                }
                if (previousQuestion == 1)
                    prevBtn.setVisibility(View.INVISIBLE);
                //Question question1 = new Question();
                Toast.makeText(CreateTestActivity.this, String.valueOf(previousQuestion), Toast.LENGTH_SHORT).show();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (previousQuestion != currentQuestion) {
                    previousQuestion++;
                    if (previousQuestion != currentQuestion)
                        setAllData(previousQuestion);
                    else {
                        clearAllData();
                        questionNumber.setText(String.valueOf(currentQuestion));
                    }
                    if (previousQuestion > 1)
                        prevBtn.setVisibility(View.VISIBLE);
                }
                boolean cont = getEnteredQuestionsValue();
                if (cont) {
                    previousQuestion++;
                    currentQuestion++;
                    questionNumber.setText(String.valueOf(currentQuestion));
                    clearAllData();
                    prevBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        saveBtn.setOnClickListener(v -> {
            if (previousQuestion != currentQuestion) {
                previousQuestion++;
                if (previousQuestion != currentQuestion)
                    setAllData(previousQuestion);
                else {
                    clearAllData();
                    questionNumber.setText(String.valueOf(currentQuestion));
                }
                if (previousQuestion > 1)
                    prevBtn.setVisibility(View.VISIBLE);
            }
            boolean cont = getEnteredQuestionsValue();
            if (cont) {
                previousQuestion++;
                currentQuestion++;
                questionNumber.setText(String.valueOf(currentQuestion));
                clearAllData();
                prevBtn.setVisibility(View.VISIBLE);
            }

            if (jsonArray.length() != 0) {
                final JSONObject tempObject = new JSONObject();
                // get dialog_custom.xml view
                LayoutInflater li = LayoutInflater.from(CreateTestActivity.this);
                View promptsView = li.inflate(R.layout.dialog_custom, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        CreateTestActivity.this);

                // set dialog_custom.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText testName = promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                final EditText userTime = promptsView.findViewById(R.id.editTextDialogUserInput1);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final String time = userTime.getText().toString().trim();
                                        final String name = testName.getText().toString().trim();
                                        try {
                                            tempObject.put("Questions", jsonArray);
                                            tempObject.put("Time", Integer.parseInt(time));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        final String jsonStr = tempObject.toString();


                                        if (time != null && name != null) {
                                            Map<String, Object> result = new Gson().fromJson(jsonStr, Map.class);
                                            DatabaseReference testLocation = myRef.child("tests").child(name);
                                            testLocation.setValue(result);
                                            finish();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            } else {
                Toast.makeText(this, "Incomplete Question format", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CreateTestActivity.this);
        builder.setMessage("Exit without saving?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void updateData(int position) {
        Question question1 = new Question();
        question1 = ques.get(position - 1);
    }

    public void setAllData(int position) {
        clearAllData();
        Question question1 = new Question();
        question1 = ques.get(position - 1);
        questionNumber.setText(String.valueOf(question1.getId()));
        question.setText(question1.getQuestion());
        aText.setText(question1.getOpt_A());
        bText.setText(question1.getOpt_B());
        cText.setText(question1.getOpt_C());
        dText.setText(question1.getOpt_D());
        explanationText.setText(question1.getExplanation());
        switch (question1.getAnswer()) {
            case "A":
                aRadio.setChecked(true);
                break;
            case "B":
                bRadio.setChecked(true);
                break;
            case "C":
                cRadio.setChecked(true);
                break;
            case "D":
                dRadio.setChecked(true);
                break;
        }
    }

    private void clearAllData() {

        aRadio.setChecked(false);
        bRadio.setChecked(false);
        cRadio.setChecked(false);
        dRadio.setChecked(false);
        aText.setText(null);
        bText.setText(null);
        cText.setText(null);
        dText.setText(null);
        question.setText(null);
        explanationText.setText(null);
        selectedOption = "";
    }

    private boolean getEnteredQuestionsValue() {

        boolean cont = false;
        if (TextUtils.isEmpty(question.getText().toString().trim())) {
            question.setError("Please fill in a question");
        } else if (TextUtils.isEmpty(aText.getText().toString().trim())) {
            aText.setError("Please fill in option A");
        } else if (TextUtils.isEmpty(bText.getText().toString().trim())) {
            bText.setError("Please fill in option B");
        } else if (TextUtils.isEmpty(cText.getText().toString().trim())) {
            cText.setError("Please fill in option C");
        } else if (TextUtils.isEmpty(dText.getText().toString().trim())) {
            dText.setError("Please fill in option D");
        } else if (selectedOption.equals("")) {
            Toast.makeText(this, "Please select the correct answer", Toast.LENGTH_SHORT).show();
        } else {
            Question quest = new Question();
            quest.setId(currentQuestion);
            quest.setQuestion(question.getText().toString());
            quest.setOpt_A(aText.getText().toString());
            quest.setOpt_B(bText.getText().toString());
            quest.setOpt_C(cText.getText().toString());
            quest.setOpt_D(dText.getText().toString());
            quest.setAnswer(selectedOption);
            if (!explanationText.getText().toString().trim().equals("")) {
                quest.setExplanation(explanationText.getText().toString());
            } else {
                quest.setExplanation("");
            }
            ques.add(quest);
            cont = true;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("answer", selectedOption);
                jsonObject.put("opt_A", aText.getText().toString().trim());
                jsonObject.put("opt_B", bText.getText().toString().trim());
                jsonObject.put("opt_C", cText.getText().toString().trim());
                jsonObject.put("opt_D", dText.getText().toString().trim());
                jsonObject.put("question", question.getText().toString().trim());

                if (!explanationText.getText().toString().trim().equals("")) {
                    jsonObject.put("explanation", explanationText.getText().toString().trim());
                } else {
                    quest.setExplanation("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArray.put(jsonObject);
        }
        return cont;
    }

    private void setListeners() {
        aRadio.setOnClickListener(v -> {
            selectedOption = "A";
            bRadio.setChecked(false);
            cRadio.setChecked(false);
            dRadio.setChecked(false);
        });
        bRadio.setOnClickListener(v -> {
            selectedOption = "B";
            aRadio.setChecked(false);
            cRadio.setChecked(false);
            dRadio.setChecked(false);
        });
        cRadio.setOnClickListener(v -> {
            selectedOption = "C";
            bRadio.setChecked(false);
            aRadio.setChecked(false);
            dRadio.setChecked(false);
        });
        dRadio.setOnClickListener(v -> {
            selectedOption = "D";
            bRadio.setChecked(false);
            cRadio.setChecked(false);
            aRadio.setChecked(false);
        });

    }
}
