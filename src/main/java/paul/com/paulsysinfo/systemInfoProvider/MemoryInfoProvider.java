package paul.com.paulsysinfo.systemInfoProvider;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import paul.com.paulsysinfo.R;

/**
 * Created by cheolgyoon on 2016. 5. 16..
 *
 */
public class MemoryInfoProvider extends InfoProvider {
    @SuppressWarnings("unused")
    private static final String TAG = MemoryInfoProvider.class.getSimpleName();



    private static ArrayList<InfoItem> sMemoryContent;
    private Object[] mParams;
    private TextView infoTextView;

    public TextView getInfoTextView() {
        return infoTextView;
    }

    public void setInfoTextView(TextView infoTextView) {
        this.infoTextView = infoTextView;
    }
    public MemoryInfoProvider(Context context) {
        super(context);
    }
    @Override
    protected Object[] getInfoParams() {
        return mParams;
    }

    @SuppressLint("NewApi")
    protected InfoItem getItem(int titleId, Object... params) {
        String title = getString(titleId);
        String value;

        try {
            ActivityManager.MemoryInfo mi = (ActivityManager.MemoryInfo)(params[0]);
            Runtime rt = (Runtime)(params[1]);
            switch (titleId) {
                case R.string.memory_available: value = formatStorageSize(mi.availMem); break;
                case R.string.memory_total: value = formatStorageSize(mi.totalMem); break;
                case R.string.memory_threshold: value = formatStorageSize(mi.threshold); break;
                case R.string.memory_runtime_free: value = formatStorageSize(rt.freeMemory()); break;
                case R.string.memory_runtime_max: value = formatStorageSize(rt.maxMemory()); break;
                case R.string.memory_runtime_total: value = formatStorageSize(rt.totalMemory()); break;
                default: value = getString(R.string.invalid_item);
            }
        } catch (Error e) {
            Log.e(TAG, e.toString());
            value = getString(R.string.unsupported);
        }
        return new InfoItem(title, value);
    }

    private static final InfoSpec[] sMemorySpecs = {
            new InfoSpec(R.string.memory_total, 16),
            new InfoSpec(R.string.memory_available, 1),
            new InfoSpec(R.string.memory_threshold, 1),
            new InfoSpec(R.string.memory_runtime_free, 1),
            new InfoSpec(R.string.memory_runtime_max, 1),
            new InfoSpec(R.string.memory_runtime_total, 1),
    };

    @Override
   public ArrayList<InfoItem> getItems() {
        if (null == sMemoryContent) {
            sMemoryContent = new ArrayList<>();
        }
        ActivityManager.MemoryInfo aMeminfo = new ActivityManager.MemoryInfo();
        ((ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(aMeminfo);
        Runtime runtime = Runtime.getRuntime();
        mParams = new Object[] { aMeminfo, runtime };
        sMemoryContent.clear();

        addItems(sMemoryContent, sMemorySpecs);
        return sMemoryContent;
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
}
