# Speedometer Android

A lightweight circular indicator view library for Android

[![](https://jitpack.io/v/ibrahimsn98/speedometer.svg)](https://jitpack.io/#ibrahimsn98/speedometer)
[![](https://androidweekly.net/issues/issue-443/badge)](https://androidweekly.net/issues/issue-443)

## GIF

<img src="https://github.com/ibrahimsn98/speedometer/blob/master/art/speedometer.gif" width="480" />

## XML Attributes

```xml
<me.ibrahimsn.lib.Speedometer
    android:id="@+id/speedometer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:maxSpeed="100"
    app:borderSize="8dp"
    app:metricText="km/h"
    app:borderColor="#402c47"
    app:fillColor="#d83a78"
    app:textColor="#f5f5f5"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"/>
```

## Usage

```kotlin
val speedometer = findViewById<Speedometer>(R.id.speedometer)

speedometer.setSpeed(95, 1000L) // speed: Int, duration: Long

speedometer.setSpeed(95, 1000L, {
    // Do on animation end
})
```

## Setup
Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.ibrahimsn98:speedometer:1.0.1'
}
```

## License
```
MIT License

Copyright (c) 2020 İbrahim Süren

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
<<<<<<< HEAD

> Follow me on Twitter [@ibrahimsn98](https://twitter.com/ibrahimsn98)
=======
>>>>>>> d66541b821538b9f1106765ee0a3adb3dc60e473
