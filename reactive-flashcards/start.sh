gradle clean
gradle botJar

# DEBUG
#java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar build/lib/reactive-flashcards-1.0.0.jar

# RUN
java -jar build/lib/reactive-flashcards-1.0.0.jar
