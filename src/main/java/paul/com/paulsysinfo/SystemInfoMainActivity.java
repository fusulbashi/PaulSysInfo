package paul.com.paulsysinfo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import paul.com.paulsysinfo.systemInfoProvider.AccountInfoProvider;
import paul.com.paulsysinfo.systemInfoProvider.CameraInfoProvider;
import paul.com.paulsysinfo.systemInfoProvider.ConnectivityInfoProvider;
import paul.com.paulsysinfo.systemInfoProvider.DrmInfoProvider;
import paul.com.paulsysinfo.systemInfoProvider.MemoryInfoProvider;
import paul.com.paulsysinfo.tools.AndroidToolbox;

public class SystemInfoMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = SystemInfoMainActivity.class.getSimpleName();
    private boolean permissionCheck;
    private TextView textView ;
    private final static int REQUEST_ALL = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textView = (TextView)findViewById(R.id.infoTextView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.system_info_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v(TAG,permissions.toString());
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionCheck = true;
                } else {
                        permissionCheck =false;
                }
                return;

            }

        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_accounts:
                Log.v("SystInfo","nav_accounts");
               permissionCheck= AndroidToolbox.checkPermission(this,Manifest.permission.GET_ACCOUNTS);
                if (permissionCheck ) {
                    Log.v(TAG, "Accounts View.");
                    AccountInfoProvider accountInfoProvider = new AccountInfoProvider(getApplicationContext(), this);

                    accountInfoProvider.setInfoTextView(textView);
                    accountInfoProvider.showInfoIntoTV();
                }
                break;
            case R.id.nav_memory:
                    Log.v(TAG, "Memory View.");
                    MemoryInfoProvider memoryInfoProvider = new MemoryInfoProvider(this);
                    memoryInfoProvider.setInfoTextView(textView);
                    //memoryInfoProvider.getItems();
                    memoryInfoProvider.showInfoIntoTV();
                break;
            case R.id.nav_drm_engines:
                Log.v(TAG,"DRM engines View.");
                DrmInfoProvider drmInfoProvider = new DrmInfoProvider(this);
                drmInfoProvider.setTextView(textView);
                drmInfoProvider.showInfoIntoTV();
                break;
            case R.id.nav_camera:
                Log.v(TAG,"Camera View.");
                permissionCheck= AndroidToolbox.checkPermission(this,Manifest.permission.CAMERA);
                if (permissionCheck ) {
                    CameraInfoProvider cameraInfoProvider = new CameraInfoProvider(this);
                    cameraInfoProvider.setTextView(textView);
                    cameraInfoProvider.showInfoIntoTV();
                }
                break;
            case R.id.nav_connectivity:
                Log.v(TAG,"Connectivity View.");
                permissionCheck= AndroidToolbox.checkPermission(this,Manifest.permission.ACCESS_NETWORK_STATE);
                if (permissionCheck ) {
                    ConnectivityInfoProvider connectivityInfoProvider = new ConnectivityInfoProvider(this);
                    connectivityInfoProvider.setTextView(textView);
                    connectivityInfoProvider.showInfoIntoTV();
                }
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            /*default: R.id.nav_camera:
                break;*/
        }
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
