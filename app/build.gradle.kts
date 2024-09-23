plugins {
    alias(libs.plugins.android.application)
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.apptransport"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.apptransport"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    flavorDimensions("version")

    productFlavors {
        create("elgolf") {
            dimension = "version"
            applicationId = "com.apreciasoft.mobile.elgolf"
            signingConfig = signingConfigs.getByName("release")
            resValue("string", "app_name", "TAXI ejecutivos El Golf")
            buildConfigField("String", "INSTANCE", "\"elgolf\"")
            buildConfigField("String", "URL_SERVER", "\"as-nube.com\"")
            buildConfigField("String", "PORT", "\"8126\"")
            buildConfigField("String", "COUNTRY", "\"ARG\"")
            buildConfigField("String", "SOUND_GROUP", "\"centro_america\"")
        }
        create("tripgt") {
            dimension = "version"
            applicationId = "com.apreciasoft.mobile.tripgt"
            signingConfig = signingConfigs.getByName("release")
            resValue("string", "app_name", "Trip GT")
            buildConfigField("String", "INSTANCE", "\"tripgt\"")
            buildConfigField("String", "URL_SERVER", "\"as-nube.com\"")
            buildConfigField("String", "PORT", "\"8129\"")
            buildConfigField("String", "COUNTRY", "\"ARG\"")
            buildConfigField("String", "SOUND_GROUP", "\"centro_america\"")
        }
        // Agrega los demás flavors aquí con el mismo formato
    }

    signingConfigs {
        create("release") {
            storeFile = file("../signature/firma.jks")
            keyAlias = project.property("AppKeyAlias") as String
            keyPassword = project.property("AppKeyPassword") as String
            storePassword = project.property("AppStorePassword") as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    applicationVariants.all { variant ->
        variant.outputs.all {
            outputFileName = "apreciasoft-asRemis-${variant.name}-${variant.versionName}.apk"
        }
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "com.android.support" && !requested.name.startsWith("multidex")) {
                useVersion("28.0.0")
            }
        }
    }
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.android.material:material:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.0.0")
    implementation("com.google.code.gson:gson:2.8.5")

    implementation("com.squareup.retrofit2:retrofit:2.3.0")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.1.0")
    implementation("com.squareup.retrofit2:converter-simplexml:2.1.0") {
        exclude(group = "xpp3", module = "xpp3")
        exclude(group = "stax", module = "stax-api")
        exclude(group = "stax", module = "stax")
    }
    implementation("com.squareup.okhttp3:okhttp:3.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.9.1")

    implementation("com.google.android.gms:play-services-base:17.1.0")
    implementation("com.google.android.gms:play-services-analytics:17.0.0")
    implementation("com.google.android.gms:play-services-gcm:17.0.0")
    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("com.google.android.gms:play-services-wallet:18.0.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("com.google.android.libraries.places:places:2.0.0")
    implementation("com.google.android.gms:play-services-maps:17.0.0")

    implementation("org.java-websocket:Java-WebSocket:1.3.0")
    implementation("com.google.firebase:firebase-messaging:19.0.1")
    implementation("com.google.firebase:firebase-database:19.1.0")
    implementation("com.loopj.android:android-async-http:1.4.9")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.multidex:multidex:2.0.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.15")
    implementation("com.github.clans:fab:1.6.4")
    implementation("com.sothree.slidinguppanel:library:3.4.0")
    implementation("de.hdodenhof:circleimageview:2.2.0")

    implementation("io.socket:socket.io-client:0.8.3") {
        exclude(group = "org.json", module = "json")
    }
    implementation("com.kofigyan.stateprogressbar:stateprogressbar:1.0.0")

    implementation("com.airbnb.android:lottie:2.7.0")
    implementation("com.github.imjeevandeshmukh:GlideToast:1.0")
    implementation("com.mercadopago.android.px:checkout:4.46.2")

    implementation("com.github.bumptech.glide:glide:4.9.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.9.0")

    implementation("com.google.maps.android:android-maps-utils:0.5+")
    implementation("com.github.joielechong:countrycodepicker:2.4.1")

    implementation("com.kovachcode:timePickerWithSeconds:1.0.1")
    implementation("com.facebook.android:facebook-android-sdk:8.2.0")
    implementation("joda-time:joda-time:2.9.6")
    implementation("com.akexorcist:googledirectionlibrary:1.1.1")

    testImplementation("junit:junit:4.12")
}
