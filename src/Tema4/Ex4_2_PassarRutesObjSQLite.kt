package Tema4

import exercicis.Ruta
import java.io.EOFException
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.sql.DriverManager

fun main() {
    val fIn = ObjectInputStream(FileInputStream("Rutes.obj"))
    val rutes = arrayListOf<Ruta>()
    val url = "jdbc:sqlite:Rutes.sqlite"
    val con = DriverManager.getConnection(url)
    var st = con.createStatement()

    try {
        /* Creem un bucle per a llegir y afegir les rutes al array de rutes.
           El bucle parara quan salte la excepcio.
        */
        while (true) {
            val ruta = fIn.readObject() as Ruta
            rutes.add(ruta)
        }
    } catch (ex : EOFException) {
        fIn.close()
    }

    for (ruta in rutes) {
        st.executeUpdate("INSERT INTO RUTES VALUES (" +
                "${rutes.indexOf(ruta)},'${ruta.nom}', ${ruta.desnivell}, ${ruta.desnivellAcumulat}" +
                ")")
        st.close()
        st = con.createStatement()
        for (punt in ruta.llistaDePunts) {
            st.executeUpdate("INSERT INTO PUNTS VALUES (" +
                    "${rutes.indexOf(ruta)}, ${ruta.llistaDePunts.indexOf(punt)}," +
                    "'${punt.nom}', ${punt.coord.latitud}, ${punt.coord.longitud}" +
                    ")")
        }
    }
    st.close()
    con.close()
}