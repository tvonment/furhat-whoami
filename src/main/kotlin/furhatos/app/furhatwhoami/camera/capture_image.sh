#!/bin/bash
ffmpeg -y -f avfoundation -framerate 30 -video_size 640x480 -i "1" -frames:v 1 images/furhat_image.jpg
#ffmpeg -f avfoundation -framerate 30 -i "default" -frames:v 1 images/furhat_image.jpg
