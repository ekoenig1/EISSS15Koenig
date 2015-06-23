module.exports = {

    sucheUserName: function (result, users) {

        var responseObject = new Array(result.length);
        var usrname = "";

        // Erzeuge responseObjekt mit leeren Daten
        for (var k = 0; k < responseObject.length; k++) {
            responseObject[k] = {"usrname": ""};
        }


        for (var i = 0; i < result.length; i++) {
            for (var j = 0; j < users.length; j++) {
                // Vergleiche die ID's
                if (result[i]._id == users[j]._id) {
                    //console.log("result ID: "+result[i].player_id);
                    //console.log("users ID: "+users[j]._id);
                    usrname = users[j].username;
                    //console.log(usrname);
                    break;
                }
            }
            // Befuelle den responseObject mit den Daten
            responseObject[i].usrname = usrname;
        }

        return responseObject;
    }

};