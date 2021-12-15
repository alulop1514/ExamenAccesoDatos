package tema6

import com.db4o.Db4oEmbedded
import exercicis.Ruta

fun main() {
    val bd = Db4oEmbedded.openFile ("Rutes.db4o")
    val llistaRutes = bd.query(Ruta::class.java)
    for (ruta in llistaRutes) {
        println("${ruta.nom}: ${ruta.llistaDePunts.count()} punts")
    }
    bd.close()
}