document.addEventListener("DOMContentLoaded", tableDisplay)

function tableDisplay() {
    var tbody = document.getElementsByTagName("tbody")[0]
    if(tbody.childElementCount == 0) {
        document.getElementById("allUrls").setAttribute("style", "display:none");
    }
    
    var shortenResult = document.getElementById("shortenResult")
    shortenResult.setAttribute("style", "display: none")
}

function showResult() {
    var result = document.getElementsByClassName("shorten-result")[0]
    result.setAttribute("style", "display:flex");
}

function closeResult() {
    var result = document.getElementsByClassName("shorten-result")[0]
    result.setAttribute("style", "display:none");

    var form = document.getElementsByTagName("form")[0]
    form.children[1].value=""
}

function getAllUrls() {
    
}