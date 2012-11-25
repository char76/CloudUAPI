package kr.co.talesapp.clouduapi.interfaces;

import kr.co.talesapp.clouduapi.OAuthTokens;

public interface ICloudFS {
	public enum CloudType {
		BOX,
		DROPBOX,
		UBUNTUONE
	}
	
	public int createFile();
	public int createFolder();
	public int deleteFile();
	public int deleteFolder();
	public int copyFile();
	public int copypFolder();
	public int downloadFile();
	public int getAccountInfo();
	public int getFolderTree();
	public OAuthTokens login();
	public int moveFile();
	public int moveFolder();
	public int uploadFile();
	public OAuthTokens getRequestToken(CloudType type);
	public OAuthTokens getRequestToken(String appKey, String appSecret);
	public OAuthTokens getRequestToken(CloudType type, String appKey, String appSecret);
	public OAuthTokens getRequestToken();
	//public OAuthTokens getAuthorize(OAuthTokens tokens, String callbackUrl);
}
