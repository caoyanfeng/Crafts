package com.free.cyf.Renders;

import android.opengl.GLES30;

import java.nio.IntBuffer;

/**
 * @date: 2018-09-13
 * @description:
 */
public class ShaderUtils {

    private static final String TAG = "ShaderUtils";

    /**
     * 编译顶点着色器
     *
     * @param shaderCode
     * @return
     */
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES30.GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * 编译片段着色器
     *
     * @param shaderCode
     * @return
     */
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES30.GL_FRAGMENT_SHADER, shaderCode);
    }

    /**
     * 编译
     *
     * @param type       顶点着色器:GLES30.GL_VERTEX_SHADER
     *                   片段着色器:GLES30.GL_FRAGMENT_SHADER
     * @param shaderCode
     * @return
     */
    private static int compileShader(int type, String shaderCode) {
        //创建一个着色器
        final int shaderId = GLES30.glCreateShader(type);
        if (shaderId != 0) {
            GLES30.glShaderSource(shaderId, shaderCode);
            GLES30.glCompileShader(shaderId);
            //检测状态
            final int[] compileStatus = new int[1];
            GLES30.glGetShaderiv(shaderId, GLES30.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                String logInfo = GLES30.glGetShaderInfoLog(shaderId);
                System.err.println(logInfo);
                //创建失败
                GLES30.glDeleteShader(shaderId);
                return 0;
            }
            return shaderId;
        } else {
            //创建失败
            return 0;
        }
    }

    /**
     * 链接小程序
     *
     * @param vertexShaderId   顶点着色器
     * @param fragmentShaderId 片段着色器
     * @return
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programId = GLES30.glCreateProgram();
        if (programId != 0) {
            //将顶点着色器加入到程序
            GLES30.glAttachShader(programId, vertexShaderId);
            //将片元着色器加入到程序中
            GLES30.glAttachShader(programId, fragmentShaderId);
            //链接着色器程序
            GLES30.glLinkProgram(programId);
            final int[] linkStatus = new int[1];

            GLES30.glGetProgramiv(programId, GLES30.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                String logInfo = GLES30.glGetProgramInfoLog(programId);
                System.err.println(logInfo);
                GLES30.glDeleteProgram(programId);
                return 0;
            }
            printVertexInputAttr(programId);
            return programId;
        } else {
            //创建失败
            return 0;
        }
    }

    /**
     * 输出所有active attr
     * vPosition,0
     * aColor,1
     */
    private static void printVertexInputAttr(int programId) {
        IntBuffer attrsBuffer = IntBuffer.allocate(1);
        GLES30.glGetProgramiv(programId, GLES30.GL_ACTIVE_ATTRIBUTES, attrsBuffer);
        IntBuffer maxLenBuffer = IntBuffer.allocate(1);
        GLES30.glGetProgramiv(programId, GLES30.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, maxLenBuffer);
        System.out.println(attrsBuffer.get(0) + "," + maxLenBuffer.get(0));
        IntBuffer size = IntBuffer.allocate(1);
        IntBuffer type = IntBuffer.allocate(1);
        for (int i = 0; i < attrsBuffer.get(0); i++) {
            String attrName = GLES30.glGetActiveAttrib(programId, i, size, type);
            int location = GLES30.glGetAttribLocation(programId, attrName);
            System.out.println(attrName + "," + location);
        }
    }

    /**
     * 验证程序片段是否有效
     *
     * @param programObjectId
     * @return
     */
    public static boolean validProgram(int programObjectId) {
        GLES30.glValidateProgram(programObjectId);
        final int[] programStatus = new int[1];
        GLES30.glGetProgramiv(programObjectId, GLES30.GL_VALIDATE_STATUS, programStatus, 0);
        return programStatus[0] != 0;
    }

}
