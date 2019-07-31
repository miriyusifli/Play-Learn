package com.example.miriyusifli.cardgame;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.miriyusifli.cardgame.models.Card;
import com.example.miriyusifli.cardgame.models.DAO.Topic;
import com.example.miriyusifli.cardgame.models.EndDrawerToggle;
import com.example.miriyusifli.cardgame.services.AlarmReceiver;
import com.example.miriyusifli.cardgame.services.Utils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static List<Topic> topics;
    public FragmentManager fragmentManager = null;
    public FragmentTransaction fragmentTransaction = null;

    private String from;
    private String to;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);


        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Utils.isNull(topics))
            topics = new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        EndDrawerToggle toggle = new EndDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        TextView logOutView = (TextView) findViewById(R.id.logout);
        logOutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit().clear().apply();


                auth.signOut();

                CardGame.setTopics(new ArrayList<Topic>());
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {



                        getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit().clear().apply();




                        CardGame.setTopics(null);

                        CardGame.setSecured(false);
                        Intent myIntent = new Intent(getApplicationContext(), FirstActivity.class);
                        ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.enter_from_bottom, R.anim.exit_to_top);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myIntent, options.toBundle());
                    }
                }, 200);


            }
        });

        SharedPreferences pref = getSharedPreferences("MyPref", 0); // 0 - for private mode

        from = pref.getString("from", null);
        to = pref.getString("to", null);

        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Utils.isNull(from) || Utils.isNull(to)) {

            fragment = new Choose_language_Fragment();

        } else {
            fragment = new Topics_Fragment();

        }
        fragmentTransaction.replace(R.id.main_activity, fragment);
        fragmentTransaction.commit();






    }

    @Override
    protected void onResume() {

        super.onResume();

    }


    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }


    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (getIntent().getBooleanExtra("testPage", false) || getIntent().getBooleanExtra("gamePage", false)) {

                super.onBackPressed();
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        } else super.onBackPressed();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;

        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (CardGame.getUser() != null) {


            switch (id) {
                case R.id.menu_topics:
                    fragment = new Topics_Fragment();
                    fragmentTransaction.replace(R.id.main_activity, fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.menu_change_language:
                    fragment = new Choose_language_Fragment();
                    fragmentTransaction.replace(R.id.main_activity, fragment);
                    fragmentTransaction.addToBackStack("back");
                    fragmentTransaction.commit();
                    break;
                case R.id.menu_alarm:
                    showTimePickerDialog();
                    break;

            }





            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    private void showTimePickerDialog(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.MyTimePickerDialogStyle,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
                        am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


                        Toasty.success(getApplicationContext(), "Alarm set for every day!", Toast.LENGTH_SHORT, true).show();


                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();


    }

}
