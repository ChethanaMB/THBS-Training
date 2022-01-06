const fs = require('fs');


/*
fs.readFile('chet.txt', (err,data) => {
    if(err===null){
        console.log(data.toString());
    } else{
        console.log(err.message);
    }
});
*/
 let d= fs.readFileSync('chet.txt');
 fs.writeFile('chet.txt', d.toString() + ' \nHow are you?', err =>{
     if(err===null){
         console.log("Successfully written data");
     } else{
         console.log(err.message);
     }
 });