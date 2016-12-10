package com.raxdenstudios.commons.manager;

import android.app.FragmentManager;

/**
 * Created by Raxden on 09/12/2016.
 */

public class NavigationFragmentManager {


    public NavigationFragmentManager(Builder builder) {

    }

    public void load() {

    }

    public static class Builder {

        private FragmentManager fragmentManager;

        public Builder(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

    }

}
