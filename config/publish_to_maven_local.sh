# https://github.com/ben-manes/gradle-versions-plugin

function publishToMavenLocal {
    cd ..
  ./gradlew publishToMavenLocal
}

publishToMavenLocal
