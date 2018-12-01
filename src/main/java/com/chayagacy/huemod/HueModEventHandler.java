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
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class HueModEventHandler {

	@SubscribeEvent
	public static void onHit(LivingHurtEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			HueLightingEffects.flashRed();
		}
	}
	
	@SubscribeEvent
	public static void onExplosion(ExplosionEvent event) {
		HueLightingEffects.flashRed();
	}


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END || event.side != Side.CLIENT)
            return;
        EntityPlayer player = event.player;
        Vec3d playerPos = player.getPositionVector();
        Vec3d blockPos = new Vec3d(-28.5, 67.5, 47.5);
        Vec3d diff = playerPos.subtract(blockPos);

        float angle = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) + 180F; //Add 180 so it's between 0 and 360
        float rotation = ((player.getRotationYawHead() % 360) + 360) % 360; //First modulus gets -360 to 360, second gets 0 to 360

        //RunicMagic.LOG.info("Angle -> Degrees: {}, Rotation: {}", angle, rotation);

        float relativeAngle = ((rotation - angle) + 180) % 360;

        //RunicMagic.LOG.info("Relative Angle -> {}", relativeAngle);
    }
	
	
	@SubscribeEvent
	public static void onTicketEvent(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		if(player.getEntityWorld().getTotalWorldTime() % 5 == 0){
			Iterable<BlockPos> blocks = BlockPos.getAllInBox(player.getPosition().add(-5,-5,-5), player.getPosition().add(5,5,5)); //getEntityBoundingBox().expand(15, 15, 15));

			//player.getHorizontalFacing()
			
			for (BlockPos block : blocks) {
				IBlockState state = player.getEntityWorld().getBlockState(block);
				if (state.getBlock() instanceof BlockFire){
					Vec3d playerPos = player.getPositionVector();
			        Vec3d blockPos = new Vec3d(block);
					Vec3d diff = playerPos.subtract(blockPos);
					
			        float angle = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) + 180F; //Add 180 so it's between 0 and 360
			        float rotation = ((player.getRotationYawHead() % 360) + 360) % 360; //First modulus gets -360 to 360, second gets 0 to 360
			        float relativeAngle = ((rotation - angle) + 180) % 360;
			        
			        
//					int fireAngle = (int) Math.toDegrees(Math.atan2(player.posZ -block.getZ() ,player.posX -  block.getX())); // between 180 and -180
//					int playerAngle = (int) Math.abs(player.rotationYawHead %360);
					System.out.println(relativeAngle);
					
					HueLightingEffects.flashRed();
				}
			}
			
			
			//////////////////UNDERWATER
			
			Biome biome = player.getEntityWorld().getBiomeForCoordsBody(player.getPosition());
			if(biome.equals("WATER")){
				System.out.println("WATER");
			}
			
		}
	}

}
