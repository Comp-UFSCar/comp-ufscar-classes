/**
 * @file	PythonCallsJava.cc
 * @brief	PythonCallsJava main file
 *
 * @author Sergey Karakovskiy, sergey at idsia.ch ; Nikolay Sohryakov, Nikolay.Sohryakov at gmail.com
 *
 * Keep in mind, that ID of the @c Java method is either a pointer to the @c
 * _jmethodID @c Java class or just the value of type @c jmethodID.
 *
 * This is a customized Mario AI specific version of AmiCo.
 * It supports the PyJava direction. In this set up Mario AI benchmark is used
 * as passive entity and only as evaluation function. MarioEnvironment singleton
 * is accessed to evaluate the agent and perform one interaction: the @c tick()
 * of the MarioEnvironment.
 */ 

#include <Python.h>

#ifdef __APPLE__
 #include <sys/stat.h>
 #include <sys/resource.h>
 #include <pthread.h>
 //#undef __CONDITIONALMACROS__
 #include <CoreFoundation/CoreFoundation.h>
#else
 #include <jni.h>
#endif
#include <iostream>
#include "PythonCallsJava.h"
#include "arrayutils.h"
#include <vector>
#include <map>
#include <string>
#include <stdint.h>
#include <stdarg.h>
#include <queue>
#include <typeinfo>
#include <string.h>
#include <algorithm>

/**
 Version of the arguments of the @c Java @c Virtual @c Machine.
 */
const int JVM_VERSION = 0x00010004;

static JNIEnv *env = NULL; /**< Method invokation is performed with @c JNIEnv */
static JavaVM *jvm; /**< Pointer to the @c JVM */

static jclass jClass; /**< Pointer to the loaded @c Java class */
static jobject obj; /**< Main object */

static int numberOfButtons = 6;

static jmethodID midReset;
static jmethodID midIsLevelFinished;
static jmethodID midTick;
static jmethodID midGetEvaluationInfo;
//static jmethodID midGetEntireObservation;
static jmethodID midGetSerializedLevelSceneObservationZ;
static jmethodID midGetSerializedEnemiesObservationZ;
static jmethodID midGetMarioFloatPos;
static jmethodID midGetEnemiesFloatPos;
static jmethodID midGetMarioState;
static jmethodID midPerformAction;
static jmethodID midGetObservationDetails;
//static jmethodID midGetReceptiveFieldWidth;
//static jmethodID midGetReceptiveFieldHeight;
//static jmethodID midGetMarioReceptiveFieldCenter;

_AMICOPYJAVA_API void initMarioAIBenchmark();
/*
 *
 */


/**
 Function @c setArraySize must be invoked before calling of the method that takes arrays as arguments.
 
 @brief Dynamic array keeping number of elements in corresponding array in method args. 
*/
unsigned int* arraysSize;

const char funcSeparator = '#'; /**< Separator between function name and signature */

void destroyVM();

/**
 @brief Intializes @c amico library.

 Creates a @c JVM with given startup options. If they were errors they will be printed to the standard error output.

 @param nOptions Number of startup options.
 @param ... variable list of @c JVM startup options given as strings(i.e. each parameter is a separate startup option).
 */
_AMICOPYJAVA_API void amicoInitialize(int nOptions, ...)
{
    if (env != NULL)
    {
        std::cerr << "\nAmiCo: WARNING: Attempt to recreate JVM environment rejected. Using existing one\n";
        std::cerr.flush();
        return;
    }
    jint res;

    JavaVMInitArgs vm_args;
    vm_args.nOptions = nOptions;
    JavaVMOption* options = new JavaVMOption[nOptions];

    if (nOptions > 0)
    {
        va_list vl;
        va_start(vl, nOptions);
        for (int i = 0; i < nOptions; i++)
        {
            options[i].optionString = va_arg(vl, char*);
        }
        va_end(vl);

        if (nOptions > 0)
            vm_args.options = options;
    }

    vm_args.version = JVM_VERSION;
    vm_args.ignoreUnrecognized = JNI_FALSE; // do not ignore unrecognized JVM startup options
    /* Create the Java VM */

    std::cout << "AmiCo: Trying to create a JVM ..." << std::endl; std::cout.flush();
    res = JNI_CreateJavaVM(&jvm, (void**) &env, &vm_args);

    if (res < 0)
    {
        std::cerr << "\nAmiCo: Can't create Java VM\n";
        std::cerr.flush();
        return;
    }

    std::cout << "AmiCo: Initialization finished. JVM created succesfully" << std::endl; std::cout.flush();
}

/**
 @brief Takes a jmethodID of the method by it's name and signature and validates it.

 If not valid, then destroy @c JVM.

 @param methodName name of the method
 @param methodSig signature of the method

 @return ID of the method - @c jmethodID
 */
jmethodID getMethodIDSafe(const char* methodName, const char* methodSig)
{
    jmethodID mid = (env)->GetMethodID(jClass, methodName, methodSig);
    if (mid == NULL)
	{
        std::cerr << "AmiCo: Can't find method " << methodName << " with signature " << methodSig << std::endl;
        std::cerr.flush();
        destroyVM();
    } else
    {
        std::cout << "AmiCo: " << methodName << " found!! " << mid << std::endl;
        std::cout.flush();
    }

    return mid;
}

/**
 @brief Unloads @c Java class from the memory and destroys @c JVM.

 If they were errors they will be printed to the standard error output.
 */
_AMICOPYJAVA_API void destroyEnvironment()
{
    //std::cout <<std::endl << "Destroying JVM!" << std::endl;
    if (arraysSize != NULL)
        delete[] arraysSize;

    (jvm)->DestroyJavaVM();
    exit(0);
}

/** 
 @brief Destroys @c Java @c Virtual @c Machine.
 
 This function used when internal error occured and JVM should be destroyed.
 Return code is -1.
 */
void destroyVM() {
    if (arraysSize != NULL)
        delete[] arraysSize;
    
    std::cerr << "\n\nAmiCo: Internal ERROR! DESTROYING VM\n\n";
    std::cerr.flush();
    if ((env)->ExceptionOccurred()) {
        (env)->ExceptionDescribe();
    }
    (jvm)->DestroyJavaVM();
    exit(-1);
}

_AMICOPYJAVA_API void createMarioEnvironment(const char* javaClassName)
{
    std::cout << "AmiCo: Search for class: " << javaClassName;
    jClass = (env)->FindClass(javaClassName);
    if (jClass == NULL)
    {
        std::cerr << std::endl << "AmiCo: Can't find class " << javaClassName << std::endl;
        std::cerr.flush();
        destroyVM();
    }

    std::cout << "\nAmiCo: Creating new object:\n";
    std::cout << "\nAmiCo: Class =" << jClass << "   env = " << env << std::endl;
    std::cout.flush();

    jmethodID jm = env->GetStaticMethodID(jClass, "getInstance", "()Lch/idsia/benchmark/mario/environments/MarioEnvironment;");
    if (jm == NULL)
    {
        std::cerr << std::endl << "AmiCo: Can't find static method getInstance for " << javaClassName << std::endl;
        std::cerr.flush();
        destroyVM();
    }
    obj = (env)->CallStaticObjectMethod(jClass, jm);

    if (obj == NULL)
    {
        std::cerr << std::endl << "AmiCo: Can't Create Object " << javaClassName << std::endl;
        std::cerr.flush();
        destroyVM();
    } else
        std::cout << std::endl << "AmiCo: Object created succesfully: " << obj << std::endl;

    initMarioAIBenchmark();
}

_AMICOPYJAVA_API void initMarioAIBenchmark()
{
    midReset = getMethodIDSafe("reset", "(Ljava/lang/String;)V");
    midIsLevelFinished = getMethodIDSafe("isLevelFinished", "()Z");
    midTick = getMethodIDSafe("tick", "()V");
    midGetEvaluationInfo = getMethodIDSafe("getEvaluationInfoAsInts", "()[I");
    midGetSerializedLevelSceneObservationZ = getMethodIDSafe("getSerializedLevelSceneObservationZ", "(I)[I");
    midGetSerializedEnemiesObservationZ = getMethodIDSafe("getSerializedEnemiesObservationZ", "(I)[I");
    midGetMarioFloatPos = getMethodIDSafe("getMarioFloatPos", "()[F");
    midGetEnemiesFloatPos = getMethodIDSafe("getEnemiesFloatPos", "()[F");
    midGetMarioState = getMethodIDSafe("getMarioState", "()[I");
    midPerformAction = getMethodIDSafe("performAction", "([Z)V");
	midGetObservationDetails = getMethodIDSafe("getObservationDetails", "()[I");
    //midGetReceptiveFieldWidth = getMethodIDSafe("getReceptiveFieldWidth", "()I");
    //midGetReceptiveFieldHeight = getMethodIDSafe("getReceptiveFieldHeight", "()I");
    //midGetMarioReceptiveFieldCenter = getMethodIDSafe("getMarioReceptiveFieldCenter", "()[I");
}

/*
PyObject* getReceptiveFieldInfo()
{
    int width = env->CallIntMethod(obj, midGetReceptiveFieldWidth);
    int height = env->CallIntMethod(obj, midGetReceptiveFieldHeight);

    jintArray marioCoords = (jintArray)env->CallObjectMethod(obj, midGetMarioReceptiveFieldCenter); //mario receptive field center
    jint* center = (jint*) env->GetPrimitiveArrayCritical(marioCoords, NULL);


    PyObject* res = PyTuple_New(4);
    PyTuple_SET_ITEM(res, (Py_ssize_t) 0, PyInt_FromLong(width));
    PyTuple_SET_ITEM(res, (Py_ssize_t) 1, PyInt_FromLong(height));
    PyTuple_SET_ITEM(res, (Py_ssize_t) 2, PyInt_FromLong(center[0]));
    PyTuple_SET_ITEM(res, (Py_ssize_t) 3, PyInt_FromLong(center[1]));

    env->ReleasePrimitiveArrayCritical(marioCoords, center, 0);

    return res;
}
*/
/**
 @brief Validates ID of the method

 Parses signature in to the @c mthdSign type.
 To identify type @c String used symbol @e L without suffix(path to the class).

 @param midUInt ID of the method

 @return True if ID is valid, false otherwise.
 */
inline bool check_midUInt(void* midUInt)
{
    if (midUInt == NULL)
    {
        std::cerr << "AmiCo: midUInt is NULL!" << std::endl;
        std::cerr.flush();
        return false;
    }
    return true;
}

jobject safeCallObjectMethod(jmethodID mid, int arg)
{
    if (!check_midUInt((void*)mid))
        return NULL;

    jobject a = (env)->CallObjectMethod(obj, mid, arg);
    if (a == NULL) {
        fprintf(stderr, "C++: Mario AmiCo Error: array is NULL. Destroying VM.");
        fflush(stderr);
        destroyVM();
    } else {
        return a;
    }
}

_AMICOPYJAVA_API void reset(char* setUpOptions)
{
    jobject setUpOptionsjobject = (env)->NewStringUTF(setUpOptions);//<char*, jobjectArray, jobject>(env, setUpOptions, 'L', size);
    (env)->CallVoidMethod(obj, midReset, setUpOptionsjobject);

    env->DeleteLocalRef(setUpOptionsjobject);
}

_AMICOPYJAVA_API bool isLevelFinished()
{
    return (env)->CallBooleanMethod(obj, midIsLevelFinished);
}

_AMICOPYJAVA_API void tick()
{
    (env)->CallVoidMethod(obj, midTick);
}

_AMICOPYJAVA_API PyObject* getEvaluationInfo()
{
    jintArray a = (jintArray) (env)->CallObjectMethod(obj, midGetEvaluationInfo);
    PyObject* p = convertJavaArrayToPythonArray<jintArray, jint> (env, a, 'I');
    env->DeleteLocalRef(a);
    return p ;
}

_AMICOPYJAVA_API PyObject* buildPythonTuple(JNIEnv* env,
										    jintArray serializedLevelScene,
									 	    jintArray serializedEnemies,
									   	    jfloatArray marioPos,
										    jfloatArray enemiesPos,
										    jintArray marioState)
{
    PyObject* items = PyTuple_New(5);
    PyObject* o = convertJavaArrayToPythonArray<jintArray, jint>(env, serializedLevelScene, 'I');
    PyTuple_SET_ITEM(items, (Py_ssize_t)0, o);
    o = convertJavaArrayToPythonArray<jintArray, jint>(env, serializedEnemies, 'I');
    PyTuple_SET_ITEM(items, (Py_ssize_t)1, o);
    o = convertJavaArrayToPythonArray<jfloatArray, jfloat>(env, marioPos, 'F');
    PyTuple_SET_ITEM(items, (Py_ssize_t)2, o);
    o = convertJavaArrayToPythonArray<jfloatArray, jfloat>(env, enemiesPos, 'F');
    PyTuple_SET_ITEM(items, (Py_ssize_t)3, o);
    o = convertJavaArrayToPythonArray<jintArray, jint>(env, marioState, 'I');
    PyTuple_SET_ITEM(items, (Py_ssize_t)4, o);

    return items;
}

_AMICOPYJAVA_API PyObject* getEntireObservation(int zLevelScene, int zLevelEnemies)
{
    PyObject* pyTuple = 0;

//    std::cout << "serializedLevelScene\n"; std::cout.flush();
    jintArray serializedLevelScene = (jintArray)safeCallObjectMethod(midGetSerializedLevelSceneObservationZ, zLevelScene);
//    std::cout << "serializedEnemies\n"; std::cout.flush();
    jintArray serializedEnemies = (jintArray)safeCallObjectMethod(midGetSerializedEnemiesObservationZ, zLevelEnemies);
//    std::cout << "marioPos\n"; std::cout.flush();
    jfloatArray marioPos = (jfloatArray) safeCallObjectMethod(midGetMarioFloatPos, zLevelEnemies);
//    std::cout << "enemiesPos\n"; std::cout.flush();
    jfloatArray enemiesPos = (jfloatArray) safeCallObjectMethod(midGetEnemiesFloatPos, zLevelEnemies);
//    std::cout << "marioState\n"; std::cout.flush();
    jintArray marioState = (jintArray)safeCallObjectMethod(midGetMarioState, zLevelEnemies);

    pyTuple = buildPythonTuple(env, serializedLevelScene, serializedEnemies, marioPos, enemiesPos, marioState);

    env->DeleteLocalRef(serializedLevelScene);
    env->DeleteLocalRef(serializedEnemies);
    env->DeleteLocalRef(marioPos);
    env->DeleteLocalRef(enemiesPos);
    env->DeleteLocalRef(marioState);
    
    return pyTuple;
}

_AMICOPYJAVA_API PyObject* getObservationDetails()
{
	PyObject* pyTuple = 0;
	
	jintArray obsDetails = (jintArray)safeCallObjectMethod(midGetObservationDetails, 0);
	
	pyTuple = convertJavaArrayToPythonArray<jintArray, jint>(env, obsDetails, 'I');
	
	env->DeleteLocalRef(obsDetails);
	
	return pyTuple;
}

_AMICOPYJAVA_API void performAction(int* action)
{
    jbooleanArray a = convertPythonArrayToJavaArray<int, jbooleanArray, jboolean>(env, action, 'I', numberOfButtons);
    (env)->CallVoidMethod(obj, midPerformAction, a);
    env->DeleteLocalRef(a);
}

//void createEnvironmentApple(const char* javaClassName, int nOptions = 0, const char* argv = NULL);

//#ifdef __APPLE__

/*Starts a JVM using the options,classpath,main class, and args stored in a VMLauchOptions structure */
/*
static void* startupJava(void *options) {
    int result = 0;
    //    JNIEnv* env;
    //    JavaVM* theVM;

    VMLaunchOptions * launchOptions = (VMLaunchOptions*) options;*/

    /* default vm args */
    //JavaVMInitArgs vm_args;

    /*
    To invoke Java 1.4.1 or the currently preferred JDK as defined by the operating system (1.4.2 as of the release of this sample and the release of Mac OS X 10.4) nothing changes in 10.4 vs 10.3 in that when a JNI_VERSION_1_4 is passed into JNI_CreateJavaVM as the vm_args.version it returns the current preferred JDK.

    To specify the current preferred JDK in a family of JVM's, say the 1.5.x family, applications should set the environment variable JAVA_JVM_VERSION to 1.5, and then pass JNI_VERSION_1_4 into JNI_CreateJavaVM as the vm_args.version. To get a specific Java 1.5 JVM, say Java 1.5.0, set the environment variable JAVA_JVM_VERSION to 1.5.0. For Java 1.6 it will be the same in that applications will need to set the environment variable JAVA_JVM_VERSION to 1.6 to specify the current preferred 1.6 Java VM, and to get a specific Java 1.6 JVM, say Java 1.6.1, set the environment variable JAVA_JVM_VERSION to 1.6.1.

    To make this sample bring up the current preferred 1.5 JVM, set the environment variable JAVA_JVM_VERSION to 1.5 before calling JNI_CreateJavaVM as shown below.  Applications must currently check for availability of JDK 1.5 before requesting it.  If your application requires JDK 1.5 and it is not found, it is your responsibility to report an error to the user. To verify if a JVM is installed, check to see if the symlink, or directory exists for the JVM in /System/Library/Frameworks/JavaVM.framework/Versions/ before setting the environment variable JAVA_JVM_VERSION.

    If the environment variable JAVA_JVM_VERSION is not set, and JNI_VERSION_1_4 is passed into JNI_CreateJavaVM as the vm_args.version, JNI_CreateJavaVM will return the current preferred JDK. Java 1.4.2 is the preferred JDK as of the release of this sample and the release of Mac OS X 10.4.
     */
   /* {
        CFStringRef targetJVM = CFSTR("1.5");
        CFBundleRef JavaVMBundle;
        CFURLRef JavaVMBundleURL;
        CFURLRef JavaVMBundlerVersionsDirURL;
        CFURLRef TargetJavaVM;
        UInt8 pathToTargetJVM [PATH_MAX] = "\0";
        struct stat sbuf;


        // Look for the JavaVM bundle using its identifier
        JavaVMBundle = CFBundleGetBundleWithIdentifier(CFSTR("com.apple.JavaVM"));

        if (JavaVMBundle != NULL) {
            // Get a path for the JavaVM bundle
            JavaVMBundleURL = CFBundleCopyBundleURL(JavaVMBundle);
            CFRelease(JavaVMBundle);

            if (JavaVMBundleURL != NULL) {
                // Append to the path the Versions Component
                JavaVMBundlerVersionsDirURL = CFURLCreateCopyAppendingPathComponent(kCFAllocatorDefault, JavaVMBundleURL, CFSTR("Versions"), true);
                CFRelease(JavaVMBundleURL);

                if (JavaVMBundlerVersionsDirURL != NULL) {
                    // Append to the path the target JVM's Version
                    TargetJavaVM = CFURLCreateCopyAppendingPathComponent(kCFAllocatorDefault, JavaVMBundlerVersionsDirURL, targetJVM, true);
                    CFRelease(JavaVMBundlerVersionsDirURL);

                    if (TargetJavaVM != NULL) {
                        if (CFURLGetFileSystemRepresentation(TargetJavaVM, true, pathToTargetJVM, PATH_MAX)) {
                            // Check to see if the directory, or a sym link for the target JVM directory exists, and if so set the
                            // environment variable JAVA_JVM_VERSION to the target JVM.
                            if (stat((char*) pathToTargetJVM, &sbuf) == 0) {
                                // Ok, the directory exists, so now we need to set the environment var JAVA_JVM_VERSION to the CFSTR targetJVM
                                // We can reuse the pathToTargetJVM buffer to set the environement var.
                                if (CFStringGetCString(targetJVM, (char*) pathToTargetJVM, PATH_MAX, kCFStringEncodingUTF8))
                                    setenv("JAVA_JVM_VERSION", (char*) pathToTargetJVM, 1);
                            }
                        }
                        CFRelease(TargetJavaVM);
                    }
                }
            }
        }
    }*/

    /* JNI_VERSION_1_4 is used on Mac OS X to indicate the 1.4.x and later JVM's */
    /*vm_args.version = JNI_VERSION_1_4;
    vm_args.options = (JavaVMOption*) launchOptions->options;
    vm_args.nOptions = launchOptions->nOptions;
    vm_args.ignoreUnrecognized = JNI_TRUE;*/

    /* start a VM session */
    /*result = JNI_CreateJavaVM(&jvm, (void**) & env, &vm_args);

    if (result != 0) {
        fprintf(stderr, "[JavaAppLauncher Error] Error starting up VM.\n");
        exit(result);
        return NULL;
    }*/

    /* Find the main class */
   /* jclass mainClass = (env)->FindClass(launchOptions->mainClass);
    if (mainClass == NULL) {
        (env)->ExceptionDescribe();
        result = -1;
        goto leave;
    }
    jClass = mainClass;
    std::cout << "!!cls = " << jClass;
    std::cout.flush();*/

    /* Get the application's main method */
     /*jmethodID mainID = (env)->GetStaticMethodID(mainClass, "main",
            "([Ljava/lang/String;)V");
    if (mainID == NULL) {
        if ((env)->ExceptionOccurred()) {
            (env)->ExceptionDescribe();
        } else {
            fprintf(stderr, "[JavaAppLauncher Error] No main method found in specified class.\n");
        }
        result = -1;
        goto leave;
    }*/

    /* Build argument array */
   /* jobjectArray mainArgs = NewPlatformStringArray(env, (char **) launchOptions->args, launchOptions->numberOfArgs);
    if (mainArgs == nil) {
        (env)->ExceptionDescribe();
        goto leave;
    }*/

    /* or create an empty array of java.lang.Strings to pass in as arguments to the main method
    jobjectArray mainArgs = (*env)->NewObjectArray(env, 0,
                        (*env)->FindClass(env, "java/lang/String"), NULL);
    if (mainArgs == 0) {
        result = -1;
        goto leave;
    }
     */

    /* Invoke main method passing in the argument object. */
    /*(env)->CallStaticVoidMethod(mainClass, mainID, mainArgs);
    if ((env)->ExceptionOccurred()) {
        (env)->ExceptionDescribe();
        result = -1;
        goto leave;
    }
    std::cout << "HERE1?";
    std::cout.flush();
    CFRunLoopStop(CFRunLoopGetCurrent());
    std::cout << "HERE2?";
    return env;
leave:
    freeVMLaunchOptions(launchOptions);
    (jvm)->DestroyJavaVM();
    exit(result);
    return NULL;
}*/

/* call back for dummy source used to make sure the CFRunLoop doesn't exit right away */

/* This callback is called when the source has fired. */
/*void sourceCallBack(void *info)
{
    std::cout << "\nC++: OUT!";
    std::cout.flush();
}*/

/*  The following code will spin a new thread off to start the JVM
    while using the primordial thread to run the main runloop.
 */

/*
int mainFun(int argc, const char **argv) {
    CFRunLoopSourceContext sourceContext;

    for (int i = 0; i < argc; ++i)
        printf("argv = %s\n", argv[i]);*/

    /* Start the thread that runs the VM. */
    //pthread_t vmthread;

    /* Parse the args */
    //VMLaunchOptions * launchOptions = NewVMLaunchOptions(argc, argv);

    //    launchOptions->mainClass = "HelloWorld";

    /* Set our name for the Application Menu to our Main class */
    //setAppName(launchOptions->mainClass);

    /* create a new pthread copying the stack size of the primordial pthread */
    /*struct rlimit limit;
    size_t stack_size = 0;
    int rc = getrlimit(RLIMIT_STACK, &limit);
    if (rc == 0) {
        if (limit.rlim_cur != 0LL) {
            stack_size = (size_t) limit.rlim_cur;
        }
    }

    pthread_attr_t thread_attr;
    pthread_attr_init(&thread_attr);
    pthread_attr_setscope(&thread_attr, PTHREAD_SCOPE_SYSTEM);
    pthread_attr_setdetachstate(&thread_attr, PTHREAD_CREATE_DETACHED);
    if (stack_size > 0) {
        pthread_attr_setstacksize(&thread_attr, stack_size);
    }*/

    /* Start the thread that we will start the JVM on. */
    /*std::cout << "\nTrying to create a VM...";
    pthread_create(&vmthread, &thread_attr, startupJava, launchOptions);
    pthread_attr_destroy(&thread_attr);

    std::cout << "\nCreated VM!...";*/
    /* Create a a sourceContext to be used by our source that makes */
    /* sure the CFRunLoop doesn't exit right away */
   /* sourceContext.version = 0;
    sourceContext.info = NULL;
    sourceContext.retain = NULL;
    sourceContext.release = NULL;
    sourceContext.copyDescription = NULL;
    sourceContext.equal = NULL;
    sourceContext.hash = NULL;
    sourceContext.schedule = NULL;
    sourceContext.cancel = NULL;
    sourceContext.perform = &sourceCallBack;*/

    /* Create the Source from the sourceContext */
   // CFRunLoopSourceRef sourceRef = CFRunLoopSourceCreate(NULL, 0, &sourceContext);
    //    Ref = sourceRef;

    /* Use the constant kCFRunLoopCommonModes to add the source to the set of objects */
    /* monitored by all the common modes */
    //CFRunLoopAddSource(CFRunLoopGetCurrent(), sourceRef, kCFRunLoopCommonModes);

    /* Park this thread in the runloop */
   // std::cout << "\nBefore !...";
   // CFRunLoopRun();

  //  std::cout << "\nRETURN !...";
   // return 0;
//}
//#endif

/*void createEnvironmentApple(const char* javaClassName, int nOptions = 0, const char* argv = NULL)
{
#ifdef __APPLE__
    int argc = 2;
    const char** argm = new const char*[2];
    argm[0] = "./dummy";
    argm[1] = javaClassName;
    printf("argc = %d\n", argc);
    std::cout << "\nCall to main!";
    mainFun(2, argm);
    std::cout << "\nRETUUUURN!";
#endif
}*/