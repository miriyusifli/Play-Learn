package com.example.miriyusifli.cardgame.services;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    public static boolean isNull(String text) {
        if (text == null || text.equals("") || text.equalsIgnoreCase("null")) return true;
        return false;
    }

    public static boolean isNull(int n) {
        if (n == 0) return true;
        return false;
    }

    public static boolean isNull(double n) {
        if (n == 0) return true;
        return false;
    }

    public static boolean isNull(Object n) {
        if (n == null) return true;
        return false;
    }

    public static boolean isNull(List<?> list) {
        if (list == null || list.size() == 0) return true;
        return false;
    }

    public static boolean isSingle(List<?> list) {
        if (list == null || list.size() != 1) return false;
        return true;
    }

    public static boolean isNull(Object[] array) {
        if (array == null || array.length == 0) return true;
        return false;
    }

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.split("@").length == 2 && email.split("@")[1].contains(".");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public static boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 4;
    }

    public static void hideKeyboard(Activity activity) {
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
