package org.novasparkle.lunaspring.self.lunaengine;

import lombok.Getter;
import lombok.SneakyThrows;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;

import java.net.Inet4Address;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Getter
public class LunaEngine {
    private final String host;
    private final int port;
    private final String pw;
    private final String user;
    private final String db;
    private Connection connection;

    public LunaEngine() {
        this.host = "localhost";
        this.port = 3306;
        this.pw = "Ufus@2008";
        this.user = "root";
        this.db = "telegram";
        this.connect();
    }
    @SneakyThrows
    public void connect() {
        synchronized (LunaSpring.getINSTANCE()) {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pw);
            }
        }
    }

    @SneakyThrows
    public boolean checkPlugin(LunaPlugin plugin) {
        PreparedStatement statement = this.connection.prepareStatement(String.format("SELECT value from plugin_keys WHERE ipv4='%s' AND plugin_name='%s'", Inet4Address.getLocalHost().getHostAddress(), plugin.getName()));
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}
