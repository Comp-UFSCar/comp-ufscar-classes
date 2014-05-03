#!/bin/bash

############# ABOUT ##############
# Function detect_java_vm is given from SciLab project.
# Found path with java labraries exported in to LD_LIBRARY_PATH if target OS is Linux
# and in to DYLD_LIBRARY_PATH variable
###

if test ! -z "$SCIVERBOSE"; then
	SCIVERBOSE=1
else
	SCIVERBOSE=0
fi

detect_java_vm() {
############# JAVA DETECTION ##############
# Basic inspiration from Eclipse
#
# Looking for Java in various places with different ways :
# * if JAVA_HOME is specificed, use it
# * Check in 'standard' places we know Java could be installed
# if some are missing, please contact us
# * look in the PATH if there is a java binary
# if it is the case, resolve the symlink and set the JAVA_HOME from it
#
# Once we have JAVA_HOME, we check that native libraries (libjava.so and
# 2 others) are available
###

	if test "${OS}" = ""; then
            OS=`uname -s` # Retrieve the Operating System
	fi
	if test "${MODEL}" = ""; then
            MODEL=`uname -m` # Retrieve the model
	fi

# Get the proc name matching to the one of the JVM
# Inspiration and data are from http://www.google.com/codesearch?hl=en&q=+java+ppc+libjava.so+show:a78sz0SP41k:zxNozD-vc0A:FWiIy7Hinmg&sa=N&cd=2&ct=rc&cs_p=http://fresh.t-systems-sfr.com/unix/src/misc/tclBlendSrc1.2.5.tar.gz&cs_f=tclBlend1.2.5/configure.in#a0
# For Mac OS X, we will get stuff like that:
# uname -s Power Macintosh
# uname -m Darwin

# If the user has the variable JAVA_HOME in the ENV
	if test -n "$JAVA_HOME" ; then
		if test $SCIVERBOSE -ne 0; then
			echo "Using specified vm: $JAVA_HOME"
		fi
		if test ! -x "$JAVA_HOME/bin/java" ; then
			echo "Cannot find $JAVA_HOME/bin/java"
			exit 1
		fi
	fi

	if test -z "$JAVA_HOME"; then
        case $OS in
            *darwin* | *Darwin*)
                    # Apple thinks and does things different (it would be too easy otherwise)
		    # They provide a script which returns the JAVA_HOME path
		    JAVA_HOME=$(/usr/libexec/java_home --arch x86_64 --failfast --version 1.6+)
                    DYLD_LIBRARY_PATH="$JAVA_HOME/../Libraries:$DYLD_LIBRARY_PATH"
		    if test $SCIVERBOSE -ne 0; then
			echo "Using default Mac OS X vm: $JAVA_HOME"
			echo "And DYLD_LIBRARY_PATH: $DYLD_LIBRARY_PATH"
		    fi
		    ;;
                *)
					;;
			esac
fi

# Browse "default directory" (if it means anything under Unix/Linux) of Java
# to find where it could be !
	if test -z "$JAVA_HOME"; then
		JAVA_DEFAULT_DIR="/usr/lib/jvm/java-6-openjdk/ /usr/lib/jvm/java/ /usr/lib/jvm/java-6-sun/ /usr/lib/j2se/1.6 /usr/java/jdk1.6.0*/ /usr/java/jdk1.5.0*/ /usr/lib/j2sdk1.6-ibm /usr/lib/jvm/java-1.5.0-sun /usr/java/ /usr/lib/j2se/1.5 /usr/lib/j2se/1.4 /usr/java/jre1.6.0*/ /usr/java/jre1.5.0*/ /usr/lib/j2sdk1.5-ibm /usr/lib/j2sdk1.4-ibm /usr/lib/j2sdk1.5-sun /usr/lib/j2sdk1.4-sun /usr/lib/jvm/java-gcj /usr/lib/jvm/java-1.5.0-gcj/ /usr/lib/kaffe/pthreads /usr/lib/jvm/java-1.7.0-icedtea-*/ /usr/lib/jvm/jre/ /usr/local/diablo-jdk1.6.0/"
		for JAVA_HOME in $JAVA_DEFAULT_DIR ; do
			if test $SCIVERBOSE -ne 0 ; then
				echo "Trying to find Java in $JAVA_HOME "
			fi
			if test -x "${JAVA_HOME}/bin/java" ; then
				export JAVA_HOME
				if test $SCIVERBOSE -ne 0 ; then
					echo "found"
				fi
				break
			fi
		done
	fi

# JAVA_HOME still haven't been found. Well, we can now try to find it in the path

	if test -z "$JAVA_HOME"; then
		PATH_SEPARATOR=:
		as_save_IFS=$IFS; IFS=$PATH_SEPARATOR
		if test $SCIVERBOSE -ne 0; then
			echo "Look the binary java in the PATH. If it is a symlink, try to resolve it"
		fi
		for DIR in $PATH; do
			if test -f $DIR/java; then

				_cur=$DIR/java
			# resolve symlink
			# Will probably bug if a file contains -> in the name
				while ls -ld "$_cur" 2>/dev/null | grep " -> " >/dev/null; do
					_cur=`ls -ld "$_cur" | sed 's/.* -> //'`
				done
			# symlink resolved to the real file

			# Now, create JAVA_HOME
				TMP=`dirname $_cur`
				JAVA_HOME=`dirname $TMP`
				if test ! -z "$JAVA_HOME"; then
					break;
				fi
			fi
		done
		IFS=$as_save_IFS
	fi
	if test ! -z "$JAVA_HOME"; then
		if test $SCIVERBOSE -ne 0; then
			echo "JAVA_HOME : $JAVA_HOME"
		fi

# Manage JRE & JDK because libjava.so and others are stored in the JRE
		if test -d $JAVA_HOME/jre; then
			JRE_HOME=$JAVA_HOME/jre
		else
			JRE_HOME=$JAVA_HOME
		fi

# Configuring LD_LIBRARY_PATH for libjava.so libhpi.so and libxxxx.so

		JAVA_SHLIB="libjava.so"

		case $OS in
			"Linux")
				case $MODEL in
					"x86_64")
						proc="amd64"
						;;
					i?86)

						if test -d ${JRE_HOME}/lib/`uname -m` ; then
							proc=`uname -m`
						else
							proc="i386"
						fi
						;;
					"ia64")
						proc="i64"
						;;
					"alpha")
						proc="alpha"
						;;
					"sparc64")
						proc="sparc"
						;;
					sh*)
						proc="sh"
						;;
					"ia64")
						proc="ia64"
						;;
					"mips"|"mipsel") # under mipsel, uname -m is
					# usually returning mips ...
					# but binary are in mipsel
						if test -d ${JRE_HOME}/lib/mipsel ; then
							proc="mipsel"
						else
							proc="mips"
						fi
						;;
					"ppc"|"powerpc"|"ppc64")
						proc="ppc"
						;;
					"s390"|"s390x")
						proc="s390"
						;;
					"armv4l"|"armv5tel")
						proc="arm"
						;;
					*)
						echo "Could not find the Java configuration for the model <${MODEL}>. Please contact us."
						;;
				esac
				;;
			"HP-UX")
				JAVA_SHLIB="libjava.sl"
				proc="lib" # TODO : check if it is right or not ... seems not !
				;;
			"IRIX")
				proc="lib32"
				;;
			"SunOS")
    			case $MODEL in
					"i86pc") # solaris x86
						proc="i386"
						;;
				# check for the 64 bits syntax
					"sparc"|"sparc64")
						proc="sparc"
						;;
					*)
						echo "Could not find the Java configuration for the model <${MODEL}>. Please contact us."
						;;
				esac
				;;

			*win32* | *WIN32* | *CYGWIN_NT*)
				proc="win32"
				JAVA_SHLIB="jvm.dll"
				;;
			*darwin* | *Darwin*) # MacOS X
        # For Mac OS X, Since path are different from the JVM normal tree,
		# don't need to detect the CPU
				JAVA_SHLIB="libjava.jnilib"
				;;
			*FreeBSD*)
				proc=$MODEL
				;;
			*)
				echo "Could not find the Java configuration for the OS <${OS}>. Please contact us or submit a bug report with your detailed configuration http://bugzilla.scilab.org/"
                echo "Fallback on the model <${MODEL}> found by uname -m"
                proc=$MODEL
                ;;
		esac


# Check if the lib exists or not (we should manage system where .so is not the
# ext for library
		LIBJAVA="$JRE_HOME/lib/$proc/$JAVA_SHLIB"
		if test ! -f $LIBJAVA; then
			if test $SCIVERBOSE -ne 0; then
				echo "Cannot find $LIBJAVA"
			fi
		else
            CLASSPATH="$CLASSPATH:$SCI/modules/jvm/jar/org.scilab.modules.jvm.jar"
		# @TODO This is the default path of the official sun JVM. 
		# It is different for other implementation of the JVM....
		    LD_LIBRARY_PATH="$JRE_HOME/lib/$proc/:$JRE_HOME/lib/$proc/server/:$JRE_HOME/lib/$proc/native_threads/:$LD_LIBRARY_PATH"
		    LD_LIBRARY_PATH="$JRE_HOME/lib/$proc/:$JRE_HOME/lib/$proc/server/:$JRE_HOME/lib/$proc/native_threads/:$LD_LIBRARY_PATH"
		fi
	else
		if test $SCIVERBOSE -ne 0; then
			echo "Have not been able to find any Java VM on this computer. Some features may be broken."
		fi
	fi

if test "${OS}" = "Linux"; then
    export LD_LIBRARY_PATH
else
    DYLD_LIBRARY_PATH = LD_LIBRARY_PATH
    export DYLD_LIBRARY_PATH
fi
####### END OF JAVA ENV DETECTION/SETTING ######
}

detect_java_vm
python "$1" "$2"
