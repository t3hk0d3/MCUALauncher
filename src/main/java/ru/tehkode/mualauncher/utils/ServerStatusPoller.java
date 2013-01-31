package ru.tehkode.mualauncher.utils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.apache.commons.io.IOUtils;
import ru.tehkode.mualauncher.net.Downloader;

/**
 *
 * @author t3hk0d3
 */
public class ServerStatusPoller {

    private final URL statusUrl;

    public ServerStatusPoller(URL statusUrl) {
        this.statusUrl = statusUrl;
    }

    public ServerStatus pollStatus() throws IOException {
        return new ServerStatus(Downloader.create(statusUrl).downloadLines());
    }

    public class ServerStatus {

        private final boolean online;
        private final int onlinePlayers;
        private final int totalPlayers;

        protected ServerStatus(List<String> lines) {
            online = "online".equals(lines.get(0));

            if (online) {
                String[] parts = lines.get(1).split("\\/");

                onlinePlayers = Integer.parseInt(parts[0]);
                totalPlayers = Integer.parseInt(parts[1]);
            } else {
                onlinePlayers = totalPlayers = 0;
            }
        }

        public boolean isOnline() {
            return online;
        }

        public int getOnlinePlayers() {
            return onlinePlayers;
        }

        public int getTotalPlayers() {
            return totalPlayers;
        }

    }

}
