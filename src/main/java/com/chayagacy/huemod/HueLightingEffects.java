package com.chayagacy.huemod;

import com.lighting.huestream.Area;
import com.lighting.huestream.AreaEffect;
import com.lighting.huestream.ConstantAnimation;
import com.lighting.huestream.CurveAnimation;
import com.lighting.huestream.Point;
import com.lighting.huestream.PointVector;

public class HueLightingEffects {
	
	public static void StartUpLights() {

	    //Create an animation which is fixed 0
	    ConstantAnimation fixedZero = new ConstantAnimation(0.0);

	    //Create an animation which is fixed 1
	    ConstantAnimation fixedOne = new ConstantAnimation(1.0);

	    //Create an animation which repeats a 1 second sawTooth 5 times
	    PointVector pointList = new PointVector();
	    pointList.add(new Point(   0, 0));
	    pointList.add(new Point(500, 1.0));
	    pointList.add(new Point(1000, 0.0));
	    double repeatTimes = 3;
	    CurveAnimation sawTooth = new CurveAnimation(repeatTimes, pointList);
	        
	    //Create an effect on the left half of the room where blue is animated by sawTooth
	    AreaEffect effect = new AreaEffect("", 0);
	    effect.AddArea(Area.getAll());
	    effect.SetColorAnimation(sawTooth, sawTooth, sawTooth); //r=0, g=0, b=sawTooth
	        
	    //Now play both effects
	    Main.getHueStream().LockMixer();
	    Main.getHueStream().AddEffect(effect);
	    effect.Enable();
	    Main.getHueStream().UnlockMixer();
	}

	public static void flashRed() {

		// Create an animation which is fixed 0
		ConstantAnimation fixedZero = new ConstantAnimation(0.0);

		// Create an animation which repeats a 1 second sawTooth 5 times
		PointVector pointList = new PointVector();
		pointList.add(new Point(0, 0));
		pointList.add(new Point(500, 1.0));
		pointList.add(new Point(1000, 0.0));
		double repeatTimes = 0;
		CurveAnimation sawTooth = new CurveAnimation(repeatTimes, pointList);

		// Create an effect on the left half of the room where blue is animated
		// by sawTooth
		AreaEffect effect = new AreaEffect("", 0);
		effect.AddArea(Area.getAll());
		effect.SetColorAnimation(sawTooth, fixedZero, fixedZero); // r=0, g=0,
																	// b=sawTooth

		// Now play both effects
		Main.getHueStream().LockMixer();
		Main.getHueStream().AddEffect(effect);
		effect.Enable();
		Main.getHueStream().UnlockMixer();
	}
}
