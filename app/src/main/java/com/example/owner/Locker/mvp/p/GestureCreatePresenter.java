package com.example.owner.Locker.mvp.p;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.owner.R;
import com.example.owner.Locker.model.LockStage;
import com.example.owner.Locker.mvp.contract.GestureCreateContract;
import com.example.owner.Locker.utils.LockPatternUtils;
import com.example.owner.Locker.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

import static com.example.owner.Locker.model.LockStage.ChoiceConfirmed;
import static com.example.owner.Locker.model.LockStage.ChoiceTooShort;
import static com.example.owner.Locker.model.LockStage.ConfirmWrong;
import static com.example.owner.Locker.model.LockStage.FirstChoiceValid;
import static com.example.owner.Locker.model.LockStage.Introduction;
import static com.example.owner.Locker.model.LockStage.NeedToConfirm;

/**
 * Created by xian on 2017/2/17.
 *
 *According to this project need
 *
 * Edited or modified by samran_tariq on 2020/8/5
 * */

public class GestureCreatePresenter implements GestureCreateContract.Presenter {
    private GestureCreateContract.View mView;
    private Context mContext;

    public GestureCreatePresenter(GestureCreateContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void updateStage(@NonNull LockStage stage) {
        mView.updateUiStage(stage); //更新UiStage
        if (stage == ChoiceTooShort) { //如果少于4个点
            mView.updateLockTip(mContext.getResources().getString(stage.headerMessage, LockPatternUtils.MIN_LOCK_PATTERN_SIZE), true);
        } else {
            if (stage.headerMessage == R.string.lock_need_to_unlock_wrong) {
                mView.updateLockTip(mContext.getResources().getString(R.string.lock_need_to_unlock_wrong), true);
                mView.setHeaderMessage(R.string.lock_recording_intro_header);
            } else {
                mView.setHeaderMessage(stage.headerMessage); //
            }
        }
        // same for whether the patten is enabled
        mView.lockPatternViewConfiguration(stage.patternEnabled, LockPatternView.DisplayMode.Correct);

        switch (stage) {
            case Introduction:
                mView.Introduction();
                break;
            case HelpScreen:
                mView.HelpScreen();
                break;
            case ChoiceTooShort:
                mView.ChoiceTooShort();
                break;
            case FirstChoiceValid:
                updateStage(NeedToConfirm);
                mView.moveToStatusTwo();
                break;
            case NeedToConfirm:
                mView.clearPattern();
                break;
            case ConfirmWrong:

                mView.ConfirmWrong();
                break;
            case ChoiceConfirmed:

                mView.ChoiceConfirmed();
                break;
        }
    }

    @Override
    public void onPatternDetected(@NonNull List<LockPatternView.Cell> pattern, @Nullable List<LockPatternView.Cell> mChosenPattern, LockStage mUiStage) {
        if (mUiStage == NeedToConfirm) {
            if (mChosenPattern == null)
                throw new IllegalStateException("null chosen pattern in stage 'need to confirm");
            if (mChosenPattern.equals(pattern)) {
                updateStage(ChoiceConfirmed);
            } else {
                updateStage(ConfirmWrong);
            }
        } else if (mUiStage == ConfirmWrong) {
            if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                updateStage(ChoiceTooShort);
            } else {
                if (mChosenPattern.equals(pattern)) {
                    updateStage(ChoiceConfirmed);
                } else {
                    updateStage(ConfirmWrong);
                }
            }
        } else if (mUiStage == Introduction || mUiStage == ChoiceTooShort) {
            if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                updateStage(ChoiceTooShort);
            } else {
                mChosenPattern = new ArrayList<>(pattern);
                mView.updateChosenPattern(mChosenPattern);
                updateStage(FirstChoiceValid);
            }
        } else {
            throw new IllegalStateException("Unexpected stage " + mUiStage + " when " + "entering the pattern.");
        }
    }

    @Override
    public void onDestroy() {

    }
}
