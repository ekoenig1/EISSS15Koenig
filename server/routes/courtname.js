module.exports = {

    sucheCourtName: function (matches, courts) {

        var responseObject = new Array(matches.length);
        var courtname = "";

        // Erzeuge responseObjekt mit leeren Daten
        for (var k = 0; k < responseObject.length; k++) {
            responseObject[k] = {"courtname": "", "date": "", "time": ""};
        }


        for (var i = 0; i < matches.length; i++) {
            for (var j = 0; j < courts.length; j++) {
                // Vergleiche die ID's
                if (matches[i].courts_id == courts[j]._id) {
                    //console.log("Matches Courts ID: "+matches[i].courts_id);
                    //console.log("Courts ID: "+courts[j]._id);
                    courtname = courts[j].name;
                    console.log(courtname);
                    break;
                }
            }
            // Befuelle den responseObject mit den Daten
            responseObject[i].courtname = courtname;
            responseObject[i].date = matches[i].date;
            responseObject[i].time = matches[i].time;
        }

        return responseObject;
    }

};