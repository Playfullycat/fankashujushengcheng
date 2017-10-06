package com.example.fankashujushengcheng;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText jine;
    private Button shengcheng;
    private Button clean;
    private TextView show;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        jine = (EditText) findViewById(R.id.jine);
        shengcheng = (Button) findViewById(R.id.button);
        clean = (Button) findViewById(R.id.button2);
        show = (TextView) findViewById(R.id.show);

        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jine.setText("");
            }
        });

        shengcheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jine0, jine1, jine2, jine3, jiaoyanwei1, jiaoyanwei3, jieguo;
                jine0 = jine.getText().toString();
                jine1 = jine0.substring(0, 2);    //千百
                jine2 = jine0.substring(2, 4);    //十个
                jine3 = jine0.substring(4, 6);    //毛分
                jiaoyanwei1 = convertToHex(convertToInt(jine1)
                        + convertToInt(jine2)
                        + convertToInt(jine3));
                jiaoyanwei3 = convertToHex(convertToInt(jine1)
                        + convertToInt(jine2)
                        + convertToInt(jine3)
                        + convertToInt(jiaoyanwei(jiaoyanwei1))
                        + convertToInt(jiaoyanwei2(checkcode_0007(jine0)))
                        + 218);
                jieguo = jine3 + jine2 + jine1 +
                        "00" +
                        jiaoyanwei(jiaoyanwei1) + jiaoyanwei2(checkcode_0007(jine0)) +
                        "A50502230101010800" +
                        jiaoyanwei(jiaoyanwei3);


                Log.d(TAG, "前两" + jine1 + "中间" + jine2 + "最后" + jine3 +
                        "校验位1=" + jiaoyanwei(jiaoyanwei1) +
                        "校验位2=" + jiaoyanwei2(checkcode_0007(jine0)) +
                        "校验位3=" + jiaoyanwei(jiaoyanwei3));
                show.setText(jieguo);
                ClipData clipData = ClipData.newPlainText("Label1", jieguo);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity.this, "数据已复制到剪贴板!!!", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    String jiaoyanwei(String s) {   //第一,三个校验位  取后边两位数
        if (s.length() > 2) {
            return s.substring(s.length() - 2);
        }
        return s;
    }

    String jiaoyanwei2(String s) {   //第二个校验位  金额异或 取两位数 不足两位前面补0
        if (s.length() < 2) {
            return "0" + s;
        }
        return s;
    }

    int convertToInt(String s) {
        return Integer.parseInt(s, 16);
    }

    String convertToHex(int i) {
        return Integer.toHexString(i);
    }

    int stringToInt(String s) {
        return Integer.parseInt(s);
    }

    String intToString(int a) {
        return Integer.toString(a);
    }

    public static String checkcode_0007(String para) {
        int length = para.length() / 2;
        String[] dateArr = new String[length];

        for (int i = 0; i < length; i++) {
            dateArr[i] = para.substring(i * 2, i * 2 + 2);
        }
        String code = "00";
        for (int i = 0; i < dateArr.length; i++) {
            code = xor(code, dateArr[i]);
        }
        return code;
    }

    private static String xor(String strHex_X, String strHex_Y) {
        //将x、y转成二进制形式
        String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));
        String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));
        String result = "";
        //判断是否为8位二进制，否则左补零
        if (anotherBinary.length() != 8) {
            for (int i = anotherBinary.length(); i < 8; i++) {
                anotherBinary = "0" + anotherBinary;
            }
        }
        if (thisBinary.length() != 8) {
            for (int i = thisBinary.length(); i < 8; i++) {
                thisBinary = "0" + thisBinary;
            }
        }
        //异或运算
        for (int i = 0; i < anotherBinary.length(); i++) {
            //如果相同位置数相同，则补0，否则补1
            if (thisBinary.charAt(i) == anotherBinary.charAt(i))
                result += "0";
            else {
                result += "1";
            }
        }
//        Log.e("code", result);
        return Integer.toHexString(Integer.parseInt(result, 2));
    }
}
