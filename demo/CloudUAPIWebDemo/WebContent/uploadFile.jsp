<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.*, java.util.regex.*, com.oreilly.servlet.MultipartRequest, com.oreilly.servlet.multipart.DefaultFileRenamePolicy, java.util.List, kr.co.talesapp.clouduapi.CUError, kr.co.talesapp.clouduapi.CUFile, kr.co.talesapp.clouduapi.OAuthTokens, kr.co.talesapp.clouduapi.CloudUFS, kr.co.talesapp.clouduapi.CloudFS, kr.co.talesapp.clouduapi.interfaces.*, java.net.URLEncoder;" %>
<%
CloudUFS cloudUFS=new CloudUFS();
cloudUFS.init(ICloudFS.CloudType.DROPBOX, null, "hmmwrbrwshafakh", "wtjvzh5i00ih62p", "5p1btcqv2j3axml", "mnm9rsmkcbplfol");
cloudUFS.init(ICloudFS.CloudType.BOX, "jqs5fv8rh0zgh1rmpfczpzlm4g88qgg2", "3v8fdpthe4d75v4sy46okgfkyktut3uw", null, null, null);
cloudUFS.init(ICloudFS.CloudType.UBUNTUONE, null, "JNtxahtoDovImVAPnFTnbReOarZuNMmSxmJdrcUZoXrBNZksPp",
        "RljefIBkfHZBnKmgnlAObBHPRdikErWoGZJoesHeeaargynAxv",
        "EDp6FXB",
        "iUyUXUQLzNqUnBYUoKUrPCSRXcYfWH");
cloudUFS.setDebug(true);
CUError error=null;
String pattern="multipart/form-data";
String contenttype=request.getContentType();
String savePath=System.getProperty("java.io.tmpdir");
int sizeLimit = 5 * 1024 * 1024 ;
String fileName="";

if(contenttype!=null){
    Pattern p=Pattern.compile(pattern);
    Matcher m=p.matcher(contenttype);
    if(m.find()) {
        MultipartRequest multi=new MultipartRequest(request, savePath, sizeLimit, "UTF-8", new DefaultFileRenamePolicy());

        fileName=multi.getOriginalFileName("file");
        if(fileName!=null) {
	        String type=multi.getParameter("type");
	        String path=multi.getParameter("path");
			String folder_id=multi.getParameter("folder_id");
            File file=multi.getFile("file");
			if(type.equals("ubuntuone")) {
			    error=cloudUFS.uploadFile(ICloudFS.CloudType.UBUNTUONE, file, fileName, path, folder_id);
			} else if(type.equals("box")) {
			    error=cloudUFS.uploadFile(ICloudFS.CloudType.BOX, file, fileName, path, folder_id);
			} else if(type.equals("dropbox")){
			    error=cloudUFS.uploadFile(ICloudFS.CloudType.DROPBOX, file, fileName, path, folder_id);
			}
			response.setContentType("application/json");
			out.println(error.toJSON());
        }
    }
}
%>