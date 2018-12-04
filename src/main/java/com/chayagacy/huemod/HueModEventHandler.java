package com.chayagacy.huemod;

import com.chayagacy.huemod.util.Reference;
import com.chayagacy.huemod.HueLightingEffects;

import com.lighting.huestream.Area;
import com.lighting.huestream.AreaEffect;
import com.lighting.huestream.ConstantAnimation;
import com.lighting.huestream.CurveAnimation;
import com.lighting.huestream.HueStream;
import com.lighting.huestream.Point;
import com.lighting.huestream.PointVector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class HueModEventHandler {

	@SubscribeEvent
	public static void onHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			if (player.isPotionActive(MobEffects.POISON)) {
				HueLightingEffects.flashGreen();
			} else if (player.isPotionActive(MobEffects.WITHER)) {
				// Purple

			} else // if (event.getSource().getImmediateSource() instanceof
					// EntityPlayer) {
				HueLightingEffects.flashRed();
			// }
		}
	}

	@SubscribeEvent
	public static void onDie(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			HueLightingEffects.flashRed();
		}
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent event) {
		HueLightingEffects.flashRed();
	}

	@SubscribeEvent
	public static void onTicketEvent(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END || event.side != Side.CLIENT)
			return;
		EntityPlayer player = event.player;
		if (player.getEntityWorld().getWorldTime() % 5 == 0) {
			Iterable<BlockPos> blocks = BlockPos.getAllInBox(player.getPosition().add(-5, -5, -5),
					player.getPosition().add(5, 5, 5)); // getEntityBoundingBox().expand(15,
														// 15, 15));

			// player.getHorizontalFacing()

			for (BlockPos block : blocks) {
				IBlockState state = player.getEntityWorld().getBlockState(block);
				if (state.getBlock() instanceof BlockFire) {
					Vec3d playerPos = player.getPositionVector();
					Vec3d blockPos = new Vec3d(block);
					Vec3d diff = playerPos.subtract(blockPos);

					float angle = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) + 180F; // Add
																								// 180
																								// so
																								// it's
																								// between
																								// 0
																								// and
																								// 360
					float rotation = ((player.getRotationYawHead() % 360) + 360) % 360; // First modulus gets -360 to 360, second gets 0 to 360
					float relativeAngle = ((rotation - angle) + 180) % 360;

					System.out.println(relativeAngle);

					HueLightingEffects.flashRed();
				} else if (state.getBlock() instanceof BlockTorch) { // Add a  bright  constant  light. Should it be direction?????
					Vec3d playerPos = player.getPositionVector();
					Vec3d blockPos = new Vec3d(block);
					Vec3d diff = playerPos.subtract(blockPos);

					float angle = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) + 180F; // Add  180  so  it's  between  0  and 360
					float rotation = ((player.getRotationYawHead() % 360) + 360) % 360; // First modulus gets -360 to 360, second gets 0 to 360
					float relativeAngle = ((rotation - angle) + 180) % 360;

					System.out.println(relativeAngle);

					HueLightingEffects.flashRed();
				}
			}

			////////////////// UNDERWATER
			if (player.getAir() < 300) {
				System.out.println("WATER");
				HueLightingEffects.flashBlue();
			}

		}
	}

	@SubscribeEvent
	public static void onSound(PlaySoundEvent event) {
		if (event.getName().equals("block.portal.trigger")) { // Nether portal
			System.out.println("PORTAL");
		} else if (event.getName().equals("entity.lightning.impact")) { // lightning
			HueLightingEffects.flashBlue();
		}
	}

	@SubscribeEvent
	public static void onBlockBreak(BreakEvent event) {
		if (event.getState().getBlock() instanceof BlockOre) {
			if (event.getState().getBlock() == Blocks.GOLD_ORE) {
				System.out.println("GOLD");
			} else if (event.getState().getBlock() == Blocks.IRON_ORE) {
				System.out.println("IRON");
			} else if (event.getState().getBlock() == Blocks.DIAMOND_ORE) {
				System.out.println("DIAMOND");
			} else if (event.getState().getBlock() == Blocks.COAL_ORE) {
				System.out.println("COAL");
			} else if (event.getState().getBlock() == Blocks.REDSTONE_ORE) {
				System.out.println("REDSTONE");
			} else if (event.getState().getBlock() == Blocks.EMERALD_ORE) {
				System.out.println("EMERALD");
			} else if (event.getState().getBlock() == Blocks.LAPIS_ORE) {
				System.out.println("LAPIS");
			}
		}
	}

	@SubscribeEvent
	public static void onPotionTaken(PotionAddedEvent event) {
		String potionName = event.getPotionEffect().getEffectName();
		int colour = event.getPotionEffect().getPotion().getLiquidColor();
		switch (potionName) {
		case "effect.moveSpeed":
			System.out.println("Swiftness " + colour);
			System.out.println( Integer.toHexString(colour));
			HueLightingEffects.flashBlue();
			break;
		case "effect.moveSlowdown":
			System.out.println("effect.moveSlowdown");
			break;
		case "effect.digSpeed":
			System.out.println("effect.digSpeed");
			break;
		case "effect.digSlowDown":
			System.out.println("effect.digSlowDown");
			break;
		case "effect.damageBoost":
			System.out.println("effect.damageBoost");
			break;
		case "effect.heal":
			System.out.println("effect.heal");
			break;
		case "effect.harm":
			System.out.println("effect.harm");
			break;
		case "effect.jump":
			System.out.println("effect.jump");
			break;
		case "effect.confusion":
			System.out.println("effect.confusion");
			break;
		case "effect.regeneration":
			System.out.println("effect.regeneration");
		case "effect.resistance":
			System.out.println("effect.resistance");
			break;
		case "effect.fireResistance":
			System.out.println("effect.fireResistance");
			HueLightingEffects.flashRed();
			break;
		case "effect.waterBreathing":
			System.out.println("effect.waterBreathing");
			HueLightingEffects.flashGreen();
			break;
		case "effect.invisibility":
			System.out.println("effect.invisibility");
			break;
		case "effect.blindness":
			System.out.println("effect.blindness");
			break;
		case "effect.nightVision":
			System.out.println("effect.nightVision");
			break;
		case "effect.hunger":
			System.out.println("effect.hunger");
			break;
		case "effect.weakness":
			System.out.println("effect.weakness");
			break;
		case "effect.poison":
			System.out.println("effect.poison");
			break;
		case "effect.wither":
			System.out.println("effect.wither");
			break;
		case "effect.healthBoost":
			System.out.println("effect.healthBoost");
			break;
		case "effect.absorption":
			System.out.println("effect.absorption");
			break;
		case "effect.saturation":
			System.out.println("effect.saturation");
			break;
		case "effect.glowing":
			System.out.println("effect.glowing");
			break;
		case "effect.levitation":
			System.out.println("effect.levitation");
			break;
		case "effect.luck":
			System.out.println("effect.luck");
			break;
		case "effect.unluck":
			System.out.println("effect.unluck");
			break;
		}
		// effect.moveSpeed
	}

}
