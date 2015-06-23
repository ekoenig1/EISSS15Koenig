module.exports = {

    // Meine Ergebnisse
    einzelneErgebnisse: function (result, users) {

        var responseObject = new Array(1);

        responseObject[0] = {
            "userid": "",
            "username": "",
            "win": 0,
            "lose": 0
        };

        for (var i = 0; i < result.length; i++) {
            for (var j = 0; j < users.length; j++) {

                // Vergleiche die ID's
                if (result[i].player_id == users[j]._id) {

                    var win1 = parseInt(result[i].win);
                    var lose1 = parseInt(result[i].lose);

                    responseObject[0].userid = users[j]._id;
                    responseObject[0].username = users[j].username;
                    responseObject[0].win = responseObject[0].win + win1;
                    responseObject[0].lose = responseObject[0].lose + lose1;
                }
            }
        }
        // Befuelle den responseObject mit den Daten
        console.log("userid: " + responseObject[0].userid);
        console.log("name: " + responseObject[0].username);
        console.log("win: " + responseObject[0].win);
        console.log("lose: " + responseObject[0].lose);

        return responseObject;
    },


    // Top Ergebnisse
    topErgebnisse: function (result, users) {

        var responseObject = new Array(users.length);

        // Erzeuge responseObjekt mit leeren Daten
        for (var k = 0; k < users.length; k++) {
            responseObject[k] = {
                "userid": users[k]._id,
                "username": users[k].username,
                "win": 0,
                "lose": 0
            };
        }

        for (var i = 0; i < users.length; i++) {
            for (var j = 0; j < result.length; j++) {

                // Vergleiche die ID's
                if (result[j].player_id == users[i]._id) {

                    var win1 = parseInt(result[j].win);
                   var lose1 = parseInt(result[j].lose);

                    responseObject[i].win = responseObject[i].win + win1;
                    responseObject[i].lose = responseObject[i].lose + lose1;
                    //break;
                }
            }
            console.log("userid: " + responseObject[i].userid);
            console.log("name: " + responseObject[i].username);
            console.log("win: " + responseObject[i].win);
            console.log("lose: " + responseObject[i].lose);
        }
        // Befuelle den responseObject mit den Daten
        //responseObject[i].usrname = usrname;
        console.log(responseObject);


        /*
        var response = responseObject[0].win;

        for (var m = 1; m < responseObject.length; m++) {
            if(response < responseObject[m].win){
                response[++counter] = response[counter];
                response[counter] = responseObject[m];
                counter++;
            } else {
                response[++]
            }
        }
        */


        return responseObject;
    }


};