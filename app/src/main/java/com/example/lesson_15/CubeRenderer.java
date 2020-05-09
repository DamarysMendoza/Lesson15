package com.example.lesson_15;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class CubeRenderer implements GLSurfaceView.Renderer{



    /**
     * Rotation increment per frame.
     */
    public static final float CUBE_ROTATION_INCREMENT = 0.6f;
    //private static final float CUBE_ROTATION_INCREMENT = 0.6f;

    /**
     * The refresh rate, in frames per second.
     */
    private static final int REFRESH_RATE_FPS = 60;

    /**
     * The duration, in milliseconds, of one frame.
     */
    private static final float FRAME_TIME_MILLIS = TimeUnit.SECONDS.toMillis(1) / REFRESH_RATE_FPS;

    private final float[] mMVPMatrix;
    private final float[] mProjectionMatrix;
    private final float[] mViewMatrix;
    private final float[] mRotationMatrix;
    private final float[] mFinalMVPMatrix;

    private Cube mCube;
    public float mCubeRotation;// Pesima práctica
    //private float mCubeRotation; // Práctica recomendara
    private long mLastUpdateMillis;
    private int ContadorFrames;

    public CubeRenderer() {
        Log.d("Purito","Incializacion Cube Renderer");
        ContadorFrames =0;
        mMVPMatrix = new float[16];
        mProjectionMatrix = new float[16];
        mViewMatrix = new float[16];
        mRotationMatrix = new float[16];
        mFinalMVPMatrix = new float[16];

        // Set the fixed camera position (View matrix).
        Matrix.setLookAtM(mViewMatrix, 0, 0.0f, 0.0f, -9.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    @Override
    //public void onSurfaceCreated(EGLConfig config) {
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d("Purito","Creando Superficie");
        // Set the background frame color
        //GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        mCube = new Cube();
    }

    @Override
    //public void onSurfaceChanged(int width, int height) {
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("Purito","Cambiando Superficie");
        float ratio = (float) width / height;

        GLES20.glViewport(0, 0, width, height);
        // This projection matrix is applied to object coordinates in the onDrawFrame() method.
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 12.0f);
        // modelView = projection x view
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }

    @Override
    //public void onDrawFrame() {
    public void onDrawFrame(GL10 gl) {
        Log.d("Purito","Dibujando Frame");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Apply the rotation.
        Matrix.setRotateM(mRotationMatrix, 0, mCubeRotation, 1.0f, 1.0f, 1.0f);
        // Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(mFinalMVPMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // Draw cube.
        mCube.draw(mFinalMVPMatrix);
        updateCubeRotation();

    }

    /**
     * Updates the cube rotation.
     */
    private void updateCubeRotation() {

        if (mLastUpdateMillis != 0) {
            float factor = (SystemClock.elapsedRealtime() - mLastUpdateMillis) / FRAME_TIME_MILLIS;
            mCubeRotation += CUBE_ROTATION_INCREMENT * factor;
            ContadorFrames++;
            Log.d("Purito","Renderizando Frame : "+ContadorFrames);
        }
        mLastUpdateMillis = SystemClock.elapsedRealtime();

        //Log.d("Purito","Lapso de actualizacion: "+mLastUpdateMillis);




    }




}