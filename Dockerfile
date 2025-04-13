FROM tomcat:9.0-jdk17-corretto

# Remove the default ROOT webapp.
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your WAR file from the target directory into Tomcat’s webapps folder,
# renaming it to ROOT.war so that it is deployed as the root application.
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war