# Add this global rule
-keepattributes Signature

# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models.
# Modify this rule to fit the structure of your app.
-keepclassmembers class com.wasingun.seller_lounge.data.model.** {
    *;
}
-keepclassmembers class com.wasingun.seller_lounge.network.** {
    *;
}