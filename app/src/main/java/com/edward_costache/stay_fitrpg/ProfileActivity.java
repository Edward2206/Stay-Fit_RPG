package com.edward_costache.stay_fitrpg;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edward_costache.stay_fitrpg.exercises.PushupExerciseActivity;
import com.edward_costache.stay_fitrpg.util.StepDetector;
import com.edward_costache.stay_fitrpg.util.Util;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity{

    private TextView txtUsername, txtProgress, txtLevel, txtOverallSteps, txtHealth, txtStrength, txtAgility, txtStamina;
    private Button btnLogout, btnCharacter;
    private com.google.android.material.card.MaterialCardView cardViewTrain, cardViewFight, cardViewProgress;
    private androidx.constraintlayout.widget.ConstraintLayout layout;

    private pl.pawelkleczkowski.customgauge.CustomGauge progressBarTest;
    private ProgressBar progressBarHealth, progressBarStamina, progressBarAgility, progressBarStrength;

    private FirebaseAuth mAuth;

    private DatabaseReference reference;
    private SharedPreferences sharedPreferences;
    private StepDetector stepDetector;

    private User userProfile;
    private String username;
    private String userID;
    private int health;
    private int strength;
    private int stamina;
    private int agility;
    private int steps;
    private int overallSteps;
    private int progressMax;
    private double level;

    private final int STARTING_STEP_GOAL = 100;
    private final double MULTIPLIER = 1.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        // Set-up functions that initialize declarations
        initViews();
        setUpUser();

        Log.i("TODAY TIME: ", Long.toString(Util.getToday()));
        Log.i("TOMORROW TIME: ", Long.toString(Util.getTomorrow()));
        Log.i("CURRENT TIME: ", Long.toString(System.currentTimeMillis()));

        long time = sharedPreferences.getLong("time", 0);

        // If it is a new day, reset the user progress
        if(time == Util.getToday())
        {
            Log.i("SAME DAY: ", "NOT A NEW DAY, STEPS REMAIN SAME");
        }
        else if(time != Util.getToday() && time != 0)
        {
            sharedPreferences.edit().putInt("progress", 0).apply();
            sharedPreferences.edit().putInt("overallSteps", 0).apply();
            Log.i("NEW DAY: ", "A NEW DAY, THEREFORE STEPS RESET");
        }

        // If the application has been closed before, retrieve the progress
        if(sharedPreferences.getInt("overallSteps", 0) != 0)
        {
            steps = sharedPreferences.getInt("progress", 0);
            overallSteps = sharedPreferences.getInt("overallSteps", 0);
        }
        displayUserInfo();
        initOnClickListeners();
        stepDetector = new StepDetector(ProfileActivity.this);
        stepDetector.setListener(new StepDetector.Listener() {
            @Override
            public void onStep(int Nsteps) {
                steps = Nsteps;
                overallSteps++;
                updateSteps(steps, progressMax);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        stepDetector.registerListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stepDetector.un_registerListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedPreferences.edit().putInt("progress", steps).apply();
        sharedPreferences.edit().putInt("overallSteps", overallSteps).apply();
        sharedPreferences.edit().putLong("time", Util.getToday()).apply();
        stepDetector.un_registerListener();
    }
    private void setUpUser()
    {
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            userID = user.getUid();
        }
        else
        {
            Snackbar.make(ProfileActivity.this, layout, "Something went wrong, logout and login again!", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();
        }
    }

    private void logout()
    {
        mAuth.signOut();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }

    private void initViews()
    {
        txtUsername = findViewById(R.id.profileTxtUsername);
        txtProgress = findViewById(R.id.profileTxtProgress);
        txtLevel = findViewById(R.id.profileTxtLevel);
        txtOverallSteps = findViewById(R.id.profileTxtOverallSteps);
        txtHealth = findViewById(R.id.profileTxtHealthValue);
        txtStrength = findViewById(R.id.profileTxtStrengthValue);
        txtAgility = findViewById(R.id.profileTxtAgilityValue);
        txtStamina = findViewById(R.id.profileTxtStaminaValue);

        cardViewTrain = findViewById(R.id.profileCardViewTrain);
        cardViewFight = findViewById(R.id.profileCardViewFight);
        cardViewProgress = findViewById(R.id.profileCardViewProgress);

        btnLogout = findViewById(R.id.profileBtnLogout);
        btnCharacter = findViewById(R.id.profileBtnCharacter);

        progressBarTest = findViewById(R.id.profileProgressBar);
        progressBarHealth = findViewById(R.id.profileProgressBarHealth);
        progressBarAgility = findViewById(R.id.profileProgressBarAgility);
        progressBarStamina = findViewById(R.id.profileProgressBarStamina);
        progressBarStrength = findViewById(R.id.profileProgressBarStrength);

        layout = findViewById(R.id.profileLayout);
    }

    private void initOnClickListeners()
    {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        cardViewTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ExercisesActivity.class);
                startActivity(intent);
            }
        });
        cardViewFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.displayNotImplemented(ProfileActivity.this, layout);
            }
        });
        cardViewProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.displayNotImplemented(ProfileActivity.this, layout);
            }
        });

        btnCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.displayNotImplemented(ProfileActivity.this, layout);
            }
        });

        txtLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EasterEggActivity.class));
            }
        });
    }

    private void displayUserInfo()
    {
        reference.child(userID).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.getValue(String.class);
                txtUsername.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(userID).child("level").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                level = snapshot.getValue(Integer.class);
                if(level == 1)
                {
                    progressMax = STARTING_STEP_GOAL;
                }
                else
                {
                    // A formula for deriving the required steps for next level
                    double levelPercent = ((level/7) * (level/7)) + MULTIPLIER;
                    progressMax = (int)(STARTING_STEP_GOAL * levelPercent);
                }
                txtLevel.setText(String.format("LEVEL: %d", (int)level));
                progressBarTest.setEndValue(progressMax);
                updateSteps(steps, progressMax);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(userID).child("agility").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                agility = snapshot.getValue(Integer.class);
                progressBarAgility.setProgress(agility);
                txtAgility.setText(Integer.toString(agility));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(userID).child("health").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                health = snapshot.getValue(Integer.class);
                progressBarHealth.setProgress(health);
                txtHealth.setText(Integer.toString(health));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(userID).child("stamina").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stamina = snapshot.getValue(Integer.class);
                progressBarStamina.setProgress(stamina);
                txtStamina.setText(Integer.toString(stamina));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(userID).child("strength").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                strength = snapshot.getValue(Integer.class);
                progressBarStrength.setProgress(strength);
                txtStrength.setText(Integer.toString(strength));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateSteps(int progress, int progressMax)
    {
        if(progress >= progressMax)
        {
            increaseLevel();
        }
        progressBarTest.setValue(progress);
        txtProgress.setText(String.format("%04d/%04d", progress, progressMax));
        txtOverallSteps.setText(String.format("%05d", overallSteps));
    }

    private void increaseLevel()
    {
        reference.child(userID).child("level").setValue(level+1);
        steps = 0;
    }

    @Override
    public void onBackPressed() {
        displayClosingAlertBox();
    }

    private void displayClosingAlertBox()
    {
        new AlertDialog.Builder(ProfileActivity.this, R.style.MyDialogTheme)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exiting the Application")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("ON STOP: ", "YES");
                        finishAndRemoveTask();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}