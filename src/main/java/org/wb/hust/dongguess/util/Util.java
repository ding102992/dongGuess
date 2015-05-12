package org.wb.hust.dongguess.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.wb.hust.dongguess.data.Const;
import org.wb.hust.dongguess.dongguess.R;
import org.wb.hust.dongguess.modal.IAlertDialogButtonListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Administrator on 2014/12/23.
 */
public class Util {
    private static AlertDialog mAlertDialog;

    public static View getView(Context context,int layoutId){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutId,null);
        return layout;
    }

    public static char getRandomChinese(){
        String str = "";
        int highPos;
        int lowPos;
        Random random = new Random();
        highPos = 176 + Math.abs(random.nextInt(39));
        lowPos = 161 + Math.abs(random.nextInt(93));

        byte[] b = new byte[2];
        b[0] = Integer.valueOf(highPos).byteValue();
        b[1] = Integer.valueOf(lowPos).byteValue();

        try {
            str = new String(b,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }

    /**
     * 显示自定义的对话框
     * @param context 上下文
     * @param content 显示的内容
     * @param listener 事件监听器
     */
    public static void showDialog(final Context context, String content, final IAlertDialogButtonListener listener){
        View dialogView = null;
        AlertDialog.Builder builder= new AlertDialog.Builder(context,R.style.Theme_Transparent);
        dialogView = getView(context, R.layout.dialog_view);

        //初始化
        ImageButton btnOk = (ImageButton)dialogView.findViewById(R.id.dia_btn_ok);
        ImageButton btnNo = (ImageButton) dialogView.findViewById(R.id.dia_btn_no);
        TextView txtMessageView = (TextView) dialogView.findViewById(R.id.txt_dia_text);

        //设置内容
        txtMessageView.setText(content);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAlertDialog != null){
                    MusicPlayer.PlayTone(context,MusicPlayer.TONE_INDEX_ENTER);
                    mAlertDialog.cancel();
                }
                listener.onClick();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAlertDialog != null){
                    MusicPlayer.PlayTone(context,MusicPlayer.TONE_INDEX_CANCEL);
                    mAlertDialog.cancel();
                }
            }
        });

        //为dialog设置view
        builder.setView(dialogView);
        mAlertDialog = builder.create();

        mAlertDialog.show();
    }

    /**
     * 写入游戏的数据到文件中
     * @param context 上下文
     * @param currentStageIndex 当前关卡
     * @param currentCoins 当前金币数
     */
    public static void writeData(Context context, int currentStageIndex, int currentCoins){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(Const.DATA_FILE,Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeInt(currentStageIndex);
            dos.writeInt(currentCoins);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null ){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从文件中读取数据
     * @param context 上下文
     * @return
     */
    public static int[] readData(Context context){
        //默认值关卡为 -1  , 总金币数为Const中的常量
        int[] data = {-1, Const.TOTAL_COINS};

        FileInputStream fis = null;
        try {
            fis = context.openFileInput(Const.DATA_FILE);
            DataInputStream dis = new DataInputStream(fis);
            data[Const.INDEX_DATA_CURRENT_STAGE] = dis.readInt();
            data[Const.INDEX_DATA_CURRENT_CONIS] = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 输入的字符串是否为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        if(str != null && !"".equals(str)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isEmpty(String str){
        if(str == null || "".equals(str)){
            return true;
        }else{
            return false;
        }
    }

    public static String getMD5(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return getString(m);
    }
    private static String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }
}
