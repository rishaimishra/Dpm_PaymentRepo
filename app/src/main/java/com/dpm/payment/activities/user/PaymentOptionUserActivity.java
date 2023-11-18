package com.dpm.payment.activities.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dpm.payment.utils.PrefUtil;
import com.payment.R;

public class PaymentOptionUserActivity extends AppCompatActivity {

    ImageView ivPayPal, ivBank, ivMobileMoney;
    Context mContext;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option_user);
        mContext = this;
        setUI();
        setOnclick();
    }

    private void setOnclick() {
        ivPayPal.setOnClickListener(view -> CallWebView());
        ivBank.setOnClickListener(view -> startActivity(new Intent(PaymentOptionUserActivity.this, BankListActivity.class)));
        ivMobileMoney.setOnClickListener(view -> startActivity(new Intent(PaymentOptionUserActivity.this, MobileWalletActivity.class)));
        ivBack.setOnClickListener(view -> finish());
    }

    private void CallWebView() {
        try {



                   /* String url = RestApiUrl.URL_LANDLORD_PAYMENT + "property_id=" + searchResponseModel.getId() + "&amount=" + activityUserSearchResult_tvtext_total_amount.getText().toString().trim().split(" ")[0] + "&mobile_number=" + URLEncoder.encode(mLandlordUserModel.getUser().getMobile(), "UTF-8")+"&payee_name="+activityUserSearchResult_et_payee1.getText().toString().trim();
                    Intent mIntent = new Intent(mContext, WebViewPaymentActivity.class);
                    mIntent.putExtra(WebViewPaymentActivity.KEY_PAYMENT_URL, url);
                    startActivity(mIntent);*/

            String url = PrefUtil.getPaymentUrl(mContext);

            Intent mIntent = new Intent(mContext, WebViewPaymentActivity.class);
            mIntent.putExtra(WebViewPaymentActivity.KEY_PAYMENT_URL, url);
            startActivity(mIntent);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setUI() {
        ivPayPal = findViewById(R.id.ivPayPal);
        ivBank = findViewById(R.id.ivBank);
        ivMobileMoney = findViewById(R.id.ivMobileMoney);
        ivBack= findViewById(R.id.ivBack);

    }

}
