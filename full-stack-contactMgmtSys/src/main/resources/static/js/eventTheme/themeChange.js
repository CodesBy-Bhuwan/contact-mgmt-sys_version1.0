console.log("Event Handler");

let curretTheme = getTheme;

function changeTheme(){}

//setting theme to localStorage
function setTheme(theme){
    localStorage.setItem("theme", theme);
    }

//getting theme from localStorage
function getTheme(){
    let theme = localStorage.getItem("theme");

//    if(theme) return theme
//        else return "light";

//**************Using ternary operation
    return theme ? theme : "light";


}