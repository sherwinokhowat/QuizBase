// file basically adds a new input tag every time addFlashcard and addMultipleChoice are in there
// input tags have name and id question1, question2, question3, question4, question5... and there is a function to figure out the number of children in the <form> tag
// @author Avery Chan
// also keeps track of the highest "newNumber" (since newNumber doesn't adjust when a question gets removed) and the running number of questions. by storing it as a hidden input. 
// someone can change the hidden input but nothing we can do about it for now. 

var newNumber = 1; // controls the id of the next question that gets added (if newNumber = 4, the next input tag will be named "question4"). 

var numOfQuestions = 0;

function addFlashcard() {
    // var newNumber = document.getElementById("questionForm").childElementCount - 4; // if there are 2 elements (the 2 default), we place it as #1. if there are 3 elements, #2...
    var msg = "<div id='div" + newNumber + "'>";
    msg += "<p>Question #" + newNumber + " (Flashcard)</p>";
    msg += "<label for='question" + newNumber + "'>Front:</label><br>";
    msg += "<input type='text' placeholder='Question' id='question" + newNumber + "' name='question" + newNumber + "' required><br>";
    msg += "<label for='answer" + newNumber + "'>Back:</label><br>";
    msg += "<input type='text' placeholder='Answer' id='answer" + newNumber + "' name='answer" + newNumber + "' required><br>";
    msg += "<button type='button' onclick='deleteQuestion(" + newNumber + ")'>Delete Question</button><br>";
    msg += "</div>";
    // if we have 6 questions and we delete number 3, then the POST will send the maximum number of questions (6) and leave the program to get rid of the missing ones.
    document.getElementById("questionForm").insertAdjacentHTML("beforeend", msg);
    newNumber++;
    numOfQuestions++;
    var hiddenInput1 = document.getElementById("highestNumber");
    var hiddenInput2 = document.getElementById("numOfQuestions");
    hiddenInput1.value = newNumber-1;
    hiddenInput2.value = numOfQuestions;
    /*
    hiddenInput1.parentElement.removeChild(hiddenInput1);
    hiddenInput2.parentElement.removeChild(hiddenInput2);
    var newHiddenInput = "<input type='hidden' name='highestNumber' id='highestNumber' value='" + newNumber-1 + "'>"; // "<input type='hidden' name='highestNumber' id='highestNumber' value='0'>",
    newHiddenInput += "<input type='hidden' name='numOfQuestions' id='numOfQuestions' value='" + numOfQuestions + "'>";
    document.getElementById("questionForm").insertAdjacentHTML("beforeend", newHiddenInput);
    */
    // "<input type='hidden' name='numOfQuestions' id='numOfQuestions' value='0'>",
}

function addMultipleChoice() {
    // var newNumber = document.getElementById("questionForm").childElementCount - 4; // if there are 2 elements (the 2 default), we place it as #1. if there are 3 elements, #2...
    var msg = "<div id='div" + newNumber + "'>";
    msg += "<p>Question #" + newNumber + " (Multiple Choice)</p>";
    msg += "<label for='question" + newNumber + "'>Front:</label><br>";
    msg += "<input type='text' placeholder='Question' id='question" + newNumber + "' name='question" + newNumber + "' required><br>";
    msg += "<label for='optionA" + newNumber + "'>Option 1:</label><br>";
    msg += "<input type='text' placeholder='Option 1' id='optionA" + newNumber + "' name='optionA" + newNumber + "' required><br>";
    msg += "<label for='optionB" + newNumber + "'>Option 2:</label><br>";
    msg += "<input type='text' placeholder='Option 2' id='optionB" + newNumber + "' name='optionB" + newNumber + "' required><br>";
    msg += "<label for='optionC" + newNumber + "'>Option 3:</label><br>";
    msg += "<input type='text' placeholder='Option 3' id='optionC" + newNumber + "' name='optionC" + newNumber + "' required><br>";
    msg += "<label for='optionD" + newNumber + "'>Option 4:</label><br>";
    msg += "<input type='text' placeholder='Option 4' id='optionD" + newNumber + "' name='optionD" + newNumber + "' required><br>";
    msg += "<label for='correctAnswer" + newNumber + "'>Correct Answer (1-4):</label><br>";
    msg += "<select id='correctAnswer" + newNumber + "' name='correctAnswer" + newNumber + "' required>";
    msg += "<option value='1'>1</option>";
    msg += "<option value='2'>2</option>";
    msg += "<option value='3'>3</option>";
    msg += "<option value='4'>4</option>";
    msg += "</select><br>";
    msg += "<button type='button' onclick='deleteQuestion(" + newNumber + ")'>Delete Question</button>";
    msg += "</div>";
    // if we have 6 questions and we delete number 3, then the POST will send the maximum number of questions (6) and leave the program to get rid of the missing ones.
    document.getElementById("questionForm").insertAdjacentHTML("beforeend", msg);
    newNumber++;
    numOfQuestions++;
    var hiddenInput1 = document.getElementById("highestNumber");
    var hiddenInput2 = document.getElementById("numOfQuestions");
    hiddenInput1.value = newNumber-1;
    hiddenInput2.value = numOfQuestions;
    /*
    hiddenInput1.parentElement.removeChild(hiddenInput1);
    hiddenInput2.parentElement.removeChild(hiddenInput2);
    var newHiddenInput = "<input type='hidden' name='highestNumber' id='highestNumber' value='" + newNumber-1 + "'>"; // "<input type='hidden' name='highestNumber' id='highestNumber' value='0'>",
    newHiddenInput += "<input type='hidden' name='numOfQuestions' id='numOfQuestions' value='" + numOfQuestions + "'>";
    document.getElementById("questionForm").insertAdjacentHTML("beforeend", newHiddenInput);
    */
}

function deleteQuestion (number) {
    if (confirm("Are you sure you want to delete question #" + number + "?")) {
        var question = document.getElementById("div" + number);
        question.parentElement.removeChild(question);
        numOfQuestions--; 
        var hiddenInput1 = document.getElementById("highestNumber");
        var hiddenInput2 = document.getElementById("numOfQuestions");
        hiddenInput1.value = newNumber;
        hiddenInput2.value = numOfQuestions;
        /*
        hiddenInput1.parentElement.removeChild(hiddenInput1);
        hiddenInput2.parentElement.removeChild(hiddenInput2);
        var newHiddenInput = "<input type='hidden' name='highestNumber' id='highestNumber' value='" + newNumber-1 + "'>"; // "<input type='hidden' name='highestNumber' id='highestNumber' value='0'>",
        newHiddenInput += "<input type='hidden' name='numOfQuestions' id='numOfQuestions' value='" + numOfQuestions + "'>";
        document.getElementById("questionForm").insertAdjacentHTML("beforeend", newHiddenInput);
        */
    }
}