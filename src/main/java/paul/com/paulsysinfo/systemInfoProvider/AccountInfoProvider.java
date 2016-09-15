package paul.com.paulsysinfo.systemInfoProvider;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.widget.TextView;

import java.util.ArrayList;

import paul.com.paulsysinfo.R;


public class AccountInfoProvider extends InfoProvider {
    @SuppressWarnings("unused")
    private static final String TAG = AccountInfoProvider.class.getSimpleName();
    private static ArrayList<InfoItem> sAccountItems;
    private Activity activity;
    private TextView infoTextView;

    public TextView getInfoTextView() {
        return infoTextView;
    }

    public void setInfoTextView(TextView infoTextView) {
        this.infoTextView = infoTextView;
    }

    public AccountInfoProvider(Context context, Activity activity) {
        super(context);
       this.activity = activity;

    }

    private InfoItem getAuthenticatorItem(AuthenticatorDescription ad, PackageManager pm) {
        String name;
        try {
            Resources r = pm.getResourcesForApplication(ad.packageName);
            name = r.getString(ad.labelId);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            name = getString(R.string.unknown);
        }
        StringBuffer sb = new StringBuffer();
        sb.append("Type: ").append(ad.type);
        sb.append("\nPackage: ").append(ad.packageName);
        return new InfoItem("Authenticator: " + name, sb.toString());
    }

    @Override
   public ArrayList<InfoItem> getItems() throws SecurityException{
        if (null == sAccountItems) {
            sAccountItems = new ArrayList<>();

                AccountManager am = (AccountManager) mContext.getSystemService(Context.ACCOUNT_SERVICE);

                Account[] accounts = am.getAccounts();
                if (null == accounts || 0 == accounts.length) {
                    sAccountItems.add(new InfoItem(getString(R.string.item_account), getString(R.string.account_none)));
                } else {
                    for (Account account : accounts) {
                        sAccountItems.add(new InfoItem("Account: " + account.name, "type: " + account.type));
                    }
                }
                AuthenticatorDescription[] ads = am.getAuthenticatorTypes();
                if (null == ads || 0 == ads.length) {
                    sAccountItems.add(new InfoItem(getString(R.string.account_auth), getString(R.string.account_auth_none)));
                } else {
                    PackageManager pm = mContext.getPackageManager();
                    for (AuthenticatorDescription ad : ads) {
                        sAccountItems.add(getAuthenticatorItem(ad, pm));
                    }
                }
            }
            return sAccountItems;

    }


    public void showInfoIntoTV () {
        if (getInfoTextView() == null)
            return;
        StringBuilder stringBuilder = new StringBuilder();
        for (InfoItem infoStr:getItems()) {
            stringBuilder.append(infoStr.name);
            stringBuilder.append("\n");
            stringBuilder.append(infoStr.value);
            stringBuilder.append("\n");
            stringBuilder.append("--------------------");
            stringBuilder.append("\n\n");
        }
            getInfoTextView().setText(stringBuilder.toString());
    }

    private boolean checkPermission (final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS}, 1);

                return true;
            }

        }
        return false;
    }
    @Override
    protected Object[] getInfoParams() {
        return new Object[0];
    }

    @Override
    protected InfoItem getItem(int infoId, Object... params) {
        return null;
    }
}
