package mypack.mconfig;

import mypack.log.LoggingService;

import java.io.FileInputStream;
import java.util.Properties;

public class ServerConfig {

    private static String host;

    private static int port;

    private static long timeScanSession;

    public static void initConfig() {
        try {
            Properties p = new Properties();
            String pathFile = System.getProperty("user.dir") + "/config/server.properties";
            FileInputStream fiProp = new FileInputStream(pathFile);
            p.load(fiProp);
            host = p.getProperty("host");
            port = getIntProp(p, "port");
            timeScanSession = getIntProp(p, "time_scan_session");

            LoggingService.getInstance().getLogger().info("Init Config server success!");
        } catch (Exception e) {
            LoggingService.getInstance().getLogger().error("err while init config server", e);
        }
    }

    private static int getIntProp(Properties p, String k) {
        return Integer.parseInt(p.getProperty(k));
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static long getTimeScanSession() {
        return timeScanSession;
    }
}
