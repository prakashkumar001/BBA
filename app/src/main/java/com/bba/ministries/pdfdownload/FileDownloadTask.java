package com.bba.ministries.pdfdownload;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.bba.ministries.Common.GlobalClass;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Simulate downloading a file, by increasing the progress of the FileInfo from 0 to size - 1.
 */
public class FileDownloadTask extends AsyncTask<Void, Integer, Void> {
  private static final String    TAG = FileDownloadTask.class.getSimpleName();
  final DownloadInfo  mInfo;

  Context context;

  public FileDownloadTask(DownloadInfo info, Context context) {
    mInfo = info;
    this.context=context;
  }

  @Override
  protected void onProgressUpdate(Integer... values) {
	  RateTextCircularProgressBar bar = mInfo.getProgressBar();

		  mInfo.setProgress(values[0]);

		    if(bar != null) {

              Log.i("TTTTTTTTTTTTTTTTTT","TTTTTTTTTTTTTTT");

		      bar.setProgress(mInfo.getProgress());
		      bar.invalidate();
		    }

	  
	  
   
  }

  @Override
  protected Void doInBackground(Void... params) {
    Log.d(TAG, "Starting download for " + mInfo.getFilename());


    mInfo.setDownloadState(DownloadInfo.DownloadState.DOWNLOADING);
    int count;
    try {
      URL url = new URL(GlobalClass.imageUrl+"magazines/"+mInfo.id+"/"+mInfo.fileurl);
      URLConnection conection = url.openConnection();
      long fileLength = 0;
      long downloadfilelength = 0;
      BufferedInputStream inputStream = null;
      BufferedOutputStream outputStream = null;


      String root = Environment.getExternalStorageDirectory().toString();
      File myDir = new File(root + "/BBA_Ministries/Magazines");
      myDir.mkdirs();
      File file = new File(myDir +"/"+ mInfo.getFilename()+".pdf");


      if (file.exists()) {
        downloadfilelength = file.length();
        conection.setRequestProperty("Range", "bytes=" + downloadfilelength + "-");
        outputStream = new BufferedOutputStream(new FileOutputStream(file, true));

      } else {

        outputStream = new BufferedOutputStream(new FileOutputStream(file));

      }

      fileLength = conection.getContentLength();

      conection.connect();




      inputStream = new BufferedInputStream(conection.getInputStream());



      byte data[] = new byte[1024];
      long total = downloadfilelength;

      while ((count = inputStream.read(data)) != -1) {
        total += count;
        // Publish the progress
        publishProgress((int) (total * 100 / fileLength));
        outputStream.write(data, 0, count);


      }

      // Close connection
      outputStream.flush();
      outputStream.close();
      inputStream.close();
    } catch (Exception e) {
      // Error Log
      Log.e("Error", e.getMessage());
      e.printStackTrace();
    }
    mInfo.setDownloadState(DownloadInfo.DownloadState.COMPLETE);
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    mInfo.setDownloadState(DownloadInfo.DownloadState.COMPLETE);
    if(mInfo.getDownloadState()== DownloadInfo.DownloadState.COMPLETE )
    {
      //mInfo.getButton().setEnabled(true);

      mInfo.getProgressBar().setVisibility(View.INVISIBLE);
      mInfo.getViews().setVisibility(View.VISIBLE);
      //mInfo.getFilepdf().setVisibility(View.VISIBLE);
      mInfo.getViews().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          makeImageFromPDF();


        }
      });

    }



  }

  @Override
  protected void onPreExecute() {
    mInfo.setDownloadState(DownloadInfo.DownloadState.DOWNLOADING);

  }

  public void makeImageFromPDF()

  {
    String filepath = Environment.getExternalStorageDirectory()
            .getPath();


    File f = new File(filepath + "/"
            + mInfo.getFilename()+".pdf");

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.fromFile(f), "application/pdf");
    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    context.startActivity(intent);


  }
}
