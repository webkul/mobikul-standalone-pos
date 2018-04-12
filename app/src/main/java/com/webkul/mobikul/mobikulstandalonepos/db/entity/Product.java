package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.webkul.mobikul.mobikulstandalonepos.BR;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.converters.DataConverter;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.TAG;
import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.getContext;
import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.SUCCESS_MSG_10_SKU_ALLREADY_EXIST;
import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.SUCCESS_MSG_9_CUSTOMER_ALL_READY_EXIST;

/**
 * Created by aman.gupta on 9/1/18.
 */

@Entity(tableName = "Product")
public class Product extends BaseObservable implements Serializable, Parcelable {
    @Ignore
    private Context context;
    @Ignore
    private boolean isSkuExist;

    public Product() {
    }

    public Product(Context context) {
        this.context = context;
    }

    @PrimaryKey(autoGenerate = true)
    private int pId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "product_s_deccription")
    private String productShortDes;

    @ColumnInfo(name = "sku")
    private String sku;

    @ColumnInfo(name = "is_enabled")
    private boolean isEnabled;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "formatted_price")
    private String formattedPrice;

    @ColumnInfo(name = "special_price")
    private String specialPrice;

    @ColumnInfo(name = "formatted_special_price")
    private String formattedSpecialPrice;

    @ColumnInfo(name = "is_taxable_goods_applied")
    private boolean isTaxableGoodsApplied;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "product_tax")
    private Tax productTax;

    @ColumnInfo(name = "track_inventory")
    private boolean trackInventory;

    @ColumnInfo(name = "quantity")
    private String quantity;

    @ColumnInfo(name = "stock_availability")
    private boolean stock;

    @ColumnInfo(name = "discount")
    private float discount;

    @ColumnInfo(name = "formatted_discount")
    private String formattedDiscount;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "weight")
    private String weight;

    @ColumnInfo(name = "barCode")
    private String barCode;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "productCategories")
    private List<ProductCategoryModel> productCategories;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "options")
    private List<Options> options;
    @Ignore
    private String cartQty;
    @Ignore
    private String cartProductSubtotal;
    @Ignore
    private String formattedCartProductSubtotal;
    @Ignore
    private boolean displayError;

    protected Product(Parcel in) {
        pId = in.readInt();
        productName = in.readString();
        productShortDes = in.readString();
        sku = in.readString();
        isEnabled = in.readByte() != 0;
        price = in.readString();
        formattedPrice = in.readString();
        specialPrice = in.readString();
        formattedSpecialPrice = in.readString();
        isTaxableGoodsApplied = in.readByte() != 0;
        trackInventory = in.readByte() != 0;
        quantity = in.readString();
        stock = in.readByte() != 0;
        image = in.readString();
        weight = in.readString();
        barCode = in.readString();
        productCategories = in.createTypedArrayList(ProductCategoryModel.CREATOR);
        cartQty = in.readString();
        cartProductSubtotal = in.readString();
        displayError = in.readByte() != 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getPId() {
        return pId;
    }

    public void setPId(int pId) {
        this.pId = pId;
    }

    @Bindable
    public String getProductName() {
        if (productName == null)
            return "";
        return productName;
    }

    @Bindable({"displayError", "productName"})
    public String getProductNameError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getProductName().isEmpty()) {
            return "PRODUCT NAME IS EMPTY!";
        }
        return "";
    }

    public void setProductName(String productName) {
        this.productName = productName;
        notifyPropertyChanged(BR.productName);
    }

    public String getProductShortDes() {
        return productShortDes;
    }

    public void setProductShortDes(String productShortDes) {
        this.productShortDes = productShortDes;
    }

    @Bindable
    public String getSku() {
        if (sku == null)
            return "";
        else if (!sku.isEmpty()) {
            Log.d(TAG, "getsku: " + sku);
            DataBaseController.getInstanse().checkSkuExist(getContext(), sku, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    if (responseData != null && ((Product) responseData).getPId() != getPId()) {
                        isSkuExist = true;
                    } else {
                        isSkuExist = false;
                    }
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    isSkuExist = false;
                }
            });
        }
        return sku;
    }

    @Bindable({"displayError", "sku"})
    public String getSkuError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getSku().isEmpty()) {
            return "SKU IS EMPTY!";
        }
        if (isSkuExist) {
            return SUCCESS_MSG_10_SKU_ALLREADY_EXIST;
        }
        return "";
    }

    public void setSku(String sku) {
        this.sku = sku;
        notifyPropertyChanged(BR.sku);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Bindable
    public String getPrice() {
        if (price == null)
            return "";
        return price;
    }

    @Bindable({"displayError", "price"})
    public String getPriceError() {
        if (!isDisplayError()) {
            return "";
        }
        String price = getPrice() + "";
        if (price.isEmpty()) {
            return "PRICE IS EMPTY!";
        }
        return "";
    }


    public void setPrice(String price) {
        this.price = price;
        if (context != null)
            setFormattedPrice(context.getString(R.string.currency_symbol) + price);
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public String getSpecialPrice() {
        if (specialPrice == null)
            return "";
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        if (context != null)
            setFormattedSpecialPrice(context.getString(R.string.currency_symbol) + specialPrice);
        this.specialPrice = specialPrice;
        notifyPropertyChanged(BR.specialPrice);
    }

    public boolean isTaxableGoodsApplied() {
        return isTaxableGoodsApplied;
    }

    public void setTaxableGoodsApplied(boolean taxableGoodsApplied) {
        isTaxableGoodsApplied = taxableGoodsApplied;
    }

    public boolean isTrackInventory() {
        return trackInventory;
    }

    public void setTrackInventory(boolean trackInventory) {
        this.trackInventory = trackInventory;
    }

    @Bindable
    public String getQuantity() {
        if (quantity == null)
            return "";
        return quantity;
    }

    @Bindable({"displayError", "quantity"})
    public String getQuantityError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getQuantity().isEmpty()) {
            return "QUANTITY IS EMPTY!";
        }
        return "";
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantity);
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    @Bindable

    public String getImage() {
        if (image == null)
            return "";
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    @Bindable
    public String getWeight() {
        if (weight == null)
            return "";
        return weight;
    }

    @Bindable({"displayError", "weight"})
    public String getWeightError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getWeight().isEmpty()) {
            return "WEIGHT IS EMPTY!";
        }
        return "";
    }

    public void setWeight(String weight) {
        this.weight = weight;
        notifyPropertyChanged(BR.weight);
    }

    @Bindable
    public boolean isDisplayError() {
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

    public List<ProductCategoryModel> getProductCategories() {
        if (productCategories == null)
            return productCategories = new ArrayList<>();
        return productCategories;
    }

    public void setProductCategories(List<ProductCategoryModel> productCategories) {
        this.productCategories = productCategories;
    }

    @Bindable
    public String getCartQty() {
        if (cartQty == null)
            return "0";
        return cartQty;
    }

    public void setCartQty(String cartQty) {
        this.cartQty = cartQty;
        notifyPropertyChanged(BR.cartQty);
    }

    public String getFormattedPrice() {
        if (formattedPrice == null)
            return "";
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getFormattedSpecialPrice() {
        if (formattedSpecialPrice == null)
            return "";
        return formattedSpecialPrice;
    }

    public void setFormattedSpecialPrice(String formattedSpecialPrice) {
        this.formattedSpecialPrice = formattedSpecialPrice;
    }

    @Bindable
    public String getBarCode() {
        if (barCode == null)
            return "";
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
        notifyPropertyChanged(BR.barCode);
    }

    public List<Options> getOptions() {
        if (options == null)
            options = new ArrayList<>();
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

    @Bindable
    public String getCartProductSubtotal() {
        return cartProductSubtotal;
    }

    public void setCartProductSubtotal(String cartProductSubtotal) {
        this.cartProductSubtotal = cartProductSubtotal;
        notifyPropertyChanged(BR.cartProductSubtotal);
    }

    @Bindable
    public Tax getProductTax() {
        return productTax;
    }

    public void setProductTax(Tax productTax) {
        this.productTax = productTax;
        notifyPropertyChanged(BR.productTax);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(pId);
        parcel.writeString(productName);
        parcel.writeString(productShortDes);
        parcel.writeString(sku);
        parcel.writeByte((byte) (isEnabled ? 1 : 0));
        parcel.writeString(price);
        parcel.writeString(formattedPrice);
        parcel.writeString(specialPrice);
        parcel.writeString(formattedSpecialPrice);
        parcel.writeByte((byte) (isTaxableGoodsApplied ? 1 : 0));
        parcel.writeByte((byte) (trackInventory ? 1 : 0));
        parcel.writeString(quantity);
        parcel.writeByte((byte) (stock ? 1 : 0));
        parcel.writeString(image);
        parcel.writeString(weight);
        parcel.writeString(barCode);
        parcel.writeTypedList(productCategories);
        parcel.writeString(cartQty);
        parcel.writeString(cartProductSubtotal);
        parcel.writeByte((byte) (displayError ? 1 : 0));
    }

    @Bindable
    public String getFormattedCartProductSubtotal() {
        return formattedCartProductSubtotal;
    }

    public void setFormattedCartProductSubtotal(String formattedCartProductSubtotal) {
        this.formattedCartProductSubtotal = formattedCartProductSubtotal;
        notifyPropertyChanged(BR.formattedCartProductSubtotal);
    }

    @Bindable
    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
        notifyPropertyChanged(BR.discount);
    }

    public String getFormattedDiscount() {
        if (formattedDiscount == null)
            return "";
        return formattedDiscount;
    }

    public void setFormattedDiscount(String formattedDiscount) {
        this.formattedDiscount = formattedDiscount;
    }
}