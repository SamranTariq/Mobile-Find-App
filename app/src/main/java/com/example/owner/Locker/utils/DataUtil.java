package com.example.owner.Locker.utils;

import androidx.annotation.NonNull;

import com.example.owner.Locker.model.CommLockInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public class DataUtil {

    @NonNull
    public static List<CommLockInfo> clearRepeatCommLockInfo(List<CommLockInfo> lockInfos) {
        HashMap<String, CommLockInfo> hashMap = new HashMap<>();
        for (CommLockInfo lockInfo : lockInfos) {
            if (!hashMap.containsKey(lockInfo.getPackageName())) {
                hashMap.put(lockInfo.getPackageName(), lockInfo);
            }
        }
        List<CommLockInfo> commLockInfos = new ArrayList<>();
        for (HashMap.Entry<String, CommLockInfo> entry : hashMap.entrySet()) {
            commLockInfos.add(entry.getValue());
        }
        return commLockInfos;
    }

}
