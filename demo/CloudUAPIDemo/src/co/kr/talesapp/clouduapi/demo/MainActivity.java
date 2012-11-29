package co.kr.talesapp.clouduapi.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.talesapp.clouduapi.CUFile;
import kr.co.talesapp.clouduapi.CloudUFS;
import kr.co.talesapp.clouduapi.interfaces.ICloudFS;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	List<CUFile> fileList=null;
    CloudUFS cloudUFS=new CloudUFS();
	ListView mListViewFile=null;

	@TargetApi(9)
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
        	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        	StrictMode.setThreadPolicy(policy); 
        }

        CUSetting setting=new CUSetting(getApplicationContext());
        cloudUFS.init(ICloudFS.CloudType.DROPBOX, null, setting.dBoxToken, setting.dBoxTokenSecret, setting.dBoxApiKey, setting.dBoxApiSecret);
        cloudUFS.init(ICloudFS.CloudType.BOX, setting.boxApiKey, setting.boxToken, null, null, null);
        cloudUFS.init(ICloudFS.CloudType.UBUNTUONE, null, setting.uOneToken, setting.uOneTokenSecret, setting.uOneConsumer, setting.uOneConsumerSecret);
        cloudUFS.setDebug(true);
        
        mListViewFile = (ListView)findViewById(R.id.listview_file);
        mListViewFile.setOnItemClickListener(mFileListClickListener);
        mListViewFile.setOnItemLongClickListener(mFileListLongClickListener);
        fileList=cloudUFS.getFiles(null, "/");
        FileListAdapter mFileInfoAdapter = new FileListAdapter(this, fileList);
	    mListViewFile.setAdapter(mFileInfoAdapter);
        /*
        for(int i=0;list!=null && i<list.size();i++) {
        	CUFile f=(CUFile)list.get(i);
        	Log.d("onCreate", "file : "+f.getName()+", "+f.getPath()+", "+f.getTime());
        }
        */
        
    }

	AdapterView.OnItemLongClickListener mFileListLongClickListener=new AdapterView.OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			// share and delete
			/*
			if(mFileInofiList!=null) {
				Intent i=new Intent(MainActivity.this, PreShare.class);
				i.putExtra("name", mFileInofiList.get(position).file.getAbsolutePath());
				startActivityForResult(i, 0);
			}
			*/
			return false;
		}
	};
	public void setDirectoryFilelist() {
		FileListAdapter mFileInfoAdapter = new FileListAdapter(this, fileList);
		mListViewFile.setAdapter(mFileInfoAdapter);
	}
	AdapterView.OnItemClickListener mFileListClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			CUFile file=null;
			if(fileList!=null) {
				file=fileList.get(position);
			}
	
			if(file!=null && file.isDir){
		        fileList=cloudUFS.getFiles(file.type, file.getPath());
		        setDirectoryFilelist();
		        //mListViewFile.setOnItemLongClickListener(mFileListLongClickListener);
			}
		}
	};

	class FileListAdapter extends BaseAdapter {
	    //	private ArrayList<TwitterSearch> items;
		private Context context;
		int Layout;
		LayoutInflater Inflater;
	       	
		List<CUFile> fileInfoList;
	         
		public FileListAdapter(Context context, List<CUFile> infoList) {
			this.context = context;
			Layout = R.layout.filelist_layout;
			Inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			fileInfoList = (List<CUFile>)infoList;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				v = Inflater.inflate(Layout, parent, false);
			}
			CUFile fileInfo = fileInfoList.get(position);
			// Log.e(TAG, "[S.J] item.size: "+items.size());
			if (fileInfo != null) {
				String type="";
				if(fileInfo.type==ICloudFS.CloudType.BOX){
					type="Box";
				} else if(fileInfo.type==ICloudFS.CloudType.DROPBOX){
					type="Drop Box";
				} else if(fileInfo.type==ICloudFS.CloudType.UBUNTUONE){
					type="Ubuntu One";
            	}
            	((TextView)v.findViewById(R.id.cloudtype)).setText(type);						            	
            	((TextView)v.findViewById(R.id.fileTextView)).setText(fileInfo.getName());
            }
            return v;
        }

		public int getCount() {
			// TODO Auto-generated method stub
			return fileInfoList.size();
		}

		public CUFile getItem(int arg0) {
			// TODO Auto-generated method stub
			return fileInfoList.get(arg0);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	}
		
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		case R.id.menu_settings:
			//intent
			Intent prefIntent = new Intent(this, Setting.class);
			startActivity(prefIntent);
			return true;
		}
		return false;
	}
}
