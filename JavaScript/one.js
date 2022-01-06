let counter=0;
let a= (value) => {
    console.log(value);
}

let interval = setInterval(() => {
    a(counter++);
    if(counter === 100){
        clearInterval(interval);
    }
}, 20);