package com.ishirlitton.fingerid.finger;

import android.os.Handler;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;


/**
 * Created by littonishir on 2017/9/30.
 */
public class MyAuthCallback extends FingerprintManagerCompat.AuthenticationCallback {

    private Handler handler = null;

    public MyAuthCallback(Handler handler) {
        super();
        this.handler = handler;
    }

    /**
     * 验证错误信息
     * @param errMsgId
     * @param errString
     */
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);

        if (handler != null) {
            handler.obtainMessage(Constant.MSG_AUTH_ERROR, errMsgId, 0).sendToTarget();
        }
    }

    /**
     * 身份验证帮助
     * @param helpMsgId
     * @param helpString
     */
    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);

        if (handler != null) {
            handler.obtainMessage(Constant.MSG_AUTH_HELP, helpMsgId, 0).sendToTarget();
        }
    }

    /**
     * 验证成功
     * @param result
     */
    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        if (handler != null) {
            handler.obtainMessage(Constant.MSG_AUTH_SUCCESS).sendToTarget();
        }
    }

    /**
     * 验证失败
     */
    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();

        if (handler != null) {
            handler.obtainMessage(Constant.MSG_AUTH_FAILED).sendToTarget();
        }
    }
}
