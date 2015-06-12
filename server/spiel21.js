// Externe Modulle laden
var app = require('express')();
var http = require('http').Server(app);
var mongo = require('mongoskin');
var db = mongo.db("mongodb://localhost:27017/database", {native_parser:true});



// Loginbereich mit post Ressource
app.post('/login', function(req, res){ 
  req.on('data', function (chunk) { 
    var test = JSON.parse(chunk);
    console.log(test.username + ", " + test.pass);
    // sucht den Eintrag in der db
      db.collection('users').find({
        username:test.username, 
        pass:test.pass
      }).toArray(function(err, result){
          if(err){
            res.send("FAIL");
          } else {
              if(result != ""){
                console.log("OK");
                res.send(JSON.stringify(result));
              }
              else{
               console.log("FAIL");
               res.send("FAIL"); 
              }
          }
      });
    });
});


// Einfügen Benutzer, auf die Ressource
app.post('/create', function(req, res){
  req.on('data', function (chunk) {
    // JSON parse 
    var user = JSON.parse(chunk);
    

      db.collection('users').insert({
        gender:user.gender,
        username:user.username,
        birth:user.birth,
        pass:user.pass,
        phone:user.phone,
        email:user.email, 
        location:user.location
      }, function(err, result){
    
        if(result){
          console.log("create ok");
          console.log(result);
          res.send('OK');
        }
        else{
         console.log("create nicht ok");
         res.send("FAIL");
        }
      }); 
      
    });
});



// Benutzer abrufen
app.get('/users', function(req, res) {
    db.collection('users').find().toArray(function (err, items) {
      res.json(items);
      //console.log(items);
    });
});


// Object ID abrufen %users || %courts 
app.get('/test', function(req, res) {
    db.collection('matches').find({},{_id:1}).toArray(function (err, items) {
      res.json(items);
      //console.log(items);
    });
});



// Basketballplätze abrufen
app.get('/courts', function(req, res) {
    db.collection('courts').find().toArray(function (err, items) {
      res.json(items);
      //console.log(items);
    });
});

// Basketballplätze abrufen
app.get('/matches', function(req, res) {
    db.collection('matches').find().toArray(function (err, items) {
      res.json(items);
      //console.log(items);
    });
});

// Result abrufen
app.get('/result', function(req, res) {
    db.collection('result').find().toArray(function (err, items) {
      res.json(items);
      //console.log(items);
    });
});



// Matchmaking
// ins jahr ausrechnen
var geburtstag = 10;
var geburtsmonat = 9;
var geburtsjahr = 1984;

var d = new Date (geburtsjahr, geburtsmonat -1, geburtstag);
var sekunden = d.getTime();

var heute = new Date();
var sekunden_heute = heute.getTime();

var alter = sekunden_heute - sekunden;

var jahr_in_ms = 365.25 * 24 * 60 * 60 * 1000;

console.log(alter / jahr_in_ms);

// Zufallszahl
var zahl = zufallszahl(16, 45);
function zufallszahl (min, max) {
  var zahl = Math.random();
  zahl *=max;
  zahl += min;
  zahl = Math.floor(zahl);
  return zahl;
}
console.log("RandomZahl: "+ zahl);




// Server-Port
http.listen(3000, function(){
  console.log('listening on *:3000');
});