console.log("Event Handler");

let currentTheme = getTheme();
/*
This is the function that should be stimulated when we click the theme button
Also once clicked it should stay for whole section/page/s 
*/
document.addEventListener("DOMContentLoaded", () =>{
    changeTheme();
})

// changeTheme();

function changeTheme(){

    // document.querySelector("html").classList.add(currentTheme);
    changeThemePage(currentTheme, currentTheme);

    const changeButton = document.querySelector("#change_theme");
        // This is to change the text_of_theme_button
        // changeButton.querySelector("span").textContent=currentTheme=='light'?'Dark':'Light';

        changeButton.addEventListener("click", (event)=>{
        // console.log("screen theme changed");
        let oldTheme = currentTheme;

        // remove current theme
        document.querySelector("html").classList.remove(currentTheme);

        if(currentTheme=="dark"){
            currentTheme = "light";
        } else{
            currentTheme= "dark";
        }
        // new theme or the same theme be added

        changeThemePage(currentTheme, oldTheme);
        
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
    return theme ? theme : "dark";
}

/*  ############### Best Practice ############## 
    Creating f/method for those line which are used more than once
*/
function changeThemePage(theme){
    document.querySelector("html").classList.add(theme);

    document.querySelector("#change_theme").querySelector("span").textContent=currentTheme=='light'?'Dark':'Light';
}