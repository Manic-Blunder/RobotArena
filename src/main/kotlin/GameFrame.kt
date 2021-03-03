import javax.swing.JFrame

object GameFrame : JFrame("RoboWar!") {
    init  {
        InputHandler
        defaultCloseOperation = EXIT_ON_CLOSE
        isLocationByPlatform = true
        isVisible = true
        isResizable = false
        contentPane.addMouseListener(InputHandler)
        addKeyListener(InputHandler)
        contentPane.add(GameScreen)
        pack()
    }
}