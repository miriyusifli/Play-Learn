package com.example.miriyusifli.cardgame;


import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.miriyusifli.cardgame.services.Utils;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

import static com.example.miriyusifli.cardgame.services.Utils.isEmailValid;
import static com.example.miriyusifli.cardgame.services.Utils.isPasswordValid;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends Fragment {


    private EditText emailView = null;
    private EditText passView = null;
    private String email = null;
    private String pass = null;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private TextView forgotPass;

    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_log_in, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button sign_in = (Button) getActivity().findViewById(R.id.sign_in);
        Button log_in = (Button) getActivity().findViewById(R.id.log_in);
        emailView = (EditText) getActivity().findViewById(R.id.email_login_input);
        passView = (EditText) getActivity().findViewById(R.id.password_login_input);
        forgotPass = (TextView) getActivity().findViewById(R.id.forgot_password);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new SignInFragment();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.first_activity, fragment);
                fragmentTransaction.addToBackStack("test");
                fragmentTransaction.commit();
            }
        });


        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailView.getText().toString();
                pass = passView.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    emailView.setError("This field is required ");
                    return;
                } else if (!isEmailValid(email)) {
                    emailView.setError("This email address is invalid");
                    return;
                }


                if (TextUtils.isEmpty(pass)) {
                    passView.setError("This field is required ");
                    return;
                } else if (!isPasswordValid(pass)) {
                    passView.setError("This password is short");
                    return;
                }


                logIn();


            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResetPasswordDialog();
            }
        });

    }

    private void showResetPasswordDialog() {

        final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Your Email");


        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setTitle("Reset password")
                .setMessage("Reset link will send your Email")
                .setPositiveButton("Send", null)
                .setView(input)//Set to null. We override the onclick
                .create();


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        if (Utils.isNull(input.getText().toString())) {
                            Toast.makeText(getContext(), "Please input your Email", Toast.LENGTH_LONG).show();
                            return;
                        }

                        auth.sendPasswordResetEmail(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Email sended", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                                else {
                                    Toasty.error(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();

                                }
                            }
                        });

                    }
                });
            }
        });
        dialog.show();

    }

    private void logIn() {
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    CardGame.setSecured(true);
                    getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit().clear().apply();

                    final String user_id = auth.getCurrentUser().getUid();


                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", email); // Storing string
                            editor.putString("pass", pass); // Storing string
                            editor.putString("id", user_id);
                            editor.putString("from", (String) dataSnapshot.child("from").getValue()); // Storing string
                            editor.putString("to", (String) dataSnapshot.child("to").getValue());
                            editor.apply(); // commit changes


                            hideKeyboard(getActivity());


                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Fragment fragment = new SplashFragment();
                            fragmentTransaction.replace(R.id.first_activity, fragment);
                            fragmentTransaction.commit();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    progressDialog.hide();
                    Toasty.error(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();

                }
            }
        });
    }


    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}