package com.chayagacy.huemod;

import com.chayagacy.huemod.proxy.ClientProxy;
import com.chayagacy.huemod.proxy.CommonProxy;
import com.chayagacy.huemod.util.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.IOException;
import com.lighting.huestream.Config;
import com.lighting.huestream.ConstantAnimation;
import com.lighting.huestream.CurveAnimation;
import com.lighting.huestream.ExplosionEffect;
import com.lighting.huestream.PersistenceEncryptionKey;
import com.lighting.huestream.Point;
import com.lighting.huestream.PointVector;
import com.lighting.huestream.SequenceAnimation;
import com.lighting.huestream.SequenceEffect;
import com.lighting.huestream.TweenAnimation;
import com.lighting.huestream.Animation;
import com.lighting.huestream.Area;
import com.lighting.huestream.AreaEffect;
import com.lighting.huestream.Bridge;
import com.lighting.huestream.BridgeStatus;
import com.lighting.huestream.Color;
import com.lighting.huestream.FeedbackMessage;
import com.lighting.huestream.FeedbackMessage.FeedbackType;
import com.lighting.huestream.HueStream;
import com.lighting.huestream.IFeedbackMessageHandler;
import com.lighting.huestream.LightSourceEffect;
import com.lighting.huestream.Location;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.ACCEPTED_VERSIONS)
public class Main {
	
	static {
		System.loadLibrary("huestream_java_native");
	}
	
	public static HueStream hueStream; 

	@Instance
	public static Main instance; 
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event){}
	
	@EventHandler
	public static void init(FMLInitializationEvent event){}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event){
		//MinecraftForge.EVENT_BUS.register(new HueModEventHandler());
		
		///////////////// HUE LIGHTS
		Init();
 		hueStream.ConnectBridgeAsync();
		IfMultipleGroupsSelectFirst();
		HueLightingEffects.flickerAnimation(new AreaEffect("",0), "444444", -999, 0.1, 0.1, 1000, 1000);
 		//hueStream.ShutDown();
	}
	
	public static HueStream getHueStream() {
		return hueStream;
	}
	
	
	public static void Init() {
		
		Config config = new Config("JavaDummyExample", "PC", new PersistenceEncryptionKey("jfsDn39fqSyd0fvfn"));
		hueStream = new HueStream(config);
		
		FeedBackHandler feedbackHandler = new FeedBackHandler();
		hueStream.RegisterFeedbackHandler(feedbackHandler);
	}
	
	public static void IfMultipleGroupsSelectFirst() {
	    Bridge bridge = hueStream.GetLoadedBridge();
	    if (bridge.GetStatus() == BridgeStatus.BRIDGE_INVALID_GROUP_SELECTED) {
	    	hueStream.SelectGroup(bridge.GetGroups().get(0));
	    }
	}
	
	public static class FeedBackHandler extends IFeedbackMessageHandler {
	    public void NewFeedbackMessage(FeedbackMessage message)
	    {
	        System.out.println(message.GetDebugMessage());
	        if (message.GetMessageType() == FeedbackType.FEEDBACK_TYPE_USER) {
	            System.out.println(message.GetUserMessage());
	        }
	    }
	}
}
 