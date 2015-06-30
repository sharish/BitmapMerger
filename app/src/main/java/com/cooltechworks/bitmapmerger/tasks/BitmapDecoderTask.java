package com.cooltechworks.bitmapmerger.tasks;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Harish Sridharan on 29/06/15.
 */

/**
 * BitmapDecoderTask is a background asynchronous task that decodes the image (particularly larger images) from Uri or from resources. The decoder decodes the image in background thread and samples it to prevent {@link OutOfMemoryError} when loading larger bitmaps.
 *
 * The decoding can be done for
 *
 * 1. Images present in resources
 * 2. Images present in disk which are referenced with Uri.
 */
public class BitmapDecoderTask extends AsyncTask<Void,Void,Bitmap> {

    class BitmapDecodeException extends RuntimeException {
        public BitmapDecodeException(String msg) { super(msg);
        }
    }


    enum DecodingOptions {
        DECODE_FROM_RESOURCE,
        DECODE_FROM_DISK,
        DECODING_NOT_SPECIFIED;
    }

    public interface OnDecodeListener {

        void onDecode(BitmapDecoderTask task, Bitmap output);
    }

    private int mReqHeight,mReqWidth;
    private OnDecodeListener mListener;
    private Uri mContentProviderUri; // if getting bitmap from external storage or from disk
    private ContentResolver mResolver;
    private int mDrawableId; // if getting bitmap from drawable.
    private Resources mResources;
    private DecodingOptions mDecodingOptions = DecodingOptions.DECODING_NOT_SPECIFIED;


    /**
     * Set the listener for listening to the background task of decoding and sampling the image.
     * @param listener - listener for decode complete.
     * @return the related BitmapDecoderTask
     */
    public BitmapDecoderTask setListener(OnDecodeListener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * Sets the reference image which has to be decoded.
     * @param drawableId - id of the drawable.
     * @param resources - resources reference for getting the drawable.
     * @return related BitmapDecoderTask.
     */
    public BitmapDecoderTask setDecodingImageReference(int drawableId, Resources resources) {
        this.mDrawableId = drawableId;
        this.mResources = resources;
        this.mDecodingOptions = DecodingOptions.DECODE_FROM_RESOURCE;
        return this;
    }


    /**
     *  Sets the reference image which has to be decoded.
     * @param contentProviderUri - Uri which refers to a path containing the image.
     * @param mResolver - the resolver required to get the image from the Uri
     * @return the related BitmapDecoderTask
     */
    public BitmapDecoderTask setDecodingImageReference(Uri contentProviderUri, ContentResolver mResolver) {
        this.mResolver = mResolver;
        this.mContentProviderUri = contentProviderUri;
        this.mDecodingOptions = DecodingOptions.DECODE_FROM_DISK;
        return this;
    }

    /**
     * Sets the required width for decoding the image.
     *
     * This is highly necessary for sampling the image. As referred in <a href="https://developer.android.com/training/displaying-bitmaps/load-bitmap.html#load-bitmap">https://developer.android.com/training/displaying-bitmaps/load-bitmap.html#load-bitmap</a>
     * its not worth loading a full image as large image will always be scaled down before being displayed, we have this method for sampling for reducing
     * the size before being set for efficient processing and escape from java.lang.OutOfMemoryError.
     *
     *
     * @param width - width of the reference view for sampling. Calculate the width of the reference view where the image has to be displayed.
     * @return the related BitmapDecoderTask
     */
    public BitmapDecoderTask setRequiredWidth(int width) {
        this.mReqWidth = width;
        return this;
    }

    /**
     * Sets the required height for decoding the image.
     *
     * This is highly necessary for sampling the image. As referred in <a href="https://developer.android.com/training/displaying-bitmaps/load-bitmap.html#load-bitmap">https://developer.android.com/training/displaying-bitmaps/load-bitmap.html#load-bitmap</a>
     * its not worth loading a full image as large image will always be scaled down before being displayed, we have this method for sampling for reducing
     * the size before being set for efficient processing and escape from java.lang.OutOfMemoryError.
     *
     *
     * @param height - height of the reference view for sampling. Calculate the height of the reference view where the image has to be displayed.
     * @return the related BitmapDecoderTask
     */
    public BitmapDecoderTask setRequiredHeight(int height) {
        this.mReqHeight = height;
        return this;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        switch (mDecodingOptions) {
            case DECODE_FROM_DISK:
                return decodeSampledBitmapFromDisk();
            case DECODE_FROM_RESOURCE:
                return decodeSampledBitmapFromResource();
            default:
                throw new BitmapDecodeException("Did not specify the image reference with setDecodingImageReference()");
        }
    }

    @Override
    public void onPostExecute(Bitmap bitmap) {
        if(mListener != null) {
            mListener.onDecode(this, bitmap);
        }
    }

    /**
     * Initiates the background process to decoding and sampling the image.
     */
    public void decode() {
        super.execute((Void[]) null);
    }

    private Bitmap decodeSampledBitmapFromDisk() {

        try {

            if(mResolver == null || mContentProviderUri == null) {
                throw new BitmapDecodeException("Did not provide the uri reference or resolver");
            }

            if(mReqWidth <= 0 ) {
                throw new BitmapDecodeException("Did not provide a valid required width. Should be > 0");
            }

            if(mReqHeight <= 0 ) {
                throw new BitmapDecodeException("Did not provide a valid required height. Should be > 0");
            }

            InputStream sampleStream = mResolver.openInputStream(mContentProviderUri);
            InputStream samplingStream = mResolver.openInputStream(mContentProviderUri);


            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(sampleStream, null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, mReqWidth, mReqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(samplingStream, null, options);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BitmapDecodeException("Did not provide a valid Uri.");

        }
    }


    private Bitmap decodeSampledBitmapFromResource() {

        if(mResources == null ) {
            throw new BitmapDecodeException("Did not provide the resources");
        }

        if(mReqWidth <= 0 ) {
            throw new BitmapDecodeException("Did not provide a valid required width. Should be > 0");
        }

        if(mReqHeight <= 0 ) {
            throw new BitmapDecodeException("Did not provide a valid required height. Should be > 0");
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(mResources, mDrawableId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, mReqWidth, mReqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(mResources, mDrawableId, options);
    }


    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
