package edu.cnm.deepdive.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private static final String KEY_INDEX = "index";
  private static final int REQUEST_CODE_CHEAT = 0;

  private Button mTrueButton;
  private Button mFalseButton;
  private ImageButton mNextButton;
  private ImageButton mPrevButton;
  private Button mCheatButton;
  private TextView mQuestionTextView;
  private int mCurrentIndex = 0;
  private boolean mIsCheater;

  private Question[] mQuestionBank = new Question[]{
      new Question(R.string.question_australia, true),
      new Question(R.string.question_oceans, true),
      new Question(R.string.question_mideast, false),
      new Question(R.string.question_africa, false),
      new Question(R.string.question_americas, true),
      new Question(R.string.question_asia, true),
      new Question(R.string.question_travis, true),
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "OnCreat(Bundle) Called");
    setContentView(R.layout.activity_main);
    if (savedInstanceState != null) {
      mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
    }

    mQuestionTextView = findViewById(R.id.question_text_view);
    int question = mQuestionBank[mCurrentIndex].getTextResId();
    mQuestionTextView.setText(question);
    mQuestionTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
      }
    });

    mTrueButton = findViewById(R.id.true_button);
    mTrueButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(true);
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
      }
    });
    mFalseButton = findViewById(R.id.false_button);
    mFalseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(false);
        mFalseButton.setEnabled(false);
        mTrueButton.setEnabled(false);
      }
    });

    mNextButton = findViewById(R.id.next_button);
    mNextButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        mIsCheater = false;
        updateQuestion();
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);

      }
    });

    mCheatButton = findViewById(R.id.cheat_button);
    mCheatButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean answerIsTue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTue);

        startActivityForResult(intent, REQUEST_CODE_CHEAT);
      }
    });

    mPrevButton = findViewById(R.id.prev_button);
    mPrevButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mCurrentIndex = (mCurrentIndex + (mQuestionBank.length - 1)) % mQuestionBank.length;
        updateQuestion();
      }
    });

    updateQuestion();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode != Activity.RESULT_OK) {
      return;
    }
    if (requestCode == REQUEST_CODE_CHEAT) {
      if (data == null) {
        return;
      }
      mIsCheater = CheatActivity.wasAnswerShown(data);
    }

  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(TAG, "OnStart Called");
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
    Log.d(TAG, "OnResume Called");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "OnPause Called");
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    Log.i(TAG, "onSaveInstanceState");
    savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.d(TAG, "OnStop Called");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "OnDestroy Called");
  }

  private void updateQuestion() {
    int question = mQuestionBank[mCurrentIndex].getTextResId();
    mQuestionTextView.setText(question);
  }

  private void checkAnswer(boolean userPressedTrue) {
    boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

    int messageResId = 0;
// TODO Fix cheater toast.
    if (mIsCheater) {
      messageResId = R.string.judgment_toast;
    } else {
      if (userPressedTrue == answerIsTrue) {
        messageResId = R.string.correct_toast;
      } else {
        messageResId = R.string.incorrect_toast;
      }
    }
      Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
          .show();
    }

}

