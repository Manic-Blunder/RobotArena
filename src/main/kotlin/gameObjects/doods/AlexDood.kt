package gameObjects.doods


import brains.neat.Genome
import brains.neat.Node
import fuzzyMath.*
import fuzzyMath.shapes.Line
import fuzzyMath.shapes.Vector2
import gameObjects.core.*
import gameObjects.peripherals.SightRay
import java.awt.Graphics
import kotlin.math.*


class AlexDood(arena: Arena, genome: Genome, position: Vector2) : Dood(arena, position, Team.ALEX, genome) {

    var score = 0.0
        private set(value) {
            field = max(0.0, value)
        }

    var moveTarget = Vector2(0.0,0.0)
    var rotateTarget = 0.0

    val relativeVelocity = velocity * Vector2.fromPolar(1.0, -angle)

    var commandThreshold = 0.0

    var linesToSpecialRender : ArrayList<Line> = arrayListOf()

    override fun specialRender(g: Graphics) {
        val rays = 30
        val max = (0 until rays / 2).sum().toDouble()
        repeat(rays) { i ->
            var blah = (0 until abs(i - rays / 2)).sum().toDouble()
            var piBlah = (max - blah) / max * PI
            if (i - rays / 2 < 0) piBlah *= -1
            var distanceBlah = (15.0 - abs(15.0 - i)) / 15.0 * 100.0

            SightRay(this, position, piBlah + angle - PI, 4.0, distanceBlah.toInt() + 10).draw(g)
        }

        linesToSpecialRender.forEach{g.drawLine(it.x.toInt(), it.y.toInt(), it.p2Absolute.x.toInt(), it.p2Absolute.y.toInt())}
        linesToSpecialRender = arrayListOf()
    }

    override val neuralInputs: ArrayList<Double>
        get() {
            val inputs = arrayListOf(
                //(angle % (2 * PI)) - 1,
                relativeVelocity.x,
                relativeVelocity.y,
                rotationalVelocity,
                moveTarget.x,
                moveTarget.y,
                rotateTarget,
                fireCooldownRemaining.toDouble() / fireCooldown.toDouble(),
            )

            val rays = 20
            val max = (0 until rays / 2).sum().toDouble()
            repeat(rays) { i ->
                var blah = (0 until abs(i - rays / 2)).sum().toDouble()
                var piBlah = (max - blah) / max * PI
                if (i - rays / 2 < 0) piBlah *= -1
                var distanceBlah = (15.0 - abs(15.0 - i)) / 15.0 * 100.0

                val raysult = SightRay(this, position, piBlah + angle - PI, 4.0, distanceBlah.toInt() + 10).cast()
                inputs.add(raysult.first)
                inputs.add(raysult.second)
            }

            if (arena.bullets.count() != 0){
                inputs.add(1.0)
                var closetBullet = arena.bullets[0]
                var closestBulletDistance = (closetBullet.position - position).abs
                arena.bullets.forEach{
                    val distance = (it.position - position).abs
                    if (distance < closestBulletDistance){
                        closetBullet = it
                        closestBulletDistance = distance
                    }
                }
                val relativeBulletPosition = (closetBullet.position - position) * Vector2.fromPolar(1.0, -angle)
                linesToSpecialRender.add(Line(position,relativeBulletPosition * Vector2.fromPolar(1.0, angle)))
                inputs.add(relativeBulletPosition.x)
                inputs.add(relativeBulletPosition.y)
            } else {
                inputs.add(0.0)
                inputs.add(0.0)
                inputs.add(0.0)
            }



            return inputs
        }


    override fun processNeuralOutputs(outputs: List<Double>) {
        val myOutputs = outputs.map{if(abs(it * 2 - 1) < commandThreshold) 0.0 else it * 2 - 1}
        moveTarget = if (myOutputs[0] == 0.0 && myOutputs[1] == 0.0) Vector2.zero() else Vector2(myOutputs[0],myOutputs[1]).unitVector()

        rotateTarget = myOutputs[2] * PI

        if (moveTarget.x > 0.0) commands.add(Command.MOVE_FORWARD)
        if (moveTarget.x < 0.0) commands.add(Command.MOVE_BACK)
        if (moveTarget.y > 0.0) commands.add(Command.MOVE_LEFT)
        if (moveTarget.y < 0.0) commands.add(Command.MOVE_RIGHT)
        if (rotateTarget > 0.0) commands.add(Command.ROTATE_RIGHT)
        if (rotateTarget < 0.0) commands.add(Command.ROTATE_LEFT)

        if (myOutputs[3] > commandThreshold) commands.add(Command.FIRE)

        commandThreshold = myOutputs[4]
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
        moveTarget -= relativeVelocity / dragMultiplier
        rotateTarget -= rotationalVelocity / rotationalDragMultiplier
        if( dizziness > 50) {
            score--
        }
    }

    override fun recalculateGenomeScore() {
        genome.score = score
    }
}