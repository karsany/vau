rmdir /S /Q grammars-v4

git clone -n https://github.com/antlr/grammars-v4.git --depth 1
cd grammars-v4
git checkout HEAD plsql/
git checkout HEAD pom.xml
cat pom.xml | grep -v "<module>" | sed "s/<modules>/<modules><module>plsql<\/module>/g" > pom2.xml
del pom.xml
ren pom2.xml pom.xml

cd plsql
del /Q examples\*
del /Q examples-sql-script\*
del /Q not-implemented\*

sed -i "s/parser grammar PlSqlParser;/parser grammar PlSqlParser;\n\n@header {package com.antlr.grammarsv4.plsql;}/g" PlSqlParser.g4
sed -i "s/lexer grammar PlSqlLexer;/lexer grammar PlSqlLexer;\n\n@lexer::header {package com.antlr.grammarsv4.plsql;}/g" PlSqlLexer.g4

md src\main\java\com\antlr\grammarsv4\plsql

echo package com.antlr.grammarsv4.plsql; > src\main\java\com\antlr\grammarsv4\plsql\PlSqlBaseLexer.java
echo package com.antlr.grammarsv4.plsql; > src\main\java\com\antlr\grammarsv4\plsql\PlSqlBaseParser.java

cat Java\PlSqlBaseLexer.java >> src\main\java\com\antlr\grammarsv4\plsql\PlSqlBaseLexer.java
cat Java\PlSqlBaseParser.java >> src\main\java\com\antlr\grammarsv4\plsql\PlSqlBaseParser.java

cd ..

call mvn package install
