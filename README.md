# BitmapMerger
Bitmap Merger is a simple project help you to merge two bitmaps without memory exceptions. The bitmaps are processed in background threads thereby taking the
load away from UI thread. Along with merge, it also contains the image decoder for decoding images from resources/disk and are sampled
to prevent OutOfMemoryError. 

Examples of Bitmap Merger with this project

Merging at angle away | Merging at center | Merging with offsets
------------ | ------------- | -------------
![merge_angle_small](https://cloud.githubusercontent.com/assets/13122232/8438305/9f7c2644-1f82-11e5-8f51-25ba7cca0711.gif) | ![merge_at_center_small](https://cloud.githubusercontent.com/assets/13122232/8438306/9f83ee9c-1f82-11e5-8734-954a13f1b2f2.gif) | ![merge_offset_small](https://cloud.githubusercontent.com/assets/13122232/8438307/9f8d7c78-1f82-11e5-8d77-7fb9f31dfd6f.gif)
Moon moving around the earth for various angles | Stamp at center of the document being scaled | Balloon flying in a beach with various offset values


