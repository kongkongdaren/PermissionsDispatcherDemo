package com.wen.asyl.permissionsdispatcherdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void callPhone(View view){
       MainActivityPermissionsDispatcher.callWithCheck(MainActivity.this);
    }

    //NeedsPermission 用来获取权限
    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void call(){
        Intent intent=new Intent(Intent.ACTION_CALL);
        Uri data= Uri.parse("tel:"+"10086");
        intent.setData(data);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //OnShowRationale提示用户为何要开启权限
    @OnShowRationale(Manifest.permission.CALL_PHONE)
    //提示用户为何要开启权限
    void  showWhy(final PermissionRequest request){
        new AlertDialog.Builder(this).setMessage("请开启打电话的权限").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                request.proceed();//再次请求权限
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.cancel();//取消执行请求
            }
        }).show();
    }
    //OnPermissionDenied 用户选择拒绝时的提示
    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    void showDenied(){
        Toast.makeText(this, "您拒绝了此权限", Toast.LENGTH_SHORT).show();
    }
    //OnNeverAskAgain 用户选择不再询问后的提示
    @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
    void showNotAsk(){
        new AlertDialog.Builder(this).setMessage("需要访问电话的权限，不开启将无法使用！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                //设置去向意图
                Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                intent.setData(uri);
                //发起跳转
                startActivity(intent);
            }
        }).setNegativeButton("取消",null).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
