package gameObjects.doods

import brains.neat.Genome
import fuzzyMath.shapes.Vector2
import gameObjects.core.Arena
import gameObjects.core.Dood
import gameObjects.core.Team

class GreenDood(arena: Arena, genome: Genome, position: Vector2) : Dood(arena, position, Team.GREEN, genome) {
    //This list should be the same length as the input nodes count in GreenNeat
    /*
    *  var dizziness = 0.0
        set(value) {
            field = clamp(0.0, value, 10000.0)
        }

    private  val sightAngle = 60 * PI / 180

    var score = 0.0

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
    }

    override val neuralInputs: ArrayList<Double>
        get() {
            val relativeVelocity = velocity * Vector2.fromPolar(1.0, -angle)
            val inputs = arrayListOf(
//                dizziness,
//                angle % (2 * PI),
                relativeVelocity.x,
                relativeVelocity.y,
                rotationalVelocity,
                fireCooldownRemaining.toDouble()
            )

            val rays = 30
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

            return inputs
        }


    override fun processNeuralOutputs(outputs: List<Double>) {
        if (outputs[0] > 0.6) commands.add(Command.MOVE_FORWARD)
        if (outputs[0] < 0.4) commands.add(Command.MOVE_BACK)

        if (outputs[1] > 0.6) commands.add(Command.MOVE_RIGHT)
        if (outputs[1] < 0.4) commands.add(Command.MOVE_LEFT)

        if (outputs[2] > 0.6) commands.add(Command.ROTATE_RIGHT)
        if (outputs[2] < 0.4) commands.add(Command.ROTATE_LEFT)

        if (outputs[3] > 0.5) commands.add(Command.FIRE)
    }

    override fun onKill(victim: Dood) {
        score += 5
    }

    override fun onBetrayal(victim: Dood) {
        score -= 5
    }

    override fun onWin(teamScores: Pair<Int, Int>) {
        score += 10
    }

    override fun recalculateGenomeScore() {
        genome.score = score
    }
    * */



    override val neuralInputs: ArrayList<Double>
        get() = arrayListOf()

    //Output's length will equal output nodes from GreenNeat - the length of the outputs array is = the number i give to the GreenNeat
    override fun processNeuralOutputs(outputs: List<Double>) {

    }

    override fun recalculateGenomeScore() {

    }
}