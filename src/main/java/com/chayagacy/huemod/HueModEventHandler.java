package com.chayagacy.huemod;

import com.chayagacy.huemod.util.Reference;
import com.google.common.collect.Iterators;
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
import net.minecraft.entity.monster.EntityMob;
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
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
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
				HueLightingEffects.flash("91d200", 1, 1000);
			} else if (player.isPotionActive(MobEffects.WITHER)) {
				HueLightingEffects.flash("AA00AA", 1, 1000);// Purple
			} else
				HueLightingEffects.flash("AA0000", 1, 1000);
		}
	}

	@SubscribeEvent
	public static void onDie(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			HueLightingEffects.flash("AA0000", 1, 1000);
		} else if(event.getEntity() instanceof EntityMob && event.getSource().getTrueSource() instanceof EntityPlayer){
			HueLightingEffects.flash("2b9f33", 1, 750);
		}
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent event) {
		HueLightingEffects.explosion();
	}
	
//	static int level=0;
///	
//	@SubscribeEvent
//	public static void levelUp(PlayerPickupXpEvent  event) {
//		System.out.println("Collect Orb");
//		EntityPlayer player = event.getEntityPlayer();		
//		
//		if(player.xpBarCap() > level) {
//			HueLightingEffects.flash("3F3F15", 1, 1000);
//			System.out.println("Level up!");
//			level = player.experienceLevel;
//		}
//	}
	
	private static boolean fireAnimation = false;
	private static boolean waterAnimation = false;
	private static boolean lavaAnimation = false;
	private static boolean netherAnimation = false;
	private static int distanceFromFire = 7;
	private static AreaEffect element = new AreaEffect("element", 3);
    
	
	@SubscribeEvent
	public static void onTicketEvent(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END || event.side != Side.CLIENT)
			return;
		
		EntityPlayer player = event.player;
		
		if (player.getEntityWorld().getWorldTime() % 5 == 0) {
			
			boolean nearFire = false;
			boolean inWater = false;
			boolean nearLava = false;
			
			////////////////// UNDERWATER
			if (player.getAir() < 300) {
				inWater = true;
				if(!waterAnimation){
					HueLightingEffects.flickerAnimation(element, "0083FF", -999, 0.5, 0.7, 400, 500);
					waterAnimation = true;
					System.out.println("WATER Start");
				}
			} else if (waterAnimation && !inWater){
				element.Disable();
				waterAnimation = false; // highest priority so will change all to off
				lavaAnimation = false;
				fireAnimation = false;
				System.out.println("WATER Stop");
			}
			
			////////////////////// NETHER 
			else if (player.inPortal && !netherAnimation) {
				HueLightingEffects.flickerAnimation(element, "6800FF", -999, 0.3, 0.8, 150, 250);
				netherAnimation = true;
				System.out.println("Portal Start");
			} else if (!player.inPortal && netherAnimation){
				element.Disable();
				netherAnimation = false;
				System.out.println("Portal Stop");
			} else{
				Iterable<BlockPos> blocksFire = BlockPos.getAllInBox(player.getPosition().add(-5, -3, -5),
						player.getPosition().add(5, 3, 5));
				for (BlockPos block : blocksFire) {
					IBlockState state = player.getEntityWorld().getBlockState(block);
					if (state.getBlock() instanceof BlockFire) {
						nearFire = true;
						
						//set variable to true 
						
						Vec3d playerPos = player.getPositionVector();
						Vec3d blockPos = new Vec3d(block);
						Vec3d diff = playerPos.subtract(blockPos);
	
						float angle = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) + 180F; // Add 180 so it's between 0 and 360
						float rotation = ((player.getRotationYawHead() % 360) + 360) % 360; // First modulus gets -360 to 360, second gets 0 to 360
						float relativeAngle = ((rotation - angle) + 180) % 360;			
	
						//HueLightingEffects.flash("FFAA00", 1, 1000);
						//HueLightingEffects.playCandleWithColorVariation("FFB109", (int)relativeAngle, diff.length());
						if(!fireAnimation){
							HueLightingEffects.flickerAnimation(element, "FFB109", (int)relativeAngle, 0.6, 1.0, 200, 350);
							System.out.println("Fire Start");
							fireAnimation = true;
						}
					}
					/////////////// LAVA
					else if (state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA) {
						nearLava = true;
						
						Vec3d playerPos = player.getPositionVector();
						Vec3d blockPos = new Vec3d(block);
						Vec3d diff = playerPos.subtract(blockPos);
	
						float angle = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) + 180F; // Add 180 so it's between 0 and 360
						float rotation = ((player.getRotationYawHead() % 360) + 360) % 360; // First modulus gets -360 to 360, second gets 0 to 360
						float relativeAngle = ((rotation - angle) + 180) % 360;		
						
						if(!lavaAnimation){
							HueLightingEffects.flickerAnimation(element, "301C0B", (int)relativeAngle, 0.1,0.5, 400, 500);
							System.out.println("Lava Start");
							lavaAnimation = true;
						}
					}
				}
				/////////////////// STOP ANIMATION 
				if (!nearFire && fireAnimation){
					System.out.println("Fire Stop");
					fireAnimation = false;
					lavaAnimation = false;
					waterAnimation = false;
					element.Disable();
				} else if (!nearLava && lavaAnimation){
					System.out.println("Lava Stop");
					fireAnimation = false;
					lavaAnimation = false;
					waterAnimation = false;
					element.Disable();
				}
			}			
		}
	}
 
	@SubscribeEvent
	public static void onSound(PlaySoundEvent event) {
		if (event.getName().equals("entity.lightning.impact")) { // lightning
			HueLightingEffects.flash("5555FF", 3, 250);
		}
	}

	@SubscribeEvent
	public static void onBlockBreak(BreakEvent event) {
		System.out.println(event.getState().getBlock().toString());
		if (event.getState().getBlock() == Blocks.GOLD_ORE) {
			HueLightingEffects.flash("FFAA00", 1, 500);
		} else if (event.getState().getBlock() == Blocks.IRON_ORE) {
			HueLightingEffects.flash("9e7c62", 1, 500);
		} else if (event.getState().getBlock() == Blocks.DIAMOND_ORE) {
			HueLightingEffects.flash("55FFFF", 1, 500);
		} else if (event.getState().getBlock() == Blocks.REDSTONE_ORE) {
			HueLightingEffects.flash("c90a00", 1, 500);
		} else if (event.getState().getBlock() == Blocks.COAL_ORE) {
			HueLightingEffects.flash("555555", 1, 500);
		} else if (event.getState().getBlock() == Blocks.EMERALD_ORE) {
			HueLightingEffects.flash("55FF55", 1, 500);
		} else if (event.getState().getBlock() == Blocks.LAPIS_ORE) {
			HueLightingEffects.flash("5555FF", 1, 500);
		}
	}

	@SubscribeEvent
	public static void onPotionTaken(PotionAddedEvent event) {
		if (!event.getEntity().getEntityWorld().isRemote)
			return;

        PotionEffect pOld = event.getOldPotionEffect();
        PotionEffect pNew = event.getPotionEffect();
        if(pOld == null || pOld.getPotion() != pNew.getPotion() || pOld.getAmplifier() != pNew.getAmplifier())
        {
        	Potion potion =pNew.getPotion();
    		int colourInt = potion.getLiquidColor();
    		String colourHex = Integer.toHexString(colourInt);
    		System.out.println(potion.getName() + " " + colourHex);
    		
    		HueLightingEffects.flash(colourHex, 1, 1000);
    		
    		// Reflect the colour of the potion in the lights+
    		
        }
	}

}

////DIFFERENT STRENGTH OF LIGHT DEPENDING ON DISTANCE
//if(diff.length()>=0 && diff.length()<3 && distanceFromFire!=1){
//	AreaEffect fire1 = new AreaEffect("fire",1);
//	HueLightingEffects.playCandleWithColorVariation(fire1, "FFB109", (int)relativeAngle);
//	distanceFromFire = 1;
//	System.out.println("1");
//}else if(diff.length()>=3 && diff.length()<5 && distanceFromFire!=2){
//	AreaEffect fire2 = new AreaEffect("fire",1);
//	HueLightingEffects.playCandleWithColorVariation(fire2, "765300", (int)relativeAngle);
//	distanceFromFire = 2;
//	System.out.println("2");
//} else if(diff.length()>=5 && diff.length()<6 && distanceFromFire!=3){
//	fire = new AreaEffect("fire",1);
//	HueLightingEffects.playCandleWithColorVariation(fire, "211700", (int)relativeAngle);
//	distanceFromFire = 3;
//	System.out.println("3");
//}
