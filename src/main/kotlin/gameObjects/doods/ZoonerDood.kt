package gameObjects.doods

import brains.neat.Genome
import fuzzyMath.clamp
import fuzzyMath.minus
import fuzzyMath.plus
import fuzzyMath.shapes.Line
import fuzzyMath.shapes.Vector2
import fuzzyMath.times
import gameObjects.core.*
import gameObjects.peripherals.SightRay
import java.awt.Color.*
import java.awt.Graphics
import kotlin.math.*


class ZoonerDood(arena: Arena, genome: Genome, position: Vector2) : Dood(arena, position, Team.ZOONER, genome) {

    var score = 0.0
        private set(value) {
            field = max(0.0, value)
        }

    private val closestEnemy
        get() = arena.doods.filter { it.team != team }.minByOrNull { it.distanceFromSurface(position) }
    private val closestAlly
        get() = arena.doods.filter { it.team == team && it != this }.minByOrNull { it.distanceFromSurface(position) }
    private val closestWall
        get() = arena.structures.minByOrNull { it.distanceFromSurface(position) }
    private val closestBullet
        get() = arena.bullets.minByOrNull { it.distanceFromSurface(position) }

    override fun specialRender(g: Graphics) {
        g.color = BLACK
        g.drawString(String.format("%.2f", score), 24, (arena.height - 32).toInt())

//        (myInfo + arenaInfo + previousOutputs).forEachIndexed { i, it ->
//            g.drawString(String.format("%.5f", it), 30, i * 20 + 100)
//        }

        g.color = RED
        closestEnemy?.let {
            g.drawOval((it.position.x - 15).toInt(), (it.position.y - 15).toInt(), 30, 30)
            SightRay(this, position, (it.closestEdgePoint(position) - position).angle, 4.0, 150).draw(g)
        }

        g.color = GREEN
        closestAlly?.let {
            g.drawOval((it.position.x - 15).toInt(), (it.position.y - 15).toInt(), 30, 30)
            SightRay(this, position, (it.closestEdgePoint(position) - position).angle, 4.0, 150).draw(g)
        }

        g.color = DARK_GRAY
        closestWall?.let {
            val closestPoint = it.closestEdgePoint(position)
            g.drawOval((closestPoint.x - 15).toInt(), (closestPoint.y - 15).toInt(), 30, 30)
            SightRay(this, position, (it.closestEdgePoint(position) - position).angle, 4.0, 150).draw(g)
        }

        g.color = MAGENTA
        closestBullet?.let {
            g.drawOval((it.position.x - 10).toInt(), (it.position.y - 10).toInt(), 20, 20)
            Line(position, it.closestEdgePoint(position) - position).draw(g)
        }

        val gunPosition = Vector2(
            (position.x + 27 * cos(angle + 12 * PI / 180)),
            (position.y + 27 * sin(angle + 12 * PI / 180))
        )

        SightRay(this, gunPosition, angle, 4.0, 150).draw(g)
//        val rays = 30
//        val max = (0 until rays / 2).sum().toDouble()
//        repeat(rays) { i ->
//            var blah = (0 until abs(i - rays / 2)).sum().toDouble()
//            var piBlah = (max - blah) / max * PI
//            if (i - rays / 2 < 0) piBlah *= -1
//            var distanceBlah = (15.0 - abs(15.0 - i)) / 15.0 * 100.0
//
//            SightRay(this, position, piBlah + angle - PI, 4.0, distanceBlah.toInt() + 10).draw(g)
//
//            g.color = BLUE
//            g.drawString(dizziness.toString(), 100, 100)
//        }
    }

    // My Cooldown
    // My Angle
    // My Dizziness
    // My Velocity
    // My Angle of Movement
    // My Rotational Velocity

    // Closest Enemy Distance
    // Closest Enemy Relative Angle
    // Closest Wall Distance
    // Closest Wall Relative Angle
    // Closest Friend Distance
    // Closest Friend Relative Angle
    // Closest Bullet Distance
    // Closest Bullet Relative Angle
    // Closest Bullet Angle of Movement

    // My Prev Angle
    // My Prev xVelocity
    // My Prev yVelocity
    // My Prev Rotational Velocity
    // Prev Closest Enemy Distance
    // Prev Closest Enemy Relative Angle
    // Prev Closest Wall Distance
    // Prev Closest Wall Relative Angle
    // Prev Closest Friend Distance
    // Prev Closest Friend Relative Angle

    // Sight Ray - Closest Thing
    // Sight Ray - ID of closest Thing

    val myInfo: List<Double>
        get() {
            val gunPosition = Vector2(
                (position.x + 27 * cos(angle + 12 * PI / 180)),
                (position.y + 27 * sin(angle + 12 * PI / 180))
            )
            val raysult = SightRay(this, gunPosition, angle, 4.0, 150).cast()

            val relativeVelocity = velocity * Vector2.fromPolar(1.0, -angle)
            return listOf(
                fireCooldownRemaining.toDouble(),
                angle,
                dizziness,
                relativeVelocity.x, //velocity.abs,
                relativeVelocity.y, //(velocity.angle) % (2 * PI),
                rotationalVelocity,
                raysult.first,
                raysult.second,
            )
        }

    val arenaInfo: List<Double>
        get() {
            val inputs = arrayListOf<Double>()

            fun diffAngle(a: Double, b: Double): Double {
                val diff: Double = abs(a - b) % (2 * PI)
                var adjustedDiff = if (diff > PI) (2 * PI) - diff else diff

                //calculate sign
                val sign = if (a - b >= 0 && a - b <= PI || a - b <= -PI && a - b >= -(2 * PI)) 1 else -1
                adjustedDiff *= sign
                return adjustedDiff
            }

            closestEnemy.let {
                if (it != null) {
                    val ray = SightRay(this, position, (it.closestEdgePoint(position) - position).angle, 4.0, 150)
                    val raysult = ray.cast()
                    inputs.add(diffAngle(angle, ray.angle))
                    inputs.add(raysult.first)
                    inputs.add(raysult.second)
                } else {
                    inputs.add(PI)
                    inputs.add(0.0)
                    inputs.add(0.0)
                }
            }

            closestAlly.let {
                if (it != null) {
                    val ray = SightRay(this, position, (it.closestEdgePoint(position) - position).angle, 4.0, 150)
                    val raysult = ray.cast()
                    inputs.add(diffAngle(angle, ray.angle))
                    inputs.add(raysult.first)
                    inputs.add(raysult.second)
                } else {
                    inputs.add(PI)
                    inputs.add(0.0)
                    inputs.add(0.0)
                }
            }

            closestWall.let {
                if (it != null) {
                    val ray = SightRay(this, position, (it.closestEdgePoint(position) - position).angle, 4.0, 150)
                    val raysult = ray.cast()
                    inputs.add(diffAngle(angle, ray.angle))
                    inputs.add(raysult.first)
                    inputs.add(raysult.second)
                } else {
                    inputs.add(PI)
                    inputs.add(0.0)
                    inputs.add(0.0)
                }
            }

            closestBullet.let {
                if (it != null) {
                    val line = Line(position, it.closestEdgePoint(position) - position)
                    inputs.add(diffAngle(angle, line.angle))
                    inputs.add(line.length)
                } else {
                    inputs.add(0.0)
                    inputs.add(0.0)
                }
            }

            return inputs
        }

    override val neuralInputs: ArrayList<Double>
        get() {
            val inputs = arrayListOf<Double>()
            inputs.addAll(myInfo)
            inputs.addAll(arenaInfo)

            return inputs
        }


    var previousOutputs: List<Double> = listOf()

    override fun processNeuralOutputs(outputs: List<Double>) {
        if (outputs[0] > 0.6) commands.add(Command.MOVE_FORWARD)
        if (outputs[0] < 0.4) commands.add(Command.MOVE_BACK)

        if (outputs[1] > 0.6) commands.add(Command.MOVE_RIGHT)
        if (outputs[1] < 0.4) commands.add(Command.MOVE_LEFT)

        if (outputs[2] > 0.6) commands.add(Command.ROTATE_RIGHT)
        if (outputs[2] < 0.4) commands.add(Command.ROTATE_LEFT)

        if (outputs[3] > 0.5) commands.add(Command.FIRE)
        previousOutputs = outputs
    }

    override fun afterUpdate() {
        val gunPosition = Vector2(
            (position.x + 27 * cos(angle + 12 * PI / 180)),
            (position.y + 27 * sin(angle + 12 * PI / 180))
        )

        val raysult = SightRay(this, gunPosition, angle, 4.0, 150).cast()

        if (raysult.second == 1.0) {
            score += 0.03
        }
    }

    override fun onKill(victim: Dood) {
        score += 20
    }

    override fun onBetrayal(victim: Dood) {
        score -= 5
    }

    override fun onKilled(killer: Dood) {
        score -= 2
    }

    override fun onWin(teamScores: Pair<Int, Int>) {
        score += 10
    }

    override fun afterFireCommand() {
        if (isFiring) {
            val gunPosition = Vector2(
                (position.x + 27 * cos(angle + 12 * PI / 180)),
                (position.y + 27 * sin(angle + 12 * PI / 180))
            )

            val raysult = SightRay(this, gunPosition, angle, 4.0, 150).cast()

            if (raysult.second == 1.0) {
                score += 5
            }
        }
    }

    override fun afterMove() {
        if( dizziness > 50) {
            score--
        }
    }

    override fun recalculateGenomeScore() {
        genome.score = score
    }
}