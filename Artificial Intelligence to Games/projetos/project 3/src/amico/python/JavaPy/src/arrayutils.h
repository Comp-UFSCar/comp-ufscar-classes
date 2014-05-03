/** 
 @file	arrayutils.h
 @brief	Functions to convert arrays in both directions: Java -> Python and Python -> Java

 @author Sergey.Karakovskiy at idsia.ch, Nikolay.Sohryakov at gmail.com
 
 @version 0.1
 
 Types symbols:
 @li V - void
 @li Z - boolean
 @li B - byte
 @li C - char
 @li S - short
 @li I - int
 @li J - long
 @li F - float
 @li D - double
 @li L - string
 */
#include <jni.h>
#include <iostream>

#ifndef ARRAYUTILS_H
#define	ARRAYUTILS_H

/** 
 @brief Generalizes call of the @c Set<Type>ArrayRegion @c JNI function
 
 Calls @c Set\<Type\>ArrayRegion depend on type given as char in param @a type (accordingly to @c JNI types naming).

 @param env pointer to the JNIEnv
 @param type type of the array given as char
 @param dest pointer to the java array where data will be copied from the source array(e.g. jintArray, jfloatArray)
 @param start first index in array to copy from
 @param size number of array elements to be copied
 @param src native C++ array to be copied(e.g. jint*, jfloat*)
 */
template<typename To, typename ToBaseType>
void setTJavaArrayRegion (JNIEnv* env, char type, To dest, unsigned int start, unsigned int size, ToBaseType* src)
{
    switch (type)
    {
        case 'Z': //boolean
        {
            (env)->SetBooleanArrayRegion((jbooleanArray) dest, start, size, (jboolean*) src);
            break;
        }
        case 'B': //byte
        {
            (env)->SetByteArrayRegion((jbyteArray) dest, start, size, (jbyte*) src);
            break;
        }
        case 'C': //char
        {
            (env)->SetCharArrayRegion((jcharArray)dest, start, size, (jchar*)src);
            break;
        }
        case 'S': //short
        {
            (env)->SetShortArrayRegion((jshortArray)dest, start, size, (jshort*)src);
            break;
        }
        case 'I': //integer
        {
            (env)->SetIntArrayRegion((jintArray)dest, start, size, (jint*)src);
            break;
        }
        case 'J': //long
        {
            (env)->SetLongArrayRegion((jlongArray)dest, start, size, (jlong*)src);
            break;
        }
        case 'F': //float
        {
            (env)->SetFloatArrayRegion((jfloatArray)dest, start, size, (jfloat*)src);
            break;
        }
        case 'D': //double
        {
            (env)->SetDoubleArrayRegion((jdoubleArray)dest, start, size, (jdouble*)src);
            break;
        }
        case 'L': //string
        {
            break; ///For type @c string implemented separately in instanciated function @c convertPythonArrayToJavaArray.
        }
    }
}

/**
 @brief Generalizes call of the @c New<Type>Array @c JNI function
 
 Calls New\<Type\>Array depend on type given as char(accordingly to JNI types naming). For additional information
 see Set\<Type\>ArrayRegion. 
 
 @param env pointer to the JNIEnv
 @param type type of the array given as char
 @param size number of elements in the array
 
 @return reference to the @c Jara array (e.g. jintArray, jfloatArray)
 */
template<typename To>
To newJavaArrayByType(JNIEnv* env, char type, unsigned int size)
{
    switch (type)
    {
        case 'Z': //boolean
            return (To) (env)->NewBooleanArray(size);
        case 'B': //byte
            return (To) (env)->NewByteArray(size);
        case 'C': //char
            return (To) (env)->NewCharArray(size);
        case 'S': //short
            return (To) (env)->NewShortArray(size);
        case 'I': //integer
            return (To) (env)->NewIntArray(size);
        case 'J': //long
            return (To) (env)->NewLongArray(size);
        case 'F': //float
            return (To) (env)->NewFloatArray(size);
        case 'D': //double
            return (To) (env)->NewDoubleArray(size);
        case 'L': //string
            break; //implemented separately in instanciated function convertPythonArrayToJavaArray
    }
}

/**
 @brief Creates new @c Java array of type @c To from native @c C++ array of type @c From
 Creates new @c Java array using @c newJavaArrayByType function, copies elements from the native
 
 @c C++ array and copies this array in to the @c JNI memory using @c setTJavaArrayRegion
 
 @param env pointer to the JNIEnv
 @param array native @c C++ array to be copied
 @param type type of the array given as char
 @param size number of elements in the array
 
 @return @c Java array (e.g. jintArray, jfloatArray)
 */
template<typename From, typename To, typename ToBaseType>
To convertPythonArrayToJavaArray (JNIEnv* env, From* array, char type, unsigned int size)
{
    if (array == NULL) {
        std::cerr << "\nC++ ERROR: array is NULL\n";
        std::cerr.flush();
        return NULL;
    }

    To ret = newJavaArrayByType<To>(env, type, size);
    if (ret == NULL) {
        std::cerr << "\nC++: ERROR while creating int array in convertPythonArrayToJavaArray: array is NULL\n";
        std::cerr.flush();
        return NULL;
    }

    ToBaseType* buff = new ToBaseType[size];
    for (int i = 0; i < size; i++)
        buff[i] = (ToBaseType) array[i];

    setTJavaArrayRegion<To, ToBaseType>(env, type, ret, 0, size, (ToBaseType*) buff);
    //env->SetBooleanArrayRegion((jbooleanArray) ret, 0, size, (jboolean*) buff);
    return ret;
}


/**
 @brief Creates new @c Java array of type @c jobjectArray from native @c C++ array of type @c char*.
 
 Creates new @c Java array of @c Java string from native @c C++ array @c char**.
 Instantiated for:
	@li From = char*
	@li To = jobjectArray
	@li ToBaseType = jobject
 
 @param env pointer to the JNIEnv
 @param array native @c C++ array to be copied
 @param type type of the array given as char
 @param size number of elements in the array
 
 @return @c Java array (e.g. jintArray, jfloatArray)
 */
template<>
jobjectArray convertPythonArrayToJavaArray<char*, jobjectArray, jobject> (JNIEnv* env, char** array, char type, unsigned int size)
{
    jclass cls;
    jobjectArray ary = NULL;
    int i;

    /* Look up the String class */
    cls = env->FindClass("java/lang/String");
    if (cls != NULL) {
        /* Create a new arrary with strc elements */
        ary = env->NewObjectArray(size, cls, 0);
        if (ary != NULL)
            /* Add each of the c strings to the new array as
               UTF java.lang.String objects */
            for (i = 0; i < size; i++) {
                jstring str = (env)->NewStringUTF(*array++);
                if (str != NULL) {
                    (env)->SetObjectArrayElement(ary, i, str);
                    /*The array now holds a reference to then string
                      so we can delete ours */
                    (env)->DeleteLocalRef(str);
                } else {
                    break;
                }
            }
    }
    return ary;
}

/**
 @brief Creates @c Python object from basic @c C++ native type.

 Calls @c PyInt_FromLong for any type smaller than int, @c PyLong_FromLong for type @c long.
 This template is instantiated for types @c float, @c double and @c string.
 
 @param jobj Value of basic native type to be converted in to the @c PyObject*
 @param resType Type of the @c jobj given as char.
 
 @return Pointer to the @c Python object(@c PyObject*).
 */
template<typename From>
PyObject* PyObject_FromJObject (From jobj, char resType)
{
    switch (resType)
    {
        case 'Z': //boolean
            return PyInt_FromLong(jobj);
        case 'B': //byte
            return PyInt_FromLong(jobj);
        case 'C': //char
            return PyInt_FromLong(jobj);
        case 'S': //short
            return PyInt_FromLong(jobj);
        case 'I': //integer
            return PyInt_FromLong(jobj);
        case 'J': //long
            return PyLong_FromLong(jobj);
        case 'F': //float
            break;
        case 'D': //double
            break;
        case 'L': //string
            break;
    }
}

/**
 @brief Converts @c Java array in to the @c Python tuple.
 
 Converts each element of the @c Java array in to the @c PyObject* and puts it in the @c Python tuple.
 Instantiated for: @c jfloatArray, @c jdoubleArray, @c jobjectArray (array of strings). 
 
 @param env Pointer to the @c JNIEnv
 @param array @c Java array to be converted
 @param resType Type of elements of the @c array given as char
 
 @return Pointer to the @c Python object (@c PyObject*)
 */
template<typename From, typename FromBaseType>
PyObject* convertJavaArrayToPythonArray (JNIEnv* env, From array, char resType)
{
    jsize len = (jsize) (env)->GetArrayLength(array);
    FromBaseType* obs = (FromBaseType*) (env)->GetPrimitiveArrayCritical(array, NULL);
    PyObject* items = PyTuple_New(len);
    if (items == NULL)
    {
        printf("Cannot create new PyTuple! Return Py_None\n");
        return Py_None;
    }
    for (jsize i = 0; i < len; ++i)
    {
        int k = (int)obs[i];
        PyObject* o = PyObject_FromJObject<FromBaseType>(k, resType);
        PyTuple_SET_ITEM(items, (Py_ssize_t) i, o);
    }
    env->ReleasePrimitiveArrayCritical(array, obs, 0);
    env->DeleteLocalRef(array);

    return items;
}

/**
 Converts each element of the @c Java array in to the @c PyObject* and puts it in the @c Python tuple.
 
 @brief Converts @c Java array @c jfloatArray in to the @c Python tuple.
 
 @param env Pointer to the @c JNIEnv
 @param array @c Java array to be converted
 @param resType Type of elements of the @c array given as char
 
 @return Pointer to the @c Python object (@c PyObject*)
 */
template<>
PyObject* convertJavaArrayToPythonArray<jfloatArray, jfloat> (JNIEnv* env, jfloatArray array, char resType)
{
    jsize len = env->GetArrayLength(array);
    jfloat* obs = (jfloat*) env->GetPrimitiveArrayCritical(array, NULL);
    PyObject* items = PyTuple_New(len);
    for (jsize i = 0; i < len; ++i) {
        PyTuple_SET_ITEM(items, (Py_ssize_t) i, PyFloat_FromDouble(obs[(Py_ssize_t) i]));
    }
    env->ReleasePrimitiveArrayCritical(array, obs, 0);
    env->DeleteLocalRef(array);

    return items;
}

/** 
 @brief Converts @c Java array @c jdoubleArray in to the @c Python tuple.
 
 Converts each element of the @c Java array in to the @c PyObject* and puts it in the @c Python tuple.
 
 @param env Pointer to the @c JNIEnv
 @param array @c Java array to be converted
 @param resType Type of elements of the @c array given as char
 
 @return Pointer to the @c Python object (@c PyObject*)
 */
template<>
PyObject* convertJavaArrayToPythonArray<jdoubleArray, jdouble> (JNIEnv* env, jdoubleArray array, char resType)
{
    jsize len = env->GetArrayLength(array);
    jdouble* obs = (jdouble*) env->GetPrimitiveArrayCritical(array, NULL);
    PyObject* items = PyTuple_New(len);
    for (jsize i = 0; i < len; ++i) {
        PyTuple_SET_ITEM(items, (Py_ssize_t) i, PyFloat_FromDouble(obs[(Py_ssize_t) i]));
    }
    env->ReleasePrimitiveArrayCritical(array, obs, 0);
    env->DeleteLocalRef(array);

    return items;
}

/**
 @brief Converts @c Java array @c jobjectArray in to the @c Python tuple.

 Converts each element of the @c Java array in to the @c PyObject* and puts it in the @c Python tuple.
 
 @param env Pointer to the @c JNIEnv
 @param array @c Java array to be converted
 @param resType Type of elements of the @c array given as char
 
 @return Pointer to the @c Python object (@c PyObject*)
 */
template<>
PyObject* convertJavaArrayToPythonArray<jobjectArray, jobject> (JNIEnv* env, jobjectArray array, char resType)
{
    jsize len = env->GetArrayLength(array);
    PyObject* items = PyTuple_New(len);
    for (jsize i = 0; i < len; ++i)
    {
        jstring js= (jstring) (env)->GetObjectArrayElement(array, i);
        const char* res = (env)->GetStringUTFChars(js, NULL);
        PyTuple_SET_ITEM(items, (Py_ssize_t) i, PyString_FromString(res));
        (env)->ReleaseStringUTFChars((jstring)js, res);
    }
    env->DeleteLocalRef(array);

    return items;
}

#endif	/* ARRAYUTILS_H */
