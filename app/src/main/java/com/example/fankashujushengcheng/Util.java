package com.example.fankashujushengcheng;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by yxl on 17/10/25.
 */

public class Util {
    //异或运算
    public static String xor(String strHex_X, String strHex_Y) {
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

    //异或运算
    public static String pluscode_0007(String para) {
        int length = para.length() / 2;
        String[] dateArr = new String[length];

        for (int i = 0; i < length; i++) {
            dateArr[i] = para.substring(i * 2, i * 2 + 2);
        }
        int code = 0;
        for (int i = 0; i < dateArr.length; i++) {
            code = code + Integer.parseInt(dateArr[i], 16);
//            code = xor(code, dateArr[i]);
        }
        return Integer.toHexString(code);
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

    public static void writeToSdCard(Context context, String name,   //名字
                                     String id,   //工号
                                     String money,  //金额
                                     String account,  //账号
                                     String cardNum,  //卡号
                                     String filename) {
        //1、判断sd卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //sd卡可用
            //2、获取sd卡路径
            File sdFile = Environment.getExternalStorageDirectory();
            File path = new File(sdFile + "/MifareClassicTool/dump-files/", filename);//sd卡下面的a.txt文件  参数 前面 是目录 后面是文件
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path);
                StringBuffer sb = new StringBuffer();
                sb.append("+Sector: 0\n");
                sb.append("95B2F95B850804006263646566676869\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 1\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 2\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 3\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 4\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 5\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 6\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 7\n");
                sb.append(id + "\n");   //工号  校验位:异或
                sb.append("000000000000000000044E000000004A\n");
                sb.append(name + "\n");   //姓名   校验位:异或
                sb.append("199902240628FF078069199902240628\n");
                sb.append("+Sector: 8\n");
                sb.append(account + "\n");   //账号 校验位:相加 异或
                sb.append(cardNum + "\n");   //卡号 姓名  校验位:相加 异或
                sb.append("0131123488888800261500660000B1CB\n");   //到期时间 不必修改
                sb.append("13802276230DFF0780694346A053464A\n");
                sb.append("+Sector: 9\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append(money + "\n");   //余额
                sb.append(money + "\n");   //余额
                sb.append("2F95B1159168FF0780694346A053464A\n");
                sb.append("+Sector: 10\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00130090B60005008FAE0016008D0C4A\n");   //刷卡设备信息
                sb.append("075583740268FF0780694346A053464A\n");
                sb.append("+Sector: 11\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 12\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 13\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 14\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");
                sb.append("+Sector: 15\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\n");
                sb.append("00000000000000000000000000000000\n");
                sb.append("369886663696FF078069B0B1B2B3B4B5\n");


                fileOutputStream.write(sb.toString().getBytes());
                Toast.makeText(context, "保存好了", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }


    //汉字转GBK
    public static String toGBK(String source) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = source.getBytes("GBK");
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b & 0xff)).toUpperCase());
        }

        return sb.toString();
    }

}
