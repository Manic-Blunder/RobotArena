package gameObjects.core

import fuzzyMath.*
import GameScreen
import UnitSprites.body
import UnitSprites.firing_rifle
import UnitSprites.rifle
import brains.neat.Genome
import fuzzyMath.shapes.Circular
import fuzzyMath.shapes.Vector2
import java.awt.Color
import java.awt.Graphics

import java.awt.image.BufferedImage
import kotlin.math.*


abstract class Dood(val arena: Arena, override var position: Vector2, val team: Team, val genome: Genome) : GameObject, Circular {

    var isDead = false
        private set
    var angle = Math.random() * 2 * PI
        private set(value) { field = value % (2 * PI)}
    var velocity = Vector2(0.0, 0.0)
        private set
    var rotationalVelocity = 0.0
        private set(value) {
            field = clamp((-1 * maxTurningSpeed), value, maxTurningSpeed)
        }

    var dizziness = 0.0
        private set(value) {
            field = clamp(0.0, value, 1000.0)
        }

    var isFiring = false
        private set
    var fireCooldownRemaining = fireCooldown
        private set(value) {
            field = max(0, value)
        }

    override val radius
        get() = doodRadius

    val commands: MutableSet<Command> = mutableSetOf()

    private fun processCommand(command: Command) {
        when (command) {
            Command.MOVE_FORWARD -> processMoveForwardCommand()
            Command.MOVE_BACK -> processMoveBackCommand()
            Command.MOVE_LEFT -> processMoveLeftCommand()
            Command.MOVE_RIGHT -> processMoveRightCommand()
            Command.ROTATE_LEFT -> processRotateLeftCommand()
            Command.ROTATE_RIGHT -> processRotateRightCommand()
            Command.FIRE -> processFireCommand()
        }
    }

    private fun processMoveForwardCommand() {
        velocity += Vector2.fromPolar(acceleration, angle)
        afterMoveForwardCommand()
    }

    private fun processMoveBackCommand() {
        velocity += Vector2.fromPolar(acceleration, angle - PI)
        afterMoveBackCommand()
    }

    private fun processMoveLeftCommand() {
        velocity += Vector2.fromPolar(acceleration, angle - PI / 2)
        afterMoveLeftCommand()
    }

    private fun processMoveRightCommand() {
        velocity += Vector2.fromPolar(acceleration, angle + PI / 2)
        afterMoveRightCommand()
    }

    private fun processRotateLeftCommand() {
        rotationalVelocity -= turningSpeed
        afterRotateLeftCommand()
    }

    private fun processRotateRightCommand() {
        rotationalVelocity += turningSpeed
        afterRotateRightCommand()
    }

    private fun processFireCommand() {
        if (fireCooldownRemaining <= 0) {
            isFiring = true
            arena.bullets.add(
                Bullet(
                    this,
                    Vector2(
                        (position.x + 27 * cos(angle + 12 * PI / 180)),
                        (position.y + 27 * sin(angle + 12 * PI / 180))
                    ),
                    Vector2.fromPolar(10.0, angle + (Math.random() * 0.1 - 0.05) + (Math.random() * (dizziness * 0.1) - (dizziness * 0.05)))
                )
            )
            fireCooldownRemaining = fireCooldown
        }
        afterFireCommand()
    }

    override fun update() {
        isFiring = false
        if (isDead) {
            respawn()
        } else {
            angle += rotationalVelocity
            dizziness -= 0.05
            dizziness += abs(rotationalVelocity)
            fireCooldownRemaining -= 1
            commands.forEach { processCommand(it) }
            commands.clear()
            move()
        }
        afterUpdate()
    }

    private fun move() {
        checkCollision(position)?.let { contactPoint ->
            val directionOfContact = atan2(position.y - contactPoint.y, position.x - contactPoint.x)
            position = Vector2.fromPolar(radius, directionOfContact) + contactPoint
            velocity = velocity.reflect(directionOfContact - (PI / 2)) / 2
        }
        position += velocity
        velocity *= dragMultiplier
        rotationalVelocity *= rotationalDragMultiplier
        arena.structures.forEach { it.expel(this) }
        afterMove()
    }

    fun die() {
        isDead = true
    }

    private fun respawn() {
        isDead = false
        fireCooldownRemaining = 100
        commands.clear()
        position = Vector2(Math.random() * (arena.width - 40) + 20, Math.random() * (arena.height - 40) + 20)
        afterRespawn()
    }

    private fun checkCollision(newPosition: Vector2): Vector2? {
        arena.doods.forEach {
            if (it != this && isColliding(it)) {
                return it.closestEdgePoint(newPosition)
            }
        }

        arena.structures.forEach {
            if (isColliding(it)) {
                return it.closestEdgePoint(newPosition)
            }
        }
        return null
    }

    override fun render(g: Graphics) {
        val rotatedRifle = rotateImage(if (isFiring) firing_rifle else rifle, angle)
        g.drawImage(rotatedRifle, position.x.toInt() - rotatedRifle.width / 2, position.y.toInt() - rotatedRifle.height / 2, GameScreen)
        g.drawImage(body, position.x.toInt() - body.width / 2, position.y.toInt() - body.height / 2, GameScreen)

        g.color = when (team) {
            Team.CYAN -> Color.CYAN
            Team.ORANGE -> Color.ORANGE
            Team.MAGENTA -> Color.MAGENTA
            Team.GREEN -> Color.GREEN
            Team.ALEX -> Color.BLUE
            Team.ZOONER -> Color(85, 15, 205)
        }

        g.fillOval((position.x - radius / 2).toInt(), (position.y - radius / 2).toInt(), radius.toInt(), radius.toInt())
    }

    private fun rotateImage(image: BufferedImage, angle: Double): BufferedImage {
        val rotated = BufferedImage(image.width, image.height, image.type)
        val graphic = rotated.createGraphics()
        graphic.rotate(angle, (image.width / 2).toDouble(), (image.height / 2).toDouble())
        graphic.drawImage(image, null, 0, 0)
        graphic.dispose()
        return rotated
    }

    abstract val neuralInputs: ArrayList<Double>
    abstract fun processNeuralOutputs(outputs: List<Double>)
    abstract fun recalculateGenomeScore()
    open fun afterUpdate() {}
    open fun afterMove() {}
    open fun afterRespawn() {}
    open fun specialRender(g: Graphics) {}
    open fun afterMoveForwardCommand() {}
    open fun afterMoveBackCommand() {}
    open fun afterMoveRightCommand() {}
    open fun afterMoveLeftCommand() {}
    open fun afterRotateLeftCommand() {}
    open fun afterRotateRightCommand() {}
    open fun afterFireCommand() {}
    open fun onKill(victim: Dood) {}
    open fun onKilled(killer: Dood) {}
    open fun onBetrayal(victim: Dood) {}
    open fun onBetrayed(killer: Dood) {}
    open fun onWin(teamScores: Pair<Int, Int>) {}
    open fun onLoss(teamScores: Pair<Int, Int>) {}

    companion object {
        const val fireCooldown = 250

        const val acceleration = 0.5
        const val maxVelocity = 2.0
        const val dragMultiplier = 1 - acceleration / maxVelocity

        const val turningSpeed = 0.5 * PI / 180.0
        const val maxTurningSpeed = 10.0 * PI / 180.0
        const val rotationalDragMultiplier = 1 - turningSpeed / maxTurningSpeed

        const val doodRadius = 12.0
    }
}

