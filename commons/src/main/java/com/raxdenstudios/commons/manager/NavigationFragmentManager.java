package com.raxdenstudios.commons.manager;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

import com.raxdenstudios.commons.util.FragmentUtils;
import com.raxdenstudios.commons.util.SDKUtils;
import com.raxdenstudios.commons.util.Utils;

/**
 * Created by Raxden on 09/12/2016.
 */

public class NavigationFragmentManager {

    private static final String TAG = NavigationFragmentManager.class.getSimpleName();

    private enum Operation {ADD, REPLACE}
    private FragmentTransaction ft;
    private int containerId;
    private Fragment fragment;
    private String tag;
    private int[] animations;
    private boolean addToBackStack;
    private Operation operation;

    public NavigationFragmentManager(Builder builder) {
        ft = builder.ft;
        containerId = builder.containerId;
        fragment = builder.fragment;
        tag = builder.tag;
        animations = builder.animations.length > 0 ? builder.animations : builder.detaultAnimations;
        addToBackStack = builder.addToBackStack;
        operation = builder.operation;
    }

    public void commit() {
        performOperation();
        FragmentUtils.performTransaction(ft);
    }

    public void commitAllowingStateLoss() {
        performOperation();
        ft.commitAllowingStateLoss();
    }

    public void commitNow() {
        performOperation();
        if (SDKUtils.hasNougat()) {
            ft.commitNow();
        } else {
            Log.e(TAG, "Call requires API level 24");
        }
    }

    public void commitNowAllowingStateLoss() {
        performOperation();
        if (SDKUtils.hasNougat()) {
            ft.commitNowAllowingStateLoss();
        } else {
            Log.e(TAG, "Call requires API level 24");
        }
    }

    private void performOperation() {
        switch (operation) {
            case ADD:
                performAddOperation();
                break;
            case REPLACE:
                performReplaceOperation();
                break;
            default:
        }
        FragmentUtils.setCustomAnimations(ft, animations);
        if (addToBackStack) {
            FragmentUtils.addToBackStack(ft, fragment);
        }
    }

    private void performAddOperation() {
        if (Utils.hasValue(tag)) {
            ft = ft.add(containerId, fragment, tag);
        } else {
            ft = ft.add(containerId, fragment);
        }
    }

    private void performReplaceOperation() {
        if (Utils.hasValue(tag)) {
            ft = ft.replace(containerId, fragment, tag);
        } else {
            ft = ft.replace(containerId, fragment);
        }
    }

    public static class Builder {

        private Activity activity;
        private FragmentManager fm;
        private FragmentTransaction ft;
        private int containerId;
        private Fragment fragment;
        private String tag;
        private int[] animations = new int[4];
        private int[] detaultAnimations = new int[] {
                android.R.animator.fade_in,
                android.R.animator.fade_out,
                android.R.animator.fade_in,
                android.R.animator.fade_out
        };
        private boolean addToBackStack;
        private Operation operation;

        public Builder(Activity activity) {
            this.activity = activity;
            this.fm = activity.getFragmentManager();
        }

        public Builder setEnterAnimation(int animation) {
            this.animations[0] = animation;
            return this;
        }

        public Builder setExitAnimation(int animation) {
            this.animations[1] = animation;
            return this;
        }

        public Builder setPopEnterAnimation(int animation) {
            this.animations[2] = animation;
            return this;
        }

        public Builder setPopExitAnimation(int animation) {
            this.animations[3] = animation;
            return this;
        }

        public NavigationFragmentManager add(int containerId, Fragment fragment) {
            this.operation = Operation.ADD;
            this.containerId = containerId;
            this.fragment = fragment;
            this.ft = FragmentUtils.initFragmentTransaction(fm);
            return new NavigationFragmentManager(this);
        }

        public NavigationFragmentManager replace(int containerId, Fragment fragment) {
            this.operation = Operation.REPLACE;
            this.containerId = containerId;
            this.fragment = fragment;
            this.ft = FragmentUtils.initFragmentTransaction(fm);
            return new NavigationFragmentManager(this);
        }

    }

}
