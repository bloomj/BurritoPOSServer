Burrito Point of Service Server
================================

Overview
-------------------------
This is simple Point of Service server application for a small town burrito joint.  
It serves as a starting point to refresh memory of or learn about new/existing languages, frameworks, design patterns, etc.

Currently demonstrates
-------------------------
* Eclipse
* Java
* Ant
* JUnit
* Hibernate
* MySQL
* Factory Pattern
* Spring
* N-tier architecture
* client/server comms (Cleartext & TLS)
* Mongo (NoSQL)
* JavaDoc
  
TODO
-------------------------
* Update business layer junit tests
* Add Oauth and/or HMAC
* Refactor presentation layer to use MVC or MVP and incorporate UI unit test; perhaps FEST or UISpec4J?

Notes
-------------------------
* junit dlls were placed in ant\lib for proper ant build
* If a javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: 
	PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target 
	error occurs, check the keystore against the servername to ensure the certificate exists (trust is currently ignored)
* keytool -genkey -keyalg RSA -alias <machine_name> -keystore <pathtokeystore>.jks -storepass changeit -validity 360 -keysize 4096