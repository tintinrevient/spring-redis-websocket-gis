$( document ).ready(function() {

    $('.alert').hide();

    var stompClient = null;
    var rowCount = 0;

    function setConnected(connected) {
        $("#connect").prop("disabled", connected);
        $("#disconnect").prop("disabled", !connected);
        if (connected) {
            $("#gisMessage").show();
        }
        else {
            $("#gisMessage").hide();
        }
        $("#messages").html("");
    }

    function connect(callback) {
        $('.alert').hide();
        var socket = new SockJS('/redis-gis');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/info/messages', function (gitMessage) {
                console.log("Message: ", gitMessage);
                showGisMessage(JSON.parse(gitMessage.body));
            });
            callback();
        }, function(message){
            disconnect();
            $('.alert').show();
        });
    }

    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        setConnected(false);
        console.log("Disconnected");
        messageCount = 0;
        rowCount = 0;
    }

    function showGisMessage(gitMessage) {
        rowCount++;
        $("#messages").prepend("<tr" +"><td scope='row'> <h6><b>  " +  gitMessage.id  + ".</b></h6> </td> <td> "+ gitMessage.message + "</td></tr>");
    }

    $("#disconnect").click(function(){
        console.log("Disconnect");
        disconnect();
    });

    $("#close-alert").click(function(){
        $('.alert').hide();
    });

    $("#connect").click(function(){
        if(stompClient == null){
            connect(function(){
                console.log("Connected");
            });
        }else if(!stompClient.connected){
            connect(function(){
                console.log("Connected");
            });
        }
    });

    function showPosition(position) {
        document.getElementById("gis-message").value = position.coords.latitude + ", " + position.coords.longitude;
    }


    $("#send-ws-message").click(function(){
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(showPosition);
        } else {
            document.getElementById("gis-message").value = "0.00000, 0.00000";
        }
        sendMessage();
    });

    $("#send-http-message").click(function(){
        if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(showPosition);
        } else {
                document.getElementById("gis-message").value = "0.00000, 0.00000";
        }
        var gisMessage = $("#gis-message").val();
        $.ajax({
            url:"/message",
            type:"POST",
            data: JSON.stringify({"message": gisMessage}),
            dataType: "json",
            contentType:"application/json",
            success: function(response){
                console.log(response);
            },
            error: function(err) {
                console.log(err);
            }
        })
    });

    $('#gis-message').keypress(function (e) {
        var key = e.which;
        if(key == 13){
            sendMessage();
        }
    });

    function sendMessage(){
       if(stompClient !==null && stompClient.connected){
            var gisMessage = $("#gis-message").val();
            stompClient.send("/app/message",{}, gisMessage);
            $("#gis-message").val("");
        }else{
            connect(function(){
                var gisMessage = $("#gis-message").val();
                stompClient.send("/app/message",{}, gisMessage);
                $("#gis-message").val("");
            });
        }
    }

});

