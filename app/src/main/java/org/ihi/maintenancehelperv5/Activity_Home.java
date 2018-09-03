package org.ihi.maintenancehelperv5;

//import android.app.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class Activity_Home extends AppCompatActivity {


    private NoScrollViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    /*private ViewPager mViewPager;
    public Handler mHandler;*/

    //创建监听权限的接口对象
    private PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            Toast.makeText(Activity_Home.this, "权限通过，可以做其他事情!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void forbitPermissons() {
            Toast.makeText(Activity_Home.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
            }
            String hex = hexString.toString();
            return hex;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String hex = sHA1(getApplicationContext());
        Log.i("===========", hex);

        String[] permissions = new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION};
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);

        setContentView(R.layout.activity_home);

        viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        Helper_BottomNavigationView.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item＿signal:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_lib:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_route:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.item_aboat:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //禁止ViewPager滑动
        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/

        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter_ViewPager adapter = new Adapter_ViewPager(getSupportFragmentManager());
        adapter.addFragment(new Fregment_Signal());
        adapter.addFragment(new Bluetooth());
        adapter.addFragment(new Fragment_Navi_Home());
        adapter.addFragment(new aboat());
        viewPager.setAdapter(adapter);
    }


}
