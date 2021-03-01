import gameObjects.core.Arena
import gameObjects.core.Command
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.lang.Exception
import javax.swing.JPanel

object GameScreen : JPanel() {
    private val dimensions = Point(1280, 1020)

    private val keysDown = mutableSetOf<Char>()

    var renderSpecial = true
    var renderArena = true


    var arena: Arena? = null

    fun update() {
        arena?.doods?.firstOrNull()?.let { dood ->
            keysDown.toSet().forEach { char ->
                when (char) {
                    'w' -> dood.commands.add(Command.MOVE_FORWARD)
                    'a' -> dood.commands.add(Command.MOVE_LEFT)
                    's' -> dood.commands.add(Command.MOVE_BACK)
                    'd' -> dood.commands.add(Command.MOVE_RIGHT)
                    ',' -> dood.commands.add(Command.ROTATE_LEFT)
                    '.' -> dood.commands.add(Command.ROTATE_RIGHT)
                    ' ' -> dood.commands.add(Command.FIRE)
                }
            }
        }
        arena?.update()
    }

    override fun paint(g: Graphics) {
        try {
            arena?.render(g)
        } catch (e: Exception) {

        }
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(dimensions.x, dimensions.y)
    }

    fun onKeyDownEvent(keyChar: Char) {
        keysDown.add(keyChar)
    }

    fun onMouseUpEvent(point: Point) {
        renderSpecial = !renderSpecial
    }

    fun onKeyUpEvent(keyChar: Char) {
        keysDown.remove(keyChar)
        renderArena = !renderArena
    }
}