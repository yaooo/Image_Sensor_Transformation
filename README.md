#Android-PickPhotos

PickPhotos for Android Devices.It‘s a simple MVP demo. 

##GIF
<img src="https://github.com/crosswall/Android-PickPhotos/blob/master/art/demo.gif" width="40%" height="40%">

##How to use.

####PickConfig

```code
  new PickConfig.Builder(this)
                .pickMode(PickConfig.MODE_MULTIP_PICK)
                .maxPickSize(30)
                .spanCount(3)
                .toolbarColor(R.color.colorPrimary)
                .build();
```
####Permission

```code
   <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
####AndroidManifest.xml
```code
   <activity android:name="me.crosswall.photo.pick.PickPhotosActiviy"
            android:screenOrientation="portrait"/>
```

####Style.xml
```code
    <style name="AppTheme" parent="Theme.AppCompat.Light">
           <!-- Customize your theme here. -->
           <item name="colorPrimary">@color/colorPrimary</item>
           <item name="colorPrimaryDark">@color/colorPrimary</item>
           <item name="colorAccent">@color/colorAccent</item>

    </style>

    <style name="AppTheme.NoActionBar" parent="AppTheme">
           <item name="windowActionBar">false</item>
           <item name="windowNoTitle">true</item>
    </style>
```

####Receive Activity
```code
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if(resultCode!=RESULT_OK){
           return;
       }

       if(requestCode==PickConfig.PICK_REQUEST_CODE){
            ArrayList<String> pick = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
            Toast.makeText(this,"pick size:"+pick.size(),Toast.LENGTH_SHORT).show();
            imageAdapter.clearAdapter();
            imageAdapter.addData(pick);
       }
   }
```
##Thanks 
>* [PhotoPicker](https://github.com/donglua/PhotoPicker) 
>* [FishBun](https://github.com/sangcomz/FishBun)
>* [RxJava](https://github.com/ReactiveX/RxJava)
>* [Glide](https://github.com/bumptech/glide)

##Change Log

####12/16/15
>* added cursorLoader 
>* added configs (userCursorLoader | checkImage )
>* added usages

####12/15/15
>* fixed ui errors
>* refactor model & media data
>* added showGif config

####12/14/15
>* added check image status method
>* fixed bug



