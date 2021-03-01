import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object UnitSprites {
    val body: BufferedImage = ImageIO.read(File(ClassLoader.getSystemResource("assets/unit_body.png").toURI()))
    val rifle: BufferedImage = ImageIO.read(File(ClassLoader.getSystemResource("assets/unit_rifle_weapon.png").toURI()))
    val firing_rifle: BufferedImage = ImageIO.read(File(ClassLoader.getSystemResource("assets/unit_rifle_weapon_fire.png").toURI()))
}