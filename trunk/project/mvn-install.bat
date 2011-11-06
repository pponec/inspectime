echo set MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128m"
echo set MAVEN_OPTS="-Xmx1024m"
call mvn clean
call mvn install

pause