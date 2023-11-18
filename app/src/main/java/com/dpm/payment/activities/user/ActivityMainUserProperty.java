package com.dpm.payment.activities.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.dpm.payment.activities.cashier.ActivityCashierLogin;
import com.dpm.payment.models.SearchPropertyModel;
import com.dpm.payment.models.propertydetail.PropertyItem;
import com.dpm.payment.utils.CommonUtils;
import com.dpm.payment.utils.Helper;
import com.dpm.payment.utils.LogUtils;
import com.dpm.payment.utils.PrefUtil;
import com.dpm.payment.utils.RestApiUrl;
import com.dpm.payment.utils.StringUtils;
import com.google.gson.Gson;
import com.payment.R;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Locale;


public class ActivityMainUserProperty extends AppCompatActivity implements View.OnClickListener {


    final static String KEY_FROM = "form";
    final static String VALUE_FROM_LANDLORD = "ActivityMainUserProperty";
    public static String KEY_PROPERTY_DETAILS = "property_details";
    static SearchPropertyModel searchResponseModel;
    Context mContext;
    AlertDialog dialog;
    View activityMain_view_search;
    TextView toolbar_tv_header;
    TextView activityUserSearchResult_tv_name;
    Button activityUserSearchResult_bt_view_details;
    TextView activityUserSearchResult_tv_assesment_year, activityUserSearchResult_tv_assesment_year_value, activityUserSearchResult_tv_rate_payable_value, activityUserSearchResult_tv_discount_applicable_value, activityUserSearchResult_tv_arrear, activityUserSearchResult_tv_arrear_value,
            activityUserSearchResult_tv_penalty, activityUserSearchResult_tv_penalty_value, activityUserSearchResult_tv_amount_paid, activityUserSearchResult_tv_amount_paid_value, activityUserSearchResult_tv_balance,
            activityUserSearchResult_tv_balance_value, activityUserSearchResult_tv_paying_amount, activityUserSearchResult_tv_paying_pre_calculate, activityUserSearchResult_tv_discount_rate_payable_value,activityUserSearchResult_tv_net_assessed_value, activityUserSearchResult_tv_council_adjustment_value;
    EditText activityUserSearchResult_et_paying_amount;
    TextView activityUserSearchResult_tvtext_total_amount;
    TextView activityUserSearchResult_tv_payment_type;
    RadioGroup activityUserSearchResult_rg_payment_type;
    AppCompatRadioButton activityUserSearchResult_rb_one, activityUserSearchResult_rb_two;
    TextView activityUserSearchResult_tv_total_amount, activityUserSearchResult_tv_value_le_to_usd;
    EditText activityUserSearchResult_et_total_amount;
    TextView activityUserSearchResult_tv_payee_name;
    EditText activityUserSearchResult_et_payee_name;
    RecyclerView activityUserSearchResult_rv_dates;
    Button activityOrderDetails_bt_save;
    LandlordResponseModel mLandlordUserModel;
    EditText activityUserSearchResult_et_payee1;
    TextView tvInputAmountl;
    TextView tvLeToPound;
    TextView tvGDPLevel;
    ImageView btBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_property);
        initializeViews();
        initializeListeners();
        setSearchResult();
    }

    private void setSearchResult() {
        try {
            String mPropertyDetails = this.getIntent().getStringExtra(KEY_PROPERTY_DETAILS);


            LogUtils.showErrorLog("Search Result ", "Search Result " + mPropertyDetails);
            if (mPropertyDetails != null) {
                PrefUtil.saveSearchLandlordRequest(mContext, mPropertyDetails.trim());
                searchResponseModel = (SearchPropertyModel) CommonUtils.getObjectFromJson(mPropertyDetails, SearchPropertyModel.class);
                if (searchResponseModel != null) {
                    setData(searchResponseModel);

                }
            }

            try {
                mLandlordUserModel = PrefUtil.getLandlordProfile(mContext);
                String mLeValue = mLandlordUserModel.getCurrency_rate().getLe();
                String mUSDValue = mLandlordUserModel.getCurrency_rate().getUsd();
                activityUserSearchResult_tv_value_le_to_usd.setText("Le " + StringUtils.AmountWithComma(mLeValue) + " = " + StringUtils.AmountWithComma(mUSDValue) + " USD");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                String mLePValue = mLandlordUserModel.getCurrency_rate_pound().getLe();
                String mGDPValue = mLandlordUserModel.getCurrency_rate_pound().getPound();
                tvLeToPound.setText("Le " + StringUtils.AmountWithComma(mLePValue) + " = " + StringUtils.AmountWithComma(mGDPValue) + " GDP");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void initializeViews() {

        toolbar_tv_header = findViewById(R.id.toolbar_tv_header);
        activityMain_view_search = findViewById(R.id.activityMain_view_search);
        activityUserSearchResult_tv_name = findViewById(R.id.activityUserSearchResult_tv_name);
        activityUserSearchResult_bt_view_details = findViewById(R.id.activityUserSearchResult_bt_view_details);
        activityUserSearchResult_tv_assesment_year = findViewById(R.id.activityUserSearchResult_tv_assesment_year);
        activityUserSearchResult_tv_assesment_year_value = findViewById(R.id.activityUserSearchResult_tv_assesment_year_value);
        activityUserSearchResult_tv_discount_applicable_value = findViewById(R.id.activityUserSearchResult_tv_discount_applicable_value);
        activityUserSearchResult_tv_rate_payable_value = findViewById(R.id.activityUserSearchResult_tv_rate_payable_value);
        activityUserSearchResult_tv_arrear = findViewById(R.id.activityUserSearchResult_tv_arrear);
        activityUserSearchResult_tv_arrear_value = findViewById(R.id.activityUserSearchResult_tv_arrear_value);
        activityUserSearchResult_tv_penalty = findViewById(R.id.activityUserSearchResult_tv_penalty);
        activityUserSearchResult_tv_penalty_value = findViewById(R.id.activityUserSearchResult_tv_penalty_value);
        activityUserSearchResult_tv_amount_paid = findViewById(R.id.activityUserSearchResult_tv_amount_paid);
        activityUserSearchResult_tv_amount_paid_value = findViewById(R.id.activityUserSearchResult_tv_amount_paid_value);
        activityUserSearchResult_tv_balance = findViewById(R.id.activityUserSearchResult_tv_balance);
        activityUserSearchResult_tv_balance_value = findViewById(R.id.activityUserSearchResult_tv_balance_value);
        activityUserSearchResult_tv_paying_amount = findViewById(R.id.activityUserSearchResult_tv_paying_amount);
        activityUserSearchResult_tv_paying_pre_calculate = findViewById(R.id.activityUserSearchResult_tv_paying_pre_calculate);
        activityUserSearchResult_et_paying_amount = findViewById(R.id.activityUserSearchResult_et_paying_amount);

        activityUserSearchResult_tv_discount_rate_payable_value = findViewById(R.id.activityUserSearchResult_tv_discount_rate_payable_value);
        activityUserSearchResult_tv_net_assessed_value = findViewById(R.id.activityUserSearchResult_tv_net_assessed_value);
        activityUserSearchResult_tv_council_adjustment_value = findViewById(R.id.activityUserSearchResult_tv_council_adjustment_value);


        activityUserSearchResult_tv_payment_type = findViewById(R.id.activityUserSearchResult_tv_payment_type);
        activityUserSearchResult_rg_payment_type = findViewById(R.id.activityUserSearchResult_rg_payment_type);
        activityUserSearchResult_rb_one = findViewById(R.id.activityUserSearchResult_rb_one);
        activityUserSearchResult_rb_two = findViewById(R.id.activityUserSearchResult_rb_two);
        activityUserSearchResult_tv_total_amount = findViewById(R.id.activityUserSearchResult_tv_total_amount);
        activityUserSearchResult_tv_value_le_to_usd = findViewById(R.id.activityUserSearchResult_tv_value_le_to_usd);


        activityUserSearchResult_tvtext_total_amount = findViewById(R.id.activityUserSearchResult_tvtext_total_amount);


        activityUserSearchResult_tv_payee_name = findViewById(R.id.activityUserSearchResult_tv_payee_name);
        activityUserSearchResult_et_payee_name = findViewById(R.id.activityUserSearchResult_et_payee_name);
        activityUserSearchResult_rv_dates = findViewById(R.id.activityUserSearchResult_rv_dates);
        activityOrderDetails_bt_save = findViewById(R.id.activityOrderDetails_bt_save);


        activityUserSearchResult_et_payee1 = findViewById(R.id.activityUserSearchResult_et_payee1);

        tvInputAmountl = findViewById(R.id.tvInputAmount);
        tvLeToPound = findViewById(R.id.tvLeToPound);
        tvGDPLevel = findViewById(R.id.tvLeToPound);
        btBack = findViewById(R.id.btBack);


    }

    private void initializeListeners() {

        activityUserSearchResult_bt_view_details.setOnClickListener(this);
        activityOrderDetails_bt_save.setOnClickListener(this);
        btBack.setOnClickListener(view -> finish());

        activityUserSearchResult_et_paying_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String mUsdStr = "";

                mUsdStr = activityUserSearchResult_et_paying_amount.getText().toString().trim();

                try {

                    if (activityUserSearchResult_et_paying_amount.getText().toString().trim().length() > 0) {
                        tvInputAmountl.setText("Le " + SetCommaText(activityUserSearchResult_et_paying_amount.getText().toString().trim()));
                        LogUtils.showErrorLog("Enter String ", " Enter String 1 " + mUsdStr);
                    } else {
                        tvInputAmountl.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    //---------------------------------------- to show usd amount --------------//
                    LogUtils.showErrorLog("Enter String ", " Enter String after Replace  " + mUsdStr);
                    Double mDouble = Double.parseDouble(mUsdStr);
                    LogUtils.showErrorLog("Enter mDouble ", "mDouble " + mDouble);
                  /*  mLandlordUserModel = PrefUtil.getLandlordProfile(mContext);
                    LogUtils.showErrorLog("Enter mLandlordUserModel  ", "Enter mLandlordUserModel  " + PrefUtil.getJsonFromObject(mLandlordUserModel,LandlordResponseModel.class));
                    LogUtils.showErrorLog("Enter mDouble ", "mDouble " + mDouble);*/
                    String mLandLordProfile = PrefUtil.getLandlordProfileString(mContext);
                    JSONObject mLandLordProfileJson = new JSONObject(mLandLordProfile);
                    String mUSDValue = mLandLordProfileJson.getJSONObject("currency_rate").optString("usd");
                    String mLeValue = mLandLordProfileJson.getJSONObject("currency_rate").optString("le");
                    LogUtils.showErrorLog("Enter mLeValue ", "mLeValue " + mLeValue);
                    LogUtils.showErrorLog("Enter mUSDValue ", "mUSDValue " + mUSDValue);
                    Double dresult = (mDouble * Double.parseDouble(mUSDValue)) / Double.parseDouble(mLeValue);

                    LogUtils.showErrorLog("dresult ", "dresult " + dresult);
                    String result = String.format("%.2f", dresult);
                    activityUserSearchResult_tvtext_total_amount.setText(result + " USD");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                try {
                    //---------------------------------------- to show amount GBP  --------------//
                    LogUtils.showErrorLog("Enter String ", " Enter String after Replace  " + mUsdStr);
                    Double mDouble = Double.parseDouble(mUsdStr);
                    LogUtils.showErrorLog("Enter mDouble ", "mDouble " + mDouble);
                    String mLandLordProfile = PrefUtil.getLandlordProfileString(mContext);
                    JSONObject mLandLordProfileJson = new JSONObject(mLandLordProfile);
                    String mGBPValue = mLandLordProfileJson.getJSONObject("currency_rate_pound").optString("pound");
                    String mLeValue = mLandLordProfileJson.getJSONObject("currency_rate_pound").optString("le");
                    LogUtils.showErrorLog("Enter mLeValue ", "mGDPValue " + mLeValue);
                    LogUtils.showErrorLog("Enter mGDPValue ", "mGDPValue " + mGBPValue);
                    Double dresult = (mDouble * Double.parseDouble(mGBPValue)) / Double.parseDouble(mLeValue);
                    LogUtils.showErrorLog("dresult ", "dresult " + dresult);
                    String result = String.format("%.2f", dresult);
                    tvLeToPound.setText(result + " GBP");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


    }

    private String SetCommaText(String s) {

        String temp = "";
        try {
            BigInteger bigIntegerStr = new BigInteger(s);
            NumberFormat nf = NumberFormat.getInstance(Locale.US);
            temp = nf.format(bigIntegerStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
    }


    /*  @Override
      public void onBackPressed() {

          showLogoutDialog(mContext);
      }*/
    public void showLogoutDialog(Context mCtx) {
        try {
            AlertDialog mAlertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(mCtx, R.style.AlertDialogTheme);
            // Set a title for alert dialog
            builder.setTitle("Alert?");

            builder.setCancelable(false);

            // Ask the final question
            builder.setMessage("" + mCtx.getResources().getString(R.string.do_you_want_to_logout));

            // Set the alert dialog yes button click listener
            builder.setPositiveButton("Ok", (dialog, which) -> {
                // Do something when user clicked the Yes button
                // Set the TextView visibility GONE
                dialog.dismiss();

                PrefUtil.mClearALLData(mContext);

                Intent i = new Intent(ActivityMainUserProperty.this, ActivityCashierLogin.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

            });

            // Set the alert dialog no button click listener
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Do something when No button clicked
                if (dialog != null) {
                    dialog.dismiss();
                }
            });

            mAlertDialog = builder.create();
            // Display the alert dialog on interface
            mAlertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        // Set a title for alert dialog
        builder.setTitle("Logout?");

        // Ask the final question
        builder.setMessage("Are you sure you want to logout?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Do something when user clicked the Yes button
            // Set the TextView visibility GONE
            Intent in = new Intent(mContext, ActivityCashierLogin.class);
            startActivity(in);
            finish();
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do something when No button clicked
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.activityOrderDetails_bt_save:
                try {

                    if (activityUserSearchResult_et_paying_amount.getText().toString().trim().length() == 0) {
                        Toast.makeText(mContext, "Enter paying amount", Toast.LENGTH_LONG).show();
                        return;

                    }
                    LogUtils.showErrorLog("TAG", "TAG  Line 1 ");

                    try {
                        String inputNumber = activityUserSearchResult_et_paying_amount.getText().toString();
                        Integer intNumber = Integer.parseInt(inputNumber);
                        if (intNumber < 10000) {
                            Toast.makeText(mContext, "Enter amount minimum Le 10000.", Toast.LENGTH_LONG).show();
                            return;
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    LogUtils.showErrorLog("TAG", "TAG  Line 2 ");
                    if (activityUserSearchResult_et_payee1.getText().toString().trim().length() == 0) {
                        Toast.makeText(mContext, "Enter paying name", Toast.LENGTH_LONG).show();
                        return;
                    }

                    LogUtils.showErrorLog("TAG", "TAG  Line 2 ");

                    String mLandlordProfile = PrefUtil.getLandlordProfileString(mContext);

                    JSONObject mMainObject = new JSONObject(mLandlordProfile);

                    String mobileNumber = mMainObject.getJSONObject("user").optString("mobile");

                    String url = RestApiUrl.URL_LANDLORD_PAYMENT + "property_id=" + searchResponseModel.getId() + "&amount=" + activityUserSearchResult_tvtext_total_amount.getText().toString().trim().split(" ")[0] + "&mobile_number=" + URLEncoder.encode(mobileNumber, "UTF-8") + "&payee_name=" + activityUserSearchResult_et_payee1.getText().toString().trim();
                    PrefUtil.savePayment(mContext, url);

                    Intent mIntent = new Intent(mContext, PaymentOptionUserActivity.class);
                    mIntent.putExtra(WebViewPaymentActivity.KEY_PAYMENT_URL, url);
                    startActivity(mIntent);
                    /*Intent mIntent = new Intent(mContext, WebViewPaymentActivity.class);
                    mIntent.putExtra(WebViewPaymentActivity.KEY_PAYMENT_URL, url);
                    startActivity(mIntent);*/

                   /* //  String url  = https://dpm.sigmaventuressl.com/paypal?property_id=9817&amount=10&mobile_number=%2B919982653053
                    String url = RestApiUrl.URL_LANDLORD_PAYMENT + "property_id=" + searchResponseModel.getId() + "&amount=" + activityUserSearchResult_et_paying_amount.getText().toString().trim() + "&mobile_number=" +URLEncoder.encode(mLandlordUserModel.getUser().getMobile(),"UTF-8");

                    LogUtils.showErrorLog("Final URL "," URL "+url);
                    LogUtils.showErrorLog("Final URL "," URL "+mLandlordUserModel.getUser().getMobile());
                    LogUtils.showErrorLog("Final URL "," URL "+mLandlordUserModel.getUser().toString());
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);*/


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                break;
            case R.id.activityMain_bt_search:

                break;
            case R.id.activityUserSearchResult_bt_view_details:
                intent = new Intent(mContext, ActivityUserMainDetails.class);
                intent.putExtra(KEY_FROM, VALUE_FROM_LANDLORD);
                intent.putExtra(KEY_PROPERTY_DETAILS + "1", getIntent().getStringExtra(KEY_PROPERTY_DETAILS + "1"));
                intent.putExtra("pensioner_image_path", getIntent().getStringExtra("pensioner_image_path"));
                intent.putExtra("disability_image_path", getIntent().getStringExtra("disability_image_path"));
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }


    private void setData(SearchPropertyModel mSearchPropertyModel) {


        try {
            if (mSearchPropertyModel.getIsOrganization()) {

                String mOrganizationName = ((searchResponseModel.getOrganizationName() == null) ? "" : "" + searchResponseModel.getOrganizationName());
                activityUserSearchResult_tv_name.setText("" + mOrganizationName);

            } else {

                String title = ((searchResponseModel.getLandlord().getTitles().getLabel() == null) ? "" : searchResponseModel.getLandlord().getTitles().getLabel());
                String mFirstName = ((searchResponseModel.getLandlord().getFirstName() == null) ? "" : searchResponseModel.getLandlord().getFirstName());
                String lastName = ((searchResponseModel.getLandlord().getMiddleName() == null) ? "" : searchResponseModel.getLandlord().getMiddleName());
                String surName = ((searchResponseModel.getLandlord().getSurname() == null) ? "" : searchResponseModel.getLandlord().getSurname());
                activityUserSearchResult_tv_name.setText(title + " " + mFirstName + " " + lastName + " " + surName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }



        activityUserSearchResult_tv_assesment_year.setText("Assessed Value 2022");
        activityUserSearchResult_tv_discount_rate_payable_value.setText(StringUtils.AmountWithComma(StringUtils.roundStringValue("" + new BigDecimal(mSearchPropertyModel.getAssessment().getDiscounted_rate_payable_2022()))));
        activityUserSearchResult_tv_assesment_year_value.setText(StringUtils.AmountWithComma(StringUtils.roundStringValue("" + new BigDecimal(mSearchPropertyModel.getAssessment().getPropertyRateWithoutGst()))));
        activityUserSearchResult_tv_council_adjustment_value.setText(StringUtils.AmountWithComma(StringUtils.roundStringValue("" + new BigDecimal(mSearchPropertyModel.getAssessment().getCouncil_adjustments_parameters()))));
        activityUserSearchResult_tv_net_assessed_value.setText(mSearchPropertyModel.getAssessment().getProperty_net_assessed_vaue());


        try {
            activityUserSearchResult_tv_discount_applicable_value.setText(StringUtils.AmountWithComma(StringUtils.roundStringValue("" + new BigDecimal(mSearchPropertyModel.getAssessment().getDiscounted_value_new()))));
        } catch (Exception ignored) {

        }
        try {
            activityUserSearchResult_tv_rate_payable_value.setText(StringUtils.AmountWithComma(StringUtils.roundStringValue("" + new BigDecimal(mSearchPropertyModel.getAssessment().getRate_payable_new()))));

        } catch (Exception ignored) {

        }

        Helper.ASSESSED_VALUE = mSearchPropertyModel.getAssessment().getProperty_net_assessed_vaue();
        Helper.DISCOUNT_APPLICABLE = mSearchPropertyModel.getAssessment().getRate_payable();
        Helper.RATE_PAYABLE = mSearchPropertyModel.getAssessment().getRate_payable();


        activityUserSearchResult_tv_arrear_value.setText(StringUtils.AmountWithComma(StringUtils.roundStringValue("" + new BigDecimal(mSearchPropertyModel.getAssessment().getArrearDue()))));
        activityUserSearchResult_tv_penalty_value.setText(StringUtils.AmountWithComma(StringUtils.roundStringValue("" + new BigDecimal(mSearchPropertyModel.getAssessment().getPenalty()))));
        activityUserSearchResult_tv_amount_paid.setText("Amount Paid (" + mSearchPropertyModel.getAssessment().getAssessmentYear() + ")");
        activityUserSearchResult_tv_amount_paid_value.setText(StringUtils.AmountWithComma(StringUtils.roundStringValue("" + new BigDecimal(mSearchPropertyModel.getAssessment().getAmountPaid()))));

        try {
            String mBalance = "";
            if (mSearchPropertyModel.getAssessment().getBalance().contains("E")) {
                mBalance = StringUtils.AmountWithComma(StringUtils.roundStringValue("" + new BigDecimal(mSearchPropertyModel.getAssessment().getNew_balance_due())));
            } else {
                mBalance = StringUtils.AmountWithComma(StringUtils.roundStringValue(mSearchPropertyModel.getAssessment().getNew_balance_due()));
            }

            activityUserSearchResult_tv_balance_value.setText("" + mBalance);
            activityUserSearchResult_tv_paying_pre_calculate.setText(getString(R.string.amount_due) + "\n" + "Le " + mBalance);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
