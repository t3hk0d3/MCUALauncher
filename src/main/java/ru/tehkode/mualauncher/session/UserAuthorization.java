package ru.tehkode.mualauncher.session;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import ru.tehkode.mualauncher.net.Downloader;

/**
 *
 * @author t3hk0d3
 */
public class UserAuthorization {

    private final static String LOGIN_URL_TEMPLATE = "https://login.minecraft.net/?user=%s&password=%s&version=13";
    private final UserCredenitals credenitals;

    public UserAuthorization(UserCredenitals credenitals) {
        this.credenitals = credenitals;
    }

    public UserSession authorize() throws IOException, BadLoginException {
        return new MinecraftSession(this.openSession());
    }

    private String openSession() throws IOException {
        return Downloader.create(getLoginURL()).downloadToString();
    }

    private URL getLoginURL() {
        try {
            String login = URLEncoder.encode(this.credenitals.getLogin(), "UTF-8");
            String password = URLEncoder.encode(this.credenitals.getPassword(), "UTF-8");

            return new URL(String.format(LOGIN_URL_TEMPLATE, login, password));
        } catch (Exception e) {
            throw new RuntimeException(e); // should not happen
        }
    }

    public class MinecraftSession implements UserSession {

        private final String login;
        private final String sessionId;

        protected MinecraftSession(String sessionString) throws BadLoginException {
            if ("Bad login".equalsIgnoreCase(sessionString) || !sessionString.contains(":")) {
                throw new BadLoginException();
            }

            // <timestamp>:<ticket>:<login>:<sessionId>:<userId>
            String[] parts = sessionString.split(":");
            
            this.login = parts[2];
            this.sessionId = parts[3];
        }

        @Override
        public String getLogin() {
            return login;
        }

        @Override
        public String getSessionId() {
            return sessionId;
        }
    }

    public static class BadLoginException extends Exception {
    }
}
