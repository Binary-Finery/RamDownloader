package spencerstudios.com.ramdownloader;

import android.app.ActivityManager;
import android.content.Context;

class RamInfo {

    static double getRAM(Context context){

        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);

        return memInfo.totalMem / 1000000;
    }
}
