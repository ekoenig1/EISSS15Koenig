// Externe Modulle laden
var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);

// Datenbank
var mongo = require('mongoskin');
//var ObjectID = require('mongoskin').ObjectID;
var db = mongo.db("mongodb://localhost:27017/database", {native_parser: true});

/** Debbuging: JSLint und JShint **/


// Vordefinierte courtsVariable mit Basketballplätzen
var courts;
db.collection('courts').find().toArray(function (err, result) {
    courts = result;
});

// Vordefinierte usersVariable mit Benutzernamen
var users;
db.collection('users').find().toArray(function (err, result) {
    users = result;
});



// Loginbereich mit post Ressource
app.post('/users/login', function (req, res) {
    req.on('data', function (chunk) {
        var login = JSON.parse(chunk);
        console.log(login.username + ", " + login.pass);
        // sucht den Eintrag in der db
        db.collection('users').find({
            username: login.username,
            pass: login.pass
        }).toArray(function (err, result) {
            if (err) {
                res.send("FAIL");
            } else {
                if (result != "") {
                    console.log("OK");
                    res.send(JSON.stringify(result));
                }
                else {
                    console.log("FAIL");
                    res.send("FAIL");
                }
            }
        });
    });
});


// Einfügen Benutzer, auf die Ressource
app.post('/users/create', function (req, res) {
    req.on('data', function (chunk) {
        // JSON parse
        var user = JSON.parse(chunk);

        db.collection('users').insert({
            gender: user.gender,
            username: user.username,
            birth: user.birth,
            pass: user.pass,
            phone: user.phone,
            email: user.email,
            location: user.location
        }, function (err, result) {

            if (result) {
                console.log("create ok");
                console.log(result);
                res.send('OK');
            }
            else {
                console.log("create nicht ok");
                res.send("FAIL");
            }
        });

    });
});


// Matchbereich mit post Ressource
app.post('/matches', function (req, res) {
    req.on('data', function (chunk) {
        // JSON parse
        var matches = JSON.parse(chunk);
        /*
         var st = matches.date;
         var pattern = /(\d{2})\.(\d{2})\.(\d{4})/;
         var dt = new Date(st.replace(pattern, '$3-$2-$1'));
         */
        db.collection('matches').insert({
            player1_id: matches.player1_id,
            player2_id: matches.player2_id,
            player3_id: matches.player3_id,
            player4_id: matches.player4_id,
            courts_id: matches.courts_id,
            date: matches.date,
            time: matches.time
        }, function (err, result) {

            if (result) {
                console.log("create ok");
                console.log(result);
                res.send('OK');
            }
            else {
                console.log("create nicht ok");
                res.send("FAIL");
            }
        });

    });
});



// Nachrichtenaustausch (Chat)
app.get('/users/chat', function (req, res) {
    res.sendFile(__dirname + '/public/chat.html');
});
io.on('connection', function (socket) {
    socket.on('chat message', function (msg) {
        io.emit('chat message', msg);
    });});
io.on('connection', function (socket) {
    console.log('User connected');
    socket.on('disconnect', function () {
        console.log('User disconnected');
    });
});

// Benutzer abrufen
app.get('/users', function (req, res) {
    db.collection('users').find().toArray(function (err, items) {
        res.json(items);
        //console.log(items);
    });
});

// Benutzer per ID abrufen
app.get('/users/:id', function (req, res) {
    db.collection('users').findById(req.params.id, function (err, user) {
        res.json(user);
        //console.log(items);
    });
});



// Basketballplätze abrufen
app.get('/courts', function (req, res) {
    db.collection('courts').find().toArray(function (err, items) {
        res.json(items);
        //console.log(items);
    });
});

/*
 // Basketballplätze abrufen
 app.get('/matches', function (req, res) {
 db.collection('matches').find().toArray(function (err, items) {
 res.json(items);
 // hier die Tabelen filtern und die Users
 //console.log(items);
 });
 });
 */

/*
 app.get('/matches/:id', function (req, res) {
 db.collection('matches').findById(req.params.id, function (err, match) {
 res.json(match);
 // hier die Tabelen filtern und die Users
 //console.log(items);
 });
 });
 */

// Basketballplätze abrufen
/*app.get('/result', function (req, res) {
 db.collection('result').find().limit(10).toArray(function (err, items) {
 res.json(items);
 // hier die Tabelen filtern und die Users
 //console.log(items);
 });
 });*/
app.get('/result', function (req, res) {
    db.collection('matches').find().sort({date: -1}).limit(10).toArray(function (err, items) {
        res.json(items);
        // hier die Tabelen filtern und die Users
        //console.log(items);
    });
});


// Matches abrufen
var courtsName = require('./routes/courtname');
app.get('/matches', function (req, res) {
    db.collection('matches').find().sort({date: -1}).limit(50).toArray(function (err, matches) {
        var result = courtsName.sucheCourtName(matches, courts);
        //console.log(result);
        res.json(result);
    });
});


// Users abrufen
var usersName = require('./routes/username');
app.get('/result1', function (req, res) {
    db.collection('result').find().toArray(function (err, items) {
        var result = usersName.sucheUserName(items, users);
        console.log(result);
        res.json(result);
    });
});


// Ergebnisse abrufen
var resultName = require('./routes/resultname');
app.get('/playerresult/:id', function (req, res) {
    db.collection('result').find({"player_id": req.params.id}).toArray(function (err, items) {
        var result = resultName.einzelneErgebnisse(items, users);
        console.log(result);
        res.json(result);
    });
});

var resultName = require('./routes/resultname');
app.get('/playerresult', function (req, res) {
    db.collection('result').find().toArray(function (err, items) {
        var result = resultName.topErgebnisse(items, users);
        console.log(result);
        res.json(result);
    });
});


/*
 // Aggregates a document
 app.get('/test', function (req, res) {
 db.collection('result').group({
 //'$group': {
 //_id: {$player_id: { '$sum': '$win'}}} }, function (err, items) {
 //_id: '$player_id', win: {'$sum': 1}
 //"_id": { "player_id": "$player_id", "win": "$win"}, "count": {"$sum": 1}


 }, function (err, items) {
 var ergebnis = usersName.sucheUserName(items, users);
 for (var i = 0; i < ergebnis.length; i++) {
 console.log("--> " + items[i]._id);
 console.log("--> " + items[i].count);
 console.log("--> " + ergebnis[i].usrname);

 }
 //console.log(ergebnis[i]);
 });

 });
 */







// Server-Port
http.listen(3000, function () {
    console.log('listening on *:3000');
});