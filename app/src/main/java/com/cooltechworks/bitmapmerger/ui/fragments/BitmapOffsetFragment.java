package com.cooltechworks.bitmapmerger.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cooltechworks.bitmapmerger.R;
import com.cooltechworks.bitmapmerger.tasks.BitmapDecoderTask;
import com.cooltechworks.bitmapmerger.tasks.BitmapMergerTask;


public class BitmapOffsetFragment extends Fragment {


    private static final int SELECT_PHOTO_1 = 1;
    private static final int SELECT_PHOTO_2 = 2;

    private Bitmap mBaseBitmap, mMergeBitmap;
    private int mFromTop = 0, mFromLeft = 0;
    private float mScale = 0.5f;

    private BitmapMergerTask mBitmapMergerTask;

    private View mRootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root,Bundle savedInstanceState) {


        mRootView = inflater.inflate(R.layout.frag_bmp_merge_offset,root,false);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");

                switch (v.getId()) {
                    case R.id.base_image_label:
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO_1);
                        break;
                    case R.id.merge_image_label:
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO_2);
                        break;

                }
            }
        };

        View baseLabelView =  mRootView.findViewById(R.id.base_image_label);
        View mergeLabelView = mRootView.findViewById(R.id.merge_image_label);

        baseLabelView.setOnClickListener(listener);
        mergeLabelView.setOnClickListener(listener);

        int maxWidth = getDimens(R.id.image_holder)[0];
        int maxHeight = getDimens(R.id.image_holder)[1];


        SeekBar fromLeftSeek = (SeekBar) mRootView.findViewById(R.id.from_left_seek);


        fromLeftSeek.setMax(maxWidth);
        fromLeftSeek.setProgress(mFromLeft);

        SeekBar fromTopSeek = (SeekBar) mRootView.findViewById(R.id.from_top_seek);
        fromTopSeek.setMax(maxHeight);
        fromTopSeek.setProgress(mFromTop);

        SeekBar scaleSeekbar = (SeekBar) mRootView.findViewById(R.id.scale_size);
        scaleSeekbar.setProgress((int) (mScale * 100));

        ((TextView) mRootView.findViewById(R.id.from_left_label)).setText(getString(R.string.from_left, mFromLeft));
        ((TextView) mRootView.findViewById(R.id.from_top_label)).setText(getString(R.string.from_top, mFromTop));

        ((TextView) mRootView.findViewById(R.id.scale_label)).setText(getString(R.string.scale_factor, mScale));


        fromLeftSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mFromLeft = progress;

                ((TextView) mRootView.findViewById(R.id.from_left_label)).setText(getString(R.string.from_left, mFromLeft));
                refresh();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        fromTopSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mFromTop = progress;

                ((TextView) mRootView.findViewById(R.id.from_top_label)).setText(getString(R.string.from_top, mFromTop));
                refresh();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        scaleSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mScale = progress / 100f;

                ((TextView) mRootView.findViewById(R.id.scale_label)).setText(getString(R.string.scale_factor, mScale));
                refresh();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        refresh();

        return mRootView;


    }

    public void refresh() {

        final ImageView imgView = (ImageView) mRootView.findViewById(R.id.image_holder);

        TextView baseLabelTextView = (TextView) mRootView.findViewById(R.id.base_image_label);
        TextView mergeLabelTextView = (TextView) mRootView.findViewById(R.id.merge_image_label);


        if (mMergeBitmap != null && mBaseBitmap != null) {


            int maxWidth = mBaseBitmap.getWidth();
            int maxHeight = mBaseBitmap.getHeight();


            SeekBar fromLeftSeek = (SeekBar) mRootView.findViewById(R.id.from_left_seek);


            fromLeftSeek.setMax(maxWidth);
            fromLeftSeek.setProgress(mFromLeft);

            SeekBar fromTopSeek = (SeekBar) mRootView.findViewById(R.id.from_top_seek);
            fromTopSeek.setMax(maxHeight);
            fromTopSeek.setProgress(mFromTop);


//          If you're working with larger bitmaps and continuously changing the scale value or angle value, you might notice the lag between slider change
//            and the image position. To get rid of that, uncomment the following codes.
            if (mBitmapMergerTask != null && mBitmapMergerTask.getStatus() == BitmapMergerTask.Status.RUNNING) {
                mBitmapMergerTask.cancel(true);
            }


            mBitmapMergerTask = new BitmapMergerTask();
            mBitmapMergerTask.setBaseBitmap(mBaseBitmap)
                    .setMergeBitmap(mMergeBitmap)
                    .setMergeListener(new BitmapMergerTask.OnMergeListener() {
                        @Override
                        public void onMerge(BitmapMergerTask task, Bitmap mergedBitmap) {
                            imgView.setImageBitmap(mergedBitmap);
                        }
                    })
                    .setScale(mScale)
                    .setOffsets(mFromLeft,mFromTop)
                    .merge();

            baseLabelTextView.setTextColor(getResources().getColor(R.color.dark_blue_fg));
            mergeLabelTextView.setTextColor(getResources().getColor(R.color.dark_blue_fg));


        }
        else {


            if(mBaseBitmap != null) {
                imgView.setImageBitmap(mBaseBitmap);
                baseLabelTextView.setTextColor(getResources().getColor(R.color.dark_blue_fg));
                mergeLabelTextView.setTextColor(getResources().getColor(R.color.green_fg));
            }
            else {
                baseLabelTextView.setTextColor(getResources().getColor(R.color.green_fg));
                mergeLabelTextView.setTextColor(getResources().getColor(R.color.dark_blue_fg));
            }

        }

    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO_1:
            case SELECT_PHOTO_2:
                if (resultCode == Activity.RESULT_OK) {
                    final Uri imageUri = imageReturnedIntent.getData();

                    int dimens[] = getDimens(R.id.image_holder);

                    new BitmapDecoderTask()
                            .setDecodingImageReference(imageUri, getActivity().getContentResolver())
                            .setRequiredWidth(dimens[0])
                            .setRequiredHeight(dimens[1])
                            .setListener(new BitmapDecoderTask.OnDecodeListener() {
                                @Override
                                public void onDecode(BitmapDecoderTask task, Bitmap bitmap) {

                                    if (requestCode == SELECT_PHOTO_1) {

                                        mBaseBitmap = bitmap;

                                    } else {

                                        mMergeBitmap = bitmap;

                                    }
                                    refresh();
                                }
                            })
                            .decode();

                }
        }
    }

    public int[] getDimens(int resId) {
        int width = mRootView.findViewById(resId).getMeasuredWidth();
        int height = mRootView.findViewById(resId).getMeasuredHeight();

        return new int[]{width, height};

    }

}
