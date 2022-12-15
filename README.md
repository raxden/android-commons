Android Commons
==========

<a href='https://github.com/raxden/android-commons/actions/workflows/deploy_library.yml'><img src='https://github.com/raxden/android-commons/workflows/Continuous%20Delivery/badge.svg'></a>
[![codecov](https://codecov.io/gh/raxden/android-commons/branch/master/graph/badge.svg?token=E55S5DHJ9B)](https://codecov.io/gh/raxden/android-commons)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.raxdenstudios/commons-android/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.raxdenstudios/commons-android)

Android Commons is a set of libraries that I have been creating and collecting throughout these
years
of development and that I use continuously in my new developments in order to avoid having to
maintain
the code in different sources.

With the purpose of avoiding having to depend on only one library, I decided to split the library in
different modules since the user that needs only use some utils or extensions from Retrofit library
don't need to download nothing related with the coroutines and vice-versa. That approach permits us
to reduce the size of the application that we are developing.

## Getting started

### Setting up the dependency that you requires.

```groovy
implementation "com.raxdenstudios:commons-android:x.y.z"
implementation "com.raxdenstudios:commons-coroutines:x.y.z"
implementation "com.raxdenstudios:commons-threeten:x.y.z"
implementation "com.raxdenstudios:commons-glide:x.y.z"
implementation "com.raxdenstudios:commons-pagination:x.y.z"
implementation "com.raxdenstudios:commons-pagination-rx:x.y.z"
implementation "com.raxdenstudios:commons-preferences:x.y.z"
implementation "com.raxdenstudios:commons-retrofit:x.y.z"
implementation "com.raxdenstudios:commons-retrofit-rx:x.y.z"
implementation "com.raxdenstudios:commons-rx:x.y.z"
implementation "com.raxdenstudios:commons-unit-test:x.y.z"
implementation "com.raxdenstudios:commons-android-test:x.y.z"
```

Please replace `x`, `y` and `z` with the latest version numbers
-> [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.raxdenstudios/commons-android/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.raxdenstudios/commons-android)

## LICENSE

    Copyright 2015 Ángel Gómez

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
