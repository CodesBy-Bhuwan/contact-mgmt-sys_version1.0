console.log("Event Handler");

let currentTheme = getTheme();
changeTheme();

function changeTheme(){

    document.querySelector("html").classList.add(currentTheme);

    const changeButton = document.querySelector("#change_theme");
    changeButton.addEventListener("click", (event)=>{
        console.log("screen theme changed");

        document.querySelector("html").classList.remove(currentTheme);

        if(currentTheme=="Dark"){
            currentTheme = "Light";
        } else{
            currentTheme= "Dark";
        }
        document.querySelector("html").classList.add(currentTheme);

        // This is to change the text_of_theme_button
        changeButton.querySelector("span").textContent=currentTheme=='Light'?'Dark':'Light';

    })
}


//setting theme to localStorage
function setTheme(theme){
    localStorage.setItem("theme", theme);
    }

//getting theme from localStorage
function getTheme(theme){
    localStorage.getItem("theme");

//**************Using ternary operation
    return theme ? theme : "Light";


}