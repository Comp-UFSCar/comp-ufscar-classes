#!/bin/bash

#
# AmiCoJavaPy build script. Works for both Mac OS X and Linux operating systems.
# If you have a problem while compiling, please contact us to solve your problem.
#
# All usage options will be described in documentation soon.
#
# Author: Nikolay Sohryakov, nikolay.sohryakov@gmail.com
#

MARIO_DIR="../../../../bin"
OUT_DIR="../../../../bin"
BUILD_DIR="AmiCoBuild/JavaPy"
MAKE_OUT_DIR="./build"
DEFAULT_AGENT=forwardagent.ForwardAgent.py
CMD_LINE_OPTIONS=
LIBRARY_FILE_NAME=
COMPILE_LIBRARY="true"

if [ "$#" -gt 0 ]; then
    until [ -z "$1" ];
    do
        case "$1" in
            "-nc") COMPILE_LIBRARY="false" ;;
            "-ch") shift;
            MARIO_DIR="$1" ;;
            "-out") shift;
            OUT_DIR="$1" ;;
            "-o") shift;
            CMD_LINE_OPTIONS="$1" ;;
            *) echo "Usage: [-ch path_to_ch_directory] [-out path_to_output_directory] [-o cmd_line_options]";
            exit 1 ;;
        esac
        shift
    done
fi

if [ "$COMPILE_LIBRARY" = "true" ]; then
    make
fi

mkdir -p -v "$OUT_DIR/$BUILD_DIR"

echo "$OUT_DIR/$BUILD_DIR"

#if test "${OS}" = ""; then
OS=`uname -s` # Retrieve the Operating System
#fi
if [ "${OS}" = "Linux" ]; then
    LIBRARY_FILE_NAME="libAmiCoJavaPy.so"
elif [ "${OS}" = "Darwin" ]; then
    LIBRARY_FILE_NAME="libAmiCoJavaPy.dylib"
fi
#TODO make contatination instead of hard code

if [ "$LIBRARY_FILE_NAME" = "" ]; then
    echo "Unknown OS type: ${OS}. Exit..."
    exit 1
fi

if [ "$COMPILE_LIBRARY" = "true" ]; then
    mv "$MAKE_OUT_DIR/$LIBRARY_FILE_NAME" "$OUT_DIR/$BUILD_DIR/"
#	rm -rf "$MAKE_OUT_DIR"
fi

cp ../agents/*.py "$OUT_DIR/$BUILD_DIR/"
cp -r "$MARIO_DIR/ch" "$OUT_DIR/$BUILD_DIR/"

cd "$OUT_DIR/$BUILD_DIR/"
if [ "${OS}" = "Linux" ]; then
    export LD_LIBRARY_PATH=`pwd`:$LD_LIBRARY_PATH
    echo $LD_LIBRARY_PATH
elif [ "${OS}" = "Darwin" ]; then
    export DYLD_LIBRARY_PATH=`pwd`:$DYLD_LIBRARY_PATH
    echo $DYLD_LIBRARY_PATH
fi

export PYTHONPATH=`pwd`:$PYTHONPATH
echo $PYTHONPATH

if [ "$CMD_LINE_OPTIONS" = "" ]; then
    java ch.idsia.scenarios.Main -ag $DEFAULT_AGENT
else
    java ch.idsia.scenarios.Main $CMD_LINE_OPTIONS
fi

#if [ "$CMD_LINE_OPTIONS" = "" ]; then
#	/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Home/bin/java ch.idsia.scenarios.Main -ag $DEFAULT_AGENT
#else
#	/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Home/bin/java ch.idsia.scenarios.Main $CMD_LINE_OPTIONS
#fi
