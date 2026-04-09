========================
BUILD OUTPUT DESCRIPTION
========================

When you build an Java application project that has a main class, the IDE
automatically copies all of the JAR
files on the projects classpath to your projects dist/lib folder. The IDE
also adds each of the JAR files to the Class-Path element in the application
JAR files manifest file (MANIFEST.MF).

To run the project from the command line, go to the dist folder and
type the following:

java -jar -Xms8g -Xmx32g \KmersTool\dist\KmersTool.jar \KmersTool\test\NC_060925.1.fasta 18 \KmersTool\test\NC_060925.1.out
