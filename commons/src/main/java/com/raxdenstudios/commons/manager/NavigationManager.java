package com.raxdenstudios.commons.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.raxdenstudios.commons.util.NavigationUtils;

import java.io.Serializable;
import java.util.Map;

public class NavigationManager {

    private Context context;
    private Intent intent;
    private int requestCode;
    private int[] transitions;

    public NavigationManager(Builder builder) {
        context = builder.context;
        requestCode = builder.requestCode;
        transitions = builder.transitions;

        if (builder.intent != null){
            intent = builder.intent;
        } else if (builder.classToStartIntent != null) {
            intent = new Intent();
            intent.setClass(context, builder.classToStartIntent);
        }
        intent.addFlags(builder.flags);
        if (builder.extras != null) {
            if (intent.getExtras() != null) {
                intent.getExtras().putAll(builder.extras);
            } else {
                intent.putExtras(builder.extras);
            }
        }
    }

    public void launch() {
        NavigationUtils.navigateToActivityForResult(context, intent, requestCode, transitions);
    }

    public void launchAndFinish() {
        launch();
        if (context instanceof Activity) {
            ((Activity)context).finish();
        }
    }

    public static class Builder {

        private Context context;
        private Intent intent;
        private Class<?> classToStartIntent;
        private Bundle extras = new Bundle();
        private int flags;
        private int requestCode;
        private int[] transitions = new int[] {
                android.R.animator.fade_in,
                android.R.animator.fade_out};

        public Builder(Context context) {
            this.context = context;
        }

        public Builder putData(Bundle extras) {
            extras.putAll(extras);
            return this;
        }

        public Builder putData(Parcelable data) {
            extras.putParcelable(data.getClass().getSimpleName(), data);
            return this;
        }

        public Builder putData(Serializable data) {
            extras.putSerializable(data.getClass().getSimpleName(), data);
            return this;
        }

        public Builder putData(Map<String, Parcelable> data) {
            for (Map.Entry<String, Parcelable> entry : data.entrySet()) {
                extras.putParcelable(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public Builder setFlags(int flags) {
            this.flags = flags;
            return this;
        }

        public Builder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder setTransitionIn(int transition) {
            this.transitions[0] = transition;
            return this;
        }

        public Builder setTransitionOut(int transition) {
            this.transitions[1] = transition;
            return this;
        }

        public NavigationManager navigateTo(Intent intent) {
            this.intent = intent;
            return new NavigationManager(this);
        }

        public NavigationManager navigateTo(Class<?> classToStartIntent) {
            this.classToStartIntent = classToStartIntent;
            return new NavigationManager(this);
        }

    }

}
