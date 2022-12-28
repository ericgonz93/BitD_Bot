package me.ericg;
import java.sql.*;

public class Storage {
    key pass = new key();   //password holding object while I get env to work
    String url = pass.getSupaPass();
    Scoundrel readInfo(String character, Scoundrel chara) throws SQLException {
        Connection conn = DriverManager.getConnection(url);

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM scoundrels WHERE name = '"
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
                    rs.getString(25), rs.getInt(26));
            System.out.print("Name: ");
            System.out.println(rs.getString(3));
        }
        rs.close();
        st.close();

        return chara;
    }

    String createCharacter(String name, String playbook, String alias, String crew,
                         String heritage, String background, String vice) throws SQLException{
        Connection conn = DriverManager.getConnection(url);

        Statement st = conn.createStatement();
        st.execute("INSERT into scoundrels (name, playbook, alias, " +
                "crew, heritage, background, vice) values (\'" +
                name + "\', \'" + playbook + "\', \'" + alias + "\', \'" + crew +
                        "\', \'" + heritage + "\', \'" +
                background + "\', \'" + vice + "\');");

        st.close();

        return "Character " + name +
                " created! Please use !update [name] [stat] [value] " +
                "to set your bonus stats and special ability";

    }

    String updateStat(String name, String stat, String value) throws SQLException{
        Connection conn = DriverManager.getConnection(url);
        Statement st = conn.createStatement();
        //int number = 404;   //error number

        if(stat.equalsIgnoreCase("special")){
            st.execute("UPDATE scoundrels set " + stat.toLowerCase() + "_ability" +
                    " = \'" + value +"\' WHERE name = \'" + name + "\';");
            st.close();
            return "Updated " + name + "'s " + stat + " to " + value +".";
        }


        if((!stat.equalsIgnoreCase("name")) && (!stat.equalsIgnoreCase("playbook")) &&
        (!stat.equalsIgnoreCase("alias")) && (!stat.equalsIgnoreCase("crew")) &&
        (!stat.equalsIgnoreCase("heritage")) && (!stat.equalsIgnoreCase("background")) &&
        (!stat.equalsIgnoreCase("vice")) && (!stat.equalsIgnoreCase("special")) &&
        (!stat.equalsIgnoreCase("trauma"))){
        System.out.print("stat:" + stat + "\n");
            System.out.print("value:" + value + "\n");
            //checking if expected string is text
            try{
               int number = Integer.parseInt(value);

               String check = checkStatValidity(stat, value);   //for checking valid input
               if(check != "ok"){return check;}

               st.execute("UPDATE scoundrels set " + stat.toLowerCase() +
                        " = " + number +" WHERE name = \'" + name + "\';");
                st.close();
                return "Updated " + name + "'s " + stat + " to " + value +".";
            }
            catch(NumberFormatException n){
                return "value needs to be a valid number.";
            }
        }

        else{
            st.execute("UPDATE scoundrels set " + stat.toLowerCase() +
                " = \'" + value +"\' WHERE name = \'" + name + "\';");
        st.close();
        return "Updated " + name + "'s " + stat + " to " + value +".";
        }
    }

    String checkStatValidity(String stat, String value){
        if(stat.equalsIgnoreCase("stress") && (Integer.parseInt(value) >=9)){
            return "Your stress has overflowed! Update your trauma and reset stress to 0.";
        }
        if((stat.equalsIgnoreCase("attune") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("command") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("consort") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("finesse") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("hunt") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("prowl") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("skirmish") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("study") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("survey") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("sway") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("tinker") && (Integer.parseInt(value) >4)) ||
        (stat.equalsIgnoreCase("wreck") && (Integer.parseInt(value) >4)) )
        {
            return "This stat cannot be above 4.";
        }
        return "ok";
    }
}
