package com.free.cyf;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.free.cyf.Renders.EnableVertexRenderer;
import com.free.cyf.Renders.RectangleRenderer;
import com.free.cyf.Renders.SimpleRenderer;
import com.free.cyf.Renders.UniformRenderer;
import com.free.cyf.Renders.VertexPointerRenderer;
import com.free.cyf.cookbook.ScenebasicUniform;

class MyGLSurfaceView extends GLSurfaceView {

    private final GLSurfaceView.Renderer mRenderer;

    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new ScenebasicUniform();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        // Set the RenderMode
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
