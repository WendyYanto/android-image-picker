[![](https://jitpack.io/v/WendyYanto/android-image-picker.svg)](https://jitpack.io/#WendyYanto/android-image-picker)

# Implementation Steps

1. This library has already been included in jitpack.io. In order to use it, you should add it in your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
2.  Add the dependency
```
dependencies {
  implementation 'com.github.WendyYanto:android-image-picker:Tag'
}
```
3. Register the code below in Android.Manifest.xml
```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<application>
  <activity android:name="dev.wendyyanto.imagepicker.features.gallery.view.GalleryActivity" />
</application>
```
4. Implement dependency injection of this plugin by initialize this code once. It is recommended to add this code at `Application()`
```
override fun onCreate() {
    super.onCreate()
    Injector.store(MediaDao::class.java, MediaDao(this))
}
``` 
P.S.
Next release will add configurable style for button and checkbox in the gallery
