package com.webkul.mobikul.mobikulstandalonepos.helper;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.TableWrapper;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.webkul.mobikul.mobikulstandalonepos.BuildConfig;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.OtherActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseAsyncUtils;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.itextpdf.text.html.HtmlTags.BOLD;
import static com.itextpdf.text.html.HtmlTags.FONT;
import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.PDF_ALIGNMENT_CENTER;
import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.PDF_ALIGNMENT_RIGHT;

/**
 * Created by aman.gupta on 13/1/18. @Webkul Software Private limited
 */

public class Helper {

    private static Helper helper;
    public static final String FONT1 = "resources/fonts/PlayfairDisplay-Regular.ttf";
    public static String DB_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";
    public static String DB_NAME = "db_pos.db";
    public static String DB_NAME_IMAGES = "db_pos_images.zip";

    public static synchronized Helper getInstanse() {
        if (helper == null)
            helper = new Helper();
        return helper;
    }

    public static String currencyFormater(double rate, String code) {
        Locale us = new Locale("en", code.substring(0, 2));
        NumberFormat usFormat = NumberFormat.getCurrencyInstance(us);
        return usFormat.format(rate);
    }

    public static String currencyFormater(double rate, Context context) {
        Locale us = new Locale("en", AppSharedPref.getSelectedCurrency(context).substring(0, 2));
        NumberFormat usFormat = NumberFormat.getCurrencyInstance(us);
        return usFormat.format(rate);
    }

    public static double currencyConverter(double price, Context context) {
        double finalPrice = price * AppSharedPref.getSelectedCurrencyRate(context);
        return finalPrice;
    }

    public static Uri saveToInternalStorage(Context context, Bitmap bitmapImage, String id) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, id + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri path = Uri.fromFile(mypath);
        Log.d(TAG, "saveToInternalStorage: " + path);
        return path;
    }

    public static String fromCartModelToString(CartModel cartData) {
        if (cartData == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CartModel>() {
        }.getType();
        String json = gson.toJson(cartData, type);
        return json;
    }

    public static CartModel fromStringToCartModel(String cartDataString) {
        if (cartDataString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CartModel>() {
        }.getType();
        CartModel cartData = gson.fromJson(cartDataString, type);
        return cartData;
    }

    public static void shake(Context context, View view) {
        Vibrator vibrateObject = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 200 milliseconds
        vibrateObject.vibrate(300);
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_error));
    }

    public static final String TIME_SERVER = "time-a.nist.gov";

    public static long getCurrentNetworkTime() {
        long returnTime = 0;
        NTPUDPClient timeClient = new NTPUDPClient();
        timeClient.setDefaultTimeout(1000);
        // Connect to network. Try again on timeout (max 6).
        for (int retries = 7; retries >= 0; retries--) {

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                // Try connecting to network to get time. May timeout.
                InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
                TimeInfo timeInfo = timeClient.getTime(inetAddress);
                long networkTimeLong = timeInfo.getMessage().getTransmitTimeStamp().getTime();

                // Convert long to Date, and Date to String. Log results.
                Date networkTimeDate = new Date(networkTimeLong);
                Log.i("Time", "Time from " + TIME_SERVER + ": " + networkTimeDate);
                returnTime = networkTimeDate.getTime();
                break;
                // Return resulting time as a String.
            } catch (IOException e) {
                // Max of 6 retries.
                Log.i("RTCTestActivity", "Unable to connect. Retries left: " + (retries - 1));
            }
        }
        return returnTime;
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yy");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    public static int getViewWidth(final View view) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        return view.getWidth();
    }

    public static int getViewHeight(final View view) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        return view.getHeight();
    }

    public File generateInvoice(Context context, OrderEntity orderData) {
        File file;

        String invoiceFolder = Environment.getExternalStorageDirectory().getPath() + "/Mobikul-Pos-Invoices";
        String invoiceNO = "order-" + orderData.getOrderId();
        File invoiceFolderFile = new File(invoiceFolder);
        if (!invoiceFolderFile.exists())
            invoiceFolderFile.mkdirs();
        file = new File(invoiceFolderFile.getAbsolutePath(), "/" + invoiceNO + ".pdf");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fatchInvoiceList(orderData, file, context))
            return file;
        else
            return null;
    }

    public static void setDefaultDataBase(Context context) {
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void imageImport(InputStream imageStreamFile, File imageDirectory) {
        File backupDB = new File(imageDirectory.getAbsolutePath());
        if (!backupDB.exists())
            backupDB.mkdirs();
        try {
            ZipManager.unzipFolder(imageStreamFile, backupDB.getAbsolutePath().replace("/app_imageDir", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean fatchInvoiceList(OrderEntity orderEntity, File file, Context context) {
        try {
            // To customise the text of the pdf
            // we can use FontFamily

            Font mFBold14 = FontFactory.getFont(FONT1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.BOLD);
            Font mF12 = FontFactory.getFont(FONT1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
            Font mFBold12 = FontFactory.getFont(FONT1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD);
            // create an instance of itext document
            Document document = new Document();

            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
            document.open();

            //using add method in document to insert a paragraph
            document.add(getParagraph("Mobikul Pos Invoice", mFBold14, PDF_ALIGNMENT_CENTER));
            document.add(getParagraph("Order No.- " + orderEntity.getOrderId(), mFBold14, PDF_ALIGNMENT_CENTER));
            document.add(getParagraph("Order Details", mFBold14));
            document.add(getParagraph("Order ID - " + orderEntity.getOrderId(), mFBold12));
            document.add(getParagraph("Date - " + orderEntity.getDate(), mFBold12));
            document.add(getParagraph("Payment Method - Cash", mFBold12));
            document.add(new Paragraph("\n"));
            document.add(getParagraph("Customer Infomation", mFBold14));
            document.add(getParagraph(orderEntity.getCartData().getCustomer().getFirstName() + " " + orderEntity.getCartData().getCustomer().getLastName(), mF12));
            document.add(getParagraph(orderEntity.getCartData().getCustomer().getEmail(), mF12));
            document.add(getParagraph(orderEntity.getCartData().getCustomer().getContactNumber(), mF12));
            document.add(new Paragraph("\n"));
            document.add(getParagraph("Shipping Address", mFBold14));
            document.add(getParagraph(orderEntity.getCartData().getCustomer().getAddressLine(), mF12));
            document.add(getParagraph(orderEntity.getCartData().getCustomer().getCity() + ", " + orderEntity.getCartData().getCustomer().getPostalCode(), mF12));
            String countryText;
            if (orderEntity.getCartData().getCustomer().getState().equalsIgnoreCase(""))
                countryText = orderEntity.getCartData().getCustomer().getCountry();
            else if (orderEntity.getCartData().getCustomer().getCountry().equalsIgnoreCase(""))
                countryText = orderEntity.getCartData().getCustomer().getState();
            else
                countryText = orderEntity.getCartData().getCustomer().getState() + ", " + orderEntity.getCartData().getCustomer().getCountry();

            document.add(getParagraph(countryText, mF12));
            document.add(new Paragraph("\n"));

            List<PdfPCell> val1 = new ArrayList<>();
            TableWrapper tableWrapper = new TableWrapper(new HashMap<String, String>());
            val1.add(getPdfCell(getParagraph("Product Price", mFBold14, PDF_ALIGNMENT_RIGHT)));
            val1.add(getPdfCell(getParagraph("Product Quantity", mFBold14, PDF_ALIGNMENT_RIGHT)));
            val1.add(getPdfCell(getParagraph("Product Name", mFBold14)));
            tableWrapper.addRow(val1);
            for (Product pro : orderEntity.getCartData().getProducts()) {
                List<PdfPCell> val = new ArrayList<>();
                if (pro.getSpecialPrice().isEmpty())
                    val.add(getPdfCell(getParagraph(pro.getFormattedPrice(), mF12, PDF_ALIGNMENT_RIGHT)));
                else
                    val.add(getPdfCell(getParagraph(pro.getFormattedSpecialPrice(), mF12, PDF_ALIGNMENT_RIGHT)));
                val.add(getPdfCell(getParagraph(pro.getCartQty(), mF12, PDF_ALIGNMENT_RIGHT)));
                val.add(getPdfCell(getParagraph(pro.getProductName(), mF12)));
                tableWrapper.addRow(val);
            }
            val1 = new ArrayList<>();

            Log.d(TAG, "fatchInvoiceList: " + orderEntity.getCartData().getTotals().getFormatedSubTotal());
            val1.add(getPdfCell(getParagraph(orderEntity.getCartData().getTotals().getFormatedSubTotal(), mF12, PDF_ALIGNMENT_RIGHT)));
            PdfPCell cell = getPdfCell(getParagraph("Subtotal", mFBold12, PDF_ALIGNMENT_RIGHT));
            cell.setColspan(2);
            val1.add(cell);
            tableWrapper.addRow(val1);

            val1 = new ArrayList<>();
            val1.add(getPdfCell(getParagraph(orderEntity.getCartData().getTotals().getFormatedTax(), mF12, PDF_ALIGNMENT_RIGHT)));
            cell = getPdfCell(getParagraph("Tax", mFBold12, PDF_ALIGNMENT_RIGHT));
            cell.setColspan(2);
            val1.add(cell);
            tableWrapper.addRow(val1);

            val1 = new ArrayList<>();
            val1.add(getPdfCell(getParagraph(orderEntity.getCartData().getTotals().getFormatedDiscount(), mF12, PDF_ALIGNMENT_RIGHT)));
            cell = getPdfCell(getParagraph("Discount", mFBold12, PDF_ALIGNMENT_RIGHT));
            cell.setColspan(2);
            val1.add(cell);
            tableWrapper.addRow(val1);

            val1 = new ArrayList<>();
            val1.add(getPdfCell(getParagraph(orderEntity.getCartData().getTotals().getFormatedGrandTotal(), mF12, PDF_ALIGNMENT_RIGHT)));
            cell = getPdfCell(getParagraph("Grand Total", mFBold12, PDF_ALIGNMENT_RIGHT));
            cell.setColspan(2);
            val1.add(cell);
            tableWrapper.addRow(val1);

            val1 = new ArrayList<>();
            val1.add(getPdfCell(getParagraph(orderEntity.getCartData().getTotals().getFormatedRoundTotal(), mF12, PDF_ALIGNMENT_RIGHT)));
            cell = getPdfCell(getParagraph("Round Total", mFBold12, PDF_ALIGNMENT_RIGHT));
            cell.setColspan(2);
            val1.add(cell);
            tableWrapper.addRow(val1);

            PdfPTable pTable = tableWrapper.createTable();
            document.add(pTable);
            // close document
            document.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    PdfPCell getPdfCell(Paragraph elements) {
        PdfPCell pCell = new PdfPCell();
        pCell.setPadding(5);
        pCell.addElement(elements);
        return pCell;
    }

    Paragraph getParagraph(String name, Font font, int alignment) {
        Paragraph paragraph = new Paragraph(name, font);
        if (alignment == PDF_ALIGNMENT_RIGHT)
            paragraph.setAlignment(Element.ALIGN_RIGHT);
        else if (alignment == PDF_ALIGNMENT_CENTER)
            paragraph.setAlignment(Element.ALIGN_CENTER);
        else
            paragraph.setAlignment(Element.ALIGN_LEFT);
        return paragraph;
    }

    Paragraph getParagraph(String name, Font font) {
        return new Paragraph(name, font);
    }

}