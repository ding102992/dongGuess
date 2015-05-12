package org.wb.hust.dongguess.dongguess;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.wb.hust.dongguess.modal.PortalUser;
import org.wb.hust.dongguess.util.Util;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2015/1/22.
 */
public class RegisterActivity extends BaseActivity{

    private EditText mEtUsername,mEtPassword,mEtPassword2;
    private Button mBtnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtPassword2 = (EditText) findViewById(R.id.et_password_repeat);
        //更改title标题
        mBtnReg = (Button)findViewById(R.id.btn_register);
        ((TextView)findViewById(R.id.title_bar_title)).setText("注册");
        mBtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        super.onCreate(savedInstanceState);
    }

    private void register(){
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();
        String password2 = mEtPassword2.getText().toString();
        Integer msg = checkInput(username,password,password2);
        if(msg != null){
            showToast(msg);
            return;
        }
        PortalUser bmobUser = new PortalUser();
        bmobUser.setUsername(username);
        bmobUser.setEmail(username);
        try {
            bmobUser.setPassword(Util.getMD5(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }
        bmobUser.setEmailVerified(true);
        bmobUser.setCoins(1000);
        bmobUser.signUp(this,new SaveListener() {
            @Override
            public void onSuccess() {
                showToast(R.string.toast_register_success);
            }

            @Override
            public void onFailure(int i, String s) {
                showToast(R.string.toast_register_fail);

            }
        });

    }

    private Integer checkInput(String username, String password, String password2){
        Pattern patternEmail = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        if(Util.isEmpty(username)){
            return  R.string.toast_username_require;
        }
        if(!patternEmail.matcher(username).matches()){
            return R.string.toast_username_format;
        }
        if(Util.isEmpty(password) || Util.isEmpty(password2)){
            return R.string.toast_password_require;
        }
        if(password != null && !password.equals(password2)){
            return R.string.toast_password_not_same;
        }
        if(password.length() < getResources().getInteger(R.integer.password_length_require)){
            return R.string.toast_password_too_short;
        }
        return null;
    }


    /**
     * 返回按钮事件
     * @param view
     */
    public void back(View view){

    }
}
