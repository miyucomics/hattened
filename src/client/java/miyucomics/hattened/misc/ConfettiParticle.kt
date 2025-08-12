package miyucomics.hattened.misc

import miyucomics.hattened.HattenedMain
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.SimpleParticleType
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler
import net.minecraft.util.math.random.CheckedRandom
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.max

class ConfettiParticle(world: ClientWorld, x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double, sprite: SpriteProvider) : SpriteBillboardParticle(world, x, y, z) {
	private val particleId: Double
	private var pitch = 0f
	private var yaw = 0f
	private var lastPitch = 0f
	private var lastYaw = 0f
	private var deltaPitch = 0f
	private var deltaYaw = 0f
	private var deltaRoll = 0f

	init {
		this.setSprite(sprite)
		this.particleId = random.nextDouble()
		this.velocityX = dx
		this.velocityY = dy
		this.velocityZ = dz

		this.setBoundingBoxSpacing(0.001f, 0.001f)
		this.gravityStrength = 0.2f
		this.velocityMultiplier = 0.9f
		this.maxAge = random.nextInt() * 400 + 300
		this.scale *= 1.25f
	}

	override fun render(consumer: VertexConsumer, camera: Camera, deltaTick: Float) {
		val rotation = Quaternionf()
			.rotateZ(MathHelper.lerp(deltaTick, this.lastAngle, this.angle))
			.rotateY(MathHelper.lerp(deltaTick, this.lastYaw, this.yaw))
			.rotateX(MathHelper.lerp(deltaTick, this.lastPitch, this.pitch))

		val vertices = arrayOf(
			Vector3f(-this.scale, -this.scale, 0f),
			Vector3f(-this.scale, this.scale, 0f),
			Vector3f(this.scale, this.scale, 0f),
			Vector3f(this.scale, -this.scale, 0f)
		).map { vertex ->
			vertex.rotate(rotation).add(
				(MathHelper.lerp(deltaTick, this.lastX.toFloat(), this.x.toFloat()) - camera.pos.x.toFloat()),
				(MathHelper.lerp(deltaTick, this.lastY.toFloat(), this.y.toFloat()) - camera.pos.y.toFloat()),
				(MathHelper.lerp(deltaTick, this.lastZ.toFloat(), this.z.toFloat()) - camera.pos.z.toFloat())
			)
		}

		val light = this.getBrightness(deltaTick)
		fun vertex(vertex: Vector3f, u: Float, v: Float) = consumer.vertex(vertex.x(), vertex.y(), vertex.z()).texture(u, v).color(this.red, this.green, this.blue, this.alpha).light(light)

		vertex(vertices[0], this.maxU, this.maxV)
		vertex(vertices[1], this.maxU, this.minV)
		vertex(vertices[2], this.minU, this.minV)
		vertex(vertices[3], this.minU, this.maxV)

		vertex(vertices[3], this.minU, this.maxV)
		vertex(vertices[2], this.minU, this.minV)
		vertex(vertices[1], this.maxU, this.minV)
		vertex(vertices[0], this.maxU, this.maxV)
	}

	override fun tick() {
		this.velocityX += X_NOISE.sample(particleId, age.toDouble(), false) / 100
		this.velocityZ += Z_NOISE.sample(particleId, age.toDouble(), false) / 100

		this.lastYaw = this.yaw
		this.lastPitch = this.pitch
		this.lastAngle = this.angle

		if (onGround || (this.x == this.lastX && this.y == this.lastY && this.z == this.lastZ && this.age != 0)) {
			this.age = max(this.age, this.maxAge - 5)
			this.alpha = (this.maxAge - this.age) / 5f
		} else {
			this.deltaYaw += (YAW_NOISE.sample(particleId, age.toDouble(), false)).toFloat() / 10f
			this.deltaRoll += (ROLL_NOISE.sample(particleId, age.toDouble(), false)).toFloat() / 10f
			this.deltaPitch += (PITCH_NOISE.sample(particleId, age.toDouble(), false)).toFloat() / 10f
			this.yaw += this.deltaYaw
			this.pitch += this.deltaPitch
			this.angle += this.deltaRoll
		}

		this.deltaYaw *= 0.98f
		this.deltaRoll *= 0.98f
		this.deltaPitch *= 0.98f

		super.tick()
	}

	override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
	class Factory(private val sprite: SpriteProvider) : ParticleFactory<SimpleParticleType> {
		override fun createParticle(typeIn: SimpleParticleType, world: ClientWorld, x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double): Particle {
			return ConfettiParticle(world, x, y, z, dx, dy, dz, sprite).also {
				it.yaw = HattenedMain.RANDOM.nextFloat(MathHelper.TAU)
				it.pitch = HattenedMain.RANDOM.nextFloat(MathHelper.TAU)
				it.angle = HattenedMain.RANDOM.nextFloat(MathHelper.TAU)
			}
		}
	}

	companion object {
		private val X_NOISE = noise(58637214)
		private val Z_NOISE = noise(823917)
		private val YAW_NOISE = noise(28943157)
		private val ROLL_NOISE = noise(80085)
		private val PITCH_NOISE = noise(49715286)
		private fun noise(seed: Int) =
			OctaveSimplexNoiseSampler(CheckedRandom(seed.toLong()), listOf(-7, -2, -1, 0, 1, 2))
	}
}