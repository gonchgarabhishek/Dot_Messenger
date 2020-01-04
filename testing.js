

var j1=document.getElementsByTagName("head")[0];

var s1=document.createElement('script')
s1.setAttribute('src','js/common.js')

var  s2=document.createElement('script');
s2.setAttribute('src','js/conversation.js');
var s3=document.createElement('script');
s3.setAttribute('src','js/global.js');

var s4=document.createElement('script');
s4.setAttribute('src','https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js');
var s5=document.createElement('script')
s5.setAttribute('src','https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js');
var s6=document.createElement('script');
s6.setAttribute('src','js/api.js');

j1.appendChild(s1);
j1.appendChild(s6);
j1.appendChild(s2);
j1.appendChild(s3);
j1.appendChild(s4);
j1.appendChild(s5);


var l1=document.createElement('link')
l1.setAttribute("rel", "stylesheet")
l1.setAttribute("type", "text/css")
l1.setAttribute('href','css/app.css');

var l2=document.createElement('link')
l2.setAttribute("rel", "shortcut icon")
l2.setAttribute("type", "image/png")
l2.setAttribute('href','favicon.png');

var l3=document.createElement('link')
l3.setAttribute("rel", "stylesheet")
l3.setAttribute('href','https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css');

var l4=document.createElement('link')
l4.setAttribute("rel", "stylesheet")
l4.setAttribute('href','https://fonts.googleapis.com/css?family=Roboto&display=swap');

var l5=document.createElement('link')
l5.setAttribute("rel", "stylesheet")
l5.setAttribute('href','https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css');


document.getElementsByTagName("head")[0].appendChild(l1);
document.getElementsByTagName("head")[0].appendChild(l2);
document.getElementsByTagName("head")[0].appendChild(l3);
document.getElementsByTagName("head")[0].appendChild(l4);
document.getElementsByTagName("head")[0].appendChild(l5);

// document.body.innerHTML='<object type="text/html" data="index.html" ></object>';
var div = document.createElement("div");
div.innerHTML = '<button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">Open Modal</button>';
document.body.appendChild(div);

var div2 = document.createElement("div");
div2.innerHTML = '<div class="modal-dialog" ><div class="topnav" ><p class="vertical-center"> Any Help? -- Ask Vajra  </p><input type="image"  data-dismiss="modal" src="img/delete.png" alt="Submit" width="20" height="20" style="margin-top: 10;float: right;margin-right: 10;"><input  type="image" onClick="window.location.reload();" src="img/reload64.png" alt="Submit" width="20" height="20" style="margin-top: 10;float: right;margin-right: 20;"></div><div class="chat-column"><div id="scrollingChat"></div><p class="user-typing" id="user-typing-field"></p><label for="textInput" class="inputOutline"><div class="message-box"> <input autocomplete="off" id="textInput" class="input responsive-column" placeholder="Type something" type="text" onkeydown="ConversationPanel.inputKeyDown(event, this)" autofocus><input type="image" class="message-submit" src="img/send.png" onclick="ConversationPanel.sendB()" alt="Submit"></div></label></div></div></div>';
div2.setAttribute("class", "modal fade");
div2.setAttribute("id","myModal");
div2.setAttribute("role", "dialog");

document.body.appendChild(div2);




