package uc.kircheplus.utils;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.util.Preconditions;
import java.io.IOException;

public class testAuthorize extends AuthorizationCodeInstalledApp{

    private final AuthorizationCodeFlow flow;

    /** Verification code receiver. */
    private final VerificationCodeReceiver receiver;

    public testAuthorize(AuthorizationCodeFlow flow, VerificationCodeReceiver receiver) {
        super(flow, receiver);
        this.flow = flow;
        this.receiver = receiver;
    }


    @Override
    public Credential authorize(String userId) throws IOException {
        try {
            Credential credential = flow.loadCredential(userId);
            if (credential != null
                && (credential.getRefreshToken() != null
                || credential.getExpiresInSeconds() == null
                || credential.getExpiresInSeconds() > 60)) {
                return credential;
            }
            // open in browser
            String redirectUri = receiver.getRedirectUri();
            AuthorizationCodeRequestUrl authorizationUrl =
                flow.newAuthorizationUrl().setRedirectUri(redirectUri);


            //onAuthorization(authorizationUrl);
            String url = authorizationUrl.build();
            Preconditions.checkNotNull(url);
            browser(url);
            System.out.println("Allahu akbar du bastard");

            // receive authorization code and exchange it for an access token
            String code = receiver.waitForCode();
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
            // store credential and return it
            return flow.createAndStoreCredential(response, userId);
        } finally {
            receiver.stop();
        }
    }


    private void browser(String url){
        try{
            String os = System.getProperty("os.name").toLowerCase();
            if(os.indexOf("win") >= 0){
                Runtime rt = Runtime.getRuntime();
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            }else if(os.indexOf("mac") >= 0){
                Runtime rt = Runtime.getRuntime();
                rt.exec("open " + url);
            }else if(os.indexOf("nix") >=0 || os.indexOf("nux") >=0){
                Runtime rt = Runtime.getRuntime();
                String[] browsers = { "google-chrome", "firefox", "mozilla", "epiphany", "konqueror",
                    "netscape", "opera", "links", "lynx" };

                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++)
                    if(i == 0)
                        cmd.append(String.format(    "%s \"%s\"", browsers[i], url));
                    else
                        cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
                rt.exec(new String[] { "sh", "-c", cmd.toString() });
            }
        }catch (IOException e){
            System.out.println("Fehler du hurensohn");
        }
    }
}
