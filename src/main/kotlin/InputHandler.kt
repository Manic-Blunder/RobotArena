import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

object InputHandler : KeyListener, MouseListener {
    override fun keyTyped(e: KeyEvent) {

    }

    override fun keyPressed(e: KeyEvent) {
        GameScreen.onKeyDownEvent(e.keyChar)
    }

    override fun keyReleased(e: KeyEvent) {
        GameScreen.onKeyUpEvent(e.keyChar)
    }

    override fun mouseClicked(e: MouseEvent) {

    }

    override fun mousePressed(e: MouseEvent) {

    }

    override fun mouseReleased(e: MouseEvent) {
        GameScreen.onMouseUpEvent(e.point)
    }

    override fun mouseEntered(e: MouseEvent) {

    }

    override fun mouseExited(e: MouseEvent) {

    }
}