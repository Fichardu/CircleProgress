# CircleProgress

A Circle Progress View with a rotate animation. Just make for fun. Hope you enjoy it.

# Quick Look

<img src="./art/progress.gif" width="360">

# Usage

```xml
<me.fichardu.circleprogress.CircleProgress
        android:id="@+id/progress"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_height="wrap_content"
        circleprogress:color1="@android:color/holo_red_light"
        circleprogress:color2="@android:color/holo_green_light"
        circleprogress:color3="@android:color/holo_blue_light" />
```

```java
mProgressView.startAnim();
mProgressView.stopAnim();
mProgressView.reset();
mProgressView.setDuration();
mProgressView.setInterpolator();
```

# Analyse

[Analyse Blog post here](http://fichardu.github.io/blog/circle-progress-view/)
