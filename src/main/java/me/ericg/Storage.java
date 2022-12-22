package me.ericg;
import java.security.Key;
import java.sql.*;
import java.util.Properties;

public class Storage {

    String readinfo(String character) throws SQLException {
        key pass = new key();   //password holding object while I get env to work
        String url = pass.getSupaPass();
        Connection conn = DriverManager.getConnection(url);

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT task_title FROM todos WHERE id = 3");
        while (rs.next()) {
            System.out.print("Column 1 returned ");
            System.out.println(rs.getString(1));
        }
        rs.close();
        st.close();

        return "cheese";
    }
}
