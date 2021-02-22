#!/usr/bin/env bash

ant -Dant.build.javac.target=1.8 -Dant.build.javac.source=1.8

zip -r SYG.zip ./out/
