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
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class HueModEventHandler {

	@SubscribeEvent
	public static void OnHit(LivingHurtEvent event) {
		System.out.println("HURT");
		if (event.getEntity() instanceof EntityPlayer) {
			System.out.println("HURT EntityPlayer");
			HueLightingEffects.flashRed();
		}
	}

	@SubscribeEvent
	public static void closeToFire(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		if(player.getEntityWorld().getTotalWorldTime() % 5 == 0){
			Iterable<BlockPos> blocks = BlockPos.getAllInBox(player.getPosition().add(-5,-5,-5), player.getPosition().add(5,5,5)); //getEntityBoundingBox().expand(15, 15, 15));
			
			for (BlockPos block : blocks) {
				IBlockState state = player.getEntityWorld().getBlockState(block);
				if (state.getBlock() instanceof BlockFire){
					HueLightingEffects.flashRed();
				}
			}
		}
	}

}
