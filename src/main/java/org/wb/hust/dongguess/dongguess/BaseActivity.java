package org.wb.hust.dongguess.dongguess;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import org.wb.hust.dongguess.util.Util;

/**
 * Created by Administrator on 2015/1/22.
 */
public class BaseActivity extends Activity {
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showToast(final Integer id) {
        final String text = getResources().getString(id);
        if (!Util.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), text,
                                Toast.LENGTH_LONG);
                    } else {
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });

        }
    }
}
