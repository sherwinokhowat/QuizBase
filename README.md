# QuizBase

For Windows, run the following commands on the command line:

```
dir src /b /s *.java > sources.txt
javac -d ./out/ @sources.txt
cd out
java -cp ".;../sqlite-jdbc-3.21.0.jar" Main
```
