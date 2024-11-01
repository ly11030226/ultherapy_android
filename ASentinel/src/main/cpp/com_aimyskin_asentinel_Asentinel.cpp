//
// Created by GuHaijun on 9/27/22.
//

#include <jni.h>
#include <string>
#include "sentinel.hpp"

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_aimyskin_asentinel_Asentinel
 * Method:    generateToken
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_aimyskin_asentinel_Asentinel_generateToken
        (JNIEnv *env, jobject) {
    AppSentinel::Sentinel *processer = AppSentinel::Sentinel::instance();
    std::string hello = processer->generateToken();
    return env->NewStringUTF(hello.c_str());
}

/*
 * Class:     com_aimyskin_asentinel_Asentinel
 * Method:    verifyToken
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_aimyskin_asentinel_Asentinel_verifyToken
        (JNIEnv *env, jobject, jstring token) {
    const char *ctoken = env->GetStringUTFChars(token, nullptr);
    std::string cpptoken = std::string(ctoken);
    env->ReleaseStringUTFChars(token, ctoken);
    AppSentinel::Sentinel *processer = AppSentinel::Sentinel::instance();
    bool ret = processer->verifyToken(cpptoken);
    return (jboolean)ret;
}

#ifdef __cplusplus
}
#endif