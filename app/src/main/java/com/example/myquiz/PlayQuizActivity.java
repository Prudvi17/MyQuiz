package com.example.myquiz;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class PlayQuizActivity extends AppCompatActivity {
    ArrayList<Question> listQuestion = new ArrayList();
    int score = 0;
    TextView txtQuestion,txtOption1,txtOption2,txtOption3,txtOption4,txtProgress;

    TextView txtNExt;
    Boolean isSoundEnabled = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_quiz);
        getSupportActionBar().hide();
        SharedPreferences prefs =  getSharedPreferences("MySharedPRefs", MODE_PRIVATE);
        isSoundEnabled = prefs.getBoolean("hasSound",true);

        txtQuestion = findViewById(R.id.txtQuestion);
        txtOption1 = findViewById(R.id.txtOption1);
        txtOption2 = findViewById(R.id.txtOption2);
        txtOption3 = findViewById(R.id.txtOption3);
        txtOption4 = findViewById(R.id.txtOption4);
        txtNExt = findViewById(R.id.txtNext);
        txtProgress = findViewById(R.id.txtProgress);

        txtOption1.setOnClickListener(view -> {
            txtOption2.setEnabled(false);
            txtOption3.setEnabled(false);
            txtOption4.setEnabled(false);
            if(isAnswerCorrect(currentQuestion,txtOption1.getText().toString())){
                setAnswerWrongOrRight(currentQuestion,txtOption1,true);
                if(isSoundEnabled)
                    playMusicFor(true);
            }else{
                setAnswerWrongOrRight(currentQuestion,txtOption1,false);
                if(isSoundEnabled)
                    playMusicFor(false);
            }
            txtNExt.setVisibility(View.VISIBLE);
            txtNExt.setEnabled(true);
            if(isQuizFinished(currentQuestion)) {
                txtNExt.setText("FINISH");
            }
        });
        txtOption2.setOnClickListener(view -> {
            txtOption1.setEnabled(false);
            txtOption3.setEnabled(false);
            txtOption4.setEnabled(false);
            if(isAnswerCorrect(currentQuestion,txtOption2.getText().toString())){
                setAnswerWrongOrRight(currentQuestion,txtOption2,true);
                if(isSoundEnabled)
                    playMusicFor(true);
            }else{
                setAnswerWrongOrRight(currentQuestion,txtOption2,false);
                if(isSoundEnabled)
                    playMusicFor(false);
            }
            txtNExt.setVisibility(View.VISIBLE);
            txtNExt.setEnabled(true);
            if(isQuizFinished(currentQuestion)) {
                txtNExt.setText("FINISH");
            }
        });
        txtOption3.setOnClickListener(view -> {
            txtOption2.setEnabled(false);
            txtOption1.setEnabled(false);
            txtOption4.setEnabled(false);
            if(isAnswerCorrect(currentQuestion,txtOption3.getText().toString())){
                setAnswerWrongOrRight(currentQuestion,txtOption3,true);
                if(isSoundEnabled)
                    playMusicFor(true);
            }else{
                setAnswerWrongOrRight(currentQuestion,txtOption3,false);
                if(isSoundEnabled)
                    playMusicFor(false);
            }
            txtNExt.setVisibility(View.VISIBLE);
            txtNExt.setEnabled(true);
            if(isQuizFinished(currentQuestion)) {
                txtNExt.setText("FINISH");
            }
        });
        txtOption4.setOnClickListener(view -> {
            txtOption2.setEnabled(false);
            txtOption3.setEnabled(false);
            txtOption1.setEnabled(false);
            if(isAnswerCorrect(currentQuestion,txtOption4.getText().toString())){
                setAnswerWrongOrRight(currentQuestion,txtOption4,true);
                if(isSoundEnabled)
                    playMusicFor(true);
            }else{
                setAnswerWrongOrRight(currentQuestion,txtOption4,false);
                if(isSoundEnabled)
                    playMusicFor(false);
            }
            txtNExt.setVisibility(View.VISIBLE);
            txtNExt.setEnabled(true);
            if(isQuizFinished(currentQuestion)) {
                txtNExt.setText("FINISH");
            }
        });
        setQuestions();
        startQuiz();
        txtNExt.setOnClickListener(view -> {
            if(isQuizFinished(currentQuestion)) {
                //show result quiz finish here
                showThankYouDialog();
            }else {
                txtOption1.setEnabled(true);
                txtOption2.setEnabled(true);
                txtOption3.setEnabled(true);
                txtOption4.setEnabled(true);
                currentQuestion++;
                showQuestion(currentQuestion);
                txtNExt.setVisibility(View.INVISIBLE);
                txtNExt.setEnabled(false);
            }
        });
    }
    private void showThankYouDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_thank_you);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

        TextView btnReset = dialog.findViewById(R.id.btnResetQuiz);
        TextView btnExit = dialog.findViewById(R.id.btnExit);
        TextView txtScore = dialog.findViewById(R.id.txtScore);
        txtScore.setText("Your Score : "+score);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dialog.getContext());

        btnReset.setOnClickListener(view -> {
            dialog.dismiss();
            startQuiz();
            //start again quiz here
        });
        btnExit.setOnClickListener(view -> {
            dialog.dismiss();
            finishAffinity();
        });
        dialog.show();
    }
    private void playMusicFor(boolean isRight) {
        if(isRight){
            MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.correct);
            mediaPlayer.start();
        }else{
            MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.wrong);
            mediaPlayer.start();
        }
    }

    private boolean isQuizFinished(int currentQuestion) {
        return currentQuestion >= listQuestion.size();
    }

    private void setAnswerWrongOrRight(int currentQuestion, TextView selectedOption,boolean isRight) {

        if(isRight){
            selectedOption.setBackgroundResource(R.drawable.bg_right);
            score++;
        }else{
            showRightAnswer(currentQuestion);
            selectedOption.setBackgroundResource(R.drawable.bg_wrong);
        }

    }

    private void showRightAnswer(int currentQuestion) {
        String temp = listQuestion.get(currentQuestion-1).answer;
        if(listQuestion.get(currentQuestion-1).option1.equals(temp))
            txtOption1.setBackgroundResource(R.drawable.bg_right);
        else if(listQuestion.get(currentQuestion-1).option2.equals(temp))
            txtOption2.setBackgroundResource(R.drawable.bg_right);
        else if(listQuestion.get(currentQuestion-1).option3.equals(temp))
            txtOption3.setBackgroundResource(R.drawable.bg_right);
        else if(listQuestion.get(currentQuestion-1).option4.equals(temp))
            txtOption4.setBackgroundResource(R.drawable.bg_right);
    }

    private boolean isAnswerCorrect(int currentQuestion, String answer) {
        return listQuestion.get(currentQuestion-1).answer.equals(answer);
    }

    int currentQuestion =1;
    private void startQuiz() {
        txtOption1.setEnabled(true);
        txtOption2.setEnabled(true);
        txtOption3.setEnabled(true);
        txtOption4.setEnabled(true);
        score = 0;
        txtNExt.setText("Next");
        currentQuestion=1;
        txtNExt.setVisibility(View.GONE);
        showQuestion(currentQuestion);
    }

    private void showQuestion(int currentQuestion) {
        txtQuestion.setText(listQuestion.get(currentQuestion-1).question);
        txtOption1.setText(listQuestion.get(currentQuestion-1).option1);
        txtOption2.setText(listQuestion.get(currentQuestion-1).option2);
        txtOption3.setText(listQuestion.get(currentQuestion-1).option3);
        txtOption4.setText(listQuestion.get(currentQuestion-1).option4);
        txtOption1.setBackgroundResource(R.drawable.bg_unclick);
        txtOption2.setBackgroundResource(R.drawable.bg_unclick);
        txtOption3.setBackgroundResource(R.drawable.bg_unclick);
        txtOption4.setBackgroundResource(R.drawable.bg_unclick);
        txtProgress.setText("Question "+currentQuestion+" of 10");
    }

    private void setQuestions() {
        listQuestion.add(new Question(
                "What is three fifth of 100?",
                "3","5","20","60",
                "60"));
        listQuestion.add(new Question(
                "If David’s age is 27 years old in 2011. What was his age in 2003?",
                "17 years","37 years","20 years","19 years",
                "19 years"));
        listQuestion.add(new Question(
                "What is the remainder of 21 divided by 7?",
                "21","7","1","None of the above.",
                "None of the above."));
        listQuestion.add(new Question(
                "What is 7% equal to?",
                "0.007","0.07","0.7","7",
                "0.07"));
        listQuestion.add(new Question(
                "I am a number. I have 7 in the ones place. I am less than 80 but greater than 70. What is my number?",
                "71","73","75","77",
                "77"));
        listQuestion.add(new Question(
                "What is the square of 15?",
                "15","30","225","252",
                "225"));
        listQuestion.add(new Question(
                "What is the value of x if x2 = 169",
                "1","13","169","338",
                "13"));
        listQuestion.add(new Question(
                "What is the reciprocal of 17/15?",
                "1.13","15/17","17/15","30/34",
                "15/17"));
        listQuestion.add(new Question(
                "13 – 13 × 111 – 111 = ……..",
                "1319","1443","-1541","1",
                "-1541"));
        listQuestion.add(new Question(
                "In a century how many months are there?",
                "12","120","1200","12000",
                "1200"));

    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog dialog = new AlertDialog.Builder(PlayQuizActivity.this).create();
        dialog.setTitle(R.string.app_name);
        dialog.setMessage("Are You Sure To Exit Quiz?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialog.show();
    }
}