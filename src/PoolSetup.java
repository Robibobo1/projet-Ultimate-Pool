import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle;
import ch.hevs.gdx2d.components.physics.primitives.PhysicsStaticLine;
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.desktop.physics.DebugRenderer;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.physics.PhysicsWorld;

public class PoolSetup extends PortableApplication{
	
	

	PoolSetup(){
		
		
		
		
		PhysicsStaticLine ligne = new PhysicsStaticLine("boite1", new Vector2(100,100), new Vector2(500,100));
		PhysicsStaticLine ligne2 = new PhysicsStaticLine("boite2", new Vector2(500,100), new Vector2(500,400));
		PhysicsStaticLine ligne3 = new PhysicsStaticLine("boite3", new Vector2(500,400), new Vector2(100,400));
		PhysicsStaticLine ligne4 = new PhysicsStaticLine("boite4", new Vector2(100,400), new Vector2(100,100));
		
		ballPlacer(5);
		
	}
	
	void ballPlacer(int rowLeft) {
		for (int i = 0; i < rowLeft; i++) {
			new PhysicsCircle(null, new Vector2(320+(24*rowLeft), 200+12*(5-rowLeft)+24*i), 12);
		}
		if (rowLeft>1) {
			ballPlacer(rowLeft-1);
		}

	}

	@Override
	public void onInit() {
		
		
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		// TODO Auto-generated method stub
		
	}
	
}
