// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("neuroger");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("neuroger")
//      }
//    }
#include <jni.h>
#include <string>
/* Include files */
#include "rt_nonfinite.h"
#include "getglobalCDR.h"
#include "getglobalCDR_terminate.h"
#include "getglobalCDR_initialize.h"

// https://github.com/android/ndk/wiki/JNI
// https://www3.ntu.edu.sg/home/ehchua/programming/java/javanativeinterface.html
// https://champlnx.blogspot.com/2019/06/android-ndk-passing-complex-data-types.html

std::string ConvertJString(JNIEnv *env, jstring str) {
    if (!str) std::string();

    const jsize len = env->GetStringUTFLength(str);
    const char *strChars = env->GetStringUTFChars(str, (jboolean *) nullptr);

    std::string Result(strChars, len);

    env->ReleaseStringUTFChars(str, strChars);

    return Result;
}

static double main_getglobalCDR(double boxscores[6]) {
//    double boxscores[6] = {2, 1, 1, 1, 3, 3}; cdr = 2
    double CDR_data[5];
    int CDR_size[2];
    double CDR_SB;
    boolean_T rules[6];
    boolean_T excep[3];

    /* Initialize function 'getglobalCDR' input arguments. */
    /* Initialize function input argument 'boxscores'. */
    /* Call the entry-point 'getglobalCDR'. */
    getglobalCDR(boxscores, CDR_data, CDR_size, &CDR_SB, rules, excep);

    return CDR_data[0];
}

extern "C" JNIEXPORT
jdouble Java_software_cneuro_neuroger_ui_input_questionnaire_QuestionnairePagerFragment_evaluateCDR(
        JNIEnv *env,
        jobject /* this */,
        jdoubleArray boxscores) {
    /* Initialize the application.
       You do not need to do this more than one time. */
    getglobalCDR_initialize();

    jdouble *tmpArray = env->GetDoubleArrayElements(boxscores, nullptr);
    /* Invoke the entry-point functions.
       You can call entry-point functions multiple times. */
    double result = main_getglobalCDR(tmpArray);

    /* Terminate the application.
       You do not need to do this more than one time. */
    getglobalCDR_terminate();

//    std::string str = ConvertJString(env, string1);
//    std::string hello = "Hello from C++ and ";
//    return env->NewStringUTF(hello.c_str());
    env->ReleaseDoubleArrayElements(boxscores, tmpArray, 0);
    return result;
}