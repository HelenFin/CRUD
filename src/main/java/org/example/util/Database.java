package org.example.util;

import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

public class Database {
    private static Database instance;
    private DataSource dataSource;

    private Database() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:~/megasoft");
        ds.setUser("sa");
        ds.setPassword("");
        this.dataSource = ds;
    }

    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }
}
