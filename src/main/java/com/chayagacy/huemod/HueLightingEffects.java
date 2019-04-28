package com.chayagacy.huemod;

import com.lighting.huestream.Area;
import com.lighting.huestream.AreaEffect;
import com.lighting.huestream.Color;
import com.lighting.huestream.ConstantAnimation;
import com.lighting.huestream.CurveAnimation;
import com.lighting.huestream.ExplosionEffect;
import com.lighting.huestream.Location;
import com.lighting.huestream.Point;
import com.lighting.huestream.PointVector;
import com.lighting.huestream.RandomAnimation;
import com.lighting.huestream.TweenType;

public class HueLightingEffects {
	
	/**
	 * Stop any animation and take it back to the dim lights
	 * @param element area which you want to stop 
	 * @param colour colour of last effect
	 */
	public static void stop(AreaEffect element, String colour){
		
		int  r =  Integer.valueOf( colour.substring( 0, 2 ), 16 );
		int  g =  Integer.valueOf( colour.substring( 2, 4 ), 16 );
		int  b =  Integer.valueOf( colour.substring( 4, 6 ), 16 );

		CurveAnimation sawToothR;
		CurveAnimation sawToothG;
		CurveAnimation sawToothB;

		PointVector pointListR = new PointVector();
		pointListR.add(new Point(0, r/255.0));
		pointListR.add(new Point(1000, 0.1));
		sawToothR = new CurveAnimation(0, pointListR);
		PointVector pointListG = new PointVector();
		pointListG.add(new Point(0, g/255.0));
		pointListG.add(new Point(1000, 0.1));
		sawToothG = new CurveAnimation(0, pointListG);
		PointVector pointListB = new PointVector();
		pointListB.add(new Point(0, b/255.0));
		pointListB.add(new Point(1000, 0.1));
		sawToothB = new CurveAnimation(0, pointListB);

		AreaEffect effect = new AreaEffect("AllLights", 2);
		effect.AddArea(Area.getAll());
		effect.SetColorAnimation(sawToothR, sawToothG, sawToothB);
	
		Main.getHueStream().LockMixer();
		Main.getHueStream().AddEffect(effect);
		effect.Enable();
		Main.getHueStream().UnlockMixer();
		
		element.Disable();
	}
	
	/**
	 * flash affact that will flash for given amount of time and given amount of times
	 * @param hexColour - colour of flash
	 * @param noOfFlashes - number of flashes
	 * @param timeOFflash - length of flash
	 * @param area - area of the flash
	 */
	public static void flash(String hexColour, int noOfFlashes, int timeOFflash, String area) {
		int  r =  Integer.valueOf( hexColour.substring( 0, 2 ), 16 );
		int  g =  Integer.valueOf( hexColour.substring( 2, 4 ), 16 );
		int  b =  Integer.valueOf( hexColour.substring( 4, 6 ), 16 );

		CurveAnimation sawTooth;
		CurveAnimation sawToothR;
		CurveAnimation sawToothG;
		CurveAnimation sawToothB;

		PointVector pointListR = new PointVector();
		pointListR.add(new Point(0, 0.1));
		pointListR.add(new Point(timeOFflash/2, r/255.0));
		pointListR.add(new Point(timeOFflash, 0.2));
		sawToothR = new CurveAnimation(noOfFlashes-1, pointListR);
		PointVector pointListG = new PointVector();
		pointListG.add(new Point(0, 0.1));
		pointListG.add(new Point(timeOFflash/2, g/255.0));
		pointListG.add(new Point(timeOFflash, 0.2));
		sawToothG = new CurveAnimation(noOfFlashes-1, pointListG);
		PointVector pointListB = new PointVector();
		pointListB.add(new Point(0, 0.1));
		pointListB.add(new Point(timeOFflash/2, b/255.0));
		pointListB.add(new Point(timeOFflash, 0.2));
		sawToothB = new CurveAnimation(noOfFlashes-1, pointListB);

		AreaEffect effect = new AreaEffect("AllLights", 2);
		if(area.equals("all")){
			effect.AddArea(Area.getAll());
		} else if (area.equals("front")){
			effect.AddArea(Area.getFrontCenter());
		} else if (area.equals("left")){
			effect.AddArea(Area.getLeftHalf());
		} else if (area.equals("right")){
			effect.AddArea(Area.getRightHalf());
		} else if (area.equals("back")){
			effect.AddArea(Area.getBack());
		} else if (area.equals("leftright")){
			effect.AddArea(Area.getFrontLeftQuarter());
			effect.AddArea(Area.getFrontRightQuarter());
		}
		effect.SetColorAnimation(sawToothR, sawToothG, sawToothB);
	
		Main.getHueStream().LockMixer();
		Main.getHueStream().AddEffect(effect);
		effect.Enable();
		Main.getHueStream().UnlockMixer();
	}
	
	/*
	 * Effect that imitates an explosion
	 */
	public static void explosion(){
		//Create effect from predefined explosionEffect
	    ExplosionEffect explosion = new ExplosionEffect("explosion", 2);
	    Color explosionColorRGB = new Color(1.0, 1.0, 1.0);
	    Location explosionLocationXY = new Location(0, 0);
	    double radius = 2.0;
	    double duration_ms = 2000;
	    double expAlpha_ms = 50;
	    double expRadius_ms = 100;
	    explosion.PrepareEffect(explosionColorRGB, explosionLocationXY, duration_ms, radius, expAlpha_ms, expRadius_ms);

	    //Now play both effects
	    Main.getHueStream().LockMixer();
	    Main.getHueStream().AddEffect(explosion);
	    explosion.Enable();
	    Main.getHueStream().UnlockMixer();
	}

	/*
	 * Effect that randomly changes shade and brightness based on inputs
	 * 
	 * @param effect - area of the effect
	 * @param colour - colour of the flicker
	 * @param angle - angle from player (if lights were directional) -999 if no angle
	 * @param strengthLow - lowest brightness
	 * @param strengthHigh - highest brightness
	 * @param transitionLow - slowest transition
	 * @param transitionHigh - quickest transition
	 */
	public static void flickerAnimation(AreaEffect effect, String colour, int angle, double strengthLow, double strengthHigh, int transitionLow, int transitionHigh) {	    		
		int  r =  Integer.valueOf( colour.substring( 0, 2 ), 16 ); // turns hex colour into rgb components
		int  g =  Integer.valueOf( colour.substring( 2, 4 ), 16 );
		int  b =  Integer.valueOf( colour.substring( 4, 6 ), 16 );
		
		//FADEIN
		CurveAnimation sawTooth;
		CurveAnimation sawToothR;
		CurveAnimation sawToothG;
		CurveAnimation sawToothB;

		PointVector pointListR = new PointVector(); 
		pointListR.add(new Point(0, 0.1));
		pointListR.add(new Point(1000, (r/255.0)*0.5));
		sawToothR = new CurveAnimation(0, pointListR);
		PointVector pointListG = new PointVector();
		pointListG.add(new Point(0, 0.1));
		pointListG.add(new Point(1000, (g/255.0)*0.5));
		sawToothG = new CurveAnimation(0, pointListG);
		PointVector pointListB = new PointVector();
		pointListB.add(new Point(0, 0.1));
		pointListB.add(new Point(1000, (b/255.0)*0.5));
		sawToothB = new CurveAnimation(0, pointListB);
		
		AreaEffect effectFade = new AreaEffect("fadein", 2);
		
		effectFade.AddArea(Area.getAll());
		
		effectFade.SetColorAnimation(sawToothR, sawToothG, sawToothB);
	
		Main.getHueStream().LockMixer();
		Main.getHueStream().AddEffect(effectFade);
		effectFade.Enable();
		Main.getHueStream().UnlockMixer();
		
		//////////////////////////////////////
		
		if (angle == -999){
			effect.AddArea(Area.getAll());
		} else{
			System.out.println("Select Area");
			effect.AddArea(Area.getAll());
//		    if(angle > 22.5 && angle <= 67.5){ // To be used for making lights directional
//		    	effect.AddArea(Area.getFrontRightQuarter());
//		    	effect.AddArea(Area.getFrontCenter());
//		    } else if(angle > 67.5 && angle <= 112.5){
//		    	effect.AddArea(Area.getFrontHalf());
//		    } else if(angle > 112.5 && angle <= 157.5){
//		    	effect.AddArea(Area.getFrontLeftQuarter());
//		    	effect.AddArea(Area.getFrontCenter());
//		    } else if(angle > 157.5 && angle <= 202.5){
//		    	effect.AddArea(Area.getLeft());
//		    } else if(angle > 202.5 && angle <= 247.5){
//		    	effect.AddArea(Area.getBackHalfLeft());
//		    } else if(angle > 247.5 && angle <= 292.5){
//		    	effect.AddArea(Area.getBackHalf());
//		    } else if(angle > 292.5 && angle <= 337.5){
//		    	effect.AddArea(Area.getBackHalfRight());
//		    } else if(angle > 337.5 || angle <= 22.5){
//		    	effect.AddArea(Area.getRight());
//		    }
		}
		
	    RandomAnimation redVariation;
	    RandomAnimation greenVariation;
	    RandomAnimation blueVariation;
	    double minValue=0;
	    double maxValue=0;
	    double minTransitionMs=0;
	    double maxTransitionMs=0;
	    redVariation = new RandomAnimation((r/255.0), (r/255.0), 200, 400, TweenType.EaseInOutQuad);
		greenVariation = new RandomAnimation((g/255.0), (g/255.0), 200, 400, TweenType.EaseInOutQuad);
	    blueVariation = new RandomAnimation((b/255.0), (b/255.0), 200, 400, TweenType.EaseInOutQuad);

		effect.SetColorAnimation(redVariation, greenVariation, blueVariation);

	    minValue = strengthLow;
	    maxValue = strengthHigh;
	    minTransitionMs = transitionLow;
	    maxTransitionMs = transitionHigh;
	   
	    
	    RandomAnimation i = new RandomAnimation(minValue, maxValue, minTransitionMs, maxTransitionMs, TweenType.EaseInOutQuad);
	    effect.SetIntensityAnimation(i);
	    
	    Main.getHueStream().LockMixer();
	    Main.getHueStream().AddEffect(effect);
	    effect.Enable();
	    Main.getHueStream().UnlockMixer();
	}
}
