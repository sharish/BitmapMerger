
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-BitmapMerger-green.svg?style=flat)](https://android-arsenal.com/details/1/2085) [![Travis CI](https://api.travis-ci.org/cooltechworks/BitmapMerger.svg?branch=master)](https://travis-ci.org/cooltechworks/BitmapMerger/)


# BitmapMerger
Bitmap Merger is a simple project help you to merge two bitmaps without memory exceptions. The bitmaps are processed in background threads thereby taking the
load away from UI thread. Along with merge, it also contains the image decoder for decoding images from resources/disk and are sampled
to prevent OutOfMemoryError. 

Examples of Bitmap Merger with this project

Merging at angle away | Merging at center | Merging with offsets
------------ | ------------- | -------------
![merge_angle_small](https://cloud.githubusercontent.com/assets/13122232/8438305/9f7c2644-1f82-11e5-8f51-25ba7cca0711.gif) | ![merge_at_center_small](https://cloud.githubusercontent.com/assets/13122232/8438306/9f83ee9c-1f82-11e5-8734-954a13f1b2f2.gif) | ![merge_offset_small](https://cloud.githubusercontent.com/assets/13122232/8438307/9f8d7c78-1f82-11e5-8d77-7fb9f31dfd6f.gif)
Moon moving around the earth for various angles | Stamp at center of the document being scaled | Balloon flying in a beach with various offset values


### Usage:

#### Merging at angle away

```java
int angle = 90; // your angle here
float scale = 0.5f; // scaling option for merging the image
Bitmap baseBitmap; // your base bitmap here
Bitmap mergeBitmap; // your merging bitmap here
ImageView imgView; // your image view for displaying the merged bitmaps.
BitmapMergerTask task = new BitmapMergerTask();
            task.setBaseBitmap(baseBitmap)
                    .setMergeBitmap(mergeBitmap)
                    .setMergeListener(new BitmapMergerTask.OnMergeListener() {
                        @Override
                        public void onMerge(BitmapMergerTask task, Bitmap mergedBitmap) {
                            if(imgView != null) {
                              imgView.setImageBitmap(mergedBitmap);
                            }
                        }
                    })
                    .setScale(scale)
                    .setAngle(angle)
                    .merge();

```

#### Merging at center

```java
float scale = 0.5f; // scaling option for merging the image
Bitmap baseBitmap; // your base bitmap here
Bitmap mergeBitmap; // your merging bitmap here
ImageView imgView; // your image view for displaying the merged bitmaps.
BitmapMergerTask task = new BitmapMergerTask();
            task.setBaseBitmap(baseBitmap)
                    .setMergeBitmap(mergeBitmap)
                    .setMergeListener(new BitmapMergerTask.OnMergeListener() {
                        @Override
                        public void onMerge(BitmapMergerTask task, Bitmap mergedBitmap) {
                            if(imgView != null) {
                              imgView.setImageBitmap(mergedBitmap);
                            }
                        }
                    })
                    .setScale(scale)
                    .merge();

```

#### Merging with offsets from top left

```java
int leftOffset = 0; // your left offset in pixels
int topOffset = 0; // your top offset in pixels
float scale = 0.5f; // scaling option for merging the image
Bitmap baseBitmap; // your base bitmap here
Bitmap mergeBitmap; // your merging bitmap here
ImageView imgView; // your image view for displaying the merged bitmaps.
BitmapMergerTask task = new BitmapMergerTask();
            task.setBaseBitmap(baseBitmap)
                    .setMergeBitmap(mergeBitmap)
                    .setMergeListener(new BitmapMergerTask.OnMergeListener() {
                        @Override
                        public void onMerge(BitmapMergerTask task, Bitmap mergedBitmap) {
                            if(imgView != null) {
                              imgView.setImageBitmap(mergedBitmap);
                            }
                        }
                    })
                    .setScale(scale)
                    .setOffsets(leftOffset,topOffset)
                    .merge();

```

Developed By
============

* Harish Sridharan - <harish.sridhar@gmail.com>





