#!/usr/bin/env bash

ant -Dant.build.javac.target=1.8 -Dant.build.javac.source=1.8

zip -r SYG_Windows.zip ./out/SYG.jar ./out/res ./out/*.dll

zip -r SYG_Linux.zip ./out/SYG.jar ./out/res ./out/*.so

zip -r SYG_macOS.zip ./out/SYG.jar ./out/res ./out/*.jnilib ./out/*.dylib
