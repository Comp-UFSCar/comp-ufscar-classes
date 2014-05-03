/**
 @file PythonCallsJava.h
 
 @brief In this file declared functions, that are exported from the library.

 @author Sergey.Karakovskiy at idsia.ch, Nikolay.Sohryakov at gmail.com
 
 You can add your own functions to be exportd in the section @e Custom @e Code at the end of the file.
 Functions declared here are implemented in @e PythonCallsJava.cc file.
 */
#include "arrayutils.h"
#ifndef _PYTHONCALLSJAVA_H
#define	_PYTHONCALLSJAVA_H

#ifdef _WIN32
#define _AMICOPYJAVA_API __declspec(dllexport)
#else
#define _AMICOPYJAVA_API
#endif

#ifdef __cplusplus
extern "C" {
#endif

_AMICOPYJAVA_API void amicoInitialize(int nOptions = 0, ...);
_AMICOPYJAVA_API void destroyEnvironment();

/*
 * Mario AI Specific methods:
 */
_AMICOPYJAVA_API void initMarioAIBenchmark();
_AMICOPYJAVA_API void createMarioEnvironment(const char* javaClassName);
_AMICOPYJAVA_API void reset(char* setUpOptions);
_AMICOPYJAVA_API bool isLevelFinished();
_AMICOPYJAVA_API void tick();
_AMICOPYJAVA_API PyObject* getEvaluationInfo() ;
_AMICOPYJAVA_API PyObject* buildPythonTuple(JNIEnv* env,
											jintArray serializedLevelScene,
											jintArray serializedEnemies,
										    jfloatArray marioPos,
										    jfloatArray enemiesPos,
										    jintArray marioState);
_AMICOPYJAVA_API PyObject* getEntireObservation(int zLevelScene, int zLevelEnemies);
_AMICOPYJAVA_API PyObject* getObservationDetails();
_AMICOPYJAVA_API void performAction(int* action);
//_AMICOPYJAVA_API PyObject* getReceptiveFieldInfo();
/*
 *
 */

#ifdef __cplusplus
}
#endif


#endif	/* _PYTHONCALLSJAVA_H */

