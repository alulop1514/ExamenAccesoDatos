package tema6

import com.db4o.Db4oEmbedded

fun main() {
    val bd = Db4oEmbedded.openFile("Rutes.db4o")
    val gestionarRutesBD = GestionarRutesBD("jdbc:sqlite:Rutes.sqlite")
    val llistaRutes = gestionarRutesBD.llistat()
    println(llistaRutes)
    bd.close()
}
