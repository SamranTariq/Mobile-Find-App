package com.example.owner.Locker.mvp.contract;

import android.content.Context;

import com.example.owner.Locker.model.CommLockInfo;
import com.example.owner.Locker.mvp.p.LockMainPresenter;

import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public interface LockMainContract {
    interface View {

        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter {
        void loadAppInfo(Context context);

        void searchAppInfo(String search, LockMainPresenter.ISearchResultListener listener);

        void onDestroy();
    }
}
