// Externe Modulle laden
var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var mongoose = require('mongoose');

//var user = require('./routes/user');
var db = mongoose.connection;

var Schema = mongoose.Schema;
var usermodel = new Schema({
	email: String,
	pass: String
});
// Mongoose Model definition
var Users = mongoose.model('users',usermodel);

// Datenbankverbindung
mongoose.connect('mongodb://localhost:27017/daten', function (error) {
	if (error) {
        console.log(error);
    }
	else {}
});


// URLS management
/*app.get('/', function (req, res) {
    res.send("<a href='/user'>Show Users</a>");
}); */

app.use(function(req,res,next){
    req.db = db;
    next();
});

// Datenbankverbindung 
db.on('error', function callback(){
	console.log("Verbindung NOK");
});
db.once('open', function callback() {
	console.log("Verbindung OK");
});

//INSERT, auf die Ressource
app.post('/create', function(req, res){
	req.on('data', function (chunk) {
		// JSON parse 
		var test = JSON.parse(chunk);
		var ObjectID = require('mongodb').ObjectID;
		var newUser = {
		  email: test.email,
		  pass: test.pass,
		  _id: new ObjectID()
		};

	db.collection('users').insert(newUser);	
     	
    });
});

// Loginbereich mit post Ressource
app.post('/login', function(request, response){	
	request.on('data', function (chunk) {	
		var test = JSON.parse(chunk);
		console.log(test.email + ", " + test.pass);
		// sucht den Eintrag in der db
		Users.find({email:test.email, pass:test.pass}, function (err, docs) {
			if(err){
				console.log("Fehler");
			}
			else{
				if(docs != ""){
					console.log(docs);
					response.send("OK");
				}
				else{
					response.send("FAIL");
				}				
			}				
		});			
    });
});


// Chat-Funktion
app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function(socket){
  socket.on('chat message', function(msg){
    io.emit('chat message', msg);
  });
});

// Server-Port
http.listen(8080, function(){
  console.log('listening on *:8080');
});