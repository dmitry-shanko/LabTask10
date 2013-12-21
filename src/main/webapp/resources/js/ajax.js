function getXmlHttp()
{
	var xmlhttp;
	try 
	{
		xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
	} 
	catch (e) 
	{
		try 
		{
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		} 
		catch (E) 
		{
			xmlhttp = false;
		}
	}
	if (!xmlhttp && typeof XMLHttpRequest!='undefined') 
	{
		xmlhttp = new XMLHttpRequest();
	}
	return xmlhttp;
}

function getList()
{
	var req = getXmlHttp();
	req.open('POST', '/LabTask10/list.do', true);  
	req.onreadystatechange = function() 
	{ 
		if (req.readyState == 4) 
		{
			if(req.status == 200) 
			{
				var resp = req.responseText;
				var div = document.getElementById('newsList');
				var arr = JSON.parse(resp);				
				if(arr.length < 1)
				{
					var error = document.createElement('div');
					error.setAttribute('class', 'noNewsClass');
					div.appendChild(error);
					var text = document.createTextNode(noNews);
					error.appendChild(text);
				}
				else
				{
					var form = document.createElement('form');
					form.setAttribute('action', '');
					form.setAttribute('method', 'POST');
					div.appendChild(form);
					form.setAttribute('onsubmit', 'return deleteGroupOfNews(); return false;');

					var table = document.createElement('table');
					table.setAttribute('class', 'newsListTable');
					var table = document.createElement('table');
					table.setAttribute('class', 'newsListTable');
					form.appendChild(table);
					for(var i = 0; i < arr.length; i++)
					{
						var tr = document.createElement('tr');
						table.appendChild(tr);
						var td = document.createElement('td');
						td.setAttribute('class', 'newsListTableTitle');
						tr.appendChild(td);
						td.appendChild(document.createTextNode(arr[i].title));		
						td = document.createElement('td');
						tr.appendChild(td);
						if (dateFormat.toLowerCase() == "dd/mm/yyyy")
						{					
							td.appendChild(document.createTextNode(arr[i].newsDate.dayOfMonth));
							td.appendChild(document.createTextNode('/'));
							if (arr[i].newsDate.month < 9)
							{
								td.appendChild(document.createTextNode('0'));
							}
							td.appendChild(document.createTextNode(arr[i].newsDate.month + 1));
							td.appendChild(document.createTextNode('/'));
							td.appendChild(document.createTextNode(arr[i].newsDate.year));
						}
						else
						{
							if (arr[i].newsDate.month < 9)
							{
								td.appendChild(document.createTextNode('0'));
							}
							td.appendChild(document.createTextNode(arr[i].newsDate.month + 1));
							td.appendChild(document.createTextNode('/'));
							td.appendChild(document.createTextNode(arr[i].newsDate.dayOfMonth));
							td.appendChild(document.createTextNode('/'));
							td.appendChild(document.createTextNode(arr[i].newsDate.year));
						}

						tr = document.createElement('tr');
						table.appendChild(tr);
						td = document.createElement('td');
						td.setAttribute('class', 'newsListTableBrief');
						tr.appendChild(td);
						td.appendChild(document.createTextNode(arr[i].brief));

						tr = document.createElement('tr');
						table.appendChild(tr);
						var input = document.createElement('input');
						input.setAttribute('type', 'hidden');
						input.setAttribute('name', 'newsId');
						input.setAttribute('value', arr[i].id);
						tr.appendChild(input);
						td = document.createElement('td');
						tr.appendChild(td);
						td.setAttribute('class', 'newsListTableLinks');
						td.setAttribute('colspan', '2');
						var a = document.createElement('a');
						td.appendChild(a);
						a.setAttribute('href', 'newsview.do?newsId=' + arr[i].id);
						a.appendChild(document.createTextNode(newsListView));
						td.appendChild(document.createTextNode('    '));
						a = document.createElement('a');
						td.appendChild(a);
						a.setAttribute('href', 'newsedit.do?newsId=' + arr[i].id);
						a.appendChild(document.createTextNode(newsListEdit));
						td.appendChild(document.createTextNode('    '));				
						var checkbox = document.createElement('input');
						td.appendChild(checkbox);
						checkbox.setAttribute('type', 'checkbox');
						checkbox.setAttribute('name', 'selectedItems');
						checkbox.setAttribute('value', arr[i].id);
					}
					table = document.createElement('table');
					table.setAttribute('class', 'newsListTable');
					form.appendChild(table);
					var tr = document.createElement('tr');
					table.appendChild(tr);
					var td = document.createElement('td');
					td.setAttribute('class', 'afterTableButtons');
					tr.appendChild(td);					
					var submit = document.createElement('input');
					submit.setAttribute('type', 'submit');
					submit.setAttribute('value', deleteSubmitValue);
					td.appendChild(submit);
				}
			}
		}
	};
	req.send(null);
}

function getView()
{
	var req = getXmlHttp();
	req.open('POST', '/LabTask10/view.do', true);  
	req.onreadystatechange = function() 
	{ 
		if (req.readyState == 4) 
		{
			if(req.status == 200) 
			{
				var resp = req.responseText;
				var div = document.getElementById('newsView');
				var arr = JSON.parse(resp);	
				if(arr.id < 1)
				{
					var error = document.createElement('div');
					error.setAttribute('class', 'noNewsClass');
					div.appendChild(error);
					var text = document.createTextNode(noNewsView);
					error.appendChild(text);
				}
				else
				{
					var date = null;
					if (dateFormat.toLowerCase() == "dd/mm/yyyy")
					{					
						date += arr.newsDate.dayOfMonth;
						date += '/';
						if (arr.newsDate.month < 9)
						{
							date += '0';
						}
						date += arr.newsDate.month + 1;
						date += '/';
						date += arr.newsDate.year;
					}
					else
					{
						if (arr.newsDate.month < 9)
						{
							date += '0';
						}
						date += arr.newsDate.month + 1;
						date += '/';
						date += arr.newsDate.dayOfMonth;
						date += '/';						
						date += arr.newsDate.year;
					}
					var myInnerHtml = '<table>' + 
					'<tr>' +
					'<td class="newsTableTitles"><span class="NewsViewTitle">' + newsTitle + '</span></td>' +
					'<td>' + arr.title + '</td>' +
					'</tr>' +
					'<tr>' +
					'<td><span class="NewsViewTitle">' + newsDate + '</span></td>' +
					'<td>' + date + '</td>' +
					'</tr>' +
					'<tr>' +
					'<td><span class="NewsViewTitle">' + newsBrief + '</span></td>' +
					'<td>' + arr.brief + '</td>' +
					'</tr>' +
					'<tr>' +
					'<td><span class="NewsViewTitle">' + newsContent + '</span></td>' +
					'<td>' + arr.content + '</td>' +
					'</tr>' +
					'</table>' +
					'<table class="newsListTable">' +
					'<tr>' +
					'<td class="afterTableButtons"><input type="button" onclick="editNews(); return false;" value="' + newsEdit +'"/></td>' +
					'<td><input type="button" onclick="deleteNews(); return false;" value="' + newsDelete +'"/></td>' +
					'</tr>' +
					'</table>';
					div.innerHTML = myInnerHtml;
				}
			}
		}
	};
	var newsId =  document.getElementById('newsId').getAttribute('value');
	req.setRequestHeader('Content-type','application/x-www-form-urlencoded');
	req.send('newsId=' + newsId);
}

function getEdit()
{
	var req = getXmlHttp();
	req.open('POST', '/LabTask10/edit.do', true);  
	req.onreadystatechange = function() 
	{ 
		if (req.readyState == 4) 
		{
			if(req.status == 200) 
			{
				var resp = req.responseText;
				var div = document.getElementById('newsEdit');
				var arr = JSON.parse(resp);	

				var date = null;
				if (dateFormat.toLowerCase() == "dd/mm/yyyy")
				{					
					date += arr.newsDate.dayOfMonth;
					date += '/';
					if (arr.newsDate.month < 9)
					{
						date += '0';
					}
					date += arr.newsDate.month + 1;
					date += '/';
					date += arr.newsDate.year;
				}
				else
				{
					if (arr.newsDate.month < 9)
					{
						date += '0';
					}
					date += arr.newsDate.month + 1;
					date += '/';
					date += arr.newsDate.dayOfMonth;
					date += '/';						
					date += arr.newsDate.year;
				}

				var myInnerHtml = '<table>' +
				'<tr>' +
				'<td class="newsTableTitles">' + newsTitle + '</td>' +
				'<td><textarea rows="1" cols="100" name="newsTitle" id="newsTitleInput">' + arr.title + '</textarea></td>' +
				'</tr>' +
				'<tr>' +
				'<td>' + newsDate + '</td>' +
				'<td><input type="text" size="10" name="dateString" id="dateStringInput" value="' + date + '"/></td>' +
				'</tr>' +
				'<tr>' +
				'<td>' + newsBrief + '</td>' +
				'<td><textarea rows="2" cols="100" name="newsBrief" id="newsBriefInput">' + arr.title + '</textarea></td>' +
				'</tr>' +
				'<tr>' +
				'<td>' + newsContent + '</td>' +
				'<td><textarea rows="20" cols="100" name="newsContent" id="newsContentInput">' + arr.content + '</textarea></td>' +
				'</tr>' +
				'</table>' +
				'<table class="newsListTable">' +
				'<tr>' +
				'<td class="afterTableButtons"><input type="button" onclick="saveNews(); return false;" value="' + newsSave + '"/></td>' + 
				'<td><input type="button" value="' + newsCancel + '" onclick="cancelNews(); return false;"/></td>' +
				'</tr>' +
				'</table>';
				div.innerHTML = myInnerHtml;
			}
		}
	};
	var newsId =  document.getElementById('newsId').getAttribute('value');
	req.setRequestHeader('Content-type','application/x-www-form-urlencoded');
	req.send('newsId=' + newsId);
}


function editNews()
{
	document.location.href = 'newsedit.do?newsId=' + document.getElementById('newsId').getAttribute('value');
}

function deleteNews()
{
	var req = getXmlHttp();
	req.open('POST', '/LabTask10/delete.do', true);  
	req.onreadystatechange = function() 
	{ 
		if (req.readyState == 4) 
		{
			if(req.status == 200) 
			{
				document.location.href = 'newslist.do';
			}
		}
	};
	req.setRequestHeader('Content-type','application/x-www-form-urlencoded');
	var newsId =  document.getElementById('newsId').getAttribute('value');
	req.send('newsId=' + newsId);
}

function cancelNews()
{
	document.location.href = 'cancel.do?newsId=' + document.getElementById('newsId').getAttribute('value');
}

function saveNews()
{
	var req = getXmlHttp();
	req.open('POST', '/LabTask10/save.do', true);  
	req.onreadystatechange = function() 
	{ 
		if (req.readyState == 4) 
		{
			if(req.status == 200) 
			{
				var resp = req.responseText;
				var arr = JSON.parse(resp);	
				document.location.href = 'newsview.do?newsId=' + arr.id;
			}
		}
	};
	req.setRequestHeader('Content-type','application/x-www-form-urlencoded');
	req.send('newsId=' + document.getElementById('newsId').getAttribute('value') + '&newsTitle=' + document.getElementById('newsTitleInput').value + '&dateString=' + document.getElementById('dateStringInput').value + '&newsBrief=' + document.getElementById('newsBriefInput').value + '&newsContent=' + document.getElementById('newsContentInput').value);	
}

function deleteGroupOfNews() {
	var chks = document.getElementsByName('selectedItems');
	var ids = document.getElementsByName('newsId');
	var checked = false;
	for ( var i = 0; i < chks.length; i++) {
		if (chks[i].checked) {
			if (confirmDialog())
			{
				checked = true;
				break;
			}
		}
	}
	if (checked)
	{
		var str = 'newsId=';
		for (var i = 0; i < chks.length; i++)
		{
			if (chks[i].checked)
			{
				str += ids[i].getAttribute('value');
				str += '&newsId=';
			}
		}
		var req = getXmlHttp();
		req.open('POST', '/LabTask10/delete.do', true);  
		req.onreadystatechange = function() 
		{ 
			if (req.readyState == 4) 
			{
				if(req.status == 200) 
				{
					document.location.href = 'newslist.do';
				}
			}
		};
		req.setRequestHeader('Content-type','application/x-www-form-urlencoded');
		req.send(str);
		return false;
	}
	alert(notChecked);
	return false;
}