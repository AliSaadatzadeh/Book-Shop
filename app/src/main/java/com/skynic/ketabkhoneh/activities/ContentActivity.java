package com.skynic.ketabkhoneh.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.skynic.ketabkhoneh.R;


public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        TextView textView = findViewById(R.id.txtContent);
        TextView titleText = findViewById(R.id.txtTitle);

        titleText.setText(getIntent().getExtras().getString("TITLE"));
        String title = getIntent().getExtras().getString("TITLE");
        if (title.equals("درباره ما")) {
            textView.setText("نرم افزار كتابخونه با ايده ي به چرخه انداختن كتاب هاي بدون استفاده و حفظ محيط" +
                    "زيست در بهمن ماه 1399 در تهران شروع به كار كرد.") ;
        } else {
            textView.setText("نصب برنامه ي كتابخونه به معني پذيرش شرايط و مقررات آن توسط كاربر مي باشد.\n" +
                    "\n" +
                    "اطلاعات شخصي شما كه در اختيار كتابخونه قرار مي گيرد شامل موارد زيراست:\n" +
                    "\n" +
                    "1 .نام و نام خانوادگي، اطلاعات تماس(آدرس دقيق، كدپستي، شماره تماس)،شماره ي شبا\n" +
                    "2 .سوابق كليه ي خريدها در بستر نرم افزار\n" +
                    "3 .نظرات ثبت شده در نرم افزار\n" +
                    "4 .اطلاعات مربوط به مبادلات مالي موفق و غيرموفق\n" +
                    "\n" +
                    "مواردي كه نقض شرايط و مقررات كتابخونه مي باشد شامل موارد زير است:\n" +
                    "1 .مغايرت با قوانين جاري جمهوري اسلامي ايران\n" +
                    "2 .نقض حريم شخصي افراد\n" +
                    "3 .استفاده از اطلاعات شخصي غيرواقعي\n" +
                    "4 .استفاده از كلمات نامناسب در توضيحات كتاب يا نظرات\n" +
                    "5 .درج اطلاعات تماس در متن آگهي كتاب\n" +
                    "6 .عدم تطابق اطلاعات ارائه شده در آگهي كتاب و كالاي ارائه شده\n" +
                    "7 .كامل نبودن يا نادرست بودن اطلاعات كتاب\n" +
                    "در صورت نقض هر كدام از قوانين بالا، ابتدا آگهي حذف و در صورت تكرار، حساب كاربري فرد، غيرفعال مي شود.\n" +
                    "\n" +
                    "كتابخونه هيچ گونه مسئوليتي در قبال اطلاعاتي كه با اشخاص ثالث در خارج از بستر نرم افزار به اشتراك مي گذاريد، ندارد.\n" +
                    "\n" +
                    "كتابخونه صحت آگهي ها را تضمين نمي نمايد و هيچ گونه مسئوليتي در قبال آگهي بر عهده نمي گيرد.\n" +
                    "\n" +
                    "پس از خريد كتاب، مبلغ واريزي در حساب نرم افزار باقي مانده و پس از تاييد خريدار مبني بر دريافت كتاب، مبلغ پس از كسر كارمزد كتابخونه به شماره ي شباي فروشنده واريز مي گردد.\n" +
                    "\n" +
                    "تا زماني كه اطلاعات شماره ي شباي فروشنده كامل نباشد،مبلغ فروش در حساب كتابخونه محفوظ مي ماند.\n" +
                    "\n" +
                    "شهرهايي كه در حال حاضر در كتابخونه تعريف شده اند، شامل شهرهاي داراي خدمات تيپاكس مي باشند.\n" +
                    "\n" +
                    "درج آگهي در كتابخونه كاملا ريگان مي باشد.") ;
        }


        findViewById(R.id.btnBack).setOnClickListener(view -> {
            finish();
        });
    }
}
