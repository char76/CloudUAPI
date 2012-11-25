package kr.co.talesapp.clouduapi;

import java.util.Date;

import kr.co.talesapp.clouduapi.interfaces.ICloudFS.CloudType;

public class CUFile {
	public int size;
	public boolean isDir;
	public String name;
	public String path;
	public String root;
	public CloudType type;
	public Date time;
	public String id;
	public String sha1;
	
	public void setSize(int size) {
		this.size=size;
	}
	public int getSize() {
		return size;
	}
	public void setDir(boolean dir) {
		this.isDir=dir;
	}
	public boolean getDir() {
		return isDir;
	}
	public void setName(String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setPath(String path) {
		this.path=path;
	}
	public String getPath() {
		return path;
	}
	public void setRoot(String root) {
		this.root=root;
	}
	public String getRoot() {
		return root;
	}
	public void setType(CloudType type) {
		this.type=type;
	}
	public CloudType getType() {
		return type;
	}
	public void setTime(Date time) {
		this.time=time;
	}
	public Date getTime() {
		return time;
	}
	public void setId(String id) {
		this.id=id;
	}
	public String getId() {
		return id;
	}
	public void setSha1(String sha1) {
		this.sha1=sha1;
	}
	public String getSha1() {
		return sha1;
	}
}
