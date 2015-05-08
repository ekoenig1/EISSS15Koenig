var mongoDB = require('mongoskin');
ObjectID = require('mongoskin').ObjectID;

//Verbindung zur Datenbank herstellen
//safe = true damit "err" und "result" in Callbacks gefuellt werden
var db = mongoDB.db('mongodb://localhost/node-android?auto_reconnect=true', {
    safe: true
});

//Collection "user" binden
db.bind("user");


//Attribut "id" indizieren
db.user.ensureIndex({
    id: 1
}, function (err, replies) {
});


/*
var store = {

    //Alle Items der Collection abrufen
    findAllMusic: function (callback) {
        db.music.findItems(callback);
    },

    //Die ersten 10 Items der Collection abrufen nachdem nach "votes" absteigend sortiert wurde
    findOrderedMusic: function (attribute, order, limit, callback) {
        var orderattr = {};
        orderattr[attribute] = order;
        db.music.find().sort(orderattr).limit(limit).toArray(callback)
    },

    //Sucht den Eintrag aus der Songliste , der das übergebene Suchwort enthält
    findSimilarSongs: function (query, callback) {
        db.music.find({$or: [
            {'title': new RegExp(".*" + query + ".*", "i")},
            {'interpret': new RegExp(".*" + query + ".*", "i")}
        ]}).toArray(callback);
    },

    //Den Rang eines Musikstuecks verändern
    changeMusicRank: function (amount, idToChange, callback) {
        db.music.update({_id: new ObjectID(idToChange)}, { $inc: {votes: amount}}, callback);
    },

    //Sucht den Eintrag aus der Songliste , der den übergebenen Titel enthält
    findTitle: function (id, callback) {
        db.music.find({'_id': new ObjectID(id)}).toArray(callback);
    },

    //Neue Nutzer-Song Beziehung anlegen
    registerNewUserVote: function (songID, userID, callback) {
        db.uservotes.insert({'user': userID, 'song': songID}, callback);
    },

    //Sucht alle Einträge aus der uservotes-Liste, die zu dem angegebenen Nutzer gehören
    checkIfUserVotedForThis: function (userID, songID, callback) {
        db.uservotes.findOne({$and: [
            {'user': userID},
            {'song': songID}
        ]}, callback);
    },

    //Sucht den Eintrag aus der Codeliste , der den übergebenen Code enthält
    findCode: function (authCode, callback) {
        db.qrcodes.find({'code': authCode}).toArray(callback);
    }
};
*/

//Modul exportieren
//module.exports = store;