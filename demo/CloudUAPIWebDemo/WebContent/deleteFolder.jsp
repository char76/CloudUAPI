<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, kr.co.talesapp.clouduapi.CUError, kr.co.talesapp.clouduapi.CUFile, kr.co.talesapp.clouduapi.OAuthTokens, kr.co.talesapp.clouduapi.CloudUFS, kr.co.talesapp.clouduapi.CloudFS, kr.co.talesapp.clouduapi.interfaces.*, java.net.URLEncoder;" %>
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
String folder_id=request.getParameter("folder_id");
CUError error=null;
if(type.equals("ubuntuone")) {
	error=cloudUFS.deleteFolder(ICloudFS.CloudType.UBUNTUONE, path, folder_id);
} else if(type.equals("box")) {
    error=cloudUFS.deleteFolder(ICloudFS.CloudType.BOX, path, folder_id);
} else if(type.equals("dropbox")){
    error=cloudUFS.deleteFolder(ICloudFS.CloudType.DROPBOX, path, folder_id);
}
response.setContentType("application/json");
out.println(error.toJSON());
%>
