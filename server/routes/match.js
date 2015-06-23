// Matchmaking
// ins jahr ausrechnen
var geburtstag = 10;
var geburtsmonat = 9;
var geburtsjahr = 1984;

var d = new Date(geburtsjahr, geburtsmonat - 1, geburtstag);
var sekunden = d.getTime();

var heute = new Date();
var sekunden_heute = heute.getTime();

var alter = sekunden_heute - sekunden;

var jahr_in_ms = 365.25 * 24 * 60 * 60 * 1000;

console.log(alter / jahr_in_ms);

// Zufallszahl
var zahl = zufallszahl(16, 45);
function zufallszahl(min, max) {
    var zahl = Math.random();
    zahl *= max;
    zahl += min;
    zahl = Math.floor(zahl);
    return zahl;
}
console.log("RandomZahl: " + zahl);