package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.WalkthroughActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.fragment.SignInFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.SignUpFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import static android.content.ContentValues.TAG;

/**
 * Created by aman.gupta on 2/1/18.
 */

public class SignUpSignInHandler {

    private Context context;
    private Administrator administrator;
    private Vibrator vibrateObject;

    public SignUpSignInHandler(Context context, Administrator administrator) {
        this.context = context;
        this.administrator = administrator;

    }

    public void signUp(Administrator data) {
        if (isFormValidated()) {

            DataBaseController.getInstanse().insertAdministorDetails(context, data, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    Intent i = new Intent(context, WalkthroughActivity.class);
                    context.startActivity(i);
                    AppSharedPref.setSignedUp(context, true);
                    AppSharedPref.setShowWalkThrough(context, true);
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentByTag(SignUpFragment.class.getSimpleName());
                    if (fragment != null)
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    SignInFragment signInFragment = new SignInFragment();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    fragmentTransaction.replace(R.id.fragment_container, signInFragment, SignInFragment.class.getSimpleName());
                    fragmentTransaction.addToBackStack(SignInFragment.class.getSimpleName());
                    fragmentTransaction.commit();
                    ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    ToastHelper.showToast(context, errorMsg, Toast.LENGTH_LONG);
                }

            });

        } else
            Toast.makeText(context, "Please check the form carefully!!", Toast.LENGTH_SHORT).show();
    }

    public void signIn(Administrator data) {
        if (isSignInFormValidated()) {
            DataBaseController.getInstanse().getAdminDataByEmail(context, data, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    administrator = (Administrator) responseData;
                    Log.d(TAG, "onSuccess: " + AppSharedPref.isShowWalkThrough(context, false));
                    if (!AppSharedPref.isShowWalkThrough(context, false)) {
                        Intent i = new Intent(context, WalkthroughActivity.class);
                        context.startActivity(i);
                        Log.d(TAG, "onSuccess: " + AppSharedPref.isShowWalkThrough(context, false));
                    } else {
                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);
                        ToastHelper.showToast(context, "You have Successfully loggedIn!!"
                                , Toast.LENGTH_SHORT);
                    }
                    AppSharedPref.setLoggedIn(context, true);
                    AppSharedPref.setUserId(context, administrator.getUid());
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    ToastHelper.showToast(context, errorMsg, Toast.LENGTH_LONG);
                }
            });
        } else
            ToastHelper.showToast(context, "Check form carefully!", Toast.LENGTH_LONG);

    }

    public boolean isFormValidated() {
        administrator.setDisplayError(true);
        Log.d("name", SignUpFragment.class.getSimpleName() + "");
        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(SignUpFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            SignUpFragment signUpFragment = (SignUpFragment) fragment;
            if (!administrator.getEmailError().isEmpty()) {
                signUpFragment.binding.email.requestFocus();
                shake(signUpFragment.binding.emailTil);
                return false;
            }
            if (!administrator.getFirstNameError().isEmpty()) {
                signUpFragment.binding.firstName.requestFocus();
                shake(signUpFragment.binding.firstNameTil);
                return false;
            }
            if (!administrator.getLastNameError().isEmpty()) {
                signUpFragment.binding.lastname.requestFocus();
                shake(signUpFragment.binding.lastnameTil);
                return false;
            }
            if (!administrator.getPasswordError().isEmpty()) {
                signUpFragment.binding.password.requestFocus();
                shake(signUpFragment.binding.passwordTil);
                return false;
            }
            administrator.setDisplayError(false);
            return true;
        }
        return false;
    }

    private void shake(View view) {
        vibrateObject = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 200 milliseconds
        vibrateObject.vibrate(300);
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_error));
    }

    public boolean isSignInFormValidated() {
        administrator.setDisplayError(true);
        Log.d("name", SignUpFragment.class.getSimpleName() + "");
        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(SignInFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            SignInFragment signInFragment = ((SignInFragment) fragment);
            if (!administrator.getEmailError().isEmpty()) {
                signInFragment.binding.email.requestFocus();
                shake(signInFragment.binding.emailTil);
                return false;
            }

            if (!administrator.getPasswordError().isEmpty()) {
                signInFragment.binding.password.requestFocus();
                shake(signInFragment.binding.passwordTil);
                return false;
            }
            administrator.setDisplayError(false);
            return true;
        }
        return false;
    }
}