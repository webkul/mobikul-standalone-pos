package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivitySignUpSignInBinding;
import com.webkul.mobikul.mobikulstandalonepos.fragment.SignInFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.SignUpFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;

import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.DEFAULT_BACK_PRESSED_TIME_TO_CLOSE;

public class SignUpSignInActivity extends BaseActivity {


    private ActivitySignUpSignInBinding mBinding;
    private long mBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(SignUpSignInActivity.this, R.layout.activity_sign_up_sign_in);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        if (!AppSharedPref.isSignedUp(this, false)) {
            fragment = new SignUpFragment();
        } else {
            fragment = new SignInFragment();
        }
        Log.d("name", fragment.getClass().getSimpleName() + "");
        fragmentTransaction.add(mBinding.fragmentContainer.getId(), fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(SignUpFragment.class.getSimpleName());
        fragmentTransaction.commit();

//        Thread thread = new Thread(new Runnable() {
//            public void run() {
//                Log.d(TAG, "onCreate: " + db.administratorDao().getAll().get(0));
//            }
//        });
//        thread.start();
//        thread.stop();
//        mBinding.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Administrator administrator = new Administrator();
//                administrator.setFirstName(mBinding.firstName.getText().toString());
//                administrator.setLastName(mBinding.lastname.getText().toString());
//                administrator.setEmail(mBinding.email.getText().toString());
//                administrator.setPassword(mBinding.password.getText().toString());
//                db.administratorDao().insertAll(administrator);
//
//                Log.d("data", db.administratorDao() .getAll().get(0).getEmail() + "");
//                Log.d("data", db.administratorDao().getAll().get(0).getFirstName() + "");
//                Log.d("data", db.administratorDao().getAll().get(0).getLastName() + "");
//                Log.d("data", db.administratorDao().getAll().get(0).getPassword() + "");
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        long time = System.currentTimeMillis();
        if (time - mBackPressedTime > DEFAULT_BACK_PRESSED_TIME_TO_CLOSE) {
            mBackPressedTime = time;
            ToastHelper.showToast(this, getString(R.string.press_back_to_exit)
                    , Toast.LENGTH_SHORT);
        } else {
            super.onBackPressed();
        }
    }
}
