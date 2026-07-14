
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("neuroger")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        neuroger

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        neuroger.cpp)

add_library( # Sets the name of the library.
        getglobalCDR

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        getglobalCDR.c)

add_library( # Sets the name of the library.
        getglobalCDR_initialize

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        getglobalCDR_initialize.c)

add_library( # Sets the name of the library.
        getglobalCDR_terminate

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        getglobalCDR_terminate.c)

add_library( # Sets the name of the library.
        sum

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        sum.c)

add_library( # Sets the name of the library.
        sortIdx

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        sortIdx.c)

add_library( # Sets the name of the library.
        sort1

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        sort1.c)

add_library( # Sets the name of the library.
        sign

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        sign.c)

add_library( # Sets the name of the library.
        rtGetNaN

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        rtGetNaN.c)

add_library( # Sets the name of the library.
        rtGetInf

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        rtGetInf.c)

add_library( # Sets the name of the library.
        rt_nonfinite

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        rt_nonfinite.c)

add_library( # Sets the name of the library.
        ifWhileCond

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ifWhileCond.c)

add_library( # Sets the name of the library.
        histcounts

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        histcounts.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
target_link_libraries( # Specifies the target library.
        neuroger # Indicate that getglobalCDR, getglobalCDR_initialize and getglobalCDR_terminate is needed for neuroger
        getglobalCDR
        getglobalCDR_initialize
        getglobalCDR_terminate

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        getglobalCDR
        ifWhileCond
        rt_nonfinite
        sum
        sort1
        sign
        histcounts

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        getglobalCDR_initialize
        rt_nonfinite

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        ifWhileCond
        rt_nonfinite

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        rt_nonfinite
        rtGetNaN
        rtGetInf

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        sign
        rt_nonfinite

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        sort1
        rt_nonfinite
        sortIdx

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        sortIdx
        rt_nonfinite

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        sum
        rt_nonfinite

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        histcounts
        rt_nonfinite

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
