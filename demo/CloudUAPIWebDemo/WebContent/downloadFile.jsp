<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.*, java.util.List, kr.co.talesapp.clouduapi.CUError, kr.co.talesapp.clouduapi.CUFile, kr.co.talesapp.clouduapi.OAuthTokens, kr.co.talesapp.clouduapi.CloudUFS, kr.co.talesapp.clouduapi.CloudFS, kr.co.talesapp.clouduapi.interfaces.*, java.net.URLEncoder;" %>
<%
CloudUFS cloudUFS=new CloudUFS();
cloudUFS.init(ICloudFS.CloudType.DROPBOX, null, "token", "secret", "api key", "api secret");
cloudUFS.init(ICloudFS.CloudType.BOX, "api key", "token", null, null, null);
cloudUFS.init(ICloudFS.CloudType.UBUNTUONE, null, "token",
        "secret",
        "consumer key",
        "consumer secret");
cloudUFS.setDebug(true);
String type=request.getParameter("type");
String path=request.getParameter("path");
String name=request.getParameter("name");
String file_id=request.getParameter("file_id");
CUError error=null;
if(type.equals("ubuntuone")) {
    error=cloudUFS.downloadFile(ICloudFS.CloudType.UBUNTUONE, path, file_id);
} else if(type.equals("box")) {
    error=cloudUFS.downloadFile(ICloudFS.CloudType.BOX, path, file_id);
} else if(type.equals("dropbox")){
    error=cloudUFS.downloadFile(ICloudFS.CloudType.DROPBOX, path, file_id);
}
//System.out.println(error.contentType.toString());
//System.out.println(error.contentLength);
response.setCharacterEncoding("utf-8");
response.setContentType(error.contentType.toString());
response.setContentLength((int)error.contentLength);
response.setHeader("Content-Disposition", "attachment;filename=\""+URLEncoder.encode(name, "UTF-8")+"\"");
OutputStream outstr=response.getOutputStream();  
outstr.write(error.body, 0, error.body.length); 
outstr.flush();
outstr.close();
%>
