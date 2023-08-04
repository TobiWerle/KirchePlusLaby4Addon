version = "0.1.0"

plugins {
    id("java-library")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    api(project(":api"))

    // If you want to use external libraries, you can do that here.
    // The dependencies that are specified here are loaded into your project but will also
    // automatically be downloaded by labymod, but only if the repository is public.
    // If it is private, you have to add and compile the dependency manually.
    // You have to specify the repository, there are getters for maven central and sonatype, every
    // other repository has to be specified with their url. Example:
    // maven(mavenCentral(), "org.apache.httpcomponents:httpclient:4.5.13")

    maven(mavenCentral(), "org.jsoup:jsoup:1.14.3")
    maven(mavenCentral(), "com.google.api-client:google-api-client:1.33.2")
    maven(mavenCentral(), "com.google.oauth-client:google-oauth-client-jetty:1.33.1")
    maven(mavenCentral(), "com.google.apis:google-api-services-sheets:v4-rev614-1.18.0-rc")
    maven(mavenCentral(), "commons-io:commons-io:2.13.0")

    // Google API
    maven(mavenCentral(),"com.google.http-client:google-http-client-gson:1.42.3");
    maven(mavenCentral(),"com.google.http-client:google-http-client-apache-v2:1.42.3");
    maven(mavenCentral(),"com.google.http-client:google-http-client:1.42.3");
    maven(mavenCentral(),"com.google.oauth-client:google-oauth-client:1.34.1");
    maven(mavenCentral(),"commons-codec:commons-codec:1.15");
    maven(mavenCentral(),"org.apache.httpcomponents:httpcore:4.4.16");
    maven(mavenCentral(),"org.apache.httpcomponents:httpclient:4.5.14");

    // oAuth
    maven(mavenCentral(),"com.google.http-client:google-http-client:1.42.0");
    maven(mavenCentral(),"com.google.oauth-client:google-oauth-client-java6:1.34.1");

    //Sheets
    maven(mavenCentral(),"io.opencensus:opencensus-api:0.31.1");
    maven(mavenCentral(),"io.opencensus:opencensus-contrib-http-util:0.11.0");
    maven(mavenCentral(),"io.grpc:grpc-context:1.56.1");
    //maven(mavenCentral(),"com.google.api-client:google-api-client:1.25.0")


    //maven(mavenCentral(), "com.google.api.client:google-api-client-json:1.2.3-alpha")
   // maven(mavenCentral(), "com.google.http-client:google-http-client-jackson:1.29.2")
    //maven(mavenCentral(), "com.google.http-client:google-http-client-jackson2:1.11.0-beta")
    //maven(mavenCentral(), "com.google.http-client:google-http-client-gson:1.43.3")


    //maven(mavenCentral(), "com.google.http-client:google-http-client:1.43.3")
    //maven(mavenCentral(), "com.google.oauth-client:google-oauth-client-java6:1.34.1")

    //maven(mavenCentral(), "com.google.oauth-client:google-oauth-client:1.34.1")
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}