# Script simplificado - crea estructura de directorios y archivos clave
$root = "C:\Users\Jorge\MiStream"

function MkDir-Force($path) {
    if (!(Test-Path $path)) { New-Item -ItemType Directory -Path $path -Force | Out-Null }
}

function Write-UTF8($path, $content) {
    $dir = Split-Path $path -Parent
    if (!(Test-Path $dir)) { New-Item -ItemType Directory -Path $dir -Force | Out-Null }
    [System.IO.File]::WriteAllText($path, $content, [System.Text.UTF8Encoding]::new($false))
}

Write-Host "Creando estructura..." -ForegroundColor Cyan

# Directorios
$dirs = @(
    "gradle\wrapper",
    "app\src\main\java\com\mistream\app\data\api",
    "app\src\main\java\com\mistream\app\data\model",
    "app\src\main\java\com\mistream\app\data\repository",
    "app\src\main\java\com\mistream\app\di",
    "app\src\main\java\com\mistream\app\ui\detail",
    "app\src\main\java\com\mistream\app\ui\player",
    "app\src\main\java\com\mistream\app\ui\search",
    "app\src\main\java\com\mistream\app\ui\settings",
    "app\src\main\res\layout",
    "app\src\main\res\values",
    "app\src\main\res\drawable"
)
foreach ($d in $dirs) { MkDir-Force "$root\$d" }

# ── settings.gradle.kts ──────────────────────────────────────────────────────
Write-UTF8 "$root\settings.gradle.kts" 'pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}
rootProject.name = "MiStream"
include(":app")'

# ── build.gradle.kts (root) ──────────────────────────────────────────────────
Write-UTF8 "$root\build.gradle.kts" 'plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
}'

# ── gradle/libs.versions.toml ────────────────────────────────────────────────
Write-UTF8 "$root\gradle\libs.versions.toml" '[versions]
agp = "8.7.3"
kotlin = "2.0.21"
coreKtx = "1.15.0"
appcompat = "1.7.0"
leanback = "1.2.0"
lifecycle = "2.8.7"
activity = "1.9.3"
fragment = "1.8.5"
navigation = "2.8.5"
hilt = "2.53"
retrofit = "2.11.0"
okhttp = "4.12.0"
gson = "2.11.0"
glide = "4.16.0"
exoplayer = "1.5.0"
room = "2.6.1"
coroutines = "1.9.0"
datastore = "1.1.1"
junit = "4.13.2"
junitExt = "1.2.1"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
androidx-leanback = { group = "androidx.leanback", name = "leanback", version.ref = "leanback" }
androidx-lifecycle-viewmodel = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version.ref = "lifecycle" }
androidx-lifecycle-runtime = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycle" }
androidx-activity = { group = "androidx.activity", name = "activity-ktx", version.ref = "activity" }
androidx-fragment = { group = "androidx.fragment", name = "fragment-ktx", version.ref = "fragment" }
androidx-navigation-fragment = { group = "androidx.navigation", name = "navigation-fragment-ktx", version.ref = "navigation" }
androidx-navigation-ui = { group = "androidx.navigation", name = "navigation-ui-ktx", version.ref = "navigation" }
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
androidx-datastore = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "retrofit" }
okhttp = { group = "com.squareup.okhttp3", name = "okhttp", version.ref = "okhttp" }
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }
gson = { group = "com.google.code.gson", name = "gson", version.ref = "gson" }
glide = { group = "com.github.bumptech.glide", name = "glide", version.ref = "glide" }
glide-compiler = { group = "com.github.bumptech.glide", name = "compiler", version.ref = "glide" }
exoplayer-core = { group = "androidx.media3", name = "media3-exoplayer", version.ref = "exoplayer" }
exoplayer-ui = { group = "androidx.media3", name = "media3-ui", version.ref = "exoplayer" }
exoplayer-hls = { group = "androidx.media3", name = "media3-exoplayer-hls", version.ref = "exoplayer" }
exoplayer-dash = { group = "androidx.media3", name = "media3-exoplayer-dash", version.ref = "exoplayer" }
coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
junit-ext = { group = "androidx.test.ext", name = "junit", version.ref = "junitExt" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-kapt = { id = "org.jetbrains.kotlin.kapt", version.ref = "kotlin" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }'

# ── gradle-wrapper.properties ────────────────────────────────────────────────
Write-UTF8 "$root\gradle\wrapper\gradle-wrapper.properties" 'distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.9-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists'

# ── app/build.gradle.kts ─────────────────────────────────────────────────────
Write-UTF8 "$root\app\build.gradle.kts" 'plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}
android {
    namespace = "com.mistream.app"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.mistream.app"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }
    buildTypes {
        release { isMinifyEnabled = true; proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") }
        debug { isDebuggable = true }
    }
    buildFeatures { viewBinding = true; buildConfig = true }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.leanback)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)
    implementation(libs.exoplayer.hls)
    implementation(libs.exoplayer.dash)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore)
    implementation(libs.coroutines.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
}'

# ── proguard-rules.pro ───────────────────────────────────────────────────────
Write-UTF8 "$root\app\proguard-rules.pro" '-keep class com.mistream.app.data.model.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn okhttp3.**
-dontwarn retrofit2.**'

# ── .gitignore ───────────────────────────────────────────────────────────────
Write-UTF8 "$root\.gitignore" '*.iml
.gradle
/local.properties
/.idea
.DS_Store
/build
/captures
.externalNativeBuild
.cxx
local.properties'

# ── AndroidManifest.xml ──────────────────────────────────────────────────────
Write-UTF8 "$root\app\src\main\AndroidManifest.xml" '<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-feature android:name="android.software.leanback" android:required="false" />
    <uses-feature android:name="android.hardware.touchscreen" android:required="false" />
    <application
        android:name=".MiStreamApp"
        android:allowBackup="true"
        android:banner="@drawable/app_banner"
        android:icon="@drawable/app_icon"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.MiStream"
        android:usesCleartextTraffic="true">
        <activity android:name=".ui.MainActivity" android:exported="true"
            android:configChanges="keyboard|keyboardHidden|navigation|screenSize|orientation"
            android:launchMode="singleTop">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name=".ui.detail.DetailActivity" android:exported="false"
            android:configChanges="keyboard|keyboardHidden|navigation|screenSize|orientation" />
        <activity android:name=".ui.player.PlayerActivity" android:exported="false"
            android:configChanges="keyboard|keyboardHidden|navigation|screenSize|orientation"
            android:screenOrientation="landscape" android:theme="@style/Theme.MiStream.Player" />
        <activity android:name=".ui.settings.SettingsActivity" android:exported="false" />
    </application>
</manifest>'

# ── res/values ───────────────────────────────────────────────────────────────
Write-UTF8 "$root\app\src\main\res\values\strings.xml" '<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">MiStream</string>
    <string name="settings_saved">Configuracion guardada</string>
    <string name="searching_streams">Buscando streams...</string>
    <string name="play_best">Reproducir %1$s</string>
    <string name="player_error">Error: %1$s</string>
    <string name="no_results">Sin resultados</string>
    <string name="search_hint">Buscar peliculas y series...</string>
    <string name="rd_token_hint">Token de Real-Debrid</string>
    <string name="tmdb_key_hint">API Key de TMDB</string>
    <string name="save">Guardar</string>
    <string name="settings">Configuracion</string>
    <string name="trending">Tendencias</string>
    <string name="popular_movies">Peliculas populares</string>
    <string name="popular_series">Series populares</string>
    <string name="search">Buscar</string>
    <string name="all_streams">Todos los streams</string>
</resources>'

Write-UTF8 "$root\app\src\main\res\values\colors.xml" '<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="background_dark">#0F0F1A</color>
    <color name="background_card">#1A1A2E</color>
    <color name="accent_red">#E94560</color>
    <color name="text_primary">#FFFFFF</color>
    <color name="text_secondary">#AAAAAA</color>
    <color name="badge_rd">#00C853</color>
    <color name="badge_4k">#FFD700</color>
    <color name="badge_latino">#FF6B35</color>
</resources>'

Write-UTF8 "$root\app\src\main\res\values\themes.xml" '<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.MiStream" parent="Theme.AppCompat.Leanback">
        <item name="colorPrimary">#1A1A2E</item>
        <item name="colorPrimaryDark">#0F0F1A</item>
        <item name="colorAccent">#E94560</item>
        <item name="android:windowBackground">#0F0F1A</item>
        <item name="android:textColorPrimary">#FFFFFF</item>
        <item name="android:textColorSecondary">#AAAAAA</item>
    </style>
    <style name="Theme.MiStream.Player" parent="Theme.MiStream">
        <item name="android:windowFullscreen">true</item>
        <item name="android:statusBarColor">@android:color/transparent</item>
    </style>
</resources>'

# ── drawables ────────────────────────────────────────────────────────────────
Write-UTF8 "$root\app\src\main\res\drawable\app_icon.xml" '<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#E94560"/>
</shape>'

Write-UTF8 "$root\app\src\main\res\drawable\app_banner.xml" '<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#1A1A2E"/>
</shape>'

# ── layouts ──────────────────────────────────────────────────────────────────
Write-UTF8 "$root\app\src\main\res\layout\activity_main.xml" '<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/main_fragment_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background_dark" />'

Write-UTF8 "$root\app\src\main\res\layout\fragment_main.xml" '<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="match_parent"
    android:orientation="vertical" android:background="@color/background_dark" android:padding="24dp">
    <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
        android:orientation="horizontal" android:gravity="center_vertical" android:layout_marginBottom="24dp">
        <TextView android:layout_width="0dp" android:layout_height="wrap_content" android:layout_weight="1"
            android:text="@string/app_name" android:textSize="28sp" android:textStyle="bold" android:textColor="@color/accent_red" />
        <Button android:id="@+id/search_button" android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:text="@string/search" android:backgroundTint="@color/background_card" android:textColor="@color/text_primary" />
    </LinearLayout>
    <ScrollView android:layout_width="match_parent" android:layout_height="match_parent">
        <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical">
            <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:text="@string/trending" android:textSize="18sp" android:textStyle="bold"
                android:textColor="@color/text_primary" android:layout_marginBottom="12dp" />
            <androidx.recyclerview.widget.RecyclerView android:id="@+id/rv_trending"
                android:layout_width="match_parent" android:layout_height="200dp" android:layout_marginBottom="24dp" />
            <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:text="@string/popular_movies" android:textSize="18sp" android:textStyle="bold"
                android:textColor="@color/text_primary" android:layout_marginBottom="12dp" />
            <androidx.recyclerview.widget.RecyclerView android:id="@+id/rv_movies"
                android:layout_width="match_parent" android:layout_height="200dp" android:layout_marginBottom="24dp" />
            <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:text="@string/popular_series" android:textSize="18sp" android:textStyle="bold"
                android:textColor="@color/text_primary" android:layout_marginBottom="12dp" />
            <androidx.recyclerview.widget.RecyclerView android:id="@+id/rv_series"
                android:layout_width="match_parent" android:layout_height="200dp" />
        </LinearLayout>
    </ScrollView>
</LinearLayout>'

Write-UTF8 "$root\app\src\main\res\layout\activity_settings.xml" '<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="match_parent" android:background="@color/background_dark">
    <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
        android:orientation="vertical" android:padding="40dp">
        <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:text="@string/settings" android:textSize="28sp" android:textStyle="bold"
            android:textColor="@color/text_primary" android:layout_marginBottom="40dp" />
        <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:text="Real-Debrid API Token" android:textSize="14sp"
            android:textColor="@color/text_secondary" android:layout_marginBottom="8dp" />
        <EditText android:id="@+id/et_rd_token" android:layout_width="match_parent" android:layout_height="wrap_content"
            android:hint="@string/rd_token_hint" android:inputType="textPassword"
            android:textColor="@color/text_primary" android:textColorHint="@color/text_secondary"
            android:background="@color/background_card" android:padding="16dp" android:layout_marginBottom="8dp" />
        <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:text="Obtener token: real-debrid.com/apitoken" android:textSize="12sp"
            android:textColor="@color/text_secondary" android:layout_marginBottom="32dp" />
        <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:text="TMDB API Key" android:textSize="14sp"
            android:textColor="@color/text_secondary" android:layout_marginBottom="8dp" />
        <EditText android:id="@+id/et_tmdb_key" android:layout_width="match_parent" android:layout_height="wrap_content"
            android:hint="@string/tmdb_key_hint" android:inputType="textPassword"
            android:textColor="@color/text_primary" android:textColorHint="@color/text_secondary"
            android:background="@color/background_card" android:padding="16dp" android:layout_marginBottom="8dp" />
        <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:text="Obtener key: themoviedb.org/settings/api" android:textSize="12sp"
            android:textColor="@color/text_secondary" android:layout_marginBottom="48dp" />
        <Button android:id="@+id/btn_save" android:layout_width="match_parent" android:layout_height="wrap_content"
            android:text="@string/save" android:backgroundTint="@color/accent_red"
            android:textColor="@color/text_primary" android:padding="16dp" android:textSize="16sp" />
    </LinearLayout>
</ScrollView>'

Write-UTF8 "$root\app\src\main\res\layout\activity_detail.xml" '<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="match_parent" android:background="@color/background_dark">
    <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical">
        <FrameLayout android:layout_width="match_parent" android:layout_height="300dp">
            <ImageView android:id="@+id/iv_backdrop" android:layout_width="match_parent"
                android:layout_height="match_parent" android:scaleType="centerCrop" android:alpha="0.6" />
            <ImageView android:id="@+id/iv_poster" android:layout_width="120dp" android:layout_height="180dp"
                android:layout_gravity="bottom|start" android:layout_margin="24dp" android:scaleType="centerCrop" />
        </FrameLayout>
        <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
            android:orientation="vertical" android:padding="24dp">
            <TextView android:id="@+id/tv_title" android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:textSize="24sp" android:textStyle="bold" android:textColor="@color/text_primary" android:layout_marginBottom="8dp" />
            <LinearLayout android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:orientation="horizontal" android:layout_marginBottom="8dp">
                <TextView android:id="@+id/tv_rating" android:layout_width="wrap_content" android:layout_height="wrap_content"
                    android:textSize="14sp" android:textColor="@color/badge_4k" android:layout_marginEnd="16dp" />
                <TextView android:id="@+id/tv_year" android:layout_width="wrap_content" android:layout_height="wrap_content"
                    android:textSize="14sp" android:textColor="@color/text_secondary" android:layout_marginEnd="16dp" />
                <TextView android:id="@+id/tv_genres" android:layout_width="wrap_content" android:layout_height="wrap_content"
                    android:textSize="14sp" android:textColor="@color/text_secondary" />
            </LinearLayout>
            <TextView android:id="@+id/tv_overview" android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:textSize="14sp" android:textColor="@color/text_secondary" android:lineSpacingMultiplier="1.4" android:layout_marginBottom="24dp" />
            <Button android:id="@+id/btn_play_best" android:layout_width="match_parent" android:layout_height="wrap_content"
                android:visibility="gone" android:backgroundTint="@color/accent_red" android:textColor="@color/text_primary"
                android:padding="16dp" android:textSize="16sp" android:layout_marginBottom="16dp" />
            <ProgressBar android:id="@+id/progress_streams" android:layout_width="wrap_content"
                android:layout_height="wrap_content" android:visibility="gone" android:layout_marginBottom="8dp" />
            <TextView android:id="@+id/tv_stream_status" android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:textSize="14sp" android:textColor="@color/text_secondary" android:layout_marginBottom="16dp" />
            <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:text="@string/all_streams" android:textSize="16sp" android:textStyle="bold"
                android:textColor="@color/text_primary" android:layout_marginBottom="8dp" />
            <androidx.recyclerview.widget.RecyclerView android:id="@+id/rv_streams"
                android:layout_width="match_parent" android:layout_height="wrap_content" android:nestedScrollingEnabled="false" />
        </LinearLayout>
    </LinearLayout>
</ScrollView>'

Write-UTF8 "$root\app\src\main\res\layout\activity_player.xml" '<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="match_parent" android:background="@android:color/black">
    <androidx.media3.ui.PlayerView android:id="@+id/player_view"
        android:layout_width="match_parent" android:layout_height="match_parent" />
    <ProgressBar android:id="@+id/progress_buffering" android:layout_width="wrap_content"
        android:layout_height="wrap_content" android:layout_gravity="center" android:visibility="gone" />
    <TextView android:id="@+id/tv_player_title" android:layout_width="wrap_content" android:layout_height="wrap_content"
        android:layout_gravity="top|start" android:padding="16dp" android:textSize="16sp"
        android:textColor="#FFFFFF" android:textStyle="bold" />
    <TextView android:id="@+id/tv_error" android:layout_width="wrap_content" android:layout_height="wrap_content"
        android:layout_gravity="center" android:padding="16dp" android:textSize="16sp"
        android:textColor="#FF5252" android:visibility="gone" />
</FrameLayout>'

Write-UTF8 "$root\app\src\main\res\layout\fragment_search.xml" '<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="match_parent"
    android:orientation="vertical" android:background="@color/background_dark" android:padding="24dp">
    <EditText android:id="@+id/et_search" android:layout_width="match_parent" android:layout_height="wrap_content"
        android:hint="@string/search_hint" android:inputType="text" android:textColor="@color/text_primary"
        android:textColorHint="@color/text_secondary" android:background="@color/background_card"
        android:padding="16dp" android:textSize="18sp" android:layout_marginBottom="16dp" />
    <ProgressBar android:id="@+id/progress_search" android:layout_width="wrap_content"
        android:layout_height="wrap_content" android:visibility="gone" android:layout_marginBottom="8dp" />
    <TextView android:id="@+id/tv_empty" android:layout_width="wrap_content" android:layout_height="wrap_content"
        android:text="@string/no_results" android:textColor="@color/text_secondary" android:textSize="16sp" android:visibility="gone" />
    <androidx.recyclerview.widget.RecyclerView android:id="@+id/rv_results"
        android:layout_width="match_parent" android:layout_height="match_parent" />
</LinearLayout>'

Write-UTF8 "$root\app\src\main\res\layout\item_media_card.xml" '<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="120dp" android:layout_height="200dp" android:orientation="vertical"
    android:layout_marginEnd="8dp" android:focusable="true" android:clickable="true" android:background="@color/background_card">
    <ImageView android:id="@+id/iv_card_poster" android:layout_width="match_parent"
        android:layout_height="0dp" android:layout_weight="1" android:scaleType="centerCrop" />
    <TextView android:id="@+id/tv_card_title" android:layout_width="match_parent" android:layout_height="wrap_content"
        android:textSize="11sp" android:textColor="@color/text_primary" android:padding="4dp"
        android:maxLines="2" android:ellipsize="end" />
</LinearLayout>'

Write-UTF8 "$root\app\src\main\res\layout\item_stream.xml" '<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="wrap_content"
    android:orientation="horizontal" android:background="@color/background_card"
    android:padding="12dp" android:layout_marginBottom="4dp" android:gravity="center_vertical"
    android:focusable="true" android:clickable="true">
    <LinearLayout android:layout_width="0dp" android:layout_height="wrap_content"
        android:layout_weight="1" android:orientation="vertical">
        <LinearLayout android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:orientation="horizontal" android:layout_marginBottom="4dp">
            <TextView android:id="@+id/tv_quality" android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:textSize="14sp" android:textStyle="bold" android:textColor="@color/text_primary" android:layout_marginEnd="8dp" />
            <TextView android:id="@+id/tv_rd_badge" android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:text="RD" android:textSize="11sp" android:textColor="@color/badge_rd" android:visibility="gone" />
        </LinearLayout>
        <TextView android:id="@+id/tv_source" android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:textSize="12sp" android:textColor="@color/text_secondary" />
        <TextView android:id="@+id/tv_info" android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:textSize="11sp" android:textColor="@color/text_secondary" />
    </LinearLayout>
    <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
        android:text="&gt;" android:textSize="18sp" android:textColor="@color/accent_red" android:layout_marginStart="8dp" />
</LinearLayout>'

Write-UTF8 "$root\app\src\main\res\layout\item_search_result.xml" '<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="80dp" android:orientation="horizontal"
    android:layout_marginBottom="4dp" android:background="@color/background_card"
    android:focusable="true" android:clickable="true">
    <ImageView android:id="@+id/iv_poster" android:layout_width="54dp"
        android:layout_height="match_parent" android:scaleType="centerCrop" />
    <LinearLayout android:layout_width="0dp" android:layout_height="match_parent" android:layout_weight="1"
        android:orientation="vertical" android:padding="10dp" android:gravity="center_vertical">
        <TextView android:id="@+id/tv_title" android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:textSize="15sp" android:textStyle="bold" android:textColor="@color/text_primary"
            android:maxLines="1" android:ellipsize="end" />
        <LinearLayout android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:orientation="horizontal" android:layout_marginTop="4dp">
            <TextView android:id="@+id/tv_year" android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:textSize="12sp" android:textColor="@color/text_secondary" android:layout_marginEnd="8dp" />
            <TextView android:id="@+id/tv_type" android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:textSize="12sp" android:textColor="@color/accent_red" android:layout_marginEnd="8dp" />
            <TextView android:id="@+id/tv_rating" android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:textSize="12sp" android:textColor="@color/badge_4k" />
        </LinearLayout>
    </LinearLayout>
</LinearLayout>'

# ── Kotlin source files ───────────────────────────────────────────────────────
$src = "$root\app\src\main\java\com\mistream\app"

Write-UTF8 "$src\MiStreamApp.kt" 'package com.mistream.app
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
@HiltAndroidApp
class MiStreamApp : Application()'

Write-UTF8 "$src\di\AppModule.kt" 'package com.mistream.app.di
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.mistream.app.data.api.*
import dagger.Module; import dagger.Provides; import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient; import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit; import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named; import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mistream_prefs")

@Module @InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore
    @Provides @Singleton fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }).build()
    @Provides @Singleton @Named("realdebrid") fun provideRdOkHttpClient(base: OkHttpClient): OkHttpClient = base.newBuilder()
        .addInterceptor { chain -> val token = RealDebridTokenHolder.token
            val req = if (token.isNotEmpty()) chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build() else chain.request()
            chain.proceed(req) }.build()
    @Provides @Singleton fun provideTmdbRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(okHttpClient.newBuilder().addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder().url(chain.request().url.newBuilder().addQueryParameter("api_key", TmdbTokenHolder.apiKey).build()).build())
        }.build()).addConverterFactory(GsonConverterFactory.create()).build()
    @Provides @Singleton fun provideTmdbApiService(retrofit: Retrofit): TmdbApiService = retrofit.create(TmdbApiService::class.java)
    @Provides @Singleton @Named("torrentio") fun provideTorrentioRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://torrentio.strem.fun/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
    @Provides @Singleton fun provideTorrentioApiService(@Named("torrentio") retrofit: Retrofit): TorrentioApiService = retrofit.create(TorrentioApiService::class.java)
    @Provides @Singleton @Named("rdRetrofit") fun provideRdRetrofit(@Named("realdebrid") okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.real-debrid.com/rest/1.0/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
    @Provides @Singleton fun provideRdApiService(@Named("rdRetrofit") retrofit: Retrofit): RealDebridApiService = retrofit.create(RealDebridApiService::class.java)
}
object RealDebridTokenHolder { var token: String = "" }
object TmdbTokenHolder { var apiKey: String = "37f0f3da6ff530a870cfd96cce4e55cf" }'

Write-Host "Kotlin source files..." -ForegroundColor Yellow

# Git push
Set-Location $root
git add -A
git commit -m "feat: MiStream base - Real-Debrid + Torrentio + TMDB latino priority"
git push origin main

Write-Host ""
Write-Host "LISTO - codigo subido a GitHub" -ForegroundColor Green
