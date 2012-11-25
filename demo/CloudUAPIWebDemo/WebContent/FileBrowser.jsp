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
var path;
var name;
var parent;
var dir;
var sha1;
var currentPath='<%=path%>';

$(function () {
    var rname = $( "#rname" );
    var name = $( "#name" );
    $( "#dialog-copy" ).dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        buttons: {
            "Copy": function() {
                    var uri='';
                    var t='';
                    switch(type) {
                    case 0:
                        t='box';
                        break;
                    case 1:
                        t='dropbox';
                        break;
                    case 2:
                        t='ubuntuone';
                        break;
                    }
                    uri='createFolder.jsp?type='+t+'&parent_path='+currentPath+'&parent_id='+currentPath+'&name='+name.val();
                    $.ajax({
                        type: 'GET',
                        url: uri
                    }).done(function(data) {
                        alert(data.error_str);
                        window.location.reload();
                    });
                    $( this ).dialog( "close" );
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        },
        close: function() {
        }
    });
    $( "#dialog-move" ).dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        buttons: {
            "Move": function() {
                    var uri='';
                    var t='';
                    switch(type) {
                    case 0:
                        t='box';
                        break;
                    case 1:
                        t='dropbox';
                        break;
                    case 2:
                        t='ubuntuone';
                        break;
                    }
                    uri='createFolder.jsp?type='+t+'&parent_path='+currentPath+'&parent_id='+currentPath+'&name='+name.val();
                    $.ajax({
                        type: 'GET',
                        url: uri
                    }).done(function(data) {
                        alert(data.error_str);
                        window.location.reload();
                    });
                    $( this ).dialog( "close" );
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        },
        close: function() {
        }
    });
    $( "#dialog-upload" ).dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        buttons: {
            "Upload": function() {
                    var uri='';
                    var t='';
                    switch(type) {
                    case 0:
                        t='box';
                        break;
                    case 1:
                        t='dropbox';
                        break;
                    case 2:
                        t='ubuntuone';
                        break;
                    }
                    var form=document.getElementById("uploadForm");
                    form.type.value=t;
                    form.path.value=currentPath;
                    form.folder_id.value=currentPath;
                    form.submit();
                    /*
                    if(dir){
                        uri='uploadFile.jsp?type='+t+'&path='+path+'&newPath='+currentPath+'/'+rname.val();
                    } else {
                        uri='uploadFile.jsp?type='+t+'&path='+path+'&newPath='+currentPath+'/'+rname.val();
                    }
                    */
                    $( this ).dialog( "close" );
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        },
        close: function() {
        }
    });

    $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        buttons: {
            "Rename": function() {
                    var uri='';
                    var t='';
                    switch(type) {
                    case 0:
                        t='box';
                        break;
                    case 1:
                        t='dropbox';
                        break;
                    case 2:
                        t='ubuntuone';
                        break;
                    }
                    if(dir){
                        uri='renameFolder.jsp?type='+t+'&path='+path+'&newPath='+currentPath+'/'+rname.val();
                    } else {
                        uri='renameFile.jsp?type='+t+'&path='+path+'&newPath='+currentPath+'/'+rname.val();
                    }
                    $.ajax({
                        type: 'GET',
                        url: uri
                    }).done(function(data) {
                        alert(data.error_str);
                        window.location.reload();
                    });
                    $( this ).dialog( "close" );
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        },
        close: function() {
        }
    });

    $( "#dialog-create" ).dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        buttons: {
            "Create Folder": function() {
                    var uri='';
                    var t='';
                    switch(type) {
                    case 0:
                        t='box';
                        break;
                    case 1:
                        t='dropbox';
                        break;
                    case 2:
                        t='ubuntuone';
                        break;
                    }
                    uri='createFolder.jsp?type='+t+'&parent_path='+currentPath+'&parent_id='+currentPath+'&name='+name.val();
                    $.ajax({
                        type: 'GET',
                        url: uri
                    }).done(function(data) {
                        alert(data.error_str);
                        window.location.reload();
                    });
                    $( this ).dialog( "close" );
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        },
        close: function() {
        }
    });

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
function renamef() {
    $( "#dialog-form" ).dialog( "open" );
}
function deletef() {
	if(confirm('Do you want to delete '+name+'?')){
		var uri='';
		var t='';
		switch(type) {
		case 0:
			t='box';
			break;
		case 1:
			t='dropbox';
			break;
		case 2:
			t='ubuntuone';
			break;
		}
		if(dir){
			uri='deleteFolder.jsp?type='+t+'&path='+path+'&folder_id='+id+'&sha1='+sha1;
		} else {
			uri='deleteFile.jsp?type='+t+'&path='+path+'&file_id='+id+'&sha1='+sha1;
		}
        $.ajax({
            type: 'GET',
            url: uri
        }).done(function(data) {
            alert(data.error_str);
            window.location.reload();
        });
	}
}
function createfolder() {
    $( "#dialog-create" ).dialog( "open" );
}
function downloadfile() {
	// TODO
    var uri='';
    var t='';
    switch(type) {
    case 0:
        t='box';
        break;
    case 1:
        t='dropbox';
        break;
    case 2:
        t='ubuntuone';
        break;
    }
    uri='downloadFile.jsp?type='+t+'&path='+path+'&file_id='+id+'&name='+name;
    window.open(uri);
}
function copyf() {
    //$( "#dialog-copy" ).dialog( "open" );
    var d=0;
    if(dir) {
        d=1;
    } else {
        d=0;
    }
    window.open("getDir.jsp?type="+type+"&dir="+d+"&buttonText=Copy");
}
function movef() {
    //$( "#dialog-move" ).dialog( "open" );
    var d=0;
    if(dir) {
        d=1;
    } else {
        d=0;
    }
    window.open("getDir.jsp?type="+type+"&dir="+d+"&buttonText=Move");
}
function uploadfile() {
    $( "#dialog-upload" ).dialog( "open" );
}
</script>
</head>
<body>
<div id="dialog-move" title="Move">
</div>
<div id="dialog-copy" title="Copy">
</div>
<div id="dialog-upload" title="Upload">
    <form action="uploadFile.jsp" id="uploadForm" method="post" enctype="multipart/form-data">
	    <input type="hidden" name="type" />
	    <input type="hidden" name="path" />
	    <input type="hidden" name="folder_id" />
	    <fieldset>
	        <label for="rname">File</label>
	        <input type="file" name="file" id="file" class="text ui-widget-content ui-corner-all" />
	    </fieldset>
    </form>
</div>
<div id="dialog-form" title="Rename">
    <form>
    <fieldset>
        <label for="rname">Name</label>
        <input type="text" name="rname" id="rname" class="text ui-widget-content ui-corner-all" />
    </fieldset>
    </form>
</div>
<div id="dialog-create" title="CreateFolder">
    <form>
    <fieldset>
        <label for="name">Name</label>
        <input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
    </fieldset>
    </form>
</div>
<div id="toolbar">
<input type="button" value="Create Folder" id="createfolder" onclick="createfolder();" />
<input type="button" value="Delete" id="delete" onclick="deletef();" />
<input type="button" value="Rename" id="rename" onclick="renamef();" />
<input type="button" value="Download File" id="downloadfile" onclick="downloadfile();" />
<input type="button" value="Upload File" id="uploadfile" onclick="uploadfile();" />
<!-- TODO -->
<input type="button" value="Copy" id="copy" onclick="copyf();" />
<input type="button" value="Move" id="move" onclick="movef();" />
</div>
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
    //if(f.getDir()) {
%>
        <tr onclick="display_toolbar(<%=typei%>, <%=f.getDir()%>, '<%=f.getName()%>', '<%=f.getPath()%>', '<%=f.getId()%>', '<%=f.getRoot()%>', '<%=f.getSha1()%>');">
<%
//	} else {
%>
        <!-- <tr>  -->
<%
//	}
	if(f.getDir()) {
%>
        <td><a href="?type=<%=typei%>&path=<%=f.getPath()%>"><%=f.getName()%></a></td>
<%
} else {
%>
        <td><%=f.getName()%></td>
<%
}
%>
        <td><%=f.getSize()%></td>
        <td><%=f.getTime()%></td>
        <td><%=f.getType()%></td>
    </tr>
<%
}
%>
</table>
</body>
</html>
