# JSONEventProcStoreHSQLDB
The utility will read events log generated by multi threaded application, process them to set alert and add processed records in In-Memory HSQLDB


Do update configurataion for your respective DB in file "C:\hsqldb-2.6.0\hsqldb\server.properties"
server.database.0 = file:hsqldb/demodb
server.dbname.0 = testdb

For JDK 1.8 user rename files from below path C:\hsqldb-2.6.0\hsqldb\lib\

rename hsqldb.jar to hsqldb.jar_latestjava_backup
rename hsqldb-jdk8.jar to hsqldb.jar


Sample command to start hsqldb
CD -> C:\Windows\System32\cmd.exe
-> java -classpath lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/demodb --dbname.0 testdb

HSQL DB UI utility start command 
cd -> C:\hsqldb-2.6.0\hsqldb\bin
dubble click on -> runManagerSwing.bat

