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

	public Window(int wIDTH, int hEIGHT, String title) {
		WIDTH = wIDTH;
		HEIGHT = hEIGHT;
		this.title = title;
		try {
			Display.setDisplayMode(new DisplayMode(this.WIDTH, this.HEIGHT));
			Display.setTitle(this.title);
			Display.create();
			glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
			glDisable(GL_LIGHTING);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.out.println("Display could not be initialised");
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 0, 1);

		/*
		GL11.glMatrixMode(GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(1, Display.getWidth()/Display.getHeight(), -100, 100);
		GL11.glMatrixMode(GL_MODELVIEW);
		GL11.glLoadIdentity();
		*/
		
		//perspectiveGL();

		glTranslatef(0.0f, 0.0f, 0.0f);

		glClearDepth(100.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_SMOOTH);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

	}


	public void beginRender() {
		GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		GL11.glBegin(GL_TRIANGLES);
		GL11.glClear(GL_COLOR_BUFFER_BIT);

	}

	public void beginLineRender() {
		GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		GL11.glBegin(GL_TRIANGLES);
		GL11.glClear(GL_COLOR_BUFFER_BIT);

	}

	public void clear() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void addVertex(Vector3f vertex) {
		GL11.glVertex3f(vertex.x, vertex.y, vertex.z);

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