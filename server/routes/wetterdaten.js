// Wetterdaten abfrage
var express = require('express');
var router = express.Router();

/* GET weather data listing. */
router.get('/wetterdaten', function(req, res) {
  res.send('Respond with weather data');
  console.log("Test Wetterdaten");
});

module.exports = router;



exports.listWetterdaten = function(request, mongoURL){
	return function(req, res) {
	
		// Wettervorhersage: cnt=abgerufene Tage, 
		// lat&lon = Koordinaten, mode = json, units = metrisches System
		request('http://api.openweathermap.org/data/2.5/forecast?lat=55&lon=55&mode=json&units=metric', function (error, res, data) {
			if(!error && response.statusCode == 200) {
				res.json(data);
				var test = JSON.parse(data);
				console.log(test.coord);
				var eingabe = test.coord;
				mongoURL.collection('wetterdaten').insert(test, function(err, result){console.log(result)});
						
				console.log("Wetterdaten wurden gespeichert");

				//db.collection('wetterdaten').update(test ,function(err, result) {
				//console.log("update");
				//});
				

			} else {
				console.error.log("Ein Fehler ist aufgetreten. Die Schnittstelle zur Stadt konnte nicht abgefragt werden.");
			}
		});
	}
};