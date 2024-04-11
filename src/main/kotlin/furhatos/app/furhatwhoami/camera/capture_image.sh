#!/bin/bash
ffmpeg -f avfoundation -framerate 30 -video_size 640x480 -i "1" -frames:v 1 "images/$1"
