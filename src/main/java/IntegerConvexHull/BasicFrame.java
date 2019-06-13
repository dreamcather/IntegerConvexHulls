package IntegerConvexHull;

import java.awt.DisplayMode;
import java.io.*;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;

public class BasicFrame implements GLEventListener {

    public static DisplayMode dm, dm_old;
    private GLU glu = new GLU();
    private float rquad = 0.0f;
    private FaceCase[] faceCases;
    private FaceCase[] faceCases2;

    private void showFace(FaceCase faceCase, GL2 gl) {

        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3d(faceCase.x_1, faceCase.y_1, faceCase.z_1);
        gl.glVertex3d(faceCase.x_2, faceCase.y_2, faceCase.z_2);
        gl.glVertex3d(faceCase.x_3, faceCase.y_3, faceCase.z_3);
        gl.glEnd();
    }


    @Override
    public void display(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(-0.5f, 0f, -5.0f);

        // Rotate The Cube On X, Y & Z
        gl.glRotatef(rquad, 1.0f, 0.5f, 0.75f);

        for (int i = 0; i < faceCases.length; i++) {
            gl.glBegin(GL2.GL_LINE_LOOP);
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glVertex3d(faceCases[i].x_1, faceCases[i].y_1, faceCases[i].z_1);
            gl.glVertex3d(faceCases[i].x_2, faceCases[i].y_2, faceCases[i].z_2);
            gl.glVertex3d(faceCases[i].x_3, faceCases[i].y_3, faceCases[i].z_3);
            gl.glEnd();
        }


        for (int i = 0; i < faceCases2.length; i++) {
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glColor3f(0.0f, 0.1f, 1.0f);
            gl.glVertex3d(faceCases2[i].x_1, faceCases2[i].y_1, faceCases2[i].z_1);
            gl.glVertex3d(faceCases2[i].x_2, faceCases2[i].y_2, faceCases2[i].z_2);
            gl.glVertex3d(faceCases2[i].x_3, faceCases2[i].y_3, faceCases2[i].z_3);
            gl.glEnd();
        }

        for (int i = 0; i < faceCases2.length; i++) {
            gl.glLineWidth(3);
            gl.glBegin(GL2.GL_LINE_LOOP);
            gl.glColor3f(0.1f, 1.0f, 0.1f);
            gl.glVertex3d(faceCases2[i].x_1, faceCases2[i].y_1, faceCases2[i].z_1);
            gl.glVertex3d(faceCases2[i].x_2, faceCases2[i].y_2, faceCases2[i].z_2);
            gl.glVertex3d(faceCases2[i].x_3, faceCases2[i].y_3, faceCases2[i].z_3);
            gl.glEnd();
        }

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++)
                for (int k = 0; k <= 2; k++) {
                    gl.glPointSize(10);
                    gl.glBegin(GL2.GL_POINTS);
                    gl.glColor3f(1.0f, 0.0f, 0.0f);
                    gl.glVertex3i(i, j, k);
                    gl.glEnd();
                }
        }

        gl.glFlush();


        rquad -= 1.95f;

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(1f, 1f, 1f, 0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        // TODO Auto-generated method stub
        final GL2 gl = drawable.getGL().getGL2();
        if (height == 0)
            height = 1;

        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(40.0f, h, 1.0, 10.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private FaceCase readFace(BufferedReader br) throws IOException {

        br.readLine();
        String first = br.readLine();
        String second = br.readLine();
        String third = br.readLine();
        String[] s_1 = first.split(" ");
        String[] s_2 = second.split(" ");
        String[] s_3 = third.split(" ");

        double x_1 = Double.valueOf(s_1[0]);
        double y_1 = Double.valueOf(s_1[1]);
        double z_1 = Double.valueOf(s_1[2]);

        double x_2 = Double.valueOf(s_2[0]);
        double y_2 = Double.valueOf(s_2[1]);
        double z_2 = Double.valueOf(s_2[2]);

        double x_3 = Double.valueOf(s_3[0]);
        double y_3 = Double.valueOf(s_3[1]);
        double z_3 = Double.valueOf(s_3[2]);

        return new FaceCase(x_1, y_1, z_1, x_2, y_2, z_2, x_3, y_3, z_3);


    }

    private void readFaces(String path) {
        try {
            FileInputStream fstream = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            int count = Integer.valueOf(br.readLine());
            faceCases = new FaceCase[count];

            for (int i = 0; i < count; i++) {
                faceCases[i] = readFace(br);
            }

        } catch (IOException e) {
            System.out.println("Ошибка");
        }
    }

    private void readFaces2(String path) {
        try {
            FileInputStream fstream = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            int count = Integer.valueOf(br.readLine());
            faceCases2 = new FaceCase[count];

            for (int i = 0; i < count; i++) {
                faceCases2[i] = readFace(br);
            }

        } catch (IOException e) {
            System.out.println("Ошибка");
        }
    }

    public static void show(String path, String path2) {

        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas
        GLCanvas glcanvas = new GLCanvas(capabilities);


        BasicFrame cube = new BasicFrame();
        cube.readFaces(path);
        cube.readFaces2(path2);

        glcanvas.addGLEventListener(cube);
        glcanvas.setSize(400, 400);

        final JFrame frame = new JFrame(" Convex Hull");
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
        final FPSAnimator animator = new FPSAnimator(glcanvas, 300, true);


        animator.start();
    }

    private class FaceCase {
        final double x_1;
        final double y_1;
        final double z_1;

        final double x_2;
        final double y_2;
        final double z_2;

        final double x_3;
        final double y_3;
        final double z_3;

        private FaceCase(double x_1, double y_1, double z_1, double x_2, double y_2, double z_2, double x_3, double y_3, double z_3) {
            this.x_1 = x_1;
            this.y_1 = y_1;
            this.z_1 = z_1;
            this.x_2 = x_2;
            this.y_2 = y_2;
            this.z_2 = z_2;
            this.x_3 = x_3;
            this.y_3 = y_3;
            this.z_3 = z_3;
        }
    }
}