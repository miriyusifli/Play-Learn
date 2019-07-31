package com.example.miriyusifli.cardgame;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miriyusifli.cardgame.models.DAO.User;
import com.example.miriyusifli.cardgame.services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;
import static com.example.miriyusifli.cardgame.services.Utils.hideKeyboard;
import static com.example.miriyusifli.cardgame.services.Utils.isEmailValid;
import static com.example.miriyusifli.cardgame.services.Utils.isPasswordValid;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    private EditText emailView = null;
    private EditText passView = null;
    private FirebaseUser user;
    private String email = null;
    private String pass = null;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button sign_in = (Button) getActivity().findViewById(R.id.sign_in);
        emailView = (EditText) getActivity().findViewById(R.id.email_signin_input);
        passView = (EditText) getActivity().findViewById(R.id.signin_password);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        progressDialog = new ProgressDialog(getContext());


        sign_in.setOnClickListener(new View.OnClickListener() {
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

                hideKeyboard(getActivity());


                startRegister();


            }
        });


    }

    private void startRegister() {

        progressDialog.setMessage("Signing up ...");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    user = auth.getCurrentUser();


                    if (!Utils.isNull(user)) {

                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.e(TAG, "GONDERILIR.....");

                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    showAlertDialog();

                                }
                            }
                        });

                    }


                } else {
                    progressDialog.hide();
                    Toasty.error(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();

                }
            }
        });
    }


    private void showAlertDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Verify Email")
                .setMessage("Verification link sent to your email.Please verify your email.")
                .setPositiveButton("Verified", null) //Set to null. We override the onclick
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        auth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                user = auth.getCurrentUser();

                                if (!user.isEmailVerified()) {
                                    Toasty.warning(getContext(), "Please Verify your email", Toast.LENGTH_SHORT, true).show();

                                    return;
                                }
                                showVerifyEmailDialog();
                                dialog.dismiss();
                            }
                        });


                    }
                });
            }
        });
        dialog.show();

    }

    private void showVerifyEmailDialog() {
        DatabaseReference new_user = databaseReference.child(user.getUid());
        new_user.child("email").setValue(email);


        getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit().clear().apply();

        SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id", user.getUid()); // Storing string
        editor.putString("email", email);
        editor.putString("pass", pass);
        editor.apply(); // commit changes




      /*  FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new Choose_language_Fragment();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.first_activity, fragment);
        fragmentTransaction.commit();*/

        Intent myIntent = new Intent(getActivity(), MainActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.enter_from_top, R.anim.exit_to_bottom);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(myIntent, options.toBundle());


    }
}


// if this button is clicked, close
// current activity


//getActivity().finish();