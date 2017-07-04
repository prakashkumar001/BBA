package com.bba.ministries.pdfdownload;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;


public class DownloadInfoArrayAdapter extends ArrayAdapter<DownloadInfo> {
  // Simple class to make it so that we don't have to call findViewById frequently

    Context context;
    List<DownloadInfo> objects;
  ImageLoader loader;
    private static class ViewHolder {
    TextView filename;
    RateTextCircularProgressBar progressBar;
      LinearLayout download,views;
      ImageView filepdf;

    DownloadInfo info;
    }
  
  
  private static final String TAG = DownloadInfoArrayAdapter.class.getSimpleName();

  public DownloadInfoArrayAdapter(Context context, int textViewResourceId,
      List<DownloadInfo> objects) {
    super(context, textViewResourceId, objects);

    this.objects=objects;
    this.context=context;
    loader=ImageLoader.getInstance();
  }
  
  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    View row = convertView;
    final DownloadInfo info = objects.get(position);
    // We need to set the convertView's progressBar to null.

    ViewHolder holder = null;
    
    if(null == row) {
      LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.pdf_list_item, parent, false);
      
      holder = new ViewHolder();
      holder.filename = (TextView) row.findViewById(R.id.title);
      holder.progressBar = (RateTextCircularProgressBar) row.findViewById(R.id.rate_progress_bar);
      holder.download = (LinearLayout)row.findViewById(R.id.download);
      holder.filepdf=(ImageView) row.findViewById(R.id.image);
      holder.views=(LinearLayout) row.findViewById(R.id.views);
      holder.info = info;
      
      row.setTag(holder);
    } else {
      holder = (ViewHolder) row.getTag();
      
     // holder.info.setProgressBar(null);
      //holder.info.setButton(null);
      holder.info = info;
      holder.info.setProgressBar(holder.progressBar);
      holder.info.setButton(holder.download);
      holder.info.setFilepdf(holder.filepdf);
    }


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.loadimage) // resource or drawable
            .showImageForEmptyUri(R.drawable.loadimage) // resource or drawable
            .showImageOnFail(R.drawable.loadimage) // resource or drawable
            .resetViewBeforeLoading(false)  // default
            .delayBeforeLoading(10)
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .build();


    holder.filename.setText(info.getFilename());
    loader.displayImage(GlobalClass.imageUrl+"magazines/"+info.id+"/"+info.fileimage,holder.filepdf,options);

    //holder.progressBar.setProgress(info.getProgress());
    info.setProgressBar(holder.progressBar);
    info.setButton(holder.download);
    info.setViews(holder.views);
    info.setFilepdf(holder.filepdf);
    
    //holder.button.setEnabled(info.getDownloadState() == DownloadState.NOT_STARTED);
    
    if(info.getDownloadState()== DownloadInfo.DownloadState.NOT_STARTED )
    {
    	//info.getButton().setEnabled(true);
         holder.download.setVisibility(View.VISIBLE);
    	 holder.progressBar.setVisibility(View.INVISIBLE);
      holder.views.setVisibility(View.GONE);
         //holder.filepdf.setVisibility(View.INVISIBLE);
    }
   
    if(info.getDownloadState()== DownloadInfo.DownloadState.DOWNLOADING )
    {
    	//info.getButton().setEnabled(true);
    	 holder.progressBar.setVisibility(View.VISIBLE);
      holder.download.setVisibility(View.GONE);
      //holder.filepdf.setVisibility(View.INVISIBLE);

    }
    if(info.getDownloadState()== DownloadInfo.DownloadState.COMPLETE)
    {
    	//info.getButton().setEnabled(true);
    	 holder.progressBar.setVisibility(View.INVISIBLE);
          holder.download.setVisibility(View.GONE);
      holder.views.setVisibility(View.VISIBLE);
      //holder.filepdf.setVisibility(View.VISIBLE);

    }

    String root = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/BBA_Ministries/Magazines";

    Log.i("RRRRRRRRRRRRRR","RRRRRRRRRrr"+root);
    File file = new File(root +"/"+ info.getFilename()+".pdf");

    if(file.exists()) {
      //Do something

      Log.i("Inside","Inside");
      holder.filepdf.setVisibility(View.VISIBLE);
      holder.download.setVisibility(View.GONE);
      holder.views.setVisibility(View.VISIBLE);
    }
    else
    {
      // Do something else
    }


    final LinearLayout button = holder.download;
    final RateTextCircularProgressBar progress = holder.progressBar;
    final ImageView view = holder.filepdf;

    holder.download.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
          info.setDownloadState(DownloadInfo.DownloadState.QUEUED);
        //button.setEnabled(false);
        progress.setVisibility(View.VISIBLE);
        button.setVisibility(View.INVISIBLE);
       // view.setVisibility(View.INVISIBLE);
        button.invalidate();

       // view.invalidate();
        FileDownloadTask task = new FileDownloadTask(info,context);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      }
    });

    holder.views.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        makeImageFromPDF(objects.get(position).getFilename());

      }
    });
    
    
    //TODO: When reusing a view, invalidate the current progressBar.
    
    return row;
  }
  public void makeImageFromPDF(String filename)

  {
    String root = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/BBA_Ministries/Magazines";

    Log.i("RRRRRRRRRRRRRR","RRRRRRRRRrr"+root);
    File file = new File(root +"/"+ filename+".pdf");

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    context.startActivity(intent);


  }
}
