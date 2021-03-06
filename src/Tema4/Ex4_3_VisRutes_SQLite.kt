package Tema4

import javax.swing.JFrame
import java.awt.EventQueue
import java.awt.BorderLayout
import javax.swing.JPanel
import java.awt.FlowLayout
import java.sql.DriverManager
import javax.swing.JComboBox
import javax.swing.JButton
import javax.swing.JTextArea
import javax.swing.JLabel
import kotlin.system.exitProcess

class Finestra : JFrame() {

    init {
        // Sentències per a fer la connexió
        val con = DriverManager.getConnection("jdbc:sqlite:Rutes.sqlite")
        var st = con.createStatement()
        defaultCloseOperation = EXIT_ON_CLOSE
        setTitle("JDBC: Visualitzar Rutes")
        setSize(450, 450)
        setLayout(BorderLayout())

        val panell1 = JPanel(FlowLayout())
        val panell2 = JPanel(BorderLayout())
        add(panell1, BorderLayout.NORTH)
        add(panell2, BorderLayout.CENTER)

        val llistaRutes = arrayListOf<String>()
        // Sentències per a omplir l'ArrayList amb el nom de les rutes

        var rs = st.executeQuery("SELECT nom_r FROM RUTES")
        while (rs.next()) {
            llistaRutes.add(rs.getString(1))
        }
        println(llistaRutes)


        val combo = JComboBox(llistaRutes.toTypedArray())
        panell1.add(combo)
        val eixir = JButton("Eixir")
        panell1.add(eixir)
        val area = JTextArea()
        panell2.add(JLabel("Llista de punts de la ruta:"),BorderLayout.NORTH)
        panell2.add(area,BorderLayout.CENTER)


        combo.addActionListener {
            // Sentèncis quan s'ha seleccionat un element del JComboBox
            // Han de consistir en omplir el JTextArea
            area.text = ""
            st = con.createStatement()
            rs = st.executeQuery("SELECT nom_p, latitud, longitud FROM PUNTS WHERE num_r=${combo.selectedIndex}")
            while (rs.next()) {
                area.append("${rs.getString(1)}(${rs.getDouble(2)}, ${rs.getDouble(3)})\n")
            }
        }

        eixir.addActionListener {
            // Sentències per a tancar la connexió i eixir
            con.close()
            exitProcess(0)
        }
        combo.selectedIndex = 0
    }
}

fun main() {
    EventQueue.invokeLater {
        Finestra().isVisible = true
    }
}