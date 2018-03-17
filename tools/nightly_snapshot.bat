cd ..

for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /format:list') do set datetime=%%I
set datetime=%datetime:~0,8%-%datetime:~8,6%

call mvn clean

cd tools

call install_antlr4_plsql_grammar.bat

cd ..\..

call mvn clean package

copy target\vau-bin.zip vau-bin-%datetime%.zip