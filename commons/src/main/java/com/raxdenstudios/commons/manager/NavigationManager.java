/*
 * Copyright 2014 Ángel Gómez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.raxdenstudios.commons.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.raxdenstudios.commons.util.NavigationUtils;

import java.io.Serializable;
import java.util.Map;

public class NavigationManager {

    private Activity activity;
    private Intent intent;
    private int requestCode;
    private int[] transitions;

    public NavigationManager(Builder builder) {
        activity = builder.activity;
        requestCode = builder.requestCode;
        transitions = builder.transitions;

        if (builder.intent != null){
            intent = builder.intent;
        } else if (builder.classToStartIntent != null) {
            intent = new Intent();
            intent.setClass(activity, builder.classToStartIntent);
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
        NavigationUtils.navigateToActivityForResult(activity, intent, requestCode, transitions);
    }

    public void launchAndFinish() {
        launch();
        activity.finish();
    }

    public static class Builder {

        private Activity activity;
        private Intent intent;
        private Class<?> classToStartIntent;
        private Bundle extras = new Bundle();
        private int flags;
        private int requestCode;
        private int[] transitions = new int[] {
                android.R.animator.fade_in,
                android.R.animator.fade_out};

        public Builder(Activity activity) {
            this.activity = activity;
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
