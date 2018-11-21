package Main;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Data.Matrix2d;

public class GraphicalFunctions {
	
	private static Float[][] arrow1;
	private static Float[][] arrow2;
	private static Float[][] arrow3;
	
	
	public static void setup () {
		arrow1 = new Float[2][3];
		arrow1[0][0] = (float) 1.5;
		arrow1[1][0] = (float) 0;
		arrow1[0][1] = (float) 1;
		arrow1[1][1] = (float) 0.8;
		arrow1[0][2] = (float) 1;
		arrow1[1][2] = (float) -0.8;
		
		arrow2 = new Float[2][3];
		arrow2[0][0] = (float) -1;
		arrow2[1][0] = (float) -0.2;
		arrow2[0][1] = (float) 1;
		arrow2[1][1] = (float) -0.2;
		arrow2[0][2] = (float) -1;
		arrow2[1][2] = (float) 0.2;
		
		arrow3 = new Float[2][3];
		arrow3[0][0] = (float) -1;
		arrow3[1][0] = (float) 0.2;
		arrow3[0][1] = (float) 1;
		arrow3[1][1] = (float) 0.2;
		arrow3[0][2] = (float) 1;
		arrow3[1][2] = (float) -0.2;
	}

	public static void drawArrow(Window window, Vector2f start, Vector2f direction, float scale) {

		if (direction != null) {
			
			double angle = Math.atan (direction.y / direction.x) ;
			Float[][] transform = new Float[2][2];
			transform[0][0] = (Float)(float)(Math.cos(angle));
			transform[0][1] = (Float)(float)(Math.sin(angle));
			transform[1][0] = (Float)(float)(-Math.sin(angle));
			transform[1][1] = (Float)(float)(Math.cos(angle));
			Matrix2d<Float> tMat = new Matrix2d<Float>(transform);
			
			Matrix2d<Float> shape = new Matrix2d<Float>(arrow1);
			shape = shape.mult(tMat);
			Float[][] arr = toF(shape.getMat());
			drawArray(translate(arr, start.x, start.y, scale), window);
			
			shape = new Matrix2d<Float>(arrow2);
			shape = shape.mult(tMat);
			arr = toF(shape.getMat());
			drawArray(translate(arr, start.x, start.y, scale), window);
			
			shape = new Matrix2d<Float>(arrow3);
			shape = shape.mult(tMat);
			arr = toF(shape.getMat());
			drawArray(translate(arr, start.x, start.y, scale), window);
		}

	}private static void drawArray (Object[][] arr, Window window) {
		window.beginRender();
		window.addVertex(new Vector3f ((Float)arr[0][0], (Float)arr[1][0], 0));
		window.addVertex(new Vector3f ((Float)arr[0][1], (Float)arr[1][1], 0));
		window.addVertex(new Vector3f ((Float)arr[0][2], (Float)arr[1][2], 0));
		window.endRender();
	}private static Object[][] translate (Object shape, float x, float y, float scale) {
		Object[][] f = (Object[][])shape;
		f[0][0] = (float)(f[0][0]) * scale + x;
		f[1][0] = (float)(f[1][0]) * scale + y;
		
		f[0][1] = (float)(f[0][1]) * scale + x;
		f[1][1] = (float)(f[1][1]) * scale + y;
		
		f[0][2] = (float)(f[0][2]) * scale + x;
		f[1][2] = (float)(f[1][2]) * scale + y;
		
		return f;
	}private static Float[][] toF (Object[][] nums) {
		Float[][] F = new Float[2][3];
		F[0][0] = (float)nums[0][0];
		F[1][0] = (float)nums[1][0];
		F[0][1] = (float)nums[0][1];
		F[1][1] = (float)nums[1][1];
		F[0][2] = (float)nums[0][2];
		F[1][2] = (float)nums[1][2];
		return F;
	}

}
