package com.dpm.payment.activities.user;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dpm.payment.activities.cashier.ActivityCashierLogin;
import com.dpm.payment.utils.AlertDialogUtils;
import com.dpm.payment.utils.DataUtils;
import com.dpm.payment.utils.LogUtils;
import com.dpm.payment.utils.PrefUtil;
import com.dpm.payment.utils.RestApiRequestListener;
import com.dpm.payment.utils.RestApiUrl;
import com.payment.R;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.dpm.payment.utils.ConstantData.TAG_REQUEST_LOGIN;


public class ActivityUserLogin extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;


    private EditText etPhone, etISDPhone;
    private Button btSendOTP;
    private TextView tvCashier;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,ActivityCashierLogin.class));
        finish();
        setContentView(R.layout.activity_user_login);


        check_permissions();

        initializeViews();
        initializeListener();

    }

    public boolean check_permissions() {

        String[] PERMISSIONS = {
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS
        };

        if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS, 2);
            }
        }else {

            return true;
        }

        return false;
    }

    private void initializeListener() {
        btSendOTP.setOnClickListener(this);
        tvCashier.setOnClickListener(this);
    }


    private void initializeViews() {


        tvCashier = findViewById(R.id.tvCashier);
        etPhone = findViewById(R.id.etPhone);
        btSendOTP = findViewById(R.id.btSendOTP);



        etISDPhone = findViewById(R.id.etISDPhone);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_iv_home:
                onBackPressed();
                break;

            case R.id.btSendOTP:

             // startActivity(new Intent(this,ActivityUserOTP.class));
               try {

                    PrefUtil.mClearALLData(mContext);

                    if (checkLogInValidation()) {

                        if (DataUtils.isInternetConnectAvailable(mContext)) {
                            reqNormalLogin();
                        } else {
                            AlertDialogUtils.showInternetConnNotAvailableDialog(ActivityUserLogin.this);
                        }
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }


                break;
            case R.id.activityOtp_btVerify:
               // content_otp.setVisibility(View.VISIBLE);
                break;
            case R.id.tvCashier:
                Intent intent2 = new Intent(mContext, ActivityCashierLogin.class);
                startActivity(intent2);
                finish();
                break;

        }
    }

    ProgressDialog progressDialog;
    public void reqNormalLogin() {

        progressDialog = new ProgressDialog(mContext);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        Map<String, String> req_params = new HashMap<>();

        req_params.put("mobile_number", "+"+etISDPhone.getText().toString().trim()+""+etPhone.getText().toString().trim());


        new RestApiRequestListener(this, TAG_REQUEST_LOGIN, RestApiUrl.URL_LANDLORD_LOGIN, headers, req_params, new RestApiRequestListener.setOnRequestListener() {


            @Override
            public void onPreExecute() {
                progressDialog.setMessage("Signing In ...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }



            @Override
            public void onSuccessListener(String response) {

                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                parseLogInResponse(response);

            }

            @Override
            public void onErrorListener(String errorMessage) {
                try {
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LogUtils.showErrorLog("reqNormalLogin", errorMessage);

            }
        }).request();


    }


    private void parseLogInResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            LogUtils.showErrorLog("Response ","Response "+jsonObject.toString());

            if (jsonObject.optBoolean("success")) {

                //Profile object save===========
                try {
                    JSONObject profileJsn = jsonObject.optJSONObject("user");

                    if (profileJsn != null) {

                        Toast.makeText(mContext, profileJsn.optString("code"), Toast.LENGTH_LONG).show();


                        PrefUtil.saveLandlordProfile(mContext, response);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                   startActivity(new Intent(ActivityUserLogin.this,ActivityUserOTP.class).putExtra("data",response));

            } else {
                Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, ""+ActivityUserLogin.this.getResources().getString(R.string.error_occur), Toast.LENGTH_SHORT).show();
        }


    }


    private boolean checkLogInValidation() {

        boolean b;
        ArrayList<String> errorList = new ArrayList<>();


        if (etISDPhone.getText().toString().trim().length() == 0) {
            errorList.add("Enter ISD number");
        }

        if (etPhone.getText().toString().trim().length() == 0) {
            errorList.add("Enter registered  number.");
        }
        if (errorList.size() > 0) {

            Toast.makeText(mContext, errorList.get(0), Toast.LENGTH_SHORT).show();
            b = false;
        } else {
            b = true;
        }

        return b;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
