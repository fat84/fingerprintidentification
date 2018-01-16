package com.ishirlitton.fingerid;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ishirlitton.fingerid.finger.CryptoObjectHelper;
import com.ishirlitton.fingerid.finger.MyAuthCallback;
import com.ishirlitton.fingerid.view.FingerDialog;

import static com.ishirlitton.fingerid.finger.Constant.MSG_AUTH_ERROR;
import static com.ishirlitton.fingerid.finger.Constant.MSG_AUTH_FAILED;
import static com.ishirlitton.fingerid.finger.Constant.MSG_AUTH_HELP;
import static com.ishirlitton.fingerid.finger.Constant.MSG_AUTH_SUCCESS;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private FingerDialog fingerDialog;
    private FingerprintManagerCompat fingerprintManager = null;
    private MyAuthCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUTH_SUCCESS:
                    fingerDialog.setMsg(getResources().getString(R.string.fingerprint_success)).setNegativeButtonDisMiss();
                    LogUtil.showToast(mContext, getResources().getString(R.string.fingerprint_success));
                    cancellationSignal = null;
                    break;
                case MSG_AUTH_FAILED:
                    fingerDialog.setMsg(getResources().getString(R.string.fingerprint_not_recognized)).setNegativeButtonDisMiss();
                    LogUtil.showToast(mContext, getResources().getString(R.string.fingerprint_not_recognized));
                    cancellationSignal = null;
                    break;
                case MSG_AUTH_ERROR:
                    handleErrorCode(msg.arg1);
                    break;
                case MSG_AUTH_HELP:
                    handleHelpCode(msg.arg1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        isRightFingerPrinter();

    }

    public void finger(View view) {
        try {
            fingerDialog = new FingerDialog(mContext).builder();

            fingerDialog.setCancelable(false)
                    .setMsg("验证您的指纹")
                    .setNegativeButton("取消", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (null != cancellationSignal) {
                                cancellationSignal.cancel();
                                cancellationSignal = null;
                            }
                        }
                    }).show();
            CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
            if (cancellationSignal == null) {
                cancellationSignal = new CancellationSignal();
            }
            fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), 0,
                    cancellationSignal, myAuthCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "init failed,try Again!", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void isRightFingerPrinter() {
        fingerprintManager = FingerprintManagerCompat.from(this);
        if (!fingerprintManager.isHardwareDetected()) {//是否支持指纹识别
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.no_sensor_dialog_message);
            builder.setIcon(android.R.drawable.stat_sys_warning);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {//是否已注册指纹
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("您还没有录入指纹, 请在设置界面录入至少一个指纹");
            builder.setIcon(android.R.drawable.stat_sys_warning);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        } else {
            try {
                myAuthCallback = new MyAuthCallback(mHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleHelpCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                LogUtil.showToast(mContext, getResources().getString(R.string.AcquiredImageDirty_warning));
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
                LogUtil.showToast(mContext, getResources().getString(R.string.AcquiredInsufficient_warning));
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                break;
        }
    }

    private void handleErrorCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                LogUtil.showToast(mContext, getResources().getString(R.string.ErrorHwUnavailable_warning));
                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                LogUtil.showToast(mContext, getResources().getString(R.string.ErrorLockout_warning));
                break;
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
                LogUtil.showToast(mContext, getResources().getString(R.string.ErrorNoSpace_warning));
                break;
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
                LogUtil.showToast(mContext, getResources().getString(R.string.ErrorTimeout_warning));
                break;
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                LogUtil.showToast(mContext, getResources().getString(R.string.ErrorUnableToProcess_warning));
                break;
        }
    }

}
