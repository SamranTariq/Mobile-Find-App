package com.example.owner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class pageAdapter extends FragmentStatePagerAdapter {
    int counttab;
    public pageAdapter(FragmentManager fm,int counttab) {
        super(fm);
        this.counttab=counttab;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            /*case 0:
                Test tab_three=new Test();
                return tab_three;*/
            case 0:
                GENERIC tab=new GENERIC();
                return tab;
            case 1:
                SUPERIOR tab_two=new SUPERIOR();
                return tab_two;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return counttab;
    }
}

