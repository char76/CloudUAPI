<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kr.co.talesapp.clouduapi.OAuthTokens, kr.co.talesapp.clouduapi.CloudUFS, kr.co.talesapp.clouduapi.CloudFS, kr.co.talesapp.clouduapi.interfaces.*, java.net.URLEncoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CloudUAPI Web Demo</title>
</head>
<body>
<%
String type=request.getParameter("type");
String oauth_token=request.getParameter("oauth_token");
String uid=request.getParameter("uid");
String ticket=request.getParameter("ticket");
String auth_token=request.getParameter("auth_token");
OAuthTokens oAuthTokens=new OAuthTokens();
CloudUFS cloudUFS=new CloudUFS();
if(((auth_token==null && ticket==null) || oauth_token==null) && type!=null) {
	if(type.equals("dropbox")) {
	    oAuthTokens=cloudUFS.getRequestToken(ICloudFS.CloudType.DROPBOX, "api_key", "api_secret");
        session.setAttribute("oAuthToken", oAuthTokens);
	    response.sendRedirect(cloudUFS.prop.getProperty("DropboxFS.authorize")+"?oauth_token="+oAuthTokens.getOAuthToken()+"&oauth_consumer_key=5p1btcqv2j3axml&oauth_callback="+URLEncoder.encode("http://localhost:8080/CloudUAPIWebDemo/getAccessToken.jsp", "UTF-8"));
	} else if(type.equals("box")) {
        oAuthTokens=cloudUFS.getRequestToken(ICloudFS.CloudType.BOX, "api_key", null);
        System.out.println(oAuthTokens.getTicket());
        response.sendRedirect(cloudUFS.prop.getProperty("BoxFS.authorize")+oAuthTokens.getTicket());
	} else if(type.equals("ubuntuone")) {
		cloudUFS.setDebug(true);
        oAuthTokens=cloudUFS.getRequestToken(ICloudFS.CloudType.UBUNTUONE, "id", "pw");
        System.out.println(oAuthTokens.getOAuthToken()+", "+oAuthTokens.getOAuthTokenSecret()+", "+oAuthTokens.getConsumerKey()+", "+oAuthTokens.getConsumerSecret());
        oAuthTokens=cloudUFS.getAccessToken(ICloudFS.CloudType.UBUNTUONE, oAuthTokens);
	}
} else {
	if(oauth_token!=null && uid!=null) {
		// for dropbox
        oAuthTokens=(OAuthTokens)session.getAttribute("oAuthToken");
        oAuthTokens.setConsumerKey("api_key");
        oAuthTokens.setConsumerSecret("api_secret");
        cloudUFS.getAccessToken(ICloudFS.CloudType.DROPBOX, oAuthTokens);
	}
	out.println(oAuthTokens.getAccessToken()+", "+oAuthTokens.getConsumerKey()+", "+oAuthTokens.getConsumerSecret()+", "+oAuthTokens.getOAuthToken()+", "+oAuthTokens.getOAuthTokenSecret());
	out.println(uid+", "+oauth_token+"<br />");
    out.println(auth_token+", "+ticket+"<br />");
	// for box
    auth_token=cloudUFS.getAuthToken("box api key", ticket);
    out.println(auth_token);
}
%>
</body>
</html>
