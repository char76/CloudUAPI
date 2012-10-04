package kr.co.talesapp.clouduapi.interfaces;

public interface ICloudFS {
	public boolean createFile();
	public boolean createFolder();
	public boolean deleteFile();
	public boolean deleteFolder();
	public boolean copyFile();
	public boolean copypFolder();
	public boolean downloadFile();
	public boolean getAccountInfo();
	public boolean getFolderTree();
	public boolean login();
	public boolean moveFile();
	public boolean moveFolder();
	public boolean uploadFile();
}
