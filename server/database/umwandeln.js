node_xj = require("xls-to-json");
node_xj({
    input: "database.xls",          // input xls
    output: "random_users.json",    // output json
    sheet: "users"                  // specific sheetname
}, function (err, result) {
    if (err) {
        console.error(err);
    } else {
        console.log(result);
    }
});