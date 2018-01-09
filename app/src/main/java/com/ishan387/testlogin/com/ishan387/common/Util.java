package com.ishan387.testlogin.com.ishan387.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ishan on 09-01-2018.
 */

public class Util
{
    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null)
            {
                for(int i=0 ; i<info.length;i++)
                {
                    if(info[i].getState()== NetworkInfo.State.CONNECTED);
                    {
                        return  true;
                    }
                }
            }
        }
        return false;
    }
}
