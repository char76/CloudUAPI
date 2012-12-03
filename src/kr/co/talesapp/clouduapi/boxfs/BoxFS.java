package kr.co.talesapp.clouduapi.boxfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.co.talesapp.clouduapi.CUError;
import kr.co.talesapp.clouduapi.CUFile;
import kr.co.talesapp.clouduapi.CloudFS;
import kr.co.talesapp.clouduapi.OAuthTokens;
import kr.co.talesapp.clouduapi.interfaces.ICloudFS.CloudType;

public class BoxFS extends CloudFS {
	Properties prop;
    DefaultHttpClient httpclient = new DefaultHttpClient();
	HttpContext localContext = new BasicHttpContext();
	String authHeader=null;
	
	public CUError createFolder(String name, String parentId){
		CUError err=new CUError();
		try {
			String fullPath=prop.getProperty("BoxFS.createFolder");
			
			HttpPost httpPost=new HttpPost(fullPath);
			httpPost.setHeader("Authorization", authHeader);
			//JSONObject json=JSONObject.fromObject("{ \"path\": "+newPath+" }");
			StringEntity input;
			if(parentId.equals("/")){
				parentId="0";
			}
			input = new StringEntity("{ \"name\": \""+name+"\", \"parent\": {\"id\": \""+parentId+"\" } }", "UTF-8");
			if(debug) {
				System.out.println("box createFolder : "+input.toString()+", "+name);
			}
			input.setContentEncoding("UTF-8");
			input.setContentType("application/json");
			httpPost.setEntity(input);
			HttpResponse response;
			response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("box createFolder : "+responseString);
			}
			if(responseString.equals("")) {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			} else {
				JsonParser jsonParser=new JsonParser();
				JsonObject json=jsonParser.parse(responseString).getAsJsonObject();
				if(json!=null && json.get("type")!=null && json.get("type").getAsString().equals("error")) {
					err.error=CUError.ERROR;
					err.errorString=json.get("code").getAsString();
				} else {
					err.error=CUError.SUCCESS;
					err.errorString="success";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}

		return err;
	}
	public CUError uploadFile(File file, String fileName, String folderId) {
		CUError err=new CUError();
		try {
			if(folderId!=null && folderId.equals("/")) {
				folderId="0";
			}
			String fullPath=prop.getProperty("BoxFS.uploadFile");
			
		    HttpPost httpPost=new HttpPost(fullPath);

		    /*
		    MessageDigest md = MessageDigest.getInstance("SHA1");
		    FileInputStream fis = new FileInputStream(file);
		    byte[] dataBytes = new byte[1024];
		    int nread = 0; 
		    while ((nread = fis.read(dataBytes)) != -1) {
		      md.update(dataBytes, 0, nread);
		    }
		    byte[] mdbytes = md.digest();
		    StringBuffer sb = new StringBuffer("");
		    for (int i = 0; i < mdbytes.length; i++) {
		    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
			httpPost.addHeader("Content-MD5", sb.toString());
		    
		    */
		    httpPost.addHeader("Authorization", authHeader);
			/*
			List<NameValuePair> param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("filename", file.getName()));
			param.add(new BasicNameValuePair("folder_id", String.valueOf(folderId)));
	        UrlEncodedFormEntity sendentity = new UrlEncodedFormEntity(param, "UTF-8");  
	        FileEntity entity=new FileEntity(file, ContentType.create(new MimetypesFileTypeMap().getContentType(file)));
			httpPost.setEntity(entity);
			httpPost.setEntity(sendentity);
			*/
			if(debug) {
				System.out.println("box uploadFile : "+httpPost.getURI()+", "+folderId+", "+fileName);
			}
            FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart(new FormBodyPart("file", bin));
            reqEntity.addPart("filename", new StringBody(URLDecoder.decode(fileName, "UTF-8")));
            reqEntity.addPart("folder_id", new StringBody(String.valueOf(folderId)));
            httpPost.setEntity(reqEntity);
			HttpResponse response;
			response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("box uploadFile : "+responseString);
			}
			JsonParser jsonParser=new JsonParser();
			JsonObject json=jsonParser.parse(responseString).getAsJsonObject();
			if(json!=null && json.get("type")!=null && json.get("type").getAsString().equals("error")) {
				err.error=CUError.ERROR;
				err.errorString=json.get("code").getAsString();
			} else {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}

		return err;
	}
	public CUError downloadFile(String fileId) {
		CUError err=new CUError();
		try {
			String fullPath=prop.getProperty("BoxFS.downloadFile")+"/"+fileId+"/content";
			
			HttpGet httpGet=new HttpGet(fullPath);
			httpGet.setHeader("Authorization", authHeader);
			//JSONObject json=JSONObject.fromObject("{ \"path\": "+newPath+" }");
			HttpResponse response;
			response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			byte[] responseString=EntityUtils.toByteArray(responseEntity);
			if(debug) {
				//System.out.println(responseString);
			}
			if(response.getStatusLine().getStatusCode()==200) {
				err.error=CUError.SUCCESS;
				//err.errorString="success";
				err.body=responseString;
				err.contentLength=responseEntity.getContentLength();
				err.contentType=responseEntity.getContentType();
			} else {
				JsonParser jsonParser=new JsonParser();
				JsonObject json=jsonParser.parse(responseString.toString()).getAsJsonObject();
				if(json!=null && json.get("type")!=null && json.get("type").getAsString().equals("error")) {
					err.error=CUError.ERROR;
					err.errorString=json.get("code").getAsString();
				} else {
					err.error=CUError.SUCCESS;
					err.errorString="success";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}

		return err;
	}
	public CUError moveFolder(String path, String newPath){
		CUError err=new CUError();
		err.error=CUError.NOT_IMPLEMENTED;
		err.errorString="not implemented";
		return err;
	}
	
	public CUError moveFile(String path, String newPath){
		CUError err=new CUError();
		err.error=CUError.NOT_IMPLEMENTED;
		err.errorString="not implemented";
		return err;
	}
	
	public CUError renameFolder(String path, String newPath){
		CUError err=new CUError();
		err.error=CUError.NOT_IMPLEMENTED;
		err.errorString="not implemented";
		return err;
	}
	
	public CUError renameFile(String path, String newPath){
		CUError err=new CUError();
		err.error=CUError.NOT_IMPLEMENTED;
		err.errorString="not implemented";
		return err;
	}
	
	public CUError copyFolder(String path, String newPath) {
		CUError err=new CUError();
		try {
			String fullPath=prop.getProperty("BoxFS.copyFolder");
			fullPath+="/"+path+"/copy";
			
			HttpPost httpPost=new HttpPost(fullPath);
			httpPost.setHeader("Authorization", authHeader);
			//JSONObject json=JSONObject.fromObject("{ \"path\": "+newPath+" }");
			StringEntity input;
			if(newPath!=null && newPath.equals("/")) {
				newPath="0";
			}
			input = new StringEntity("{ \"parent\": {\"id\": \""+newPath+"\" } }");
			input.setContentType("application/json");
			httpPost.setEntity(input);
			HttpResponse response;
			response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("box copyFolder : "+httpPost.getURI());
				System.out.println("box copyFolder : { \"parent\": {\"id\": \""+newPath+"\" } }");
				System.out.println("box copyFolder : "+responseString);
			}
			if(responseString.equals("")) {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			} else {
				JsonParser jsonParser=new JsonParser();
				JsonObject json=jsonParser.parse(responseString).getAsJsonObject();
				if(json!=null && json.get("type")!=null && json.get("type").getAsString().equals("error")) {
					err.error=CUError.ERROR;
					err.errorString=json.get("code").getAsString();
				} else {
					err.error=CUError.SUCCESS;
					err.errorString="success";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}

		return err;
	}
	
	public CUError copyFile(String path, String newPath) {
		CUError err=new CUError();
		try {
			String fullPath=prop.getProperty("BoxFS.copyFile");
			fullPath+="/"+path+"/copy";
			
			HttpPost httpPost=new HttpPost(fullPath);
			httpPost.setHeader("Authorization", authHeader);
			//JSONObject json=JSONObject.fromObject("{ \"path\": "+newPath+" }");
			StringEntity input;
			if(newPath!=null && newPath.equals("/")) {
				newPath="0";
			}
			input = new StringEntity("{ \"parent\": {\"id\": \""+newPath+"\" } }");
			input.setContentType("application/json");
			httpPost.setEntity(input);
			HttpResponse response;
			response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("box copyFile : "+httpPost.getURI());
				System.out.println("box copyFile : { \"parent\": {\"id\": \""+newPath+"\" } }");
				System.out.println("box copyFile : "+responseString);
			}
			if(responseString.equals("")) {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			} else {
				JsonParser jsonParser=new JsonParser();
				JsonObject json=jsonParser.parse(responseString).getAsJsonObject();
				if(json!=null && json.get("type")!=null && json.get("type").getAsString().equals("error")) {
					err.error=CUError.ERROR;
					err.errorString=json.get("code").getAsString();
				} else {
					err.error=CUError.SUCCESS;
					err.errorString="success";
				}
			}
			// TODO Auto-generated catch block
		} catch (Exception e) {
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}

		return err;
	}
	
	public CUError deleteFolder(String folderId) {
		CUError err=new CUError();
		try {
			String fullPath=prop.getProperty("BoxFS.deleteFolder");
			fullPath+="/"+folderId+"?recursive=true";
			
			HttpDelete httpDelete=new HttpDelete(fullPath);
			if(debug) {
				System.out.println(httpDelete.getURI());
			}
			httpDelete.setHeader("Authorization", authHeader);
			//JSONObject json=JSONObject.fromObject("{ \"path\": "+newPath+" }");
			HttpResponse response;
			response = httpclient.execute(httpDelete, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println(responseString);
			}
			if(responseString.equals("")) {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			} else {
				JsonParser jsonParser=new JsonParser();
				JsonObject json=jsonParser.parse(responseString).getAsJsonObject();
				if(json!=null && json.get("type")!=null && json.get("type").getAsString().equals("error")) {
					err.error=CUError.ERROR;
					err.errorString=json.get("code").getAsString();
				} else {
					err.error=CUError.SUCCESS;
					err.errorString="success";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}

		return err;
	}
	
	public CUError deleteFile(String fileId, String sha1) {
		CUError err=new CUError();
		try {
			String fullPath=prop.getProperty("BoxFS.deleteFile");
			fullPath+="/"+fileId;
			
			HttpDelete httpDelete=new HttpDelete(fullPath);
			httpDelete.addHeader("Authorization", authHeader);
			httpDelete.addHeader("If-Match", sha1);
			//JSONObject json=JSONObject.fromObject("{ \"path\": "+newPath+" }");
			if(debug) {
				System.out.println(httpDelete.getURI());
				Header[] headers=httpDelete.getAllHeaders();
				for(Header header: headers) {
					System.out.println(header.toString());
				}
			}
			HttpResponse response;
			response = httpclient.execute(httpDelete, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println(responseString);
			}
			if(responseString.equals("")) {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			} else {
				JsonParser jsonParser=new JsonParser();
				JsonObject json=jsonParser.parse(responseString).getAsJsonObject();
				if(json!=null && json.get("type")!=null && json.get("type").getAsString().equals("error")) {
					err.error=CUError.ERROR;
					err.errorString=json.get("code").getAsString();
				} else {
					err.error=CUError.SUCCESS;
					err.errorString="success";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}

		return err;
	}
	
	public CUFile genCUFile(JsonObject map) {
		CUFile cufile=new CUFile();
		try {
			int size=0;
			String kind=map.get("type").getAsString();
			String name=map.get("name").getAsString();
			if(map.get("size")!=null) {
				size=map.get("size").getAsInt();
			}
			//String root=(String)map.get("parent");
			Date time;
			String tmp=( map.get("modified_at").getAsString()).replaceAll("T", " ");
			StringBuilder b = new StringBuilder(tmp);
			b.replace(tmp.lastIndexOf(":"), tmp.lastIndexOf(":")+1, "" );
			tmp = b.toString();
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.US).parse(tmp);
			String path=map.get("id").getAsString();
			if(kind.equals("file")) {
				cufile.setDir(false);
			} else {
				cufile.setDir(true);
			}
			cufile.setId(map.get("id").getAsString());
			cufile.setName(name);
			cufile.setType(CloudType.BOX);
			cufile.setPath(path);
			cufile.setTime(time);
			cufile.setSize(size);
			if(map.get("sha1")!=null) {
				cufile.setSha1(map.get("sha1").getAsString());
			}
			//cufile.setRoot(root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return cufile;
	}

	public List<CUFile> getFiles(String path) {
		try {
			String fullPath=prop.getProperty("BoxFS.getFolderTree");
			if(path!=null && !path.equals("") && !path.equals("/")) {
				fullPath+=path+"?fields=modified_at,path,name,size,parent,sha1";
			} else if(path!=null && path.equals("/")) {
				fullPath+="/0?fields=modified_at,path,name,size,parent,sha1";
			}
			HttpGet httpGet=new HttpGet(fullPath);
			httpGet.setHeader("Authorization", authHeader);
			if(debug) {
				System.out.println("box auth header : "+httpGet.getURI()+", "+authHeader);
			}
			HttpResponse response;
			response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("box getFiles : "+responseString);
			}
			JsonParser jsonParser=new JsonParser();
			JsonObject json=jsonParser.parse(responseString).getAsJsonObject();
			json=json.getAsJsonObject("item_collection");
			if(debug) {
				System.out.println(json.toString());
			}
			List<CUFile> cufile=new ArrayList<CUFile>();
			if(json!=null) {
				JsonArray children=json.getAsJsonArray("entries");
				for(int i=0;children!=null && i<children.size();i++){// obj : children){
					JsonObject map=children.get(i).getAsJsonObject();
					if(debug) {
						System.out.println(map.toString());
					}
					cufile.add(genCUFile(map));
				}
			}
			return cufile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void genAuthHeader() {
		if(authHeader==null) {
			authHeader="BoxAuth api_key="+key+"&auth_token="+token;
		}
	}

	public BoxFS(Properties prop) {
		this.prop=prop;
	}
	
	public String getAuthToken(String key, String ticket) {
		this.key=key;
		HttpGet httpGet=new HttpGet(prop.getProperty("BoxFS.getAuthTicket")+"&api_key="+key+"&ticket="+ticket);
		HttpResponse response;
		String auth_token=null;
		try {
			response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			String resString=EntityUtils.toString(responseEntity);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(resString));
	        Document doc=builder.parse(is);
	        doc.getDocumentElement().normalize();
	        NodeList headNodeList = doc.getElementsByTagName("response");
	        for(int i=0; i<headNodeList.getLength(); i++) {
	              Node headNode = headNodeList.item(i);
	             
	              //상단의 노드가 노드 타입이라면
	              if(headNode.getNodeType() == Node.ELEMENT_NODE) {
	                   Element headLineElement = (Element) headNode;
	                  
	                   NodeList nameElement = headLineElement.getElementsByTagName("status");
	                   Element subItem = (Element) nameElement.item(0);
	                   NodeList subElement1 = subItem.getChildNodes();
	                   if(subElement1.item(0).getNodeValue().equals("get_auth_token_ok")){
	                       nameElement = headLineElement.getElementsByTagName("auth_token");
	                       subItem = (Element) nameElement.item(0);
	                       subElement1 = subItem.getChildNodes();
	                       auth_token=subElement1.item(0).getNodeValue();
	                   }
	              }
	        }
	        // EntityUtils.consume(responseEntity);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return auth_token;
	}
	
	@Override
	public OAuthTokens getRequestToken(String appKey, String appSecret) {
		this.token=appKey;
		this.token_secret=appSecret;
	
		HttpGet httpGet=new HttpGet(prop.getProperty("BoxFS.getTicket")+"&api_key="+token);
		//System.out.println(httpGet.getURI());
		try {
			HttpResponse response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			String resString=EntityUtils.toString(responseEntity);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(resString));
	        Document doc=builder.parse(is);
	        doc.getDocumentElement().normalize();
            NodeList headNodeList = doc.getElementsByTagName("response");
            for(int i=0; i<headNodeList.getLength(); i++) {
                  Node headNode = headNodeList.item(i);
                 
                  //상단의 노드가 노드 타입이라면
                  if(headNode.getNodeType() == Node.ELEMENT_NODE) {
                       Element headLineElement = (Element) headNode;
                      
                       NodeList nameElement = headLineElement.getElementsByTagName("status");
                       Element subItem = (Element) nameElement.item(0);
                       NodeList subElement1 = subItem.getChildNodes();
                       if(subElement1.item(0).getNodeValue().equals("get_ticket_ok")){
                           nameElement = headLineElement.getElementsByTagName("ticket");
                           subItem = (Element) nameElement.item(0);
                           subElement1 = subItem.getChildNodes();
                           oauth_token.setTicket(subElement1.item(0).getNodeValue());
                       }
                  }
            }

	        //EntityUtils.consume(responseEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return oauth_token;
	}

	/*
	@Override
	public OAuthTokens getAuthorize(OAuthTokens tokens, String callbackUrl) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
}
