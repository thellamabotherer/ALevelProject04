package Main;

import org.lwjgl.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Window {

	private int WIDTH;
	private int HEIGHT;
	private String title;
	
	private float zoom = (float) 1; // 1 = max zoom out, 0.1 = 10x size etc
	private float leftX = 0;
	private float lowY = 0;

	public Window(int wIDTH, int hEIGHT, String title) {
		WIDTH = wIDTH;
		HEIGHT = hEIGHT;
		this.title = title;
		try {
			Display.setDisplayMode(new DisplayMode(this.WIDTH, this.HEIGHT));
			Display.setTitle(this.title);
			Display.create();
			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			glDisable(GL_LIGHTING);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.out.println("Display could not be initialised");
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 0, 1);

		glTranslatef(0.0f, 0.0f, 0.0f);

		glClearDepth(100.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_SMOOTH);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

	}
	
	public void zoom (int mouseX, int mouseY, boolean zoomIn) {
		if (zoomIn) {
			if (this.zoom > 0.1) {
				float w1 = Settings.screenWidth * zoom;
				float h1 = Settings.screenHeight * zoom;
				float x = (mouseX*zoom + leftX)/Settings.screenWidth;
				float y = (mouseY*zoom + lowY)/Settings.screenHeight;
				zoom = (float) (zoom - 0.1);
				float w2 = Settings.screenWidth * zoom;
				float h2 = Settings.screenHeight * zoom;
				float dw = w1 - w2;
				float dh = h1 - h2;
				leftX = leftX + x * dw;
				lowY = lowY + y * dh;
				if (leftX < 0) {
					leftX = 0;
				}if (lowY < 0) {
					lowY = 0;
				}if (leftX + zoom * Display.getWidth() > Settings.screenWidth) {
					leftX = Settings.screenWidth - zoom * Display.getWidth();
				}if (lowY + zoom * Display.getHeight() > Settings.screenHeight) {
					lowY = Settings.screenHeight - zoom * Display.getHeight();
				}
			}
		}else {
			if (this.zoom < 1) {
				float w1 = Settings.screenWidth * zoom;
				float h1 = Settings.screenHeight * zoom;
				float x = (mouseX*zoom + leftX)/Settings.screenWidth;
				float y = (mouseY*zoom + lowY)/Settings.screenHeight;
				zoom = (float) (zoom + 0.1);
				float w2 = Settings.screenWidth * zoom;
				float h2 = Settings.screenHeight * zoom;
				float dw = w1 - w2;
				float dh = h1 - h2;
				leftX = leftX + x * dw;
				lowY = lowY + y * dh;
				if (leftX < 0) {
					leftX = 0;
				}if (lowY < 0) {
					lowY = 0;
				}if (leftX + zoom * Display.getWidth() > Settings.screenWidth) {
					leftX = Settings.screenWidth - zoom * Display.getWidth();
				}if (lowY + zoom * Display.getHeight() > Settings.screenHeight) {
					lowY = Settings.screenHeight - zoom * Display.getHeight();
				}
			}
		}
	}


	public void beginRender() {
		GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		GL11.glBegin(GL_TRIANGLES);
	}

	public void beginLineRender() {
		GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		GL11.glBegin(GL_TRIANGLES);
	}

	public void clear() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	    glClear(GL_COLOR_BUFFER_BIT);
	    try {
			Display.swapBuffers();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addVertex(Vector3f vertex) {
		
		float x = (vertex.x - leftX) / zoom;
		float y = (vertex.y - lowY) / zoom;
		GL11.glVertex3f(x, y, 0);

	}

	public void changeColour(Vector4f col) {
		GL11.glColor4f(col.x, col.y, col.z, col.w); // not sure if w is meant to be first or if we use x like we
													// normally do as the first one here
	}

	public void endRender() {
		try {
			glDisable(GL_POLYGON_OFFSET_FILL);
		} catch (Exception e) {
			glDisable(GL_POLYGON_OFFSET_LINE);
		}

		GL11.glEnd();
	}

	public void polyOffsetLine(float a, float b) {
		glPolygonOffset(a, b);
		glEnable(GL_POLYGON_OFFSET_LINE);
	}

	public void polyOffsetFill(float a, float b) {
		glPolygonOffset(a, b);
		glEnable(GL_POLYGON_OFFSET_FILL);
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public void setWIDTH(int wIDTH) {
		WIDTH = wIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public void setHEIGHT(int hEIGHT) {
		HEIGHT = hEIGHT;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}