node_xj = require("xls-to-json");
  node_xj({
    input: "random_users.xls",  // input xls 
    output: "random_users.json", // output json 
    sheet: "Tabelle1",  // specific sheetname 
  }, function(err, result) {
    if(err) {
      console.error(err);
    } else {
      console.log(result);
    }
  });