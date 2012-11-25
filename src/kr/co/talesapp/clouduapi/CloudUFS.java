package kr.co.talesapp.clouduapi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kr.co.talesapp.clouduapi.boxfs.BoxFS;
import kr.co.talesapp.clouduapi.dropboxfs.DropboxFS;
import kr.co.talesapp.clouduapi.interfaces.ICloudFS.CloudType;
import kr.co.talesapp.clouduapi.ubuntuonefs.UbuntuOneFS;

public class CloudUFS  {
	BoxFS boxFS=null;
	DropboxFS dropboxFS=null;
	UbuntuOneFS uOneFS=null;
	public Properties prop;
	public boolean debug=false;
	
	public CUError createFolder(CloudType type, String name, String parent_path, String parent_id) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.createFolder(name, parent_id);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.createFolder(name, parent_path);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.createFolder(name, parent_path);
			}
			break;
		}
		return e;
	}
	public CUError downloadFile(CloudType type, String path, String file_id) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.downloadFile(file_id);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.downloadFile(path);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.downloadFile(path);
			}
			break;
		}
		return e;
	}
	public CUError uploadFile(CloudType type, String filePath, String fileName, String folder_path, String folder_id) {
		CUError e=null;
		File file=new File(filePath);
		if(file==null) {
			e=new CUError();
			e.error=-1;
			e.errorString="file not found";
			return e;
		}
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.uploadFile(file, fileName, folder_id);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.uploadFile(file, fileName, folder_path);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.uploadFile(file, fileName, folder_path);
			}
			break;
		}
		return e;
	}
	public CUError uploadFile(CloudType type, File file, String fileName, String folder_path, String folder_id) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.uploadFile(file, fileName, folder_id);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.uploadFile(file, fileName, folder_path);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.uploadFile(file, fileName, folder_path);
			}
			break;
		}
		return e;
	}
	public CUError deleteFolder(CloudType type, String path, String folder_id) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.deleteFolder(folder_id);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.deleteFolder(path);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.deleteFolder(path);
			}
			break;
		}
		return e;
	}

	public CUError deleteFile(CloudType type, String path, String file_id, String sha1) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.deleteFile(file_id, sha1);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.deleteFile(path);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.deleteFile(path);
			}
			break;
		}
		return e;
	}

	public CUError renameFolder(CloudType type, String path, String newPath) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.renameFolder(path, newPath);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.renameFolder(path, newPath);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.renameFolder(path, newPath);
			}
			break;
		}
		return e;
	}
	
	public CUError renameFile(CloudType type, String path, String newPath) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.renameFile(path, newPath);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.renameFile(path, newPath);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.renameFile(path, newPath);
			}
			break;
		}
		return e;
	}
	
	public CUError moveFolder(CloudType type, String path, String newPath) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.moveFolder(path, newPath);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.moveFolder(path, newPath);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.moveFolder(path, newPath);
			}
			break;
		}
		return e;
	}

	public CUError moveFile(CloudType type, String path, String newPath) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.moveFile(path, newPath);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.moveFile(path, newPath);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.moveFile(path, newPath);
			}
			break;
		}
		return e;
	}

	public CUError copyFolder(CloudType type, String path, String newPath) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.copyFolder(path, newPath);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.copyFolder(path, newPath);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.copyFolder(path, newPath);
			}
			break;
		}
		return e;
	}
	
	public CUError copyFile(CloudType type, String path, String newPath) {
		CUError e=null;
		switch(type){
		case BOX:
			if(boxFS!=null) {
				e=boxFS.copyFile(path, newPath);
			}
			break;
		case DROPBOX:
			if(dropboxFS!=null) {
				e=dropboxFS.copyFile(path, newPath);
			}
			break;
		case UBUNTUONE:
			if(uOneFS!=null) {
				e=uOneFS.copyFile(path, newPath);
			}
			break;
		}
		return e;
	}
	
	public void setDebug(boolean debug) {
		this.debug=debug;
		if(dropboxFS!=null) {
			dropboxFS.debug=debug;
		}
		if(boxFS!=null) {
			boxFS.debug=debug;
		}
		if(uOneFS!=null) {
			uOneFS.debug=debug;
		}
	}

	public List<CUFile> getFiles(CloudType type, String path) {
		// TODO Auto-generated method stub
		if(type!=null) {
			switch(type) {
			case BOX:
				return boxFS.getFiles(path);
			case DROPBOX:
				return dropboxFS.getFiles(path);
			case UBUNTUONE:
				return uOneFS.getFiles(path);
			}
		} else {
			List<CUFile> dstfiles=new ArrayList<CUFile>();
			List<CUFile> files=null;
			if(boxFS!=null) {
				files=boxFS.getFiles(path);
				if(files!=null) {
					dstfiles.addAll(files);
				}
			}
			files=null;
			if(dropboxFS!=null) {
				files=dropboxFS.getFiles(path);
				if(files!=null) {
					dstfiles.addAll(files);
				}
			}
			files=null;
			if(uOneFS!=null) {
				files=uOneFS.getFiles(path);
				if(files!=null) {
					dstfiles.addAll(files);
				}
			}
			return dstfiles;
		}
		return null;
	}
	
	public CloudUFS() {
    	prop = new Properties();
    	try {
			prop.load(this.getClass().getResourceAsStream("/clouduapi.properties"));
			boxFS=new BoxFS(prop);
			dropboxFS=new DropboxFS(prop);
			uOneFS=new UbuntuOneFS(prop);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init(CloudType type, String key, String token, String secret, String consumer, String consumer_secret) {
		switch(type) {
			case BOX:
				boxFS.init(key, token, secret, consumer, consumer_secret);
				boxFS.genAuthHeader();
				break;
			case DROPBOX:
				dropboxFS.init(key, token, secret, consumer, consumer_secret);
				dropboxFS.genAuthHeader();
				break;
			case UBUNTUONE:
				uOneFS.init(key, token, secret, consumer, consumer_secret);
				uOneFS.genAuthHeader();
				break;
		}
	}
	public String getAuthToken(String key, String ticket) {
		return boxFS.getAuthToken(key, ticket);
	}
	
	public OAuthTokens getAccessToken(CloudType type, OAuthTokens token) {
		OAuthTokens oAuthTokens=null;
		switch(type) {
		case BOX:
			break;
		case DROPBOX:
			oAuthTokens=dropboxFS.getAccessToken(token);
			break;
		case UBUNTUONE:
			oAuthTokens=uOneFS.getAccessToken(token);
			break;
		default:
			break;
		}
		return oAuthTokens;
	}
	public OAuthTokens getRequestToken(CloudType type, String appKey, String appSecret) {
		OAuthTokens oAuthTokens=null;
		
		// TODO Auto-generated method stub
		switch(type) {
		case BOX:
			oAuthTokens=boxFS.getRequestToken(appKey, appSecret);
			break;
		case DROPBOX:
			oAuthTokens=dropboxFS.getRequestToken(appKey, appSecret);
			break;
		case UBUNTUONE:
			oAuthTokens=uOneFS.getRequestToken(appKey, appSecret);
			break;
		default:
			break;
		}
		return oAuthTokens;
	}

	/*
	public OAuthTokens getAuthorize(CloudType type, OAuthTokens tokens, String callbackUrl) {
		switch(type) {
		case BOX:
			return boxFS.getAuthorize(tokens, callbackUrl);
		case DROPBOX:
			return dropboxFS.getAuthorize(tokens, callbackUrl);
		case UBUNTUONE:
			return uOneFS.getAuthorize(tokens, callbackUrl);
		default:
			break;
		}
		return null;
	}
	*/

	public int createFile() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int createFolder() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getAccountInfo() {
		// TODO Auto-generated method stub
		return 0;
	}

	public OAuthTokens getRequestToken(CloudType type) {
		// TODO Auto-generated method stub
		return getRequestToken(type, null, null);
	}
}
