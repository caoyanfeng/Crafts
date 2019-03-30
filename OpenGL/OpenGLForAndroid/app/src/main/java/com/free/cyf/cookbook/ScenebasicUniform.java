package com.free.cyf.cookbook;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.free.cyf.R;
import com.free.cyf.Renders.ResReadUtils;
import com.free.cyf.Renders.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class ScenebasicUniform implements GLSurfaceView.Renderer {

    private static String TAG = "ScenebasicUniform";

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer colorBuffer;

    private int mProgram;

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;

    private static final int BYTES_PER_FLOAT = 4;

    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    /**
     * 点的坐标
     */
    private float[] vertexPoints = new float[]{
            -0.8f, -0.8f, 0.0f,
            0.8f, -0.8f, 0.0f,
            0.0f,  0.8f, 0.0f
    };

    private float color[] = {
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f
    };

    private final float[] mMatrix = new float[16];


    private int uMatrixLocation;

    private int aPositionLocation;
    private int aColorLocation;

    public ScenebasicUniform() {
        //分配内存空间,每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        //传入指定的坐标数据
        vertexBuffer.put(vertexPoints);
        vertexBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(color.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        //传入指定的数据
        colorBuffer.put(color);
        colorBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        //编译
        final int vertexShaderId = ShaderUtils.compileVertexShader(ResReadUtils.readResource(R.raw.basic_uniform_vert));
        final int fragmentShaderId = ShaderUtils.compileFragmentShader(ResReadUtils.readResource(R.raw.basic_uniform_frag));
        //鏈接程序片段
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId);
        //在OpenGLES环境中使用程序片段
        GLES30.glUseProgram(mProgram);

        uMatrixLocation = GLES30.glGetUniformLocation(mProgram, "RotationMatrix");

        aPositionLocation = GLES30.glGetAttribLocation(mProgram, "VertexPosition");
        aColorLocation = GLES30.glGetAttribLocation(mProgram, "VertexColor");
        //指定了渲染时索引值为 index 的顶点属性数组的数据格式和位置。
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);

        GLES30.glEnableVertexAttribArray(aPositionLocation);


        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 0, colorBuffer);


        GLES30.glEnableVertexAttribArray(aColorLocation);
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);

        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            //横屏
            Matrix.orthoM(mMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            //竖屏
            Matrix.orthoM(mMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }

        Matrix.rotateM(mMatrix,0,60f,1,1,0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);

    }
}

