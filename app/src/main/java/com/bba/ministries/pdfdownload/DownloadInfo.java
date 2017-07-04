package com.bba.ministries.pdfdownload;

import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class DownloadInfo {
  private final static String TAG = DownloadInfo.class.getSimpleName();
  public  String mFilename;
  public String fileurl;
  public String id;
  public String fileimage;

  public enum DownloadState {
    NOT_STARTED,
    QUEUED,
    DOWNLOADING,
    COMPLETE
  }
  private volatile DownloadState mDownloadState = DownloadState.NOT_STARTED;



  private volatile Integer mProgress;
  private volatile RateTextCircularProgressBar mProgressBar;
  private volatile LinearLayout download;

  public LinearLayout getViews() {
    return views;
  }

  public void setViews(LinearLayout views) {
    this.views = views;
  }

  private volatile LinearLayout views;

  public ImageView getFilepdf() {
    return filepdf;
  }

  public void setFilepdf(ImageView filepdf) {
    this.filepdf = filepdf;
  }

  private volatile ImageView filepdf;
  
  public int position=-1;



  
  public LinearLayout getButton() {
	return download;
}

public void setButton(LinearLayout download) {
	this.download = download;
}

  public DownloadInfo(String filename, String fileurl) {
    mFilename = filename;
    this.fileurl=fileurl;

  }

  public DownloadInfo(String id,String title,String image, String fileurl) {
    this.id=id;
    fileimage=image;
    mFilename = title;
    this.fileurl=fileurl;

  }


  
  public RateTextCircularProgressBar getProgressBar() {
    return mProgressBar;
  }
  public void setProgressBar(RateTextCircularProgressBar progressBar) {
    Log.d(TAG, "setProgressBar " + mFilename + " to " + progressBar);
    mProgressBar = progressBar;
  }
  
  public void setDownloadState(DownloadState state) {
    mDownloadState = state;
  }
  public DownloadState getDownloadState() {
    return mDownloadState;
  }
  
  public Integer getProgress() {
    return mProgress;
  }

  public void setProgress(Integer progress) {
    this.mProgress = progress;
  }



  public String getFilename() {
    return mFilename;
  }
}
