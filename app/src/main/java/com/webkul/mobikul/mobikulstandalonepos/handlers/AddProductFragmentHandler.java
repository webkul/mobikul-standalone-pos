package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.CategoryActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentAddProductBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.ManageCategoriesFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddProductFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.ManageOptionsFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.SweetAlertBox;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by aman.gupta on 10/1/18. @Webkul Software Private limited
 */

public class AddProductFragmentHandler {

    private Context context;

    public AddProductFragmentHandler(Context context) {
        this.context = context;
    }

    public void addNEditProfile() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions((ProductActivity) context, new String[]{Manifest.permission.CAMERA}, 666);
        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            ((ProductActivity) context).startActivityForResult(cameraIntent, ((ProductActivity) context).CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean rename(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }

    public void saveProduct(final Product product, boolean isEdit) {
        if (isValidated(product)) {
            if (!isEdit) {
                DataBaseController.getInstanse().addProduct(context, product, new DataBaseCallBack() {
                    @Override
                    public void onSuccess(Object responseData, String successMsg) {
                        if (!product.getImage().isEmpty()) {
//                            SweetAlertBox.getInstance().showProgressDialog(BaseActivity.getContext());
                            String image = product.getImage().replace("0.jpg", responseData + ".jpg");
                            Log.d(TAG, "onSuccess: " + image + "--" + product.getImage());
                            ContextWrapper cw = new ContextWrapper(context);
                            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                            File from = new File(directory, "0.jpg");
                            File to = new File(directory, responseData + ".jpg");
                            if (rename(from, to)) {
                                DataBaseController.getInstanse().updateProductImages(context, image, (Long) responseData, new

                                        DataBaseCallBack() {
                                            @Override
                                            public void onSuccess(Object responseData, String successMsg) {
                                                ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                                                Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
                                                FragmentTransaction ft = ((BaseActivity) context).mSupportFragmentManager.beginTransaction();
                                                ft.detach(fragment);
                                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                                                ft.commit();
                                                ((BaseActivity) context).mSupportFragmentManager.popBackStackImmediate();
                                            }

                                            @Override
                                            public void onFailure(int errorCode, String errorMsg) {
//                                                SweetAlertBox.getInstance().dissmissSweetAlert();
                                            }
                                        });
                            }
                        } else {
                            ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                            Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
                            FragmentTransaction ft = ((BaseActivity) context).mSupportFragmentManager.beginTransaction();
                            ft.detach(fragment);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                            ft.commit();
                            ((BaseActivity) context).mSupportFragmentManager.popBackStackImmediate();
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        ToastHelper.showToast(context, errorMsg, Toast.LENGTH_LONG);
//                        SweetAlertBox.getInstance().dissmissSweetAlert();
                    }
                });
            } else {
                DataBaseController.getInstanse().updateProduct(context, product, new DataBaseCallBack() {
                    @Override
                    public void onSuccess(Object responseData, String successMsg) {
                        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
                        FragmentTransaction ft = ((BaseActivity) context).mSupportFragmentManager.beginTransaction();
                        ft.detach(fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft.commit();
                        ((BaseActivity) context).mSupportFragmentManager.popBackStackImmediate();
                        ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        ToastHelper.showToast(context, errorMsg + "", Toast.LENGTH_LONG);
                    }
                });
            }
        } else

        {
            Toast.makeText(context, "Please check the form carefully!", Toast.LENGTH_SHORT).show();
        }

    }

    private FragmentAddProductBinding getAddProductBinding() {
        AddProductFragment fragment = (AddProductFragment) ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
        return fragment.binding;
    }

    public void selectCategories(Product product, boolean edit) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(ManageCategoriesFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new ManageCategoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        bundle.putSerializable("edit", edit);
        fragment.setArguments(bundle);
        fragmentTransaction.add(((ProductActivity) context).binding.productFl.getId(), fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();

    }

    public void selectOptions(Product product, boolean edit) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(ManageOptionsFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new ManageOptionsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        bundle.putSerializable("edit", edit);
        fragment.setArguments(bundle);
        fragmentTransaction.add(((ProductActivity) context).binding.productFl.getId(), fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();

    }

    public void saveImage(Product product) {
        ActivityCompat.requestPermissions((ProductActivity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 666);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/pos-barcodes");
        myDir.mkdirs();
        String text = "noName";
        if (!product.getProductName().isEmpty())
            text = product.getProductName();
        String fname = "Image-" + text + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            Bitmap finalBitmap = getBarCodeBitmap(product.getBarCode());
            if (finalBitmap != null) {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                SweetAlertBox.getInstance().showSuccessPopUp(context, context.getString(R.string.barcode_saved), context.getString(R.string.image_saved_to) + file.getPath());
                ToastHelper.showToast(context, "Barcode Saved!", Toast.LENGTH_SHORT);
            } else {
                ToastHelper.showToast(context, "Barcode is empty, generate first to save!", Toast.LENGTH_LONG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Bitmap getBarCodeBitmap(String barCodeNumber) {
        String text = barCodeNumber; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            if (!text.isEmpty()) {
                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODABAR, 380, 100);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                return bitmap;
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteProduct(Product product) {
        if (product != null) {
            DataBaseController.getInstanse().deleteProduct(context, product, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
                    FragmentTransaction ft = ((BaseActivity) context).mSupportFragmentManager.beginTransaction();
                    ft.detach(fragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
                    ((BaseActivity) context).mSupportFragmentManager.popBackStackImmediate();
                    ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    ToastHelper.showToast(context, errorMsg, Toast.LENGTH_LONG);

                }
            });
        }
    }

    public boolean isValidated(Product product) {
        product.setDisplayError(true);
        Fragment fragment = ((ProductActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            AddProductFragment productFragment = ((AddProductFragment) fragment);
            if (!product.getSkuError().isEmpty()) {
                productFragment.binding.sku.requestFocus();
                return false;
            }
            if (!product.getProductNameError().isEmpty()) {
                productFragment.binding.productName.requestFocus();
                return false;
            }
            if (!product.getPriceError().isEmpty()) {
                productFragment.binding.price.requestFocus();
                return false;
            }
            if (!product.getQuantityError().isEmpty()) {
                productFragment.binding.quantity.requestFocus();
                return false;
            }
            if (!product.getWeightError().isEmpty()) {
                productFragment.binding.weight.requestFocus();
                return false;
            }
            product.setDisplayError(false);
            return true;
        }
        return false;
    }


}
