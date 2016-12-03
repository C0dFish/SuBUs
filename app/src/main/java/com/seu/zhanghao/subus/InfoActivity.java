package com.seu.zhanghao.subus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by zhanghao7 on 2016/11/30.
 */

public class InfoActivity extends Activity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_main);
        textView= (TextView) findViewById(R.id.soft_info);

       //  Log.d("debug",BusData.doGet("?LineGuid=83dba052-430b-41c2-bd33-7b1fb3a758b4&LineInfo=156(官渎里立交换乘枢纽站=>车坊首末站)"));
      //  Log.v("debug",WebData.queryRequest2("?LineGuid=83dba052-430b-41c2-bd33-7b1fb3a758b4&LineInfo=156(官渎里立交换乘枢纽站=>车坊首末站)"));
        //textView.setText(WebData.queryRequest2("?LineGuid=83dba052-430b-41c2-bd33-7b1fb3a758b4&LineInfo=156(官渎里立交换乘枢纽站=>车坊首末站)"));
        textView.setText("  实时公交\n便捷你的生活！");
    }
}
