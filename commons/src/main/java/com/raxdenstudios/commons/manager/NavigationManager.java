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
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NavigationManager {

    private Activity activity;
    private List<Intent> intentList;
    private int requestCode;
    private int[] transitions;
    private Bundle options;

    public NavigationManager(Builder builder) {
        activity = builder.activity;
        requestCode = builder.requestCode;
        transitions = builder.transitions;
        options = builder.options;

        if (builder.intentList != null && !builder.intentList.isEmpty()) {
            intentList = builder.intentList;
        } else if (builder.classToStartIntent != null) {
            intentList = new ArrayList<>();
            Intent intent = new Intent();
            intent.setClass(activity, builder.classToStartIntent);
            intentList.add(intent);
        }
        for (Intent intent : intentList) {
            intent.addFlags(builder.flags);
            if (builder.extras != null) {
                if (intent.getExtras() != null) {
                    intent.getExtras().putAll(builder.extras);
                } else {
                    intent.putExtras(builder.extras);
                }
            }
        }
    }


    /**
     * Launch the an activity and finish the showed one
     */
    public void launchAndFinish() {
        launch();
        activity.finish();
    }

    /**
     * Show the activity using the
     * {@link com.raxdenstudios.commons.util.NavigationUtils#navigateToActivityForResult} method, to
     * perform the navigation to the activity
     */
    public void launch() {
        if (hasMultipleIntentToLaunch()) {
            launchActivityStack(intentList);
        } else {
            launchActivity(intentList.get(0), options);
        }
    }

    private void launchActivity(Intent intent, Bundle options) {
        if (hasRequestCode()) {
            activity.startActivityForResult(intent, requestCode, options);
        } else {
            activity.startActivity(intent, options);
        }
        overridePendingTransition();
    }

    private void launchActivityStack(List<Intent> intentList) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
        for (Intent intent : intentList) {
            stackBuilder.addNextIntentWithParentStack(intent);
        }
        stackBuilder.startActivities();
    }

    private void overridePendingTransition() {
        if (hasTransitions()) {
            activity.overridePendingTransition(transitions[0], transitions[1]);
        }
    }

    private boolean hasMultipleIntentToLaunch() {
        return intentList.size() > 1;
    }

    private boolean hasTransitions() {
        return transitions.length == 2;
    }

    private boolean hasRequestCode() {
        return requestCode > 0;
    }

    /**
     * Class to simulate the creation of fragments
     */
    public static class Builder {

        private Activity activity;
        private List<Intent> intentList;
        private Class<?> classToStartIntent;
        private Bundle extras = new Bundle();
        private Bundle options;
        private int flags;
        private int requestCode;
        private int[] transitions = new int[] {
                android.R.anim.fade_in,
                android.R.anim.fade_out};

        /**
         * Constructor of the class
         * @param activity to initialize the class
         */
        public Builder(Activity activity) {
            this.activity = activity;
        }

        /**
         * Constructor of the class
         * @param extras data to build the intent
         */
        public Builder putData(Bundle extras) {
            this.extras.putAll(extras);
            return this;
        }

        /**
         * Set the data to the intent of the activity
         * @param data extra to set to the intent
         * @return builder with the set data
         */
        public Builder putData(Parcelable data) {
            extras.putParcelable(data.getClass().getSimpleName(), data);
            return this;
        }

        /**
         * Set the data to the intent of the activity
         * @param data extra to set to the intent
         * @return builder with the set data
         */
        public Builder putData(Serializable data) {
            extras.putSerializable(data.getClass().getSimpleName(), data);
            return this;
        }

        /**
         * Set the data to the intent of the activity
         * @param data extra to set to the intent
         * @return builder with the set data
         */
        public Builder putData(Map<String, Parcelable> data) {
            for (Map.Entry<String, Parcelable> entry : data.entrySet()) {
                extras.putParcelable(entry.getKey(), entry.getValue());
            }
            return this;
        }

        /**
         * Set the data to the intent of the activity
         * @param flags extra to set to the intent
         * @return builder with the set data
         */
        public Builder setFlags(int flags) {
            this.flags = flags;
            return this;
        }

        /**
         * Set the data to the intent of the activity
         * @param requestCode extra to set to the intent
         * @return builder with the set data
         */
        public Builder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        /**
         * Set the data to the intent of the activity
         * @param transitionIn in extra to set to the intent
         * @param transitionOut out extra to set to the intent
         * @return builder with the set data
         */
        public Builder setTransition(int transitionIn, int transitionOut) {
            this.transitions[0] = transitionIn;
            this.transitions[1] = transitionOut;
            return this;
        }

        /**
         * Set the intent to the {@link com.raxdenstudios.commons.manager.NavigationManager}
         * @param intent to initialize the NavigationManager
         * @return NavigationManager with the intent
         */
        public NavigationManager navigateTo(Intent intent) {
            return navigateTo(intent, null);
        }

        /**
         * Set the intent to the {@link com.raxdenstudios.commons.manager.NavigationManager}
         * @param intent to initialize the NavigationManager
         * @return NavigationManager with the intent
         */
        public NavigationManager navigateTo(Intent intent, Bundle options) {
            if (intentList == null) {
                intentList = new ArrayList<>();
            }
            intentList.add(intent);
            this.options = options;
            return new NavigationManager(this);
        }

        /**
         * Set the intent list to the {@link com.raxdenstudios.commons.manager.NavigationManager}
         * @param intentList
         * @return
         */
        public NavigationManager navigateTo(List<Intent> intentList) {
            if (this.intentList == null) {
                this.intentList = new ArrayList<>();
            }
            this.intentList.addAll(intentList);
            return new NavigationManager(this);
        }

        /**
         * Navigate to the intent
         * @param classToStartIntent to start the intent
         * @return NavigationManager intent to start
         */
        public NavigationManager navigateTo(Class<?> classToStartIntent) {
            return navigateTo(classToStartIntent, null);
        }

        /**
         * Navigate to the intent
         * @param classToStartIntent to start the intent
         * @return NavigationManager intent to start
         */
        public NavigationManager navigateTo(Class<?> classToStartIntent, Bundle options) {
            this.classToStartIntent = classToStartIntent;
            this.options = options;
            return new NavigationManager(this);
        }

    }

}
