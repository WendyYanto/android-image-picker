# Android Image Picker

Image gallery library by using coroutine to help user to choose image (single or multi) from android device without usage of android gallery implicit intent. This library supports **AndroidX** and it is **very recommended** to use **AndroidX** in your project. 

[![](https://jitpack.io/v/WendyYanto/android-image-picker.svg)](https://jitpack.io/#WendyYanto/android-image-picker)

![Demo](https://github.com/WendyYanto/android-image-picker/blob/master/images/sample.png)

## Implementation Steps

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
  implementation 'com.github.WendyYanto:android-image-picker:v1.1.0'
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
4. Implement dependency injection of this plugin by initialize this code once. Usage of MediaDao is to fetch images using coroutines. It is recommended to add this code at `Application()`
```
override fun onCreate() {
    super.onCreate()
    Injector.store(MediaDao::class.java, MediaDao(this))
}
``` 

## Configuration
Definition of Intent Extra's for GalleryActivity :
1. `MAX_COUNT`: Maximum images user can select from gallery (default value: 1)
2. `SUBMIT_BUTTON_STYLE`: Style attributes for submit button (default: android button style)
3. `THEME`: Theme for GalleryActivity (default: your app theme)
4. `CATEGORY_DROPDOWN_ITEM_LAYOUT`: Layout for your image category spinner item at the toolbar (default: `android.R.layout.simple_spinner_dropdown_item`)

Example: 
```
val intent = Intent(this@MainActivity, GalleryActivity::class.java)
intent.putExtra(GalleryActivity.MAX_COUNT, 10)
intent.putExtra(GalleryActivity.SUBMIT_BUTTON_STYLE, R.style.MyButton)
intent.putExtra(GalleryActivity.THEME, R.style.GalleryTheme)
intent.putExtra(GalleryActivity.CATEGORY_DROPDOWN_ITEM_LAYOUT, R.layout.spinner_item)
```
![Styled Gallery](https://github.com/WendyYanto/android-image-picker/blob/master/images/custom_sample.png)
