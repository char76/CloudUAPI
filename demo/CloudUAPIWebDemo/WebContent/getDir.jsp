<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, kr.co.talesapp.clouduapi.CUFile, kr.co.talesapp.clouduapi.OAuthTokens, kr.co.talesapp.clouduapi.CloudUFS, kr.co.talesapp.clouduapi.CloudFS, kr.co.talesapp.clouduapi.interfaces.*, java.net.URLEncoder;" %>
<%
CloudUFS cloudUFS=new CloudUFS();
cloudUFS.init(ICloudFS.CloudType.DROPBOX, null, "token", "secret", "api key", "api secret");
cloudUFS.init(ICloudFS.CloudType.BOX, "api key", "token", null, null, null);
cloudUFS.init(ICloudFS.CloudType.UBUNTUONE, null, "token",
        "secret",
        "consumer key",
        "consumer secret");
cloudUFS.setDebug(true);
String type_str=request.getParameter("type");
ICloudFS.CloudType type;
if(type_str!=null) {
    type=ICloudFS.CloudType.values()[Integer.parseInt(type_str)];
} else {
    type=null;
}
String path=request.getParameter("path");
if(path==null) {
    path="/";
}
String buttonText=request.getParameter("buttonText");
String dir=request.getParameter("dir");
List<CUFile> list=cloudUFS.getFiles(type, path);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CloudUAPI Web Demo</title>
<link rel="stylesheet" type="text/css" href="demo.css" />
<link rel="stylesheet" href="http://code.jquery.com/ui/1.9.0/themes/base/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.8.2.js"></script>
<script src="http://code.jquery.com/ui/1.9.0/jquery-ui.js"></script>
<script language="javascript">
var type=<%=type_str%>;
var id;
var path='<%=path%>';
var name;
var parent;
var dir;
var sha1;
var currentPath='<%=path%>';

$(function () {
    var trInstance = $('#browser').find('tr');
    trInstance.click(function () {
        trInstance.removeClass('noclick');
        trInstance.find('td').removeClass('click');
        var instance = $(this);
        instance.addClass('noclick');
        instance.find('td').addClass('click');
    });
});

function display_toolbar(t, d, n, p, i, pr, s) {
    type=t;
    dir=d;
    name=n;
    path=p;
    id=i;
    parent=pr;
    sha1=s;
}
function copymove(){
    var uri='';
    var t='';
    var p_path='';
    switch(window.opener.type) {
    case 0:
        t='box';
        p_path=path;
        break;
    case 1:
        t='dropbox';
        p_path=path+'/'+window.opener.name;
        break;
    case 2:
        t='ubuntuone';
        p_path=path+'/'+window.opener.name;
        break;
    }
    alert(p_path);
<%
	if(buttonText.equals("Copy")){
		if(dir.equals("0")) {
%>
    uri='copyFile.jsp?type='+t+'&dir='+dir+'&path='+window.opener.path+'&newPath='+p_path;
<%
		} else {
%>
    uri='copyFolder.jsp?type='+t+'&dir='+dir+'&path='+window.opener.path+'&newPath='+p_path;
<%
		}
%>
	$.ajax({
	    type: 'GET',
	    url: uri
	}).done(function(data) {
	    alert(data.error_str);
	    window.opener.location.reload();
	    //window.close();
	});
<%
	} else if(buttonText.equals("Move")){
		if(dir.equals("0")) {
%>
    uri='moveFile.jsp?type='+t+'&dir='+dir+'&path='+window.opener.path+'&newPath='+p_path;
<%
		} else {
%>
    uri='moveFolder.jsp?type='+t+'&dir='+dir+'&path='+window.opener.path+'&newPath='+p_path;
<%
		}
%>
    $.ajax({
        type: 'GET',
        url: uri
    }).done(function(data) {
        alert(data.error_str);
        window.opener.location.reload();
        window.close();
    });
<%
	}
%>
}
</script>
</head>
<body>
<table id="browser">
    <tr>
        <th>Name</th>
        <th>Size</th>
        <th>Modified time</th>
        <th>Where</th>
    </tr>
<%
for(int i=0;list!=null && i<list.size();i++) {
    CUFile f=(CUFile)list.get(i);
    int typei=-1;
    switch(f.getType()) {
    case DROPBOX:
        typei=1;
        break;
    case UBUNTUONE:
        typei=2;
        break;
    case BOX:
        typei=0;
        break;
    }
    if(f.getDir()) {
%>
        <tr onclick="display_toolbar(<%=typei%>, <%=f.getDir()%>, '<%=f.getName()%>', '<%=f.getPath()%>', '<%=f.getId()%>', '<%=f.getRoot()%>', '<%=f.getSha1()%>');">
        <td><a href="?buttonText=<%=buttonText%>&dir=<%=dir%>&type=<%=typei%>&path=<%=f.getPath()%>"><%=f.getName()%></a></td>
        <td><%=f.getSize()%></td>
        <td><%=f.getTime()%></td>
        <td><%=f.getType()%></td>
    </tr>
<%
    }
}
%>
</table>
<div id="toolbar">
<input type="button" value="<%=buttonText%>" id="createfolder" onclick="copymove();" />
<input type="button" value="Cancel" id="Cancel" onclick="window.close();" />
</div>
</body>
</html>
