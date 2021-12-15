package Tema4

import java.sql.DriverManager

fun main() {
    val url = "jdbc:sqlite:Rutes.sqlite"
    val con = DriverManager.getConnection(url)
    var st = con.createStatement()

    st.executeUpdate("CREATE TABLE RUTES (" +
            "num_r INTEGER, " +
            "nom_r TEXT, " +
            "desn INTEGER, " +
            "desn_ac INTEGER, " +
            "PRIMARY KEY (num_r)" +
            ")")
    st = con.createStatement()

    st.executeUpdate("CREATE TABLE PUNTS (" +
            "num_r INTEGER, " +
            "num_p INTEGER," +
            "nom_p TEXT, " +
            "latitud INTEGER, " +
            "longitud INTEGER, " +
            "PRIMARY KEY (num_r, num_p)," +
            "FOREIGN KEY(num_r) REFERENCES RUTES(num_r)" +
            ")")

    st.close()
    con.close()
}