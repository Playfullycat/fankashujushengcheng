package com.example.fankashujushengcheng;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.fankashujushengcheng.Util.checkcode_0007;
import static com.example.fankashujushengcheng.Util.pluscode_0007;
import static com.example.fankashujushengcheng.Util.toGBK;
import static com.example.fankashujushengcheng.Util.writeToSdCard;
import static com.example.fankashujushengcheng.Util.xor;


public class MainActivity extends AppCompatActivity {
    private EditText jine, jobNum, name, account, cardNum;
    private Button shengcheng, clean, save, random;
    private TextView show, textView, textView2;
    private CheckBox checkBox;
    private Spinner spinner;

    String jieguo;
    private static final String TAG = "MainActivity";
    final int REQUEST_WRITE = 1;//申请权限的请求码

    private String f;
    public List<Person> contactList;
    public boolean isContactReady = false;
    SQLiteDatabase sd;
    boolean isWrite = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        jine = (EditText) findViewById(R.id.jine);
        shengcheng = (Button) findViewById(R.id.button);
        clean = (Button) findViewById(R.id.button2);
        show = (TextView) findViewById(R.id.show);
        jobNum = (EditText) findViewById(R.id.jobNum);
        name = (EditText) findViewById(R.id.name);
        account = (EditText) findViewById(R.id.account);
        cardNum = (EditText) findViewById(R.id.cardNum);
        save = (Button) findViewById(R.id.button3);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        spinner = (Spinner) findViewById(R.id.spinner);
        random = (Button) findViewById(R.id.button4);

        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        writeDB();

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(f, MODE_PRIVATE, null);
        sd = sqLiteDatabase;
        contactList = new ArrayList<Person>();
//        ContactThread getContactTh = new ContactThread();
//        getContactTh.start();
//        while (true) {
//            if (!isContactReady)
//                continue;
//            PersonAdapter mAdapter = new PersonAdapter(this, contactList);
//            spinner.setAdapter(mAdapter);
//            break;
//        }

//  开启以上Thread后 Back键退出再打开APP会卡死,目前功力解决不了

        Cursor cursor = sd.query("person", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String workerId = cursor.getString(cursor.getColumnIndex("workerid"));
            Person p = new Person(name, workerId);
            contactList.add(p);
        }

        PersonAdapter mAdapter = new PersonAdapter(this, contactList);
        spinner.setAdapter(mAdapter);



        sqLiteDatabase.close();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Person a = (Person) adapterView.getItemAtPosition(i);
                name.setText(a.getPersonName());
                jobNum.setText(a.getPersonWorkerId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                int a = r.nextInt(2001);
                int b = (int) (Math.random() * 2000 + 1000);
                jine.setText(b + "00");
            }
        });

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jine.setText("");
                name.setText("");
                jobNum.setText("");
                show.setText("");
//                try {
//                    Toast.makeText(MainActivity.this, toGBK("22286"), Toast.LENGTH_LONG).show();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
            }
        });

        shengcheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jine0, jine1, jine2, jine3, jiaoyanwei1, jiaoyanwei3;
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
                        + convertToInt(getLastTwo(jiaoyanwei1))
                        + convertToInt(fillZero(checkcode_0007(jine0)))
                        + 218);
                jieguo = jine3 + jine2 + jine1 +
                        "00" +
                        getLastTwo(jiaoyanwei1) + fillZero(checkcode_0007(jine0)) +
                        "A50502230101010800" +
                        getLastTwo(jiaoyanwei3);
//                Log.d(TAG, "前两" + jine1 + "中间" + jine2 + "最后" + jine3 +
//                        "校验位1=" + getLastTwo(jiaoyanwei1) +
//                        "校验位2=" + fillZero(checkcode_0007(jine0)) +
//                        "校验位3=" + getLastTwo(jiaoyanwei3));
                show.setText(jieguo);
                ClipData clipData = ClipData.newPlainText("Label1", jieguo);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity.this, "生成数据成功!!!", Toast.LENGTH_LONG)
                        .show();
            }
        });

        //账号数据生成
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idCheck, nameCheck, nameRow, accountCheck, accountCheck1, accountRow, carNumRow, idRow;
                try {
                    //工号校验部分
                    idCheck = fillZero(checkcode_0007(toGBK(jobNum.getText().toString())));
                    idRow = toGBK(jobNum.getText().toString()) + "00000000001001001001" + idCheck;

                    //名字校验部分
                    nameCheck = fillZero(checkcode_0007(toGBK(name.getText().toString() + "01")));
                    if (name.getText().length() == 2) {
                        nameRow = toGBK(name.getText().toString()) + "0000" + "000000000000000001" + nameCheck;
                    } else {
                        nameRow = toGBK(name.getText().toString()) + "000000000000000001" + nameCheck;
                    }

                    //账号部分
                    String ac, actemp, actemp1, actemp2, actemp3, actemp4, jobTemp, jobTemp1, jobTemp2, xorTemp;
                    ac = account.getText().toString();
                    actemp = ac.substring(0, 2);
                    actemp1 = ac.substring(2, 4);
                    actemp2 = ac.substring(4, 6);
                    actemp3 = ac.substring(6, 8);
                    actemp4 = actemp3 + actemp2 + actemp1 + actemp;
                    jobTemp = jobNum.getText().toString().substring(0, 1);
                    jobTemp1 = jobNum.getText().toString().substring(1, 3);
                    jobTemp2 = jobNum.getText().toString().substring(3, 5);
                    accountCheck = getLastTwo(convertToHex(convertToInt(actemp) + convertToInt(actemp1) + convertToInt(actemp2) + convertToInt(actemp3) +
                            convertToInt(actemp) + convertToInt(actemp1) + convertToInt(actemp2) + convertToInt(actemp3) +
                            convertToInt(jobTemp) + convertToInt(jobTemp1) + convertToInt(jobTemp2)));
                    xorTemp = xor(checkcode_0007(ac), checkcode_0007(ac));
                    accountCheck1 = fillZero(xor(xorTemp, checkcode_0007("0" + jobNum.getText().toString())));
                    accountRow = actemp4 + actemp4 + "0000000" + jobNum.getText().toString() + accountCheck + accountCheck1;

                    //卡号校验部分
                    String cardPlus, cardXor;
                    cardPlus = getLastTwo(pluscode_0007(cardNum.getText().toString() + toGBK(name.getText().toString())));
                    cardXor = fillZero(xor(checkcode_0007(cardNum.getText().toString()), checkcode_0007(toGBK(name.getText().toString()))));
                    if (name.getText().length() == 2) {
                        carNumRow = cardNum.getText().toString() + toGBK(name.getText().toString()) + "000000000000" + cardPlus + cardXor;
                    } else {
                        carNumRow = cardNum.getText().toString() + toGBK(name.getText().toString()) + "00000000" + cardPlus + cardXor;
                    }
//                    Log.d(TAG, "工号部分" + idRow.length() + "  " + idRow + "名字部分" + nameRow.length() + "  " + nameRow + "卡号部分" + carNumRow.length() + "  " + carNumRow + "账号部分" + accountRow.length() + "  " + accountRow);

                    //保存成文件
                    //判断是否6.0以上的手机   不是就不用
                    if (Build.VERSION.SDK_INT >= 23) {
                        //判断是否有这个权限
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //2、申请权限: 参数二：权限的数组；参数三：请求码
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
                        } else {

                            writeToSdCard(MainActivity.this, nameRow, idRow, jieguo, accountRow, carNumRow, name.getText().toString());

                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //被选中
                    account.setVisibility(View.VISIBLE);
                    cardNum.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                } else {
                    //取消选中
                    account.setVisibility(View.INVISIBLE);
                    cardNum.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    String getLastTwo(String s) {   //第一,三个校验位  取后边两位数
        if (s.length() > 2) {
            return s.substring(s.length() - 2);
        }
        return s;
    }

    String fillZero(String s) {   //第二个校验位  金额异或 取两位数 不足两位前面补0
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

    //将DB写入内存卡里
    public void writeDB() {
        f = getFilesDir() + "\\databases\\" + "a1.db";
        FileOutputStream fout = null;
        InputStream inputStream = null;
        File file = new File(f);
        if (!file.exists()) {
            try {
                inputStream = getResources().openRawResource(R.raw.contact);
                fout = new FileOutputStream(new File(f));
                byte[] buffer = new byte[128];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    fout.write(buffer, 0, len);
                }
                buffer = null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fout != null) {
                    try {
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //读取数据到ArrayList
    class ContactThread extends Thread {
        @Override
        public void run() {
            super.run();
            Cursor cursor = sd.query("person", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String workerId = cursor.getString(cursor.getColumnIndex("workerid"));
                Person p = new Person(name, workerId);
                contactList.add(p);
            }
            isContactReady = true;
            sd.close();
        }
    }


}
