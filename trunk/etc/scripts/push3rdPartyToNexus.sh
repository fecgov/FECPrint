#!/bin/bash

# NOTE: You don't need to run this to set up your workstation. This loads the 3rd party jxbrowser jars
# into Nexus. It should only need run in situations where we are upgrading to a new version of it.

mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=jxbrowser-2.9.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=jxbrowser -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true 


mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=jxbrowser-license.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=license -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true 

mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=engine-ie.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=engine-ie -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true  


mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=engine-webkit.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=engine-webkit -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true 

mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=engine-gecko.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=engine-gecko -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true   


mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=MozillaInterfaces.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=MozillaInterfaces -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true 

mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=MozillaGlue.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=MozillaGlue -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true    


mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=tuxpack-0.2.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=tuxpack -Dversion=0.2 -Dpackaging=jar -DgeneratePom=true -Dclassifier=linux 

mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=xulrunner-linux.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=xulrunner-linux -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true -Dclassifier=linux 

mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=xulrunner-linux64.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=xulrunner-linux64 -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true -Dclassifier=linux 

mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=xulrunner-mac.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=xulrunner-mac -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true -Dclassifier=mac

mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=xulrunner-windows.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=xulrunner-windows -Dversion=2.9 -Dpackaging=jar -DgeneratePom=true -Dclassifier=windows  

mvn deploy:deploy-file -Durl=http://admin:admin123@jenkins-s1-01.efsi-fec.local:8081/nexus/content/repositories/thirdparty \
 -Dfile=winpack-3.8.2.jar -DgroupId=com.teamdev.jxbrowser \
 -DartifactId=winpack -Dversion=3.8.2 -Dpackaging=jar -DgeneratePom=true -Dclassifier=windows


 
