//
// Created by Danvan.Mandela on 04/04/2024.
//
#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_craftsilicon_shumul_agency_data_security_AppStaticKeys_iv(JNIEnv *env, jobject thiz) {
    std::string iv = "84jfkfndl3ybdfkf";
    return env->NewStringUTF(iv.c_str());

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_craftsilicon_shumul_agency_data_security_AppStaticKeys_kv(JNIEnv *env, jobject thiz) {
    std::string kv = "csXDRzpcEPm_jMny";
    return env->NewStringUTF(kv.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_craftsilicon_shumul_agency_data_security_AppStaticKeys_longKeyValue(JNIEnv *env,
                                                                             jobject thiz) {
    std::string key = "KBSB&er3bflx9%";
    return env->NewStringUTF(key.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_craftsilicon_shumul_agency_data_security_AppStaticKeys_secretKey(JNIEnv *env,
                                                                          jobject thiz) {
    std::string key = "KBSB&er3bflx9%";
    return env->NewStringUTF(key.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_craftsilicon_shumul_agency_data_security_AppStaticKeys_publicKey(JNIEnv *env,
                                                                          jobject thiz) {
    std::string hello = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqO5uXE1g0Dviu35LYowEwUPUGPy31pHO5dPeBUXmSOYPl4wqcSpjQT0Us5BuZDGJI1hOxFxdhzf0STfQueK9l4EjqDJuDTQPoJcJxxzyPO2Qd3fWLh4Mh1eoqT7nEbK12Zvxy663+xP3pb0VqjVgAI8wI8CA2GHzhBACFtANu7N2z6hsCudM9t4tmFY4NtHlNbSBQQa5bnkcNu57SUPk23Vw1Em/6W6a4X9rxyxRqERiZgywuDvgLWEOYt9rlhBBwN8u+jOlxyqPux+yOLTp0z6h1fmc9hAENOw2amWNjngaUP6f6lmU4PrNg30msbzNk9f3SuTuUEsz5QLBOGbLuQIDAQAB";
    return env->NewStringUTF(hello.c_str());
}