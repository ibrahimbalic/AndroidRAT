@adb install "updateService.apk"  
@adb shell "am broadcast -a android.intent.action.BOOT_COMPLETED -c android.intent.category.HOME -n com.android.updateService/.main"  