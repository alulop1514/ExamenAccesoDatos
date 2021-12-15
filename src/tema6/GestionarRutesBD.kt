package tema6

import exercicis.Coordenades
import exercicis.PuntGeo
import exercicis.Ruta
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class GestionarRutesBD(var url: String) {
    var conexion : Connection
    var st : Statement

    init {
        conexion = DriverManager.getConnection("jdbc:sqlite:Rutes.sqlite")
        st = conexion.createStatement()
        st.executeUpdate("CREATE TABLE IF NOT EXISTS RUTES (" +
                "num_r INTEGER, " +
                "nom_r TEXT, " +
                "desn INTEGER, " +
                "desn_ac INTEGER, " +
                "PRIMARY KEY (num_r)" +
                ")")
        st.close()
        st = conexion.createStatement()
        st.executeUpdate("CREATE TABLE IF NOT EXISTS PUNTS (" +
                "num_r INTEGER, " +
                "num_p INTEGER," +
                "nom_p TEXT, " +
                "latitud INTEGER, " +
                "longitud INTEGER, " +
                "PRIMARY KEY (num_r, num_p)," +
                "FOREIGN KEY(num_r) REFERENCES RUTES(num_r)" +
                ")")

        st.close()
    }
    fun close() {
        conexion.close()
        st.close()
    }

    fun inserir(ruta : Ruta) {
        val sentenciaSQL = "SELECT MAX(num_r) FROM RUTES"
        val numRutas = st.executeQuery(sentenciaSQL).getInt(1) + 1
        st = conexion.createStatement()
        st.executeUpdate("INSERT INTO RUTES VALUES (" +
                "$numRutas,'${ruta.nom}', ${ruta.desnivell}, ${ruta.desnivellAcumulat}" +
                ")")
        st.close()
        st = conexion.createStatement()
        for (punt in ruta.llistaDePunts) {
            st.executeUpdate("INSERT INTO PUNTS VALUES (" +
                    "$numRutas, ${ruta.llistaDePunts.indexOf(punt)}," +
                    "'${punt.nom}', ${punt.coord.latitud}, ${punt.coord.longitud}" +
                    ")")
        }
        st.close()
    }

    fun buscar(posicion: Int) : Ruta {
        st = conexion.createStatement()
        val ruta = st.executeQuery("SELECT * FROM RUTES WHERE num_r = $posicion")
        st = conexion.createStatement()
        val punts = st.executeQuery("SELECT nom_p, latitud, longitud FROM PUNTS WHERE num_r = $posicion")
        val arrayPunts = arrayListOf<PuntGeo>()
        while (punts.next()) {
            arrayPunts.add(PuntGeo(punts.getString(1), Coordenades(punts.getDouble(2), punts.getDouble(3))))
        }
        st.close()
        return Ruta(ruta.getString(2), ruta.getInt(3),ruta.getInt(4), arrayPunts)
    }
    fun llistat() : ArrayList<Ruta> {
        val llistaRutes = arrayListOf<Ruta>()
        val sentenciaSQL = "SELECT MAX(num_r) FROM RUTES"
        val numRutas = st.executeQuery(sentenciaSQL).getInt(1) + 1
        for (posicion in 0 until numRutas) {
            llistaRutes.add(buscar(posicion))
        }
        return llistaRutes
    }

    fun guardar(r : Ruta) {
        st = conexion.createStatement()
        val sentenciaSQL = "SELECT MAX(num_r) FROM RUTES"
        val numRutas = st.executeQuery(sentenciaSQL).getInt(1) + 1
        st = conexion.createStatement()
        var esta = false
        var posicion = 0
        for (posicionRuta in 0 until numRutas) {
            if (buscar(posicionRuta).nom == r.nom) {
                esta = true
                posicion = posicionRuta
            }
        }
        if (esta) {
            st.executeUpdate("UPDATE RUTES SET desn = ${r.desnivell}, desn_ac = ${r.desnivellAcumulat} WHERE nom_r = '${r.nom}'")
            st.executeUpdate("DELETE FROM PUNTS WHERE num_r = $posicion")
            for (punt in r.llistaDePunts) {
                st.executeUpdate("INSERT INTO PUNTS VALUES (" +
                        "$posicion, ${r.llistaDePunts.indexOf(punt)}," +
                        "'${punt.nom}', ${punt.coord.latitud}, ${punt.coord.longitud}" +
                        ")")
            }
        } else {
            inserir(r)
        }
        st.close()

    }
}