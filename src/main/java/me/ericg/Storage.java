package me.ericg;
import java.sql.*;

public class Storage {
    key pass = new key();   //password holding object while I get env to work

    Scoundrel readInfo(String character, Scoundrel chara) throws SQLException {
        String url = pass.getSupaPass();
        Connection conn = DriverManager.getConnection(url);
        //scoundrel chara = new scoundrel();  //holder for character data

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM characters WHERE name = '"
                + character.toLowerCase() + "';");
        while (rs.next()) {
            chara.setAllParameters(rs.getString(3
            ), rs.getString(4), rs.getString(5), rs.getString(6),
                    rs.getString(7), rs.getString(8), rs.getString(9),
                    rs.getString(10), rs.getInt(11), rs.getInt(12),
                    rs.getInt(13), rs.getInt(14), rs.getInt(15),
                    rs.getInt(16), rs.getInt(17), rs.getInt(18),
                    rs.getInt(19), rs.getInt(20), rs.getInt(21),
                    rs.getInt(22), rs.getInt(23), rs.getInt(24),
                    rs.getString(25));
            System.out.print("Name: ");
            System.out.println(rs.getString(3));
        }
        rs.close();
        st.close();

        return chara;
    }
}
