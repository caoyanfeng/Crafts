package com.free.cyf.Renders;


import android.opengl.GLES32;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MyGLRenderer implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES32.glClearColor(0.0f, 0.6f, 0.0f, 1.0f);
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT);
    }


    public void onSurfaceChanged(GL10 unused, int width, int height) {
        //利用glViewport()设置Screen space的大小，在onSurfaceChanged中回调
        GLES32.glViewport(0, 0, width, height);
    }
}
