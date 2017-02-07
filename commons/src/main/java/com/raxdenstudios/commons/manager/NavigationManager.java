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

    public NavigationManager(Builder builder) {
        activity = builder.activity;
        requestCode = builder.requestCode;
        transitions = builder.transitions;

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
     * Show the activity using the
     * perform the navigation to the activity
     */
    public void launch() {
        if (intentList.size() == 1) {
            Intent intent = intentList.get(0);
            if (requestCode > 0) {
                activity.startActivityForResult(intent, requestCode);
            } else {
                activity.startActivity(intent);
            }
            if (transitions.length > 1) {
                activity.overridePendingTransition(transitions[0], transitions[1]);
            }
        } else {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
            for (Intent intent : intentList) {
                stackBuilder.addNextIntentWithParentStack(intent);
            }
            stackBuilder.startActivities();
        }
    }


    /**
     * Launch the an activity and finish the current.
     */
    public void launchAndFinish() {
        launch();
        activity.finish();
    }

    /**
     *
     */
    public static class Builder {

        private Activity activity;
        private List<Intent> intentList;
        private Class<?> classToStartIntent;
        private Bundle extras = new Bundle();
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
         * @param transition in extra to set to the intent
         * @return builder with the set data
         */
        public Builder setTransitionIn(int transition) {
            this.transitions[0] = transition;
            return this;
        }

        /**
         * Set the data to the intent of the activity
         * @param transition out extra to set to the intent
         * @return builder with the set data
         */
        public Builder setTransitionOut(int transition) {
            this.transitions[1] = transition;
            return this;
        }

        /**
         * Set the intent to the {@link com.raxdenstudios.commons.manager.NavigationManager}
         * @param intent to initialize the NavigationManager
         * @return NavigationManager with the intent
         */
        public NavigationManager navigateTo(Intent intent) {
            if (intentList == null) {
                intentList = new ArrayList<>();
            }
            intentList.add(intent);
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
            this.classToStartIntent = classToStartIntent;
            return new NavigationManager(this);
        }

    }

}
