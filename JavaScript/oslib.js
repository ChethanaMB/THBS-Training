const os = require('os');

console.log("Total Memory :", os.totalmem());
console.log("Remaining free memory :", os.freemem() / 1000000000);
console.log("Os Type :", os.type());
console.log("Home directory :", os.homedir());
console.log("Platform :", os.platform());
console.log("Network Interfaces :", os.networkInterfaces());
console.log("Uptime :",os.uptime());