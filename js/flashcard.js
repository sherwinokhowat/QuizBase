function showAnswer(quizItemID) {
    var hiddenContent = document.getElementById("solution" + quizItemID);
    hiddenContent.setAttribute("display", "block");
}