import brains.neat.Genome
import gameObjects.core.Arena
import gameObjects.core.Command
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.lang.Exception
import javax.swing.JPanel

object GameScreen : JPanel() {
    private val dimensions = Point(1000, 800)

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
            arena?.let {
                it.render(g)

                g.color = Color.WHITE
                g.fillRect(0, height - 300, GameScreen.width, 300)

                it.bestDude?.let { dood -> renderGenome(g, dood.genome) }
            }
        } catch (e: Exception) {

        }
    }


    private fun renderGenome(g: Graphics, genome: Genome) {
        val x = 10
        val y = height - 300
        genome.weights.forEach { weight ->
            g.color = when {
                weight.weight > 0 && weight.inverted -> Color.GREEN
                weight.weight > 0 && !weight.inverted -> Color.MAGENTA
                weight.weight < 0 && weight.inverted -> Color.RED
                weight.weight < 0 && !weight.inverted -> Color.CYAN
                else -> Color.BLACK
            }
            g.drawLine(
                (x + weight.from.x * ((GameScreen.width - x) - 100)).toInt(),
                y + (weight.from.y * 280.0).toInt() + 20,
                (x + weight.to.x * ((GameScreen.width - 2 * x) - 100)).toInt(),
                y + (weight.to.y * 280.0).toInt() + 20,
            )
        }
        g.color = Color.DARK_GRAY
        genome.nodes.forEach { node ->
            g.fillOval(
                (x + node.x * ((GameScreen.width - 2 * x) - 100)).toInt() - 3,
                y + (node.y * 280.0).toInt() + 20 - 4,
                9,
                9
            )
        }
        g.drawString("Advance", x + ((GameScreen.width - 2 * x) - 90), y + 0 + 25)
        g.drawString("Strafe", x + ((GameScreen.width - 2 * x) - 90), y + 70 + 25)
        g.drawString("Rotate", x + ((GameScreen.width - 2 * x) - 90), y + 140 + 25)
        g.drawString("Fire", x + ((GameScreen.width - 2 * x) - 90), y + 210 + 25)
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