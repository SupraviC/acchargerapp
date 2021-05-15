package com.rxgradle.wifi.api;


import com.rxgradle.wifi.api.bean.WifiStatEnum;

/**
 * <p class="note"></p>
 * created by LRXx at 2017-8-2
 */
public interface IWifiStat  {
    void WifiStatChange(WifiStatEnum statEnum);
}
